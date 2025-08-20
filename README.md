# 쏙

<img src="https://github.com/user-attachments/assets/67b7410e-f4fb-4dae-8ba2-9217640dbea4" width="22%" />
<img src="https://github.com/user-attachments/assets/83dcef4e-6d27-478b-9b7f-df9d84530c02" width="22%" />
<img src="https://github.com/user-attachments/assets/b6b6f6bb-2e43-48ff-8afa-3d85574403af" width="22%" />
<img src="https://github.com/user-attachments/assets/70515106-fbd4-490a-8699-570bc17f3f8a" width="22%" />

## 빌드하는 법

아래 명령어를 통해 APK 또는 AAB를 빌드할 수 있습니다.

```
./gradlew assembleDebug # Debug APK
./gradlew assembleDebug appDistributionUploadDebug # Debug APK 를 Firebase Distribution에 배포한다.
./gradlew assembleRelease # Release APK
./gradlew bundleRelease # Release AAB
fastlane android deploy version_code:5 version_name:1.0.0 # Release AAB # Play 스토어 내부 테스트에 배포한다.
```

릴리즈 빌드 시 keystore.properties 및 jks 파일을 미리 설정해야 합니다. /app/build.gradle.kts 의 `signingConfigs`를 참고하세요.

## CI/CD 파이프라인

<img width="2240" height="552" alt="pipeline" src="https://github.com/user-attachments/assets/c2a018f8-60e0-4681-99cd-3c6aad936b8c" />

- CI/CD 파이프라인은 위 그림을 따릅니다.
- develop 브랜치에 merge 될 경우 Debug APK 빌드가 자동으로 Firebase App Distribution에 업로드됩니다.
- 내부테스트에 배포할 경우 아래와 같이 [디스코드 봇](https://github.com/HamBP/gongju-bot) 명령어에 version_code와 version_name를 파미터로 넘겨 Release AAB 빌드 및 Play 스토어 업로드를 진행합니다.

<img width="1102" height="123" alt="image" src="https://github.com/user-attachments/assets/a92d213b-b30d-4975-84fe-5918e32b08b3" />
