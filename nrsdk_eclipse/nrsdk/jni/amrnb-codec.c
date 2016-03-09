#include <jni.h>
#include <math.h>
#include <string.h>
#include "typedef.h"
#include "interf_enc.h"
#include "interf_dec.h"
#include "aes.h"
#include "stdio.h"
#include "speex_echo.h"
#include "speex_preprocess.h"
#include "ec_lms.h"
#define LOG_TAG "amrnb"

//#ifdef BUILD_FROM_SOURCE
//#include <utils/Log.h>
//#else
#include <android/log.h>
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , LOG_TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , LOG_TAG, __VA_ARGS__)

static int* enstate;
static int* destate;
static SpeexEchoState *st;
static SpeexPreprocessState *den;

#define FRAMESIZE  160
#define TXBUFSIZE  (LMSSIZE+2*FRAMESIZE)    /* TxBuf = [LMSSIZE][FRAMESIZE]...[FRAMESIZE] */
#define NPAST      (TXBUFSIZE-FRAMESIZE)

short buffer_Tx[TXBUFSIZE], buffer_Rx[FRAMESIZE];
struct ec_lms_struct mem_ec;

/*
 * Class:     com_travelrely_voice_Codec
 * Method:    jia_ec_init
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_jia_1ec_1init
(JNIEnv *env, jobject this)
{
	int i;
	init_ec(&mem_ec);
	for (i=0;i<NPAST;i++)
	buffer_Tx[i] = 0;
	LOGE("\nnpast is %d",NPAST);
}

/*
 * Class:     com_travelrely_voice_Codec
 * Method:    jia_ec_opt
 * Signature: ([B[B[B)V
 */
JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_jia_1ec_1opt
(JNIEnv *env, jobject this, jbyteArray recMicArray, jbyteArray refFarendArray, jbyteArray outArray)
{
	jsize recMic_sz, refFar_sz, out_sz;
	jbyte *precMic, *prefFar, *pout;
	int i;
	recMic_sz = (*env)->GetArrayLength(env, recMicArray);
	refFar_sz = (*env)->GetArrayLength(env, refFarendArray);
	out_sz = (*env)->GetArrayLength(env, outArray);
	precMic = (*env)->GetByteArrayElements(env, recMicArray, 0);
	prefFar = (*env)->GetByteArrayElements(env, refFarendArray, 0);
	pout = (*env)->GetByteArrayElements(env, outArray, 0);

	memcpy(&buffer_Tx[NPAST],precMic,320);
	memcpy(buffer_Rx,prefFar,320);
	ec_lms(&mem_ec, &buffer_Tx[NPAST], buffer_Rx, FRAMESIZE);

	for (i=0;i<NPAST;i++)
		buffer_Tx[i] = buffer_Tx[FRAMESIZE+i];
	memcpy(pout,buffer_Rx,320);
	(*env)->ReleaseByteArrayElements(env, recMicArray, precMic, 0);
	(*env)->ReleaseByteArrayElements(env, refFarendArray, prefFar, 0);
	(*env)->ReleaseByteArrayElements(env, outArray, pout, 0);
}

JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_speex_1echo_1state_1init
(JNIEnv *env, jobject this, jint frameSize, jint filterLength, jint sampleRate) {
	LOGW("!enter speex_1echo_1state_1init!");
	st = speex_echo_state_init(frameSize, filterLength);
	den = speex_preprocess_state_init(frameSize, sampleRate);
	speex_echo_ctl(st, SPEEX_ECHO_SET_SAMPLING_RATE, &sampleRate);
	speex_preprocess_ctl(den, SPEEX_PREPROCESS_SET_ECHO_STATE, st);

}

/*
 * Class:     com_travelrely_voice_Codec
 * Method:    speex_echo_state_destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_speex_1echo_1state_1destroy
(JNIEnv *env, jobject this) {
	LOGW("!speex_1echo_1state_1destroy!");
	speex_echo_state_destroy(st);
	speex_preprocess_state_destroy(den);

}

/*
 * Class:     com_travelrely_voice_Codec
 * Method:    speex_echo_cancellation
 * Signature: ([B[B[B)V
 */
JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_speex_1echo_1cancellation
(JNIEnv *env, jobject this, jbyteArray recMicArray, jbyteArray refFarendArray, jbyteArray outArray) {
	jsize recMic_sz, refFar_sz, out_sz;
	jbyte *precMic, *prefFar, *pout;
	recMic_sz = (*env)->GetArrayLength(env, recMicArray);
	refFar_sz = (*env)->GetArrayLength(env, refFarendArray);
	out_sz = (*env)->GetArrayLength(env, outArray);
	precMic = (*env)->GetByteArrayElements(env, recMicArray, 0);
	prefFar = (*env)->GetByteArrayElements(env, refFarendArray, 0);
	pout = (*env)->GetByteArrayElements(env, outArray, 0);
	speex_echo_cancellation(st, precMic, prefFar, pout);
	speex_preprocess_run(den, pout);
	(*env)->ReleaseByteArrayElements(env, recMicArray, precMic, 0);
	(*env)->ReleaseByteArrayElements(env, refFarendArray, prefFar, 0);
	(*env)->ReleaseByteArrayElements(env, outArray, pout, 0);
}

JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_encode(JNIEnv *env,
		jobject this, jbyteArray sampleArray, jint sampleOffset,
		jint sampleLength, jbyteArray dataArray, jint dataOffset, jint amrMode) {
	jsize samples_sz, data_sz;
	jbyte *samples, *data;
	int bytes_to_encode;
	int bytes_encoded_output;

	samples_sz = (*env)->GetArrayLength(env, sampleArray);
	samples = (*env)->GetByteArrayElements(env, sampleArray, 0);
	data_sz = (*env)->GetArrayLength(env, dataArray);
	data = (*env)->GetByteArrayElements(env, dataArray, 0);
	// LOGW("encode: data sz is %d, sample sz is %d",data_sz,samples_sz);
	if (sampleLength % 320)
		return 0;

	//samples += sampleOffset;
	//data += dataOffset;

	bytes_to_encode = sampleLength;
	bytes_encoded_output = 0;

	//LOGW("encode mode is %d", mode);
	int truncated = bytes_to_encode % (320);
	if (truncated) {
		LOGW("Ignore last %d bytes", truncated);
		bytes_to_encode -= truncated;
	}
	int index = 0;
	while (bytes_to_encode > 0) {
		int _encoded;
		_encoded = Encoder_Interface_Encode(enstate, amrMode, (short *) samples,
				data, 1);
		//LOGW("index=%d encode len is %d, first byte is %02X",index,_encoded,data[0]);
		samples += 320;
		data += _encoded;

		bytes_encoded_output += _encoded;
		bytes_to_encode -= 320;
		index += 1;
	}

	samples -= sampleLength;
	data -= bytes_encoded_output;

	(*env)->ReleaseByteArrayElements(env, sampleArray, samples, 0);
	(*env)->ReleaseByteArrayElements(env, dataArray, data, 0);

	return bytes_encoded_output;
}

JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_Encoder_1Interface_1init
(JNIEnv *env, jobject this, jint jdtx)
{
	LOGW("enter encoder init!");
enstate=Encoder_Interface_init(jdtx);
	//LOGW("enter encoder init! enstate is %08X",enstate);
}

JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_Encoder_1Interface_1exit
  (JNIEnv *env, jobject this)
{
	LOGW("enter encoder exit!");
Encoder_Interface_exit(enstate);
}
JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_Decoder_1Interface_1init(
	JNIEnv *env, jobject this) {
LOGW("enter decoder init!");
destate = Decoder_Interface_init();
}
JNIEXPORT void JNICALL Java_com_travelrely_v2_NR_voice_Codec_Decoder_1Interface_1exit
  (JNIEnv *env, jobject this)
{
	LOGW("enter decoder exit!");
Decoder_Interface_exit(destate);
}

JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_decode(JNIEnv *env,
jobject this, jbyteArray dataArray, jint dataOffset, jint dataLength,
jbyteArray sampleArray, jint sampleOffset) {
jsize samples_sz, data_sz;
jbyte *samples, *data;

data_sz = (*env)->GetArrayLength(env, dataArray);
data = (*env)->GetByteArrayElements(env, dataArray, 0);
samples_sz = (*env)->GetArrayLength(env, sampleArray);
samples = (*env)->GetByteArrayElements(env, sampleArray, 0);
//    LOGW("data sz is %d, sample sz is %d",data_sz,samples_sz);
//    samples += sampleOffset;
//    data += dataOffset;
short block_size[16] =
	{ 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0, 0, 0 };
unsigned char analysis[32];
short tmpoutput[160];
enum Mode dec_mode;
int blockIndex;
unsigned char cTmp;
int read_size;
int bytes_decoded;
int total_decode_output_length;
int frametype;
int index = 0;

bytes_decoded = 0;
total_decode_output_length = 0;
while (bytes_decoded < dataLength) {
cTmp = data[0];
dec_mode = (cTmp >> 3) & 0x000F;
read_size = block_size[dec_mode];
//memcpy(analysis,data,read_size);
//LOGW("index is %d, cTmp is %02X,read_size len is %d",index, cTmp,read_size);

//memcpy(analysis, data, read_size+1);//帧头
frametype = Decoder_Interface_Decode(destate, data, (short*) samples, 0);
//memcpy(samples, tmpoutput, 320);

data += read_size + 1;
samples += 320;

bytes_decoded += read_size + 1;    	//加帧头
total_decode_output_length += 320;
index += 1;
}

samples -= total_decode_output_length;
data -= bytes_decoded;

(*env)->ReleaseByteArrayElements(env, sampleArray, samples, 0);
(*env)->ReleaseByteArrayElements(env, dataArray, data, 0);

//return total_decode_output_length;
return frametype;
}

static aes_context aes_ctx_enc;
static aes_context aes_ctx_dec;

JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_aes_1set_1enc_1key(
JNIEnv *env, jobject this, jbyteArray keyArray) {
jsize keySize;
jbyte *pKey;
int nRtcode;

keySize = (*env)->GetArrayLength(env, keyArray);
pKey = (*env)->GetByteArrayElements(env, keyArray, 0);
keySize *= 8;
nRtcode = aes_set_key(&aes_ctx_enc, pKey, keySize);

LOGW("enc keysize is %d bits", keySize);
(*env)->ReleaseByteArrayElements(env, keyArray, pKey, 0);
return nRtcode;
}

JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_aes_1set_1dec_1key(
JNIEnv *env, jobject this, jbyteArray keyArray) {
jsize keySize;
jbyte *pKey;
int nRtcode;

keySize = (*env)->GetArrayLength(env, keyArray);
pKey = (*env)->GetByteArrayElements(env, keyArray, 0);
keySize *= 8;
nRtcode = aes_set_key(&aes_ctx_dec, pKey, keySize);

LOGW("dec keysize is %d bits", keySize);
(*env)->ReleaseByteArrayElements(env, keyArray, pKey, 0);
return nRtcode;
}

JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_aes_1encrypt(JNIEnv *env,
jobject this, jbyteArray srcArray, jbyteArray encArray) {
int nRtcode = 0;
int nSrcSize, nEncSize;
jbyte *pSrc, *pEnc;
int i;

nSrcSize = (*env)->GetArrayLength(env, srcArray);
nEncSize = (*env)->GetArrayLength(env, encArray);
if (nSrcSize != nEncSize || nSrcSize % 16 != 0)
return -1;
pSrc = (*env)->GetByteArrayElements(env, srcArray, 0);
pEnc = (*env)->GetByteArrayElements(env, encArray, 0);

for (i = 0; i < nSrcSize / 16; i++)
aes_encrypt(&aes_ctx_enc, pSrc + i * 16, pEnc + i * 16);
(*env)->ReleaseByteArrayElements(env, srcArray, pSrc, 0);
(*env)->ReleaseByteArrayElements(env, encArray, pEnc, 0);
return nRtcode;
}
JNIEXPORT jint JNICALL Java_com_travelrely_v2_NR_voice_Codec_aes_1decrypt(JNIEnv *env,
jobject this, jbyteArray encArray, jbyteArray decArray) {
int nRtcode = 0;
int nEncSize, nDecSize;
jbyte *pDec, *pEnc;
int i;

nEncSize = (*env)->GetArrayLength(env, encArray);
nDecSize = (*env)->GetArrayLength(env, decArray);
if (nEncSize != nDecSize || nEncSize % 16 != 0)
return -1;
pEnc = (*env)->GetByteArrayElements(env, encArray, 0);
pDec = (*env)->GetByteArrayElements(env, decArray, 0);

for (i = 0; i < nEncSize / 16; i++)
aes_decrypt(&aes_ctx_dec, pEnc + i * 16, pDec + i * 16);
(*env)->ReleaseByteArrayElements(env, encArray, pEnc, 0);
(*env)->ReleaseByteArrayElements(env, decArray, pDec, 0);
return nRtcode;
}

