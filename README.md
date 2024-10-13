# Fridgey-KMF

This branch is trying to convert Fridgey from an Android project to a Kotlin-Multiplatform project.
Through Kotlin-Multiplatform we can get both Android and IOS app from one built. 
If you are also interested with it, just have a try!

*There are still some feature not implemented on IOS, me has no IOS development experience and is trying hard to Google&ChatGPT😢* 

## Prepare

- Device running macOS
- Refer to [Set up KMF environment](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-setup.html#check-your-environment) and make sure you have the proper environment to run KMF project

## Start
### Clone this repo and checkout kmf branch
 ```shell
 cd YOUR_WORKSPACE
 
 git clone https://github.com/NielsLee/FoodRecords.git
 
 cd FoodRecords
 
 git checkout kmf
 ```

### Add Android SDK path
 ```shell
 echo "YOUR_ANDROID_SDK_PATH" > local.properties
 ```

Or your can do this by editing Android Studio -> File -> Project Structure -> SDK Location

### Build Android APK
```shell
./gradlew assembleDebug # for Debug Android apk  

./gradlew assembleRelease # for Release Android apk

```

If build successful, your can find apks in **composeApp/build/outputs/apk/**

### Compile IOS metadata
```shell
./gradlew compileNativeMainKotlinMetadata

```

If build successful, them you can turn to Xcode to build IOS apk

### Build IOS APK
Open Xcode, select **Open Existing Project**, then select **YOUR_WORKSPACE/Fridgey-KMF/iosApp/Fridgey.xcodeproj**

Refer to [Connect the framework to your IOS project](https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-integrate-in-existing-app.html#connect-the-framework-to-your-ios-project) and do followings:
1. In Xcode, open the iOS project settings by double-clicking the project name.
2. On the **Build Phases** tab of the project settings, click the + and add New Run Script Phase.
3. Add the following script:
```shell
cd "$SRCROOT/.."

./gradlew :shared:embedAndSignAppleFrameworkForXcode
```
4. Move the Run Script phase before the Compile Sources phase.
5. On the Build Settings tab, disable the User Script Sandboxing under Build Options

After that, still have to do one more thing:
6. On tht **Build Settings** tab,  add **-lsqlite3** to **Other Liker Flags**

Then build the project in Xcode. If everything is set up correctly, the project will build successfully🎉. You can run Fridgey in either IOS emulator or a real device.

## Issue

If your find any bugs or have any new ideas about Fridgey, feel free to submit an issue [here](https://github.com/NielsLee/FoodRecords/issues)

## Thanks

Thanks for these repos which Fridgey depends on:
- [Glide](https://github.com/bumptech/glide)
- [Camposer](https://github.com/ujizin/Camposer)
- [kotlin-csv](https://github.com/jsoizo/kotlin-csv)
- [MaterialKolor](https://github.com/jordond/MaterialKolor)

