//
// Created by tanghongtu on 2020/6/24.
//
#include <android/log.h>
#include "bspatch.h"

extern "C"
JNIEXPORT jint JNICALL Java_com_tang_smartupdate_PatchUtil_patch
(JNIEnv *env, jclass class_, jstring oldApkPath_, jstring newApkPath_, jstring patchPath_)
{
    int argc = 4;
    char *ch[argc];
    ch[0] = (char *) "bspatch";
    ch[1] = const_cast<char *>(env->GetStringUTFChars(oldApkPath_, 0));
    ch[2] = const_cast<char *>(env->GetStringUTFChars(newApkPath_, 0));
    ch[3] = const_cast<char *>(env->GetStringUTFChars(patchPath_, 0));

    __android_log_print(ANDROID_LOG_INFO, "SmartUpdate", "old = %s ", ch[1]);
    __android_log_print(ANDROID_LOG_INFO, "SmartUpdate", "new = %s ", ch[2]);
    __android_log_print(ANDROID_LOG_INFO, "SmartUpdate", "patch = %s ", ch[3]);
    int ret = patch(argc, ch);
    __android_log_print(ANDROID_LOG_INFO, "SmartUpdate", "patch result = %d ", ret);

    env->ReleaseStringUTFChars(oldApkPath_, ch[1]);
    env->ReleaseStringUTFChars(newApkPath_, ch[2]);
    env->ReleaseStringUTFChars(patchPath_, ch[3]);


    return ret;
}
