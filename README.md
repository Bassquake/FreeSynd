# FreeSynd port for Android and Windows

## Notes
This is based on the code from _bni_ over at [bni/freesynd](https://github.com/bni/freesynd). I've rejigged the project files and created and fixed some ARM specific errors for Android project too. I have added binaries for Android and Windows but they do not include the game files as its not allowed. See important note below about game assets and there are instructions on how to add them.

Project files are in:
```
\build
    \android <-- Open in this folder in Android Studio
    \windows <-- Open the solution file in here for Visual Studio
```

> [!IMPORTANT]
> You need to supply the games asset files. Buy the game or find cd/disk of it. Syndicate Plus will **not** work, it has to be the original 1993 release without American Revolt (Check [eBay](https://www.ebay.co.uk/sch/i.html?_nkw=syndicate+bullfrog+-wars+-plus+-american+-revolt&_sacat=0&_odkw=syndicate+bullfrog+-wars+-plus&_osacat=0&_sop=15) and the DOS version is probably easier to install and extract files from). Install the apk on your android, or go to build/windows where the exe and required dlls are already there, then copy out the data files you got from buying the game disk, into the 'data' folder. **ALL** folder and filenames needs to be lowercase (see below on how to easily do this). SDL2 and other libraries are already included in this project.

### Lowercase the game files
Go to the assets data folder in powershell and run:
```
Get-ChildItem -File | Rename-Item -NewName { $_.Name.ToLower() }
```
All files should now be from COL01.DAT to col1.dat etc.

> [!TIP]
>Best way to play is connect a bluetooth keyboard and mouse to your phone/Quest as I havent added touchscreen scrolling yet. 

### Keys
- AWSD keys can pan around.
- P to Pause.
- Number keys 1-4 to select an agent.
- Esc to quit or go back (Not added the functionality yet so you'd have to restart the app to quit and start again).
- Right mouse key to shoot manually (Not working properly yet).
- Dont click Middle Mouse button as Android uses it to minimise the app and it will crash out.

# Android (64bit)
This will work on any Android device above version 8.0 (Oreo). Also works on Quest for extra large screen gameplay (note: Dragging window size in Quest will crash it at the moment)!!

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

If you find you get a "Configuration file not found" or similar and the files are definitly in the right place, its likely due to permmisions of the files. Youll need to do the following:

Enter terminal in Android or PowerShell with your phone connected:
```
adb shell
su
restorecon -R /data/data/com.bassquake.freesynd/
```

That should now work and try to relaunch the app again.

When youre building your own apk, the game files will be auto added to the apk if they're in the assets folder. Final apk is copied into build/android folder (game files are already compressed into it). You only need to install the apk as normal on the phone/quest by copying the apk to its 'Downloads' folder and then install it on the device. On Quest you'll likely need to have [developer mode](https://developers.meta.com/horizon/documentation/native/android/mobile-device-setup/) on.

# Windows (64bit)

You can try to open the solution file in platform/windows but Visual Studio has an annoying habit of using absolute paths so they might be wrong. If so, generate new project files. I like to use cmake-gui from [cmake](https://cmake.org/download/). You might have to repoint the library and includes folders if it complains they're missing. All headers and library files are in the source folder under 'extern' and 'includes'.

For Windows, add the game files to 'assets' folder, compile as normal and the final exe and the required sdl2 dlls and game files will be in the build/windows folder. Just run the exe and go!

The file structure should be like so:
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

# To do
- Add support for using touchscreen to move around.
- Fix right mouse to shoot.
- Fix issue when minimised app it just goes black and eventually crashes out.
- Fix resizing issue on Quest.
- Untested Linux support. Will add later.
- Add support for MacOS for Intel and M chips needs adding later.
