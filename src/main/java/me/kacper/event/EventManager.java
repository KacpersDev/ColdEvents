package me.kacper.event;

import lombok.Getter;
import me.kacper.event.impl.GunGame;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EventManager {

    private final me.kacper.Event plugin;
    private final List<Event> events = new ArrayList<>();

    public EventManager(me.kacper.Event plugin){
        this.plugin = plugin;
        events.add(new GunGame(this.plugin));
    }

    public Event getClassByEventName(String eventName){
        if (eventName.equalsIgnoreCase("gungame")) {
            return events.get(0);
        } else if (eventName.equalsIgnoreCase("")) {
            return events.get(1);
        }
        return null;
    }
}
