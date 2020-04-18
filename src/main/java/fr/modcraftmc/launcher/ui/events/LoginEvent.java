package fr.modcraftmc.launcher.ui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class LoginEvent extends Event {

    public static final EventType<LoginEvent> LOGIN = new EventType(ANY, "LOGIN");
    public boolean succes = false;

    public LoginEvent() {
        super(LOGIN);
    }


    public LoginEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public void setSucces(boolean value) {
        this.succes = value;
    }

    public boolean getSucces() {
        return this.succes;
    }

    public EventType<? extends LoginEvent> getEventType() {
        return (EventType<? extends LoginEvent>) super.getEventType();
    }
}
