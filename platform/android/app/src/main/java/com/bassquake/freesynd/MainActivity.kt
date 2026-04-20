package com.bassquake.freesynd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.bassquake.freesynd.databinding.ActivityMainBinding
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

    companion object {
        // Used to load the 'freesynd' library on application startup.
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