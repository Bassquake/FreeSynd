# FreeSynd port for Android and Windows

![Screenshot of FreeSynd playing on various Android devices.](https://github.com/Bassquake/FreeSynd/blob/master/captures/Devices.jpg)

Video of it in action on a Quest on [YouTube](https://youtu.be/KYbHk_xw-PY).

## Notes
This is based on the code from _bni_ over at [bni/freesynd](https://github.com/bni/freesynd). I've rejigged the project files and created and fixed some ARM specific errors for Android project too. I have added binaries for Android and Windows but they do not include the game files as its not allowed. See important note below about game assets and there are instructions on how to add them.

Project files are in:
```
\FreeSynd
    \platform
        \android <-- Open in this folder in Android Studio
        \windows <-- Open the solution file in here for Visual Studio
```

> [!IMPORTANT]
> You need to supply the games asset files. Buy the game or find cd/disk of it. Syndicate Plus will **not** work, it has to be the original 1993 release without American Revolt (Check [eBay](https://www.ebay.co.uk/sch/i.html?_nkw=syndicate+bullfrog+-wars+-plus+-american+-revolt&_sacat=0&_odkw=syndicate+bullfrog+-wars+-plus&_osacat=0&_sop=15). The DOS version is probably easier to install and extract files from).

## Step-by-step

> [!IMPORTANT]
> Make sure you know what your final device OS version and CPU is! For Android its usually arm64-v8a but some Android TVs are 32bit so would be armeabi-v7a. This project is compatible with Android version 7.0 or above. For Windows the choice is x86 or x64, probably runs from XP upwards (not tested except Windows 10 and 11).

### Run on PC
1. Install the original pc game from disk/download like normal on your pc. If it's DOS, you'll probably have to use DosBox or similar.
2. Download the [lowercase.ps1](https://github.com/Bassquake/FreeSynd/blob/master/assets/lowercase.ps1) powershell script or find it in the assets folder of this project. Place the script into the folder where all game files are, should all be in a folder called **Data**.
3. Run the script in Powershell (type **lowercase.ps1**) and all files will now be lowercase.
4. The final file layout should look like so:

![File layout](https://github.com/Bassquake/FreeSynd/blob/master/captures/files_windows_x64.PNG)

5. Run the freesynd.exe!

### Run on Android phone
This will work on any Android device above version 7.0 (Nougat). Also works on Quest for extra large screen gameplay (instructions for installing on Quest is further down)!!

1. Download the FreeSynd apk for your device (arm64-v8a is for 64bit devices, arm-v7a is for 32bit).
2. Copy that apk to your device Download folder and run it to install it. It won't properly run yet as we need to copy the game data files.
3. Make a folder on your phone/tv/android device called **com.bassquake.freesynd** in the **sdcard/Android/data** and make another folder called **files** in that com.bassquake.freesynd folder.
4. Now copy the data folder that contains all the game assets to **sdcard/Android/data/com.bassquake.freesynd/files**.
5. You should see thr FreeSynd icon on your phone, tap to run!

### Run on Quest headset
1. Plug your headset in via usb.
2. Download the FreeSynd release apk and install it with [SideQuest](https://sidequestvr.com/setup-howto) using the "Install APK file from folder on computer". (Your headset probably should be in [Developer](https://developers.meta.com/horizon/documentation/native/android/mobile-device-setup/) mode already):

![Screenshot of apk install](https://github.com/Bassquake/FreeSynd/blob/main/captures/sidequest_install.png)

3. After install completes, you should see this in the "Currently installed apps":

![Screenshot of apk location](https://github.com/Bassquake/FreeSynd/blob/main/captures/sidequest_installed.png)

4. Now still in SideQuest, go to "Manage files on the headset".
5. Navigate to "sdcard/Android/data".
6. Create a folder called "com.bassquake.freesynd" and inside that create a folder called "files" if there isn't one already.
7. Copy all the game assets into that files folder. The layout should be like so on your device:

![Screenshot of assets location](https://github.com/Bassquake/FreeSynd/blob/main/captures/sidequest_files.png)

8. Go to Unknown Sources on the headset and run FreeSynd!

> [!TIP]
>Best way to play is connect a Bluetooth keyboard and mouse to your phone/Quest as I haven't added touchscreen scrolling yet. 

### Keys
- WASD keys can pan around.
- P to Pause.
- Number keys 1-4 to select an agent.
- Esc to quit or go back (Some devices seems to ignore this, so needs fixing).
- Right mouse key to shoot manually (Not working properly yet).
- Don't use middle click on mouse buttons as Android uses it to minimise the app.

# To do
- Add support for using touchscreen to move around.
- Fix right mouse to shoot.
- ~~Fix issue when minimised app it just goes black and eventually crashes out.~~ Fixed.
- ~~Fix hang when tap an area of the screen during game play on Arm devices.~~ Fixed.
- ~~Fix resizing issue on Quest.~~ Fixed.
- Untested Linux support.
- Add support for MacOS for Intel and M chips needs adding later.
- Add Arm support for Windows.
