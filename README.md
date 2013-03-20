# Multi-Platform Portable Minecraft Launcher#


The most current version can be found [here](https://github.com/MaPePeR/MinecraftMultiPortableLauncher/blob/master/dist/MinecraftMultiPortableLauncher.jar?raw=true).

This little program will launch Minecraft from the current directory on Windows, Linux and Mac.

What makes MinecraftMultiPortableLauncher different from other Minecraft-Portable-Tools?

- Works on all Operating Systems
- Does not rely on environment variables
- Does not modify any Minecraft-Files
- Uses [Reflection](http://en.wikipedia.org/wiki/Reflection_\(computer_programming\)) to move the Minecraft Files
- Uses the regular launcher (Online and Offline Mode)


## How to run MinecraftMultiPortableLauncher (MMPL) ##

1. Place the MinecraftMultiPortableLauncher.jar in a folder of your choice.
1. Start the .jar file.
1. You will be prompted to download the Minecraft-Launcher.jar
  You have  3 Choices:
  - *Open* the *Download-Page* in your Browser and download the minecraft.jar manually
  - *Download* the minecraft.jar  *with* your *Browser*
  - Use the internal Downloader **(recommended)**
1. Restart MMPL to start the Minecraft-Launcher
1. Log in.
1. Download the Game.
1. After the Download is finished:
1. **QUIT THE GAME**
  - Do not forget that or your game will not store data in the current directory!
1. Start MMPL again to enjoy your Portable Minecraft

> Do not get confused with the minecraft.jar-Launcher and the bin/minecraft.jar. 
> This program needs BOTH to operate correctly.


## How dows it work? ##

Singletons FTW. If you don't know what a [Singleton](http://en.wikipedia.org/wiki/Singleton_pattern) is: dont read any further.  

Minecraft has 2 Singletons, that hold the directory of Minecraft.

- One in the launcher(in net.minecraft.Util-Class)
- Another one in the game(in net.minecraft.client.Minecraft-Class)

Both Classnames are not obfuscated.  
MMPL then takes the Opporturnity and loads these classes before Minecraft can do it itself.  
After that [Reflection](http://en.wikipedia.org/wiki/Reflection_\(computer_programming\)) is used to make the Fields accessible and give them a value.

Because I don't know the exact name of the Fields i simply scan the Classes for any static File Field.  
This will break, when there are multiple Fields matching the criteria or when the singletons are removed in a future Minecraft version or the Class-Names get obfuscated lika all the others.