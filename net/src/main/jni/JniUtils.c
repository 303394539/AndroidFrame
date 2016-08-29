//
// Created by 白诚 on 16/7/7.
//

#include <stdint.h>
#include "com_baic_net_api_JniUtils.h"
#include "md5/md5.h"
#include <string.h>
#include <stdio.h>

#define PRIVATE_KEY1 "AAAAB3NzaC1yc2EAAAABIwAAAQEA1/czEfc1+yQ2TbuRHKeiaUTgiHSrdWOoLEySX8u49i6RwfUj3+fWXq0tFgRw7gwkTxK+9duUKKql55/cbcoBgCAhezwO3USiVChY3y+80B7edA2cZOwXuIn5Rm38r5gv+PGUZiKq17YJblVHxYJn/9qnOasE7BbbsP64CqUwn0pRHVW3Xgg9Duzox8MWoZL8Zyylbe3ECnSDlHqCKHKMY+MXM7ltt1tRjX3+cWgLrGfZ9w5t0+s1vrcH7eAcvseiTQkDvmSku7+A0dL8LDSXTq/wx+XWv6Lfya120ZD4h9umYs09s3+poRFfUrIy4DZpTxh8T3S+TOuzSKtI+1l5Xw=";
#define PRIVATE_KEY2 "3z1i087BBOB2Om4Tf1M1fJ9MmvlXjTNrITNKF5tK0oeOPWPZujxNOlAA0S9x1m/ctNRrKr3iwpbfzRwXqLrFB3IV0rueIENCO0vEJdIDXtJVLQK2a7/r/7iY0M6qIlijongVMZZP5JpP19CQg6ZuwfAoQ/TTCSmZ6Lpv2xOh60Ok/ppHWK3oHcpZyGxaZve/8oQMKtA3rM8dtkJzCgezk2oMz+CJ/V9uyRDT7DXUxgvQEAHDPf2oNBgSeOUsSKOyMmid+TMr0Dd+j0oowcHcc8b3FOmB03xHTM1MHsgRIUqgUarWDTUFPyjQD588FjPfB2b9d64fl/DT9xm2nmk/Ndfnt67RgE+Fo0kXGZIWNTJHDufmFJygKzSzhvNjvtXn6SetpGkDc2WqxFz6nxneTWRny8nkPiKQ/qnofdmlz/p/El8RFKZLY16uSLxkNEaaNUyijDAHkBfLc7y5LnysUT23bIpgI9NdjTnnTfursdviXHB/KXo4LxNkvYuTtaErobaPfg0kNPHHpFYo+/c9vao5azUkjNkuLvQCKj0c8+0zmC8GlYZAlnURz465Ls0GeCmVawvm/qL4gsX3joemRFVnkt/BzOna8a8v+yUI2V5ZcXaTmiGtnweaLMhkqsCkiWpXaHScTgnn9j0XZYr7Dw+he05r/UlcZeqIvVtXnEaFSsRTM7SsUyyu4sNJHn+oR3tZ175h53lvBYMWAZsDU+/bcKe3/WqR0FUmDI2h/Nba5aqepEqXYaIetmAbj5I8BtnNrQkljeMVQxPo+aXfHLpBUpLBTpE0vsCNTWHpCybKlMhhAYJW8hRElmtIaqlKsahHZ7fF0T2Iw5skjtcZ/9co3DT9RxQNEnEg5uAUfF+sBr2JqAe0mjNK833uDseavgTUyCXZ/tai1einAjczkWCAiZ5vJ+mKqpJBNUSGd5ytc/vCr0KcukeEHlb1PAD2XX7qN+AD9pxgEn3a1HD6y+NlPdatgl0OxP+N+fOm9pLSfojBvy997pbRMmM6QPnpBd55dzpH80QJRK1lW3K8SC2ZaXVzxLifzD0p6xwCrra/IYIRWGfKfpARQQWpPmfaeqQH018qo8UEVT5IjEmLLF/7AkRmB7puChWuyQaWtklCIMZhWjpTD9ZmYzOvwdEWCKHFeU+0uv+xeYkOGxzIf1GV2MbIoWZAEK+f/G1fdOJn8NWQh2gN6Evb1v+0kCEJZ3VtkhkoDNejX6mla26xJrFyX7MoyKsm653ZRhzm4agrOtJQW/ewln56NT02OHEUIFqwNWISb/CZ5ppSy2atcJoTVodVEcxRbjpq2dmE6/nQnq7ZGgCu/dFh3BfsXMcPUKbWp145nQAHpB4HpPSLMnpKg0UkMhMv7FdLgWuDUkzfpK4FoxFQv2Zt4lLVN+Vhy4nQHWA7HFi/iJuzjpHrmzcCvMLioDGay4Ali6qTQTaxwjHzKd0vd00LLxHyoUq7odwrgaXA4JeaRW9Aq5VhuP0bTt+2Yp1ZDgtLOT4cY1wF+YQnauiKpZskRMkvF2GMNyJ5I0p/FE4bpv8Ybd6P5YdCgamo6L7NCxvNcizR+RVLOHo6j6uvAfa+Up2WZQju";
static jstring KEY1 = NULL; //通过证书签名与private_key计算的用来ase的key md5(print+openid+pri1)
static jstring KEY2 = NULL; //通过证书签名与private_key计算的用来ase的key md5(print+openid+pri2)
static jstring certPrint = NULL;
static jclass StringUtil_class = NULL;

