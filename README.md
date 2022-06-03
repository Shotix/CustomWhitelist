<h1 align="center">Custom Whitelist<project-name></h1>

<p align="center"><project-description>With this Bukkit Minecraft Plugin everybody is able to join your server but only players that have the correct password are able to play on it.</p>

## Important information

This little project is just a fun project I am currently working on. So keep in mind that the code is not looking good but it **is** working.

If you find any bugs or want to help feel free to use the Discussion or Issues page to let me know. I'll try to be as active as I can here. Feel free to ask for features!

## Download the plugin

You can download the plugin on the [release page](https://github.com/Shotix/CustomWhitelist/releases "GitHub release page"). Just install it in your plugin folder and reload the running plugins via the `/reload` command. Please make sure to always use the latest version.

## Supported Minecraft Bukkit Versions
  
| Version | Supported          |
| ------- | ------------------ |
| >1.18   | :white_check_mark: |
| <1.18   | Not tested         |
  
## Available Commands

**As a normal player you can run the following commands:**

### `/join [password]`

When a user is joining the server and the new user is currently <b>not</b> on the whitelist his movement ist locked and visibility reduced. In order to play on the server he needs to use the command `"/join [password]"`. He has three tries on logging in with the correct password. If he fails to input the correct password after this three trys he will be permanently banned from the server. This can be reset with the simple `pardon [player]` command.

### `/updateStatus`

If you have used the `/join [password]` command you can use the `/updateStatus` command after that to update your status to `joinable`. After this the player will be kicked and he now can rejoin and play with the rest of the server.

\
**As an administrator you have access to the following commands:**

### `/customWhitelist setPassword [password]`

When you want to set a new password you can use this command to set the password new players need to enter in order to play on the server. 

### `/customWhitelist getPassword`

If you want to know what the current password is you can use this command.

## Built With

- Bukkit
- Java

## Future Updates

* [ - ] Custom join messages
* [ - ] Better playerStatus handling
* [ - ] Better troubleshooting for server administrators
* [ - ] General QoL improvements

## Author

**Tim Niklas Tenger**

- [GitHub Profile](https://github.com/Shotix "GitHub Profile")
- [Write me a mail](mailto:tregnet04@gmail.com?subject=CustomWhitelistPlugin "Write me a mail")

## 🤝 Support

Contributions, issues, and feature requests are welcome!

Give a ⭐️ if you like this project!
