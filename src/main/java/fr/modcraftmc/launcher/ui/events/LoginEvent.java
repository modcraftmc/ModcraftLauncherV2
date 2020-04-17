package fr.modcraftmc.launcher.ui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LoginEvent extends Event {

    public static final EventType<LoginEvent> LOGIN_SUCCES = new EventType(ANY, "LOGIN_SUCCES");
    public static final EventType<LoginEvent> LOGIN_FAILED = new EventType(ANY, "LOGIN_FAILED");


    public LoginEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public EventType<? extends LoginEvent> getEventType() {
        return (EventType<? extends LoginEvent>) super.getEventType();
    }
}
