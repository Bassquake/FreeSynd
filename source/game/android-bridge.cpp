#ifdef ANDROID
#include <jni.h>

static bool s_inGameplay = false;

void setInGameplay(bool v) {
    s_inGameplay = v;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_bassquake_freesynd_MainActivity_isInGameplay(JNIEnv *, jobject) {
    return static_cast<jboolean>(s_inGameplay);
}
#endif
