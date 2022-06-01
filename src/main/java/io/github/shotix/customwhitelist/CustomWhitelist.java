package io.github.shotix.customwhitelist;

import org.bukkit.plugin.java.JavaPlugin;

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

        getCommand("join").setExecutor(new JoinCommand());
        getCommand("updateStatus").setExecutor(new UpdateStatusCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}