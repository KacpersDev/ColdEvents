package me.kacper.event.listener;

import lombok.Getter;
import me.kacper.Event;
import me.kacper.event.EventManager;
import me.kacper.event.impl.GunGame;
import me.kacper.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

@Getter
public class LobbyEventListener implements Listener {

    private final Event plugin;
    private final EventManager eventManager;

    public LobbyEventListener(Event plugin) {
        this.plugin = plugin;
        this.eventManager = new EventManager(this.plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (event.getPlayer().getInventory().getItemInMainHand() == null || event.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null || event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() == null) return;
            if (event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(this.plugin.getSettingsConfiguration().getString("LOBBY.NAME")))) {
                GunGame.users.remove(event.getPlayer().getUniqueId());
                event.getPlayer().getInventory().clear();
                event.getPlayer().teleport(new Location(Bukkit.getWorld(
                        Objects.requireNonNull(this.plugin.getSettingsConfiguration().getString("SPAWN.WORLD"))),
                        this.plugin.getSettingsConfiguration().getInt("SPAWN.X"),
                        this.plugin.getSettingsConfiguration().getInt("SPAWN.Y"),
                        this.plugin.getSettingsConfiguration().getInt("SPAWN.Z")));
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) return;
        if (GunGame.users.contains(event.getEntity().getUniqueId())) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) return;
        if (GunGame.users.contains(event.getEntity().getUniqueId()) ) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByBlockEvent event) {
        if (event.getEntity() == null || !(event.getEntity() instanceof Player)) return;
        if (GunGame.users.contains(event.getEntity().getUniqueId())) {
            event.setDamage(0);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (GunGame.users.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockPlaceEvent event) {
        if (GunGame.users.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
