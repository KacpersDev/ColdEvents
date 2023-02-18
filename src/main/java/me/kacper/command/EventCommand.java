package me.kacper.command;

import lombok.Getter;
import me.kacper.Event;
import me.kacper.event.EventManager;
import me.kacper.event.EventStatus;
import me.kacper.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

@Getter
public class EventCommand implements CommandExecutor {

    private final Event plugin;
    private Inventory inventory;
    private final EventManager eventManager;

    public EventCommand(Event plugin) {
        this.plugin = plugin;
        this.eventManager = new EventManager(this.plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) return false;

        if (!(sender.hasPermission("cold.command.event"))) {
            sender.sendMessage(Color.translate(this.plugin.getLangConfiguration().getString("NO-PERMISSIONS")));
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            createInventory(player);
            applyItems();
            overlay();
            player.openInventory(inventory);
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length == 1) {
                usage(player);
            } else {
                String event = args[1];
                if (!event.equalsIgnoreCase("gungame") || !event.equalsIgnoreCase("tnttag") || !event.equalsIgnoreCase("oitc")) {
                    return false;
                }

                me.kacper.event.Event eventClass = eventManager.getClassByEventName(event);
                if (eventClass.status() == EventStatus.GAME) {
                    Bukkit.broadcastMessage("in game");
                    return false;
                }
                eventClass.join(player.getUniqueId());
            }
        }

        return true;
    }

    private void applyItems(){
        for (final String items : Objects.requireNonNull(this.plugin.getSettingsConfiguration().getConfigurationSection("INVENTORY.ITEMS")).getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(this.plugin.getSettingsConfiguration().getString("INVENTORY.ITEMS." + items + ".ITEM"))
            ,this.plugin.getSettingsConfiguration().getInt("INVENTORY.ITEMS." + items + ".AMOUNT"));
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(Color.translate(this.plugin.getSettingsConfiguration().getString("INVENTORY.ITEMS." + items + ".NAME")));
            ArrayList<String> lore = new ArrayList<>();
            for (final String l : this.plugin.getSettingsConfiguration().getStringList("INVENTORY.ITEMS." + items + ".LORE")) {
                lore.add(Color.translate(l));
            }
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            for (final String enchantments : this.plugin.getSettingsConfiguration().getStringList("INVENTORY.ITEMS." + items + ".ENCHANTMENTS")) {
                itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchantments.split(":")[0])), Integer.parseInt(enchantments.split(":")[1]));
            }
            inventory.setItem(this.plugin.getSettingsConfiguration().getInt("INVENTORY.ITEMS." + items + ".SLOT"), itemStack);
        }
    }
    private void createInventory(Player player){
        inventory = Bukkit.createInventory(player, this.plugin.getSettingsConfiguration().getInt("INVENTORY.SIZE"), Color.translate(this.plugin.getSettingsConfiguration()
                .getString("INVENTORY.TITLE")));
    }

    private void overlay(){
        if (this.plugin.getSettingsConfiguration().getBoolean("INVENTORY.OVERLAY")) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, new ItemStack(Material.valueOf(this.plugin.getSettingsConfiguration().getString("INVENTORY.OVERLAY-ITEM"))));
                }
            }
        }
    }

    private void usage(Player player){
        for (final String l : this.plugin.getLangConfiguration().getStringList("USAGE")) {
            player.sendMessage(Color.translate(l));
        }
    }
}
