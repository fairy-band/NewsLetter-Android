<img src="https://github.com/user-attachments/assets/67b7410e-f4fb-4dae-8ba2-9217640dbea4" width="22%" />
<img src="https://github.com/user-attachments/assets/83dcef4e-6d27-478b-9b7f-df9d84530c02" width="22%" />
<img src="https://github.com/user-attachments/assets/b6b6f6bb-2e43-48ff-8afa-3d85574403af" width="22%" />
<img src="https://github.com/user-attachments/assets/70515106-fbd4-490a-8699-570bc17f3f8a" width="22%" />

# News Letter (가제)

### 빌드하는 법

아래 명령어를 통해 APK 또는 AAB를 빌드할 수 있습니다.

```
./gradlew assembleDebug # Debug APK
./gradlew assembleDebug appDistributionUploadDebug # Debug APK 를 Firebase Distribution에 배포한다.
./gradlew assembleRelease # Release APK
./gradlew bundleRelease # Release AAB
fastlane android deploy version_code:5 version_name:1.0.0 # Release AAB # Play 스토어 내부 테스트에 배포한다.
```

릴리즈 빌드 시 keystore.properties 및 jks 파일을 미리 설정해야 합니다. /app/build.gradle.kts 의 `signingConfigs`를 참고하세요.
파이어베이스 배포 시 firebase-account.json 를 미리 설정해야 합니다.
