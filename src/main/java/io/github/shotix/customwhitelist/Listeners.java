package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;


public class Listeners implements Listener {


    @EventHandler
    public void event(PlayerJoinEvent playerJoinEvent) {

        if (HandleFiles.isPlayerStatusBanned(playerJoinEvent.getPlayer().getName())) {
            Bukkit.broadcastMessage(playerJoinEvent.getPlayer().getName());
            HandleFiles.updateTries(playerJoinEvent.getPlayer().getName(), 0);
            HandleFiles.updatePlayerStatusToNewPlayer(playerJoinEvent.getPlayer().getName());
        }
        if (playerJoinEvent.getPlayer().isOp() && HandleFiles.getPassword().matches("join")) {
            playerJoinEvent.getPlayer().sendMessage("The current join password is still the default password " + ChatColor.GRAY + "join.\n" + ChatColor.WHITE + "Please change the password with the " + ChatColor.GOLD + "/customwhitelist setPassword [newPassword]" + ChatColor.WHITE + " command.");
        }
        if (!HandleFiles.isPlayerOnWhitelist(playerJoinEvent.getPlayer().getName())) {
            HandlePlayerNotOnWhitelist.initialiseHandling(playerJoinEvent);
            playerJoinEvent.getPlayer().sendMessage(ChatColor.YELLOW + "Welcome to the server!\nYou are currently not whitelisted on this server\nIn order to achieve this, please use the " + ChatColor.GOLD + "\"/join [password]\"" + ChatColor.YELLOW + " command to register yourself\"\n");
        } else if (!HandleFiles.isPlayerOnJoinList(playerJoinEvent.getPlayer().getName())) {
            HandlePlayerNotOnWhitelist.initialiseHandling(playerJoinEvent);
            playerJoinEvent.getPlayer().sendMessage(ChatColor.YELLOW + "You are currently on the whitelist but not verified.\nPlease use the " + ChatColor.GOLD + "/updatestatus" + ChatColor.YELLOW + " command to update your status!");
        }
    }

    @EventHandler
    public void event(PlayerMoveEvent playerMoveEvent) {
        if (!HandleFiles.isPlayerOnWhitelist(playerMoveEvent.getPlayer().getName()) || !HandleFiles.isPlayerOnJoinList(playerMoveEvent.getPlayer().getName()))
            HandlePlayerNotOnWhitelist.blockMovement(playerMoveEvent);
    }
}
