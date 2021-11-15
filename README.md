# **PassLocker** - Simple password manager
PassLocker is a desktop application built with Java and JavaFX.
Downloads : [Jar][JarDl] - [Exe][exeDl]

## Features
- Create a vault and encrypt it with your pass key.
- Insert and secure your passwords, credit cards and private notes.
- Use the search bar to find your desired entry.
- Click a field to copy it's value.

## How it works
- Each user vault exists in a .vlt file.
- The contents of the .vtl files are encrypted using AES 256.
- The only way for a user to access his vault is by decrypting it using his vault's password.
- The encryption keys are derived using SHA 256.
- When a vault is succesfully decrypted, it's contents are loaded and displayed.


## How it looks
![](https://media.giphy.com/media/HQSzOUVdV9tK20C925/giphy.gif)
![](https://media.giphy.com/media/mgEruKse7xffUKH9P4/giphy.gif)
![](https://media.giphy.com/media/D2tXy3PeSLd4jEHtr4/giphy.gif)

 [exeDl]: <https://github.com/JohnEcn/PasswordManager/releases/tag/v1.0>
 [JarDl]: <https://github.com/JohnEcn/PasswordManager/releases/tag/v1.0>
   
