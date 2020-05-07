package fr.modcraftmc.launcher.ui.events;

import javafx.event.Event;
import javafx.event.EventType;

public class OptionsEvent extends Event {

    public static final EventType<OptionsEvent> NO = new EventType(ANY, "NO");

    public OptionsEvent() {
        super(NO);
    }


    public EventType<? extends OptionsEvent> getEventType() {
        return (EventType<? extends OptionsEvent>) super.getEventType();
    }
}
