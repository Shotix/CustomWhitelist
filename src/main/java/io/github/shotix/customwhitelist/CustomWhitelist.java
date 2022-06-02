package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CustomWhitelist extends JavaPlugin {

    private static CustomWhitelist instance;

    public static CustomWhitelist getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        getServer().getPluginManager().registerEvents(new Listeners(), this);

        Objects.requireNonNull(getCommand("join")).setExecutor(new JoinCommand());
        Objects.requireNonNull(getCommand("updateStatus")).setExecutor(new UpdateStatusCommand());
        Objects.requireNonNull(getCommand("customWhitelist")).setExecutor(new CustomWhitelistCommands());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}