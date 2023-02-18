package me.kacper;

import lombok.Getter;
import me.kacper.command.EventCommand;
import me.kacper.event.listener.LobbyEventListener;
import me.kacper.listener.EventListener;
import me.kacper.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

@Getter
public final class Event extends JavaPlugin {

    private static Event instance;
    private final File settings = new File(getDataFolder(), "settings.yml");
    private final FileConfiguration settingsConfiguration = new YamlConfiguration();
    private final File lang = new File(getDataFolder(), "language.yml");
    private final FileConfiguration langConfiguration = new YamlConfiguration();

    @Override
    public void onEnable() {
        instance = this;
        configuration();
        command();
        listener(Bukkit.getPluginManager());

    }

    public static Event getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    private void configuration(){
        new Config(this.settings, this.settingsConfiguration, "settings.yml");
        new Config(this.lang, this.langConfiguration, "language.yml");
    }

    private void listener(PluginManager manager) {
        manager.registerEvents(new LobbyEventListener(this),this);
        manager.registerEvents(new EventListener(this),this);
    }

    private void command(){
        Objects.requireNonNull(getCommand("event")).setExecutor(new EventCommand(this));
    }
}
