package me.kacper.event.impl;

import me.kacper.event.Event;
import me.kacper.utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GunGame implements Event {

    private final me.kacper.Event plugin;
    private BukkitTask bukkitTask;
    public final static List<UUID> users = new ArrayList<>();
    public static boolean started = false;

    public GunGame(me.kacper.Event plugin){
        this.plugin = plugin;
    }
    @Override
    public BukkitTask bukkitTask() {
        return bukkitTask;
    }

    @Override
    public String eventName() {
        return "gungame";
    }

    @Override
    public int minPlayers() {
        return me.kacper.Event.getInstance().getSettingsConfiguration().getInt("MIN-PLAYERS");
    }

    @Override
    public void join(UUID uuid) {
        users.add(uuid);
        Location lobby =  new Location(Bukkit.getWorld(Objects.requireNonNull(this.plugin.getSettingsConfiguration().getString("EVENT.GUN-GAME.SPAWN.WORLD"))),
                this.plugin.getSettingsConfiguration().getInt("EVENT.GUN-GAME.SPAWN.X"),
                this.plugin.getSettingsConfiguration().getInt("EVENT.GUN-GAME.SPAWN.Y"),
                this.plugin.getSettingsConfiguration().getInt("EVENT.GUN-GAME.SPAWN.Z"));
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).teleport(lobby);
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).getInventory().clear();
        ItemStack itemStack = new ItemStack(Material.valueOf(this.plugin.getSettingsConfiguration().getString("LOBBY.ITEM")), this.plugin.getSettingsConfiguration().getInt("LOBBY.AMOUNT"));
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(Color.translate(this.plugin.getSettingsConfiguration().getString("LOBBY.NAME")));
        ArrayList<String> lore = new ArrayList<>();
        for (final String l : this.plugin.getSettingsConfiguration().getStringList("LOBBY.LORE")) {
            lore.add(Color.translate(l));
        }
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        for (final String enchantments : this.plugin.getSettingsConfiguration().getStringList("LOBBY.ENCHANTMENTS")) {
            itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchantments.split(":")[0])), Integer.parseInt(enchantments.split(":")[1]));
        }
        Objects.requireNonNull(Bukkit.getPlayer(uuid)).getInventory().setItem(this.plugin.getSettingsConfiguration().getInt("LOBBY.SLOT"), itemStack);
    }

    @Override
    public void stop() {
        users.clear();
    }

    @Override
    public void run() {

        bukkitTask = new BukkitRunnable() {
            int timer = plugin.getSettingsConfiguration().getInt("EVENT-COUNTDOWN");
            @Override
            public void run() {

                if (timer <= 0) {
                    if (users.size() < minPlayers()) {
                        Bukkit.broadcastMessage(Color.translate(me.kacper.Event.getInstance().getLangConfiguration().getString("NOT-ENOUGH-PLAYERS")));
                        stop();
                        this.cancel();
                    }
                    start();
                    this.cancel();
                }
                timer--;

                Bukkit.broadcastMessage(String.valueOf(timer));
            }
        }.runTaskTimer(this.plugin, 0L, 20L);
    }

    @Override
    public void start() {
        Bukkit.broadcastMessage("started");
    }
}
