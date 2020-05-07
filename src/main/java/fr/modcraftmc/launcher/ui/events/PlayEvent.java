package fr.modcraftmc.launcher.ui.events;

import fr.modcraftmc.launcher.core.servers.Server;
import javafx.event.Event;
import javafx.event.EventType;

public class PlayEvent extends Event {

    public static final EventType<PlayEvent> PLAY = new EventType(ANY, "PLAY");
    private Server server;

    public PlayEvent(Server server) {
        super(PLAY);
        this.server = server;
    }


    public PlayEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public void setServer(Server value) {
        this.server = value;
    }

    public Server getServer() {
        return server;
    }

    public EventType<? extends PlayEvent> getEventType() {
        return (EventType<? extends PlayEvent>) super.getEventType();
    }
}
