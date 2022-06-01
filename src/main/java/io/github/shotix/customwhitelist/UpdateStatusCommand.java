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


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        openWhitelist();
        if (!(sender instanceof Player) || !(playersOnWhitelist.contains(sender.getName()))) {
            sender.sendMessage(ChatColor.YELLOW + "You are not on the whitelist. Please type /join [password] to register yourself!");
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "You are now verified.");
        try {
            TimeUnit.SECONDS.sleep(1);
            clearPotionEffects((Player) sender);
            ((Player) sender).setGameMode(GameMode.SURVIVAL);
            ((Player) sender).kickPlayer("You will now be able to play on the Server. Have fun!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