JNIEXPORT jstring JNICALL Java_com_baic_net_api_JniUtils_getResult(JNIEnv *env, jobject obj,
                                                                   jobject context, jstring openid,
                                                                   jstring target) {
    return encrypt(env, context, openid, target);
}

JNIEXPORT jstring JNICALL Java_com_baic_net_api_JniUtils_getSign(JNIEnv *env, jobject obj,
                                                                 jobject context, jstring openid,
                                                                 jstring target) {
    return sign(env, context, openid, target);
}

jobject getSignature(JNIEnv *env, jobject context) {

    jclass context_class = (*env)->GetObjectClass(env, context);

    //context.getPackageManager()
    jmethodID methodId = (*env)->GetMethodID(env, context_class, "getPackageManager",
                                             "()Landroid/content/pm/PackageManager;");
    jobject package_manager_object = (*env)->CallObjectMethod(env, context, methodId);

    //context.getPackageName()
    methodId = (*env)->GetMethodID(env, context_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name_string = (jstring) (*env)->CallObjectMethod(env, context, methodId);

    (*env)->DeleteLocalRef(env, context_class);
//
    //PackageManager.getPackageInfo(Sting, int)
    jclass pack_manager_class = (*env)->GetObjectClass(env, package_manager_object);
    methodId = (*env)->GetMethodID(env, pack_manager_class, "getPackageInfo",
                                   "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    (*env)->DeleteLocalRef(env, pack_manager_class);
    jobject package_info_object = (*env)->CallObjectMethod(env, package_manager_object, methodId,
                                                           package_name_string, 0x00000040);

    (*env)->DeleteLocalRef(env, package_manager_object);
    (*env)->DeleteLocalRef(env, package_name_string);

    //PackageInfo.signatures[0]
    jclass package_info_class = (*env)->GetObjectClass(env, package_info_object);
    jfieldID fieldId = (*env)->GetFieldID(env, package_info_class, "signatures",
                                          "[Landroid/content/pm/Signature;");
    (*env)->DeleteLocalRef(env, package_info_class);
    jobjectArray signature_object_array = (jobjectArray) (*env)->GetObjectField(env,
                                                                                package_info_object,
                                                                                fieldId);

    jobject signature_object = (*env)->GetObjectArrayElement(env, signature_object_array, 0);

    (*env)->DeleteLocalRef(env, package_info_object);
    (*env)->DeleteLocalRef(env, signature_object_array);

    // 返回当前应用签名对象
    return signature_object;
}

jstring getCertPrint(JNIEnv *env, jobject signature) {
    //signature.toByteArray();
    jclass signature_class = (*env)->GetObjectClass(env, signature);
    jmethodID toByteArray_mid = (*env)->GetMethodID(env, signature_class, "toByteArray", "()[B");
    (*env)->DeleteLocalRef(env, signature_class);
    jbyteArray cert_arr = (jbyteArray) (*env)->CallObjectMethod(env, signature, toByteArray_mid);

    //new ByteArrayInputStream(cert_arr);
    jclass byteArrayInputStream_class = (*env)->FindClass(env, "java/io/ByteArrayInputStream");
    jmethodID byteArrayInputStream_constructor_mid = (*env)->GetMethodID(env,
                                                                         byteArrayInputStream_class,
                                                                         "<init>", "([B)V");
    jobject input = (*env)->NewObject(env, byteArrayInputStream_class,
                                      byteArrayInputStream_constructor_mid, cert_arr);
    (*env)->DeleteLocalRef(env, byteArrayInputStream_class);
    (*env)->DeleteLocalRef(env, cert_arr);

    //CertificateFactory.getInstance("X509");
    jclass certificateFactory_class = (*env)->FindClass(env,
                                                        "java/security/cert/CertificateFactory");
    jmethodID certificateFactory_mid = (*env)->GetStaticMethodID(env, certificateFactory_class,
                                                                 "getInstance",
                                                                 "(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;");
    jstring certificateFactory_type = (*env)->NewStringUTF(env, "X509");
    jobject certificateFactory = (*env)->CallStaticObjectMethod(env, certificateFactory_class,
                                                                certificateFactory_mid,
                                                                certificateFactory_type);
    (*env)->DeleteLocalRef(env, certificateFactory_type);

    //(X509Certificate) cf.generateCertificate(input)
    certificateFactory_mid = (*env)->GetMethodID(env, certificateFactory_class,
                                                 "generateCertificate",
                                                 "(Ljava/io/InputStream;)Ljava/security/cert/Certificate;");
    (*env)->DeleteLocalRef(env, certificateFactory_class);
    jobject x509Certificate = (*env)->CallObjectMethod(env, certificateFactory,
                                                       certificateFactory_mid, input);
    (*env)->DeleteLocalRef(env, certificateFactory);
    (*env)->DeleteLocalRef(env, input);

    //MessageDigest.getInstance("SHA256")
    jclass messageDigest_class = (*env)->FindClass(env, "java/security/MessageDigest");
    jmethodID messageDigest_mid = (*env)->GetStaticMethodID(env, messageDigest_class, "getInstance",
                                                            "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jstring messageDigest_type = (*env)->NewStringUTF(env, "SHA256");
    jobject messageDigest = (*env)->CallStaticObjectMethod(env, messageDigest_class,
                                                           messageDigest_mid, messageDigest_type);
    (*env)->DeleteLocalRef(env, messageDigest_type);

    //x509Certificate.getEncoded()
    jclass x509Certificate_class = (*env)->FindClass(env, "java/security/cert/X509Certificate");
    jmethodID x509Certificate_mid = (*env)->GetMethodID(env, x509Certificate_class, "getEncoded",
                                                        "()[B");
    jbyteArray encode_arr = (jbyteArray) (*env)->CallObjectMethod(env, x509Certificate,
                                                                  x509Certificate_mid);
    (*env)->DeleteLocalRef(env, x509Certificate_class);
    (*env)->DeleteLocalRef(env, x509Certificate);

    //md.digest(c.getEncoded())
    messageDigest_mid = (*env)->GetMethodID(env, messageDigest_class, "digest", "([B)[B");
    jbyteArray messageDigest_arr = (jbyteArray) (*env)->CallObjectMethod(env, messageDigest,
                                                                         messageDigest_mid,
                                                                         encode_arr);
    (*env)->DeleteLocalRef(env, messageDigest_class);
    (*env)->DeleteLocalRef(env, encode_arr);
    (*env)->DeleteLocalRef(env, messageDigest);

    //StringUtil.bytes2HexString(arr)
    if (StringUtil_class == NULL) {
        jclass _class = (*env)->FindClass(env, "com/baic/utils/StringUtil");
        StringUtil_class = (*env)->NewGlobalRef(env, _class);
        (*env)->DeleteLocalRef(env, _class);
    }
    jmethodID stringUtil_mid = (*env)->GetStaticMethodID(env, StringUtil_class, "bytes2HexString",
                                                         "([B)Ljava/lang/String;");
    jstring result = (jstring) (*env)->CallStaticObjectMethod(env, StringUtil_class,
                                                                 stringUtil_mid,
                                                                 messageDigest_arr);
    (*env)->DeleteLocalRef(env, messageDigest_arr);

    return result;
}

void initKey(JNIEnv *env, jobject context, jstring openid) {
    if (certPrint == NULL) {
        jstring signature = getSignature(env, context);
        jstring print = getCertPrint(env, signature);
        certPrint = (*env)->NewGlobalRef(env, print);
        (*env)->DeleteLocalRef(env, signature);
        (*env)->DeleteLocalRef(env, print);
    }
    if (KEY1 == NULL) {
        char *key1 = PRIVATE_KEY1;
        jstring k1 = joinString(env, (*env)->NewStringUTF(env, key1), certPrint);
        jstring k2 = joinString(env, k1, openid);
        jstring md5 = getMd5(env, k2);
        KEY1 = (*env)->NewGlobalRef(env, md5);
        (*env)->DeleteLocalRef(env, k1);
        (*env)->DeleteLocalRef(env, k2);
        (*env)->DeleteLocalRef(env, md5);
    }
    if (KEY2 == NULL) {
        char *key2 = PRIVATE_KEY2;
        jstring k1 = joinString(env, (*env)->NewStringUTF(env, key2), certPrint);
        jstring k2 = joinString(env, k1, openid);
        jstring md5 = getMd5(env, k2);
        KEY2 = (*env)->NewGlobalRef(env, md5);
        (*env)->DeleteLocalRef(env, k1);
        (*env)->DeleteLocalRef(env, k2);
        (*env)->DeleteLocalRef(env, md5);
    }
}

jstring encrypt(JNIEnv *env, jobject context, jstring openid, jstring target) {
    initKey(env, context, openid);
    return XOR(env, KEY1, target);
}

jstring sign(JNIEnv *env, jobject context, jstring openid, jstring target) {
    initKey(env, context, openid);
    return getMd5(env, XOR(env, KEY2, target));
}

jbyteArray jString2JByteArray(JNIEnv *env, jstring target) {
    jclass class = (*env)->FindClass(env, "java/lang/String");
    jstring code = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env, class, "getBytes", "(Ljava/lang/String;)[B");
    (*env)->DeleteLocalRef(env, class);
    return (jbyteArray) (*env)->CallObjectMethod(env, target, mid, code);
}

jstring JByteArray2JString(JNIEnv *env, jbyteArray target) {
    jclass class = (*env)->FindClass(env, "java/lang/String");
    jstring code = (*env)->NewStringUTF(env, "utf-8");
    jmethodID mid = (*env)->GetMethodID(env,
                                        class,
                                        "<init>", "([BLjava/lang/String;)Ljava/lang/String;");
    (*env)->DeleteLocalRef(env, class);
    return (jstring) (*env)->NewObject(env, class,
                                       mid, target, code);
}

jstring joinString(JNIEnv *env, jstring str1, jstring str2) {
    char *a = (char *) (*env)->GetStringUTFChars(env, str1, 0);
    char *b = (char *) (*env)->GetStringUTFChars(env, str2, 0);
    strcat(a, b);
    return (*env)->NewStringUTF(env, a);
}

jstring getMd5(JNIEnv *env, jstring target) {
    char *t = (*env)->GetStringUTFChars(env, target, 0);
    MD5_CTX context = {0};
    MD5Init(&context);
    MD5Update(&context, t, strlen(t));
    unsigned char dest[16] = {0};
    MD5Final(dest, &context);
    int i = 0;
    char *md5[32] = {0};
    for (i = 0; i < 16; i++) {
        sprintf(md5, "%s%02x", md5, dest[i]);
    }
    return (*env)->NewStringUTF(env, md5);
}

jstring XOR(JNIEnv *env, jstring str1, jstring str2) {
    if (StringUtil_class == NULL) {
        jclass class = (*env)->FindClass(env, "com/baic/utils/StringUtil");
        StringUtil_class = (*env)->NewGlobalRef(env, class);
        (*env)->DeleteLocalRef(env, class);
    }
    jmethodID mid = (*env)->GetStaticMethodID(env, StringUtil_class, "XOR",
                                              "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    return (jstring) (*env)->CallStaticObjectMethod(env, StringUtil_class, mid, str1, str2);
}
