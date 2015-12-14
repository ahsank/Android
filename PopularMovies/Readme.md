Popular Movies App


Build Instruction
-------------------
Before Build get themoviedb.com Apikey and add following line to the file
`~/.graddle/graddle.properties`

    MyMovieDBApiKey="42703cf8cb2793a32227cf33019bf7c2"

Build the project:

    ./gradlew assembleDebug
    # Launch emulator from the android avd UI
    android avd
    # Install the app
    adb install -r app/build/outputs/apk/app-debug.apk
    # Run the app
    adb shell am start -n app/build/outputs/apk/app-debug.apk/com.example.ahsankhan.popularmovies.MainActivity
    # Get log
    adb logcat

