# FreeSynd port for Android and Windows

## Notes
This is based on the code from bni over at https://github.com/bni/freesynd. I've rejigged the project files and created an Android project too.
You need to supply the games asset files. Buy the game or find cd/disk of it (American Revolt will not work, has to be original release), install it on your pc/android (see below where they go) and copy the assets to this projects 'assets' folder. ALL folder and filenames needs to be lowercase. SDL2 and other libraries are already included in this project.

# Android (64bit)
This will work on any Android device above version 8.0 (Oreo). Also works on Quest for extra large gameplay!!

If you downloaded the apk and just want to get started without compiling, then install the apk, then copy the game files into the data folder. Youll need some file management on your pc.

The folder layout should be like so on your device:
/
    >data
        >data
            >com.bassquake.freesynd
                >files
                    >data <-- game files go in here
                freesynd.ini

If you find you get a "Configuration file not found" or similar and the files are definitly in the right place, its likely due to permmisions of the files. Youll need to do the following:

Enter terminal in Android with your phone connected.

Type the following:
adb shell
su
restorecon -R /data/data/com.bassquake.freesynd/

That should now work and you should try to relaunch the app again.

For Android, when youre building your own apk, enable including asset files into the apk byt editing build.gradle.kts (app) and comment the lines:
    sourceSets {
        getByName("main") {
            // GAME ASSET FILES PATH
            assets.srcDirs("${project.rootDir}../../../assets")
        }
    }

This will embed and then extract the game files when you install the apk on your device. Final apk is copied into build/android folder (game files are already compressed into it).

# Windows (64bit)

For Windows, add the game files to 'assets' folder, compile as normal and the final exe and the required sdl2 dlls and game files will be in the build/windows folder. Just run the exe and go!

## To do
- Add support for using touchscreen to move around.
- Fix issue when minimised app it just goes black and eventually crashes out.
- Untested Linux support. Will add later.
- Add support for MacOS for Intel and M chips needs adding later.
