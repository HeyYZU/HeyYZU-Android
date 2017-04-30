TOP_PATH := $(call my-dir)

# HeyYZUSecret
LOCAL_PATH := $(TOP_PATH)/HeyYZUSecretNDK
include $(CLEAR_VARS)
LOCAL_MODULE    := HeyYZUSecret
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_SRC_FILES := $(LOCAL_PATH)/tw_bingluen_heyyzu_constant_HeyYZUSecret.cpp
include $(BUILD_SHARED_LIBRARY)

# YZUSecret
LOCAL_PATH := $(TOP_PATH)/YZUSecretNDK
include $(CLEAR_VARS)
LOCAL_MODULE    := YZUSecret
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_SRC_FILES := $(LOCAL_PATH)/tw_bingluen_heyyzu_constant_YZUSecret.cpp
include $(BUILD_SHARED_LIBRARY)