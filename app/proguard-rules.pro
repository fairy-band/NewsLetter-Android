# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Crashlytics 스택 트레이스의 라인 정보를 유지하고, 원본 파일명은 숨긴다.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Kakao SDK
# https://developers.kakao.com/docs/latest/ko/android/getting-started#project-pro-guard
-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-dontwarn org.codehaus.mojo.animal_sniffer.*
