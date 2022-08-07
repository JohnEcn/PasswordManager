# **PassLocker** - Simple password manager
PassLocker is a desktop application built with Java and JavaFX.\
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
###### Loging in and making a new entry
![](https://media.giphy.com/media/HQSzOUVdV9tK20C925/giphy.gif)
###### Searching and editing an entry
![](https://media.giphy.com/media/mgEruKse7xffUKH9P4/giphy.gif)
###### Changing password
![](https://media.giphy.com/media/D2tXy3PeSLd4jEHtr4/giphy.gif)

## Project structure overview
![](https://i.postimg.cc/PJbWnJ13/Untitled-Diagram-drawio-1.png)

#### Model
- Each user entry is an **Element** type object, that exists in a **Vault** class instance.
- The **Vault** class is responsible for handling the **ArrayList** that contains the user entries. 
- The **Serializer** class is used by **Vault** class to serialize and write the data into a local file.
- Before the data is written is the file, the **Encryptor** class is used to encrypt the data.

#### ViewModel
- **ViewModel** class uses the singeton pattern, only one instance can exist.
- **ViewModel** class is used by the View to access the Vault and the user data.

#### View

- The UI consists of two javaFx scenes, **AuthScene** and **MainScene**.
- **AuthScene** and **MainScene** use the ViewModel to request/submit user data.
- The **AuthScene** is the initial display and handles the user creation, deletion and authentication.
- The **MainScene** is loaded after a user is succesfully authenticated handles the user data.
- Inside the **MainScene**, various other scenes can be loaded that make possible for the user to add/edit/delete data.

 [exeDl]: <https://github.com/JohnEcn/PasswordManager/releases/tag/v1.0>
 [JarDl]: <https://github.com/JohnEcn/PasswordManager/releases/tag/v1.0>
   