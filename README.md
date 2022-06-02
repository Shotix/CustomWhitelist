<h1 align="center">Custom Whitelist<project-name></h1>

<p align="center"><project-description>With this Bukkit Minecraft Plugin everybody is able to join your server but only Players that have the correct Password are able to play on it.</p>
  
## Important information
  
This little project is just a fun project I am currently working on. Please keep in mind that the code is not well structured and only barly working at this state. The current available version (v.09-preRelease-v4) IS working but the password is currently set to `2InchWheeler` by default. This and the setup commands for administrators will be added in the 1.0 release version.
  
 If you find any bugs or want to help feel free to use the Discussion or Issues page to let me know. I'll try to be as active as I can here.

## Available Commands

As a normal player you can run the following commands:

### `"/join [password]"`,

When a user is joining the server and the new user is currently <b>not</b> on the whitelist his movement ist locked and visibility reduced. In order to play on the server he needs to use the command `"/join [password]"`. He has three tries on logging in with the correct password. If he fails to input the correct password after this three trys he will be permanently banned from the server. This can be reset with the simple `pardon [player]` command.

### `"/updateStatus"`,

If you have used the `/join [password]` command you can use the `/updateStatus` command after that to update your status to `joinable`. After this the player will be kicked and he now can rejoin and play with the rest of the server. 

## Built With

- Bukkit
- Java

## Future Updates

`Will be filled when 1.0 is released`

## Author
  
**Tim Niklas Tenger**

- [GitHub Profile](https://github.com/Shotix "GitHub Profile")
- [Write me a mail](mailto:tregnet04@gmail.com?subject=CustomWhitelistPlugin "Write me a mail")

## 🤝 Support

Contributions, issues, and feature requests are welcome!

Give a ⭐️ if you like this project!
