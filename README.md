# News Letter (가제)

### 빌드하는 법

아래 명령어를 통해 APK 또는 AAB를 빌드할 수 있습니다.

```
./gradlew assembleDebug # Debug APK
./gradlew assembleDebug appDistributionUploadDebug # Debug APK 를 Firebase Distribution에 배포한다.
./gradlew assembleRelease # Release APK
./gradlew bundleRelease # Release AAB
```

릴리즈 빌드 시 keystore.properties 및 jks 파일을 미리 설정해야 합니다. /app/build.gradle.kts 의 `signingConfigs`를 참고하세요.
파이어베이스 배포 시 firebase-account.json 를 미리 설정해야 합니다.