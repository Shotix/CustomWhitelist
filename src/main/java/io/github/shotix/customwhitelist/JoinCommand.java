package io.github.shotix.customwhitelist;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinCommand implements CommandExecutor {

    private String playersOnWhitelist = HandleFiles.openWhitelist();
    private static String joinPassword = "2InchWheeler";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || playersOnWhitelist.contains(sender.getName())) {
            sender.sendMessage("Only valid players can run this command!");
            return false;
        }
        if (!playersOnWhitelist.contains(sender.getName())) {
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                showHelp((Player) sender);
            } else {
                boolean isPlayerOnTries = HandleFiles.isPlayerOnTries(sender.getName());

                if (!isPlayerOnTries) HandleFiles.handlePutPlayerOnTries(sender.getName());

                String passwordInput = args[0];
                if (passwordInput.matches(joinPassword)){
                    HandleFiles.putNameOnWhitelist(sender.getName());
                    sender.sendMessage(ChatColor.GREEN + "You have successfully registered yourself\nPlease use the \"/updatestatus\" command to update your status");
                    return true;
                }
                invalidPassword(sender);
            }
        }
        return false;
    }

    private void showHelp(@NotNull Player p) {
        p.sendMessage("HELP MENU NOT AVAILABLE YET");
    }

    private void invalidPassword(CommandSender c) {
        int triesRemaining = HandleFiles.handleJoinTries(c.getName());
        if (triesRemaining >= 1) c.sendMessage(ChatColor.RED + "Invalid Password! You have " + triesRemaining + " remaining.");
        else tooManyPasswordTries(c);
    }

    private void tooManyPasswordTries(CommandSender c) {
        Bukkit.getBanList(BanList.Type.NAME).addBan(c.getName(), "Too many wrong join password inputs\nPlease write an administrator to appeal!", null, "Server");
        ((Player) c).kickPlayer("You have been banned from this server because of too many invalid Password inputs.\nPlease write an administrator to appeal!");
    }
}
