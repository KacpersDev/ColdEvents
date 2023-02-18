package me.kacper.utils;

import me.kacper.Event;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    public Config(File file, FileConfiguration configuration, String dir){
        if (!(file.exists())) {
            file.getParentFile().mkdir();
            Event.getInstance().saveResource(dir, false);
        }

        try {
            configuration.load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
