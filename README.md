# FreeSynd port for Android and Windows

![Screenshot of FreeSynd playing on various Android devices.](https://github.com/Bassquake/FreeSynd/blob/master/captures/Devices.jpg)

Video of it in action on a Quest on [YouTube](https://youtu.be/KYbHk_xw-PY).

## Notes
This is based on the code from _bni_ over at [bni/freesynd](https://github.com/bni/freesynd). I've rejigged the project files and created and fixed some ARM specific errors for Android project too. I have added binaries for Android and Windows but they do not include the game files as its not allowed. See important note below about game assets and there are instructions on how to add them.

Project files are in:
```
\platform
    \android <-- Open in this folder in Android Studio
    \windows <-- Open the solution file in here for Visual Studio
```

> [!IMPORTANT]
> You need to supply the games asset files. Buy the game or find cd/disk of it. Syndicate Plus will **not** work, it has to be the original 1993 release without American Revolt (Check [eBay](https://www.ebay.co.uk/sch/i.html?_nkw=syndicate+bullfrog+-wars+-plus+-american+-revolt&_sacat=0&_odkw=syndicate+bullfrog+-wars+-plus&_osacat=0&_sop=15). The DOS version is probably easier to install and extract files from). Install the apk on your android, or go to build/windows where the exe and required dlls are already there if you've compiled, then copy out the data files you got from buying the game disk, into a folder called 'data' and copy that folder into the 'assets' folder. **ALL** folder and filenames needs to be lowercase (see below on how to easily do this). SDL2 and other libraries are already included in this project.

### Lowercase the game files
Go to the assets data folder in powershell and run:
```
Get-ChildItem -File | Rename-Item -NewName { $_.Name.ToLower() }
```
All files should now be from COL01.DAT to col1.dat etc.

> [!TIP]
>Best way to play is connect a Bluetooth keyboard and mouse to your phone/Quest as I haven't added touchscreen scrolling yet. 

### Keys
- WASD keys can pan around.
- P to Pause.
- Number keys 1-4 to select an agent.
- Esc to quit or go back (Some devices seems to ignore this, so needs fixing).
- Right mouse key to shoot manually (Not working properly yet).
- Don't use middle click on mouse buttons as Android uses it to minimise the app.

# Android
This will work on any Android device above version 7.0 (Nougat). Also works on Quest for extra large screen gameplay!!

If you downloaded the apk and just want to get started without compiling, install the apk as normal, then copy the game files into the data folder. You'll need some file management program on your pc such as the [SDK Platform-Tools](https://developer.android.com/tools/releases/platform-tools). Android is a bit of a bugbear when it comes to files and its permissions!

The folder layout should be like so on your device:
```
/
    >data
        >data
            >com.bassquake.freesynd
                >files
                    >data <-- game files go in here
                freesynd.ini
```

If you find you get a "Configuration file not found" or similar and the files are definitely in the right place, its likely due to permissions of the files. You'll need to do the following:

Enter terminal in Android or PowerShell with your phone connected:
```
adb shell
su
restorecon -R /data/data/com.bassquake.freesynd/
```

That should now work and try to relaunch the app again.

When you're building your own apk, the game files will be auto added to the apk if you've copied the game assets into a folder called 'data' and place it in the 'assets' folder. Final apk is copied into build/android folder (game files are already compressed into it). You only need to install the apk as normal on the phone/quest by copying the apk to its 'Downloads' folder and then install it on the device. On Quest and other Android devices you'll likely need to have [developer mode](https://developers.meta.com/horizon/documentation/native/android/mobile-device-setup/) on. Usually its just tapping the About in Settings multiple times until it says "Developer mode enabled).

# Windows
I like to use cmake-gui from [cmake](https://cmake.org/download/) to create the Visual Studios project files. In "Where is the source code?" point to the \source folder, in "Where to build the binaries" point to \platform\windows\x64 or x86, then hit Configure, when done, hit Generate. You can now open the solution file in Visual Studio. I use v2022. You might have to repoint the library and includes folders if it complains they're missing. All headers and library files are in the source folder under 'extern'.

For Windows, same as Android, add the game files into a folder called 'data' and copy to the 'assets' folder, compile as normal and the final exe and the required sdl2 dlls and game files will be in the build/windows folder. Just run the exe and go!

The final compiled file structure should be like so:
```
\data
    \cursors
        cursors.png
    \lang
        english.lng
        french.lng
        german.lng
        italian.lng
    \ref
        original_data.crc
        research.dat
        weapons.dat
    col01.dat
    game01.dat
    ... etc
freesynd.exe
freesynd.ini
SDL2.dll
SDL2_image.dll
SDL2_mixer.dll
```
> [!IMPORTANT]
> Make sure you know what your final device OS version and CPU is! For Android its usually arm64-v8a but some Android TVs are 32bit like mine was, so would be armeabi-v7a and has to be version 7.0 or above. For Windows the choice is x86 or x64, probably runs from XP upwards (not tested except Windows 10).

# To do
- Add support for using touchscreen to move around.
- Fix right mouse to shoot.
- ~~Fix issue when minimised app it just goes black and eventually crashes out.~~ Fixed.
- ~~Fix hang when tap an area of the screen during game play on Arm devices.~~ Fixed
- Fix resizing issue on Quest.
- Untested Linux support.
- Add support for MacOS for Intel and M chips needs adding later.
- Add Arm support for Windows.
