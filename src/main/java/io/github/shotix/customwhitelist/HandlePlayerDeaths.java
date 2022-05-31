package io.github.shotix.customwhitelist;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HandlePlayerDeaths extends Listeners{

    //TODO: Write custom death messages for each player on the server / whitelist
    public static void hasBeenKilledByPlayer(Player p, Player pK) {

        // Check if the player is on the whitelist



        if (p.getName() == "PostMarlon") {
            Bukkit.broadcastMessage("Marlon ist wegen krankhafter Eifersucht gestorben");
            //Only execute when Sh0tix is online
            if (Bukkit.getServer().getPlayer("Sh0tix") != null && pK.getName() != "Buttyman") p.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1));
        }
        if (p.getName() == "TCrossR") {
            Bukkit.broadcastMessage("Hat einen Menschen seelisch und psychisch kaputt gemacht");
        }
        if (pK.getName() == "Buttyman") {
            Bukkit.broadcastMessage("Marvin hat gerade jemanden umgebracht. Dies wird bestraft!");
            Bukkit.getServer().getPlayer("Buttyman").setDisplayName("PLAYER KILLER");
            Bukkit.getServer().getPlayer("Buttyman").playSound(pK.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 10.0f, 1.0f);
        }
    }


    public static void hasBeenKilledByEntity(Player p, Entity e) {

        /* TODO: Write custom death message for each common entity type
            Entity Types already done:
            - Creeper (ID 50)
            - Skeleton (ID 51)
            Still to do:
            - Spider (ID 52)
            - Zombie (ID 54)
            -
            -
            -
     */

        if (e.getEntityId() == 50) Bukkit.broadcastMessage(p.getDisplayName() + " wurde in die Lüfte katapultiert");
        if (e.getEntityId() == 51) Bukkit.broadcastMessage(p.getDisplayName() + " wurde genoscoped");
    }
}
