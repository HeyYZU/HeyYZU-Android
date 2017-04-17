#include "tw_bingluen_heyyzu_constant_YZUSecret.h"
#include <string.h>
#include <jni.h>

jstring Java_tw_bingluen_heyyzu_constant_YZUSecret_getUsername(JNIEnv *env, jclass that) {
    return (env)->NewStringUTF("");
}

jstring Java_tw_bingluen_heyyzu_constant_YZUSecret_getPassword(JNIEnv *env, jclass that) {
    return (env)->NewStringUTF("");
}

jstring Java_tw_bingluen_heyyzu_constant_YZUSecret_getAppId(JNIEnv *env, jclass that) {
    return (env)->NewStringUTF("");
}