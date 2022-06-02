package io.github.shotix.customwhitelist;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CustomWhitelistCommands implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by administrators");
            return false;
        }
        if (args[0].toLowerCase().matches("setpassword")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.YELLOW + "Please enter a password");
                return false;
            }
            HandleFiles.setPassword(args[1]);
            sender.sendMessage("The new password is: " + ChatColor.GRAY + HandleFiles.getPassword());
            return true;
        } else if (args[0].toLowerCase().matches("getpassword")) {
            sender.sendMessage("The current password is: " + ChatColor.GRAY + HandleFiles.getPassword());
            return true;
        }
        showHelp((Player) sender);

        return false;
    }

    private void showHelp(Player player) {
        player.sendMessage(ChatColor.YELLOW + "---" + ChatColor.GOLD + " Help menu " + ChatColor.YELLOW + "---");
        player.sendMessage(ChatColor.GOLD + "/customWhitelist setPassword [password]: " + ChatColor.WHITE + "Set the join password");
        player.sendMessage(ChatColor.GOLD + "/customWhitelist getPassword: " + ChatColor.WHITE + "Get the current join password");
    }
}
