package me.kacper.listener;

import lombok.Getter;
import me.kacper.Event;
import me.kacper.event.impl.GunGame;
import me.kacper.utils.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

@Getter
public class EventListener implements Listener {

    private final Event plugin;

    public EventListener(Event plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();

        if (event.getView().getTitle().equalsIgnoreCase(Color.translate(this.plugin.getSettingsConfiguration().getString("INVENTORY.TITLE")))) {
            event.setCancelled(true);
        }

        for (final String items : Objects.requireNonNull(this.plugin.getSettingsConfiguration().getConfigurationSection("INVENTORY.ITEMS")).getKeys(false)) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Color.translate(this.plugin.getSettingsConfiguration().getString("INVENTORY.ITEMS." + items + ".NAME")))) {
                String eventName = this.plugin.getSettingsConfiguration().getString("INVENTORY.ITEMS." + items + ".EVENT");
                String permission = this.plugin.getSettingsConfiguration().getString("INVENTORY.ITEMS." + items + ".PERMISSION");

                if (!player.hasPermission(Objects.requireNonNull(permission))) {
                    return;
                }

                if (Objects.requireNonNull(eventName).equalsIgnoreCase("gungame") && !GunGame.started) {
                    GunGame gungame = new GunGame(this.plugin);
                    gungame.run();
                    gungame.join(player.getUniqueId());
                    GunGame.started = true;
                }
            }
        }
    }
}
