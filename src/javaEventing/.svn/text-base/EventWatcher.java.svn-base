/*
    Copyright 2011 Espen Skjervold

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */
package javaEventing;

import javaEventing.interfaces.Condition;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

/**
 * Helper-class for subscribing to events.
 */
public class EventWatcher {

    private boolean triggered = false;
    private final Object lockObject = new Object();
    private Class<? extends Event> eventClass;
    private Condition condition;
    private GenericEventListener eventListener;
    private Object eventPayload;
    private EventManagerInstance eventManager;

    /**
     * Creates an event watcher, and registers an event listener for a specified event. The event watcher can later be checked to determine whether the event has been triggered or not.
     * @param eventManager An instance of EventManagerInstance where the event will be registered.
     * @param eventClass The type of Event that the event watcher will subscribe to. E.g. MyEvent.class.
     */
    public EventWatcher(EventManagerInstance eventManager, Class<? extends Event> eventClass) {
        this(eventManager, eventClass, null);
    }

    /**
     * Creates an event watcher, and registers an event listener for a specified event. The event watcher can later be checked to determine whether the event has been triggered or not.
     * @param eventManager An instance of EventManagerInstance where the event will be registered.
     * @param event An instance of the type of Event that the event watcher will subscribe to. This does not need to be the same Event instance that is used to trigger the event,
    it may be another instance of the same Event type (the class inheriting the Event class).
     * @param eventClass The type of Event that the event watcher will subscribe to. E.g. MyEvent.class.
     * @param condition A condition that must be true for the event watcher's internal eventListener to be called.
     */
    public EventWatcher(EventManagerInstance eventManager, Class<? extends Event> eventClass, Condition condition) {
        this.eventManager = eventManager;
        this.eventClass = eventClass;
        this.condition = condition;

        registerEventListener(eventClass, condition);
    }

    private synchronized void registerEventListener(Class<? extends Event> event, Condition condition) {
        createEventListener();

        if (condition != null) {
            eventManager.registerEventListener(eventListener, eventClass, condition);
        } else {
            eventManager.registerEventListener(eventListener, eventClass);
        }
    }

    private synchronized void createEventListener() {
        eventListener = new GenericEventListener() {
            public synchronized void eventTriggered(Object sender, Event event) {
                synchronized (lockObject) {
                    eventPayload = event.getPayload();
                    triggered = true;
                    lockObject.notifyAll();
                }
            }
        };
    }

    /**
     * Unregisters the event watchers internal event listener. Remember to call this method when finished with an event watcher object, in order to allow the garbage collector to destroy it.
     */
    public synchronized void unregisterEvent() {
        eventManager.unregisterEventListener(eventListener, eventClass);
    }

    /**
     *  Returns true if the event has allready been triggered and the event watcher has been notified.
     */
    public boolean hasBeenTriggered() {
        if (triggered) {
            triggered = false; //reset
            return true;
        } else {
            return false;
        }
    }

    /**
     *  Blocking call. Freezes the current thread's execution, and waits until the event is triggered or the event watcher times out. When the call unblocks, the internal event listener is automatically unregistered.
     *  @param timeout The number of milliseconds before unblocking the call, regardless of whether or not the event is triggered. If zero, it will never time out.
     */
    public boolean waitUntilTriggeredThenUnregister(long timeout) {
        boolean result = waitUntilTriggered(timeout);
        unregisterEvent();
        return result;
    }

    /**
     *  Blocking call. Freezes the current thread's execution, and waits until the event is triggered or the event watcher times out.
     *  @param timeout The number of milliseconds before unblocking the call, regardless of whether or not the event is triggered. If zero, it will never time out.
     */
    public boolean waitUntilTriggered(long timeout) {

        if (hasBeenTriggered()) {
            return true;         //return immediately if allready triggered
        }
        try {
            synchronized (lockObject) {
                lockObject.wait(timeout);
            }
        } catch (InterruptedException e) {
        }


        return hasBeenTriggered();
    }

    /**
     *  Allows re-using an event watcher object after being triggered and notified.
     */
    public void reEnableEventWatcher() {
        triggered = false;
        registerEventListener(eventClass, condition);
    }

    /**
     * Get event payload.
     * @return event payload.
     */
    public Object getEventPayload() {
        return eventPayload;
    }

    /**
     * Get the event instance.
     * @return instance of event.
     */
    public Class<? extends Event> getEvent() {
        return eventClass;
    }
}
