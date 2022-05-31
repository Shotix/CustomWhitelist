package io.github.shotix.customwhitelist;

import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HandlePlayerNotOnWhitelist {

    public static void initialiseHandling(PlayerJoinEvent playerJoinEvent) {
        playerJoinEvent.getPlayer().setGameMode(GameMode.SPECTATOR);
        playerJoinEvent.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public static void blockMovement(PlayerMoveEvent playerMoveEvent) {
        playerMoveEvent.getPlayer().teleport(playerMoveEvent.getFrom());
    }

}
