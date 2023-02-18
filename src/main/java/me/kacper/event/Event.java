package me.kacper.event;

import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public interface Event {

    BukkitTask bukkitTask();
    String eventName();
    int minPlayers();
    void run();
    void start();
    void join(UUID uuid);
    void stop();
    Enum<EventStatus> status();
    void updateStats(EventStatus status);
}
