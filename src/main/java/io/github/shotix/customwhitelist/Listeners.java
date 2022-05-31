package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.TimeUnit;

public class Listeners implements Listener {

    public static String playersOnWhitelist = "";

    @EventHandler
    public void event(PlayerJoinEvent playerJoinEvent) throws InterruptedException {
        playersOnWhitelist = playersOnWhitelist + HandleFiles.openWhitelist();

        if (playersOnWhitelist == "WhitelistNotFound") {
            Bukkit.broadcastMessage("Es konnte keine Whitelist gefunden werden. Der Server wird in 10 Sekunden beendet und wenden Sie sich an einen Administrator des Servers!");
            TimeUnit.SECONDS.sleep(10);
            Bukkit.shutdown();
        }

        if (!playersOnWhitelist.contains(playerJoinEvent.getPlayer().getName()))
            HandlePlayerNotOnWhitelist.initialiseHandling(playerJoinEvent);
    }

    @EventHandler
    public void event(PlayerMoveEvent playerMoveEvent) {
        if (!playersOnWhitelist.contains(playerMoveEvent.getPlayer().getName()))
            HandlePlayerNotOnWhitelist.blockMovement(playerMoveEvent);
    }

    @EventHandler
    public void event(PlayerDeathEvent playerDeathEvent) {
        if (playerDeathEvent.getEntity().getKiller() instanceof Player)
            HandlePlayerDeaths.hasBeenKilledByPlayer(playerDeathEvent.getEntity(), playerDeathEvent.getEntity().getKiller());
        else if (playerDeathEvent.getEntity().getKiller() instanceof Entity)
            HandlePlayerDeaths.hasBeenKilledByEntity(playerDeathEvent.getEntity(), playerDeathEvent.getEntity().getKiller());
    }
}
