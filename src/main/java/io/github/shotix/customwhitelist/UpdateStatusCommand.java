package io.github.shotix.customwhitelist;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class UpdateStatusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Handle player status
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command!");
            return false;
        } else if (!(HandleFiles.isPlayerOnWhitelist(sender.getName()))) {
            sender.sendMessage(ChatColor.YELLOW + "Your name is currently not on the whitelist.\n Please use the " + ChatColor.GREEN + "/join [password]" + ChatColor.YELLOW + " to get added to the whitelist!");
            return false;
        }
        HandleFiles.updatePlayerStatusToVerified(sender.getName());
        clearPotionEffects((Player) sender);
        ((Player) sender).setGameMode(GameMode.SURVIVAL);
        ((Player) sender).kickPlayer(ChatColor.GREEN + "You are now verified and will be able to play on the Server. Have fun!");
        return true;
    }

    public static void clearPotionEffects(Player p) {
        for(PotionEffect effect : p.getActivePotionEffects())
        {
            p.removePotionEffect(effect.getType());
        }
    }
}
