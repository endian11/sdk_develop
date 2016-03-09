# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := amrnb-codec
LOCAL_SRC_FILES := \
interf_dec.c \
interf_enc.c \
sp_dec.c \
sp_enc.c \
aes.c \
buffer.c \
filterbank.c \
fftwrap.c \
resample.c \
kiss_fft.c \
kiss_fftr.c \
jitter.c \
preprocess.c \
mdf.c \
smallft.c \
fxpop.c \
ec_lms.c \
amrnb-codec.c

LOCAL_C_INCLUDES := \
$(common_C_INCLUDES) \
$(JNI_H_INCLUDE) \
$(LOCAL_PATH)

LOCAL_LDLIBS    := -lm -llog 
include $(BUILD_SHARED_LIBRARY)

