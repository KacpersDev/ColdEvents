package me.kacper.event.listener;

import lombok.Getter;
import me.kacper.Event;
import me.kacper.event.impl.GunGame;
import me.kacper.utils.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class LobbyEventListener implements Listener {

    private final Event plugin;

    public LobbyEventListener(Event plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null || event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) return;
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(this.plugin.getSettingsConfiguration().getString("LOBBY.NAME")))) {
                GunGame.users.remove(event.getPlayer().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) return;
        if (GunGame.users.contains(event.getEntity().getUniqueId()) && !(GunGame.started)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) return;
        if (GunGame.users.contains(event.getEntity().getUniqueId()) && !(GunGame.started)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) return;
        if (GunGame.users.contains(event.getEntity().getUniqueId()) && !(GunGame.started)) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }
}
