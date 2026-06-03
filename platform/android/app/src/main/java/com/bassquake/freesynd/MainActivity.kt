package com.bassquake.freesynd

import android.os.Handler
import android.os.Looper
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.VelocityTracker
import org.libsdl.app.SDLActivity

class MainActivity : SDLActivity() {

    override fun getLibraries(): Array<String> {
        return arrayOf(
            "SDL2",
            "png",
            "SDL2_image",
            "SDL2_mixer",
            "freesynd"
        )
    }

    override fun getMainSharedObject(): String {
        return "libfreesynd.so"
    }

    override fun getMainFunction(): String {
        return "SDL_main"
    }

    private external fun isInGameplay(): Boolean

    private val edgeZoneFraction = 0.04f
    private var keyW = false; private var keyA = false
    private var keyS = false; private var keyD = false
    private var lastHoverX = 0f
    private var lastHoverY = 0f

    private val handler = Handler(Looper.getMainLooper())
    private var flickRelease: Runnable? = null
    private val velocityTracker = VelocityTracker.obtain()
    private val flickMinVelocity = 50f   // px/s below this = no flick
    private val flickMaxDuration = 250L   // ms cap on hold duration
    private val swipeThreshold = 10f      // px moved before treated as swipe, not tap
    private var touchStartX = 0f
    private var touchStartY = 0f
    private var hasSwiped = false

    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
        if ((event.source and InputDevice.SOURCE_CLASS_POINTER) != 0) {
            val action = event.actionMasked
            if (action == MotionEvent.ACTION_HOVER_MOVE || action == MotionEvent.ACTION_MOVE) {
                val x = event.x
                val y = event.y
                lastHoverX = x
                lastHoverY = y

                // Explicitly sync SDL mouse position — Quest pointer may not match SDL's expected source
                onNativeMouse(0, MotionEvent.ACTION_HOVER_MOVE, x, y, false)

                val w = window.decorView.width.toFloat()
                val h = window.decorView.height.toFloat()
                val edgeX = w * edgeZoneFraction
                val edgeY = h * edgeZoneFraction

                if (isInGameplay()) {
                    keyW = sendKey(KeyEvent.KEYCODE_W, y < edgeY,     keyW)
                    keyS = sendKey(KeyEvent.KEYCODE_S, y > h - edgeY, keyS)
                    keyA = sendKey(KeyEvent.KEYCODE_A, x < edgeX,     keyA)
                    keyD = sendKey(KeyEvent.KEYCODE_D, x > w - edgeX, keyD)
                } else {
                    releaseWasd()
                }

                return true
            } else if (action == MotionEvent.ACTION_HOVER_EXIT) {
                releaseWasd()
            }
        }
        return super.dispatchGenericMotionEvent(event)
    }

    private fun sendKey(keyCode: Int, pressed: Boolean, was: Boolean): Boolean {
        if (pressed && !was) dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        if (!pressed && was) dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
        return pressed
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            when (event.action) {
                KeyEvent.ACTION_DOWN -> onNativeMouse(MotionEvent.BUTTON_SECONDARY, MotionEvent.ACTION_DOWN, lastHoverX, lastHoverY, false)
                KeyEvent.ACTION_UP   -> onNativeMouse(0,                           MotionEvent.ACTION_UP,   lastHoverX, lastHoverY, false)
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                flickRelease?.let { handler.removeCallbacks(it) }
                flickRelease = null
                velocityTracker.clear()
                velocityTracker.addMovement(event)
                touchStartX = event.x
                touchStartY = event.y
                hasSwiped = false
            }
            MotionEvent.ACTION_MOVE -> {
                velocityTracker.addMovement(event)
                if (!hasSwiped) {
                    val dx = event.x - touchStartX
                    val dy = event.y - touchStartY
                    if (dx * dx + dy * dy > swipeThreshold * swipeThreshold) hasSwiped = true
                }
                val x = event.x
                val y = event.y
                val w = window.decorView.width.toFloat()
                val h = window.decorView.height.toFloat()
                val edgeX = w * edgeZoneFraction
                val edgeY = h * edgeZoneFraction

                if (isInGameplay()) {
                    keyW = sendKey(KeyEvent.KEYCODE_W, y < edgeY,     keyW)
                    keyS = sendKey(KeyEvent.KEYCODE_S, y > h - edgeY, keyS)
                    keyA = sendKey(KeyEvent.KEYCODE_A, x < edgeX,     keyA)
                    keyD = sendKey(KeyEvent.KEYCODE_D, x > w - edgeX, keyD)
                } else {
                    releaseWasd()
                }
            }
            MotionEvent.ACTION_UP -> {
                velocityTracker.addMovement(event)
                velocityTracker.computeCurrentVelocity(1000)
                val vx = velocityTracker.xVelocity
                val vy = velocityTracker.yVelocity
                val speed = Math.hypot(vx.toDouble(), vy.toDouble()).toFloat()

                if (speed >= flickMinVelocity) {
                    keyW = sendKey(KeyEvent.KEYCODE_W, vy >  flickMinVelocity, keyW)
                    keyS = sendKey(KeyEvent.KEYCODE_S, vy < -flickMinVelocity, keyS)
                    keyA = sendKey(KeyEvent.KEYCODE_A, vx >  flickMinVelocity, keyA)
                    keyD = sendKey(KeyEvent.KEYCODE_D, vx < -flickMinVelocity, keyD)
                    val duration = (speed.coerceAtMost(3000f) / 3000f * flickMaxDuration).toLong()
                    flickRelease = Runnable { releaseWasd() }.also { handler.postDelayed(it, duration) }
                } else {
                    releaseWasd()
                }

                if (hasSwiped) {
                    val cancel = MotionEvent.obtain(event).also { it.action = MotionEvent.ACTION_CANCEL }
                    super.dispatchTouchEvent(cancel)
                    cancel.recycle()
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL -> releaseWasd()
        }
        return super.dispatchTouchEvent(event)
    }

    private fun releaseWasd() {
        keyW = sendKey(KeyEvent.KEYCODE_W, false, keyW)
        keyS = sendKey(KeyEvent.KEYCODE_S, false, keyS)
        keyA = sendKey(KeyEvent.KEYCODE_A, false, keyA)
        keyD = sendKey(KeyEvent.KEYCODE_D, false, keyD)
    }

    companion object {
        init {
            try {
                System.loadLibrary("SDL2")
                System.loadLibrary("png")
                System.loadLibrary("SDL2_image")
                System.loadLibrary("SDL2_mixer")
                System.loadLibrary("freesynd")
                android.util.Log.i("SDL_CHECK", "SUCCESS: All libraries loaded!")
            } catch (e: UnsatisfiedLinkError) {
                android.util.Log.e("SDL_CHECK", "FAILURE: Could not load native library: ${e.message}")
            }
        }
    }
}
