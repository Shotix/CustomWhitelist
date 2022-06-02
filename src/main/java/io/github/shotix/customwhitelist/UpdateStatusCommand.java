package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

public class UpdateStatusCommand implements CommandExecutor {

    String playersOnWhitelist;

    private String openWhitelist() {
        return playersOnWhitelist = HandleFiles.openWhitelist();
    }
    private boolean openPlayerJoinList(String playerName) {
        return HandleFiles.isPlayerOnJoinList(playerName);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        openWhitelist();
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command!");
            return false;
        } else if (!(openWhitelist().contains(sender.getName()))) {
            sender.sendMessage(ChatColor.YELLOW + "Your name is currently not on the whitelist.\n Please use the " + ChatColor.GREEN + "/join [password]" + ChatColor.YELLOW + " to get added to the whitelist!");
            return false;
        }
        clearPotionEffects((Player) sender);
        ((Player) sender).setGameMode(GameMode.SURVIVAL);
        ((Player) sender).kickPlayer(ChatColor.GREEN + "You are now verified and will be able to play on the Server. Have fun!");
        return true;
    }

    public void clearPotionEffects(Player p)
    {
        for(PotionEffect effect : p.getActivePotionEffects())
        {
            p.removePotionEffect(effect.getType());
        }
    }
}
