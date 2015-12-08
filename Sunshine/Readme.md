Sunshine App.


Build Instruction
-------------------

     # Build the project
    ./gradlew assembleDebug
    # Launch emulator
    android avd
    # Install the app
    adb install -r app/build/outputs/apk/app-debug.apk
    # Run the app
    adb shell am start -n app/build/outputs/apk/app-debug.apk/com.example.ahsankhan.sunshine.MainActivity
    # Get log
    adb logcat

