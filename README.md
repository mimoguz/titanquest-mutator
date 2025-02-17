# Why fork?

I still play Titan Quest, mainly on my notebook which runs Linux. Recently I wanted to 
_"experiment"_ a bit, but I couldn't this app work on Linux properly. I recon the problem was 
not the app itself but the UI framework, (Jetpack) Compose Multiplatform, which I know 
nothing about. So I converted the code that makes the actual work to Java, slapped a lousy
Swing UI, and called it a day. Actual credit goes to the original author.

Below is the original README. This version of the app won't look like that.

Titan Quest Mutator
===================

**A simple savegame editor for Titan Quest AE**

Mostly build as a proof of concept and for getting familiar with the Compose desktop framework.

Currently it is possible to edit the following values:
- [x] Character name
- [x] Gold
- [x] Available attribute points
- [x] Available skillpoints

### How to use
1. Quit Titan Quest (this is important, do not use while game is running)
2. Start 'Titan Quest Mutator'
3. Click 'Load character' and select your savefile (`%USERPROFILE%\Documents\My Games\Titan Quest - Immortal Throne\SaveData\<_CharacterName>\Player.chr`)
4. Edit values as desired
5. Click 'Save character' to write the changes to the savefile

### Example
![Screenshot user interface](media/screenshot_ui.png)  

### Result in-game
![Screenshot attributes](media/cheat_attributes.jpg)  

![Screenshot skillpoints](media/cheat_skillpoints.jpg)  

***Disclaimer: This application alters your savefile and may corrupt it. It is strongly recommend to make backups of your savefile. Use at your own risk***

*Dual-licensed under MIT or Apache 2.0*
