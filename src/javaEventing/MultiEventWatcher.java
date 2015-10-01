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

import java.util.ArrayList;
import java.util.List;

/**
 * Helper-class for subscribing to multiple events. Use this class when actions should be taken if event x OR event y is triggered.
 */

public class MultiEventWatcher {

    private List<EventAndCondition> eventsAndConditions;
    private final Object lockobject  =new Object();
    private boolean hasBeenTriggered;
    private Event theEventThatWasTriggered;
    private EventManagerInstance eventManager;

    public MultiEventWatcher(EventManagerInstance eventManager) {
        eventsAndConditions = new ArrayList<EventAndCondition>();
        this.eventManager = eventManager;
    }

     /**
     * Registers an event for the multi-event watcher to watch. Any number of events can be registered.
     * @param event The events the multi-event watcher will subsribe to.
     */
    public void addEvent(Class<? extends Event> eventClass) {
        addEvent(eventClass, null);
    }

     /**
     * Registers an event for the multi-event watcher to watch. Any number of events can be registered.
     * @param event The events the multi-event watcher will subsribe to.
     * @param condition Each registered event may have a specific condition in order for the watcher to be notified. One may mix and match with events with and without condtions.
     */
    public void addEvent(Class<? extends Event> eventClass, Condition condition) {
        registerEventListenerForEvent(eventClass, condition);
    }

    /**
     * Blocking call. The thread execution will freeze until one of the registered events are triggered.
     * @param timeout The number of milliseconds before unblocking the call, regardless of whether or not any of the events are triggered. If zero, it will never time out.
     */
    public boolean waitForAnyEvent(long timeout) {

        if (hasBeenTriggered)
            return true;


        synchronized (lockobject) {
            try {
                lockobject.wait(timeout);
            } catch (InterruptedException e) {}
        }


        return hasBeenTriggered;

    }


    /**
        * Blocking call. The thread execution will freeze until all of the registered events are triggered.
        * @param timeout The number of milliseconds before unblocking the call, regardless of whether or not all the events have been triggered. If zero, it will never time out.
        */
       public boolean waitForAllEvents(long timeout) {

           if (hasBeenTriggered)
               return true;

           long start = System.currentTimeMillis();
           synchronized (lockobject) {
               try {
                   while (!areAllEventsTriggered() && (timeout>0 && System.currentTimeMillis()-start < timeout))
                       lockobject.wait(timeout);

               } catch (InterruptedException e) {}
           }


           hasBeenTriggered = areAllEventsTriggered();

            return hasBeenTriggered;

       }


   private boolean areAllEventsTriggered() {
        for (EventAndCondition eventAndCondition : eventsAndConditions) {
            if (!eventAndCondition.triggered)
                return false;
        }

       return true;
   }


     /**
        * Blocking call. The thread execution will freeze until all of the registered events are triggered. After notified, the watcher will automatically unregister internal listeners.
        * @param timeout The number of milliseconds before unblocking the call, regardless of whether or not all the events have been triggered. If zero, it will never time out.
        */
    public boolean waitForAllEventsThenUnregister(long timeout) {
        boolean res = waitForAllEvents(timeout);

        for (EventAndCondition eventAndCondition: eventsAndConditions) {
            eventManager.unregisterEventListener(eventAndCondition.genericEventListener, eventAndCondition.eventClass);
        }

        return res;
    }

     /**
     * Blocking call. The thread execution will freeze until one of the registered events are triggered. After notified, the watcher will automatically unregister its internal listener.
     * @param timeout The number of milliseconds before unblocking the call, regardless of whether or not any of the events are triggered. If zero, it will never time out.
     */
    public boolean waitForAnyEventThenUnregister(long timeout) {
        boolean res = waitForAnyEvent(timeout);

        for (EventAndCondition eventAndCondition: eventsAndConditions) {
            eventManager.unregisterEventListener(eventAndCondition.genericEventListener, eventAndCondition.eventClass);
        }

        return res;
    }

    /**
     *  Returns true if the event has allready been triggered and the event watcher has been notified.
     */
    public boolean hasBeenTriggered() {
        return hasBeenTriggered;
    }

    /**
     *  Returns the triggered event
     */
    public Event getTriggeredEvent() {
        return theEventThatWasTriggered;
    }


    /**
     *  Returns the event's payload
     */
    public Object getTriggeredEventPayload() {
        return theEventThatWasTriggered.getPayload();
    }


    private void registerEventListenerForEvent(Class<? extends Event> eventClass, Condition condition) {
        GenericEventListener genericEventListener = new GenericEventListener() {
                public void eventTriggered(Object sender, Event event) {
                    hasBeenTriggered=true;
                    theEventThatWasTriggered=event;
                    for (EventAndCondition eventAndCondition : eventsAndConditions) {
                        if (eventAndCondition.eventClass == event.getClass())
                            eventAndCondition.triggered=true;
                    }
                    synchronized (lockobject) {
                        lockobject.notifyAll();
                    }
                }
            };

        if (condition ==null )
            eventManager.registerEventListener(genericEventListener, eventClass);
        else
            eventManager.registerEventListener(genericEventListener, eventClass, condition);


        EventAndCondition eventAndCondition = new EventAndCondition(eventClass, condition);
        eventAndCondition.genericEventListener=genericEventListener;

        eventsAndConditions.add(eventAndCondition);
    }

    /**
     *  Allows re-using an event watcher object after being triggered and notified.
     */
    public void reEnableMultiEventWatcher() {
        hasBeenTriggered=false;
        theEventThatWasTriggered=null;

        for (EventAndCondition eventAndCondition: eventsAndConditions) {
            eventManager.registerEventListener(eventAndCondition.genericEventListener, eventAndCondition.eventClass);
        }
    }

    private class EventAndCondition {
        public Class<? extends Event> eventClass;
        public Condition condition;
        public GenericEventListener genericEventListener;
        public boolean triggered;

        public EventAndCondition(Class<? extends Event> eventClass, Condition condition) {
            this.eventClass=eventClass;
            this.condition = condition;
        }
    }

}
