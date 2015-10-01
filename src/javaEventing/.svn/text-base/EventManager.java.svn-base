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
import javaEventing.internals.EventManagerExtension;


public class EventManager {
    private static EventManagerInstance eventManager = new EventManagerInstance();

    /**
     * Returns the EventManagerInstance used by this class.
     *
     * @return EventManagerInstance.
     */
    public static EventManagerInstance getEventManagerInstance() {
        return eventManager;
    }

    public static void registerEventListener(GenericEventListener receiver, Class<? extends Event> eventClass) {
        eventManager.registerEventListener(receiver, eventClass);
    }

      /**
     * Registers an event listener, and binds it to a specific type of event. Define your own types of events by creating classes imlement the Event interface.
     * @param context Any type of object, which will serve as a context for your event listeners. Useful when bulk-unregistering event listeners. (usage: unregisterAllEventListenersForContext("myContext") )
     * Note: Context-matching is done using object.equals(object), not object references.
     * @param receiver The callback object that will be called once the event is triggered. This may typically be an anonymous implementation of the class.
     * @param eventClass The type of Event that is subscribed to. 
     */
    public static void registerEventListener(Object context, GenericEventListener receiver, Class<? extends Event> eventClass) {
        eventManager.registerEventListener(context,receiver, eventClass);
    }


    public static void unregisterAllEventSubscriptions() {
        eventManager.unregisterAllEventSubscriptions();
    }


    /**
     * Registers an event listener, and binds it to a specific type of event and provides a condition.
     * @param receiver The callback object that will be called once the event is triggered. This may typically be an anonymous implementation of the class.
     * @param eventClass The type of Event that is subscribed to.
     * @param condition A condition that must be true for the EventListener to be called.
     */
    public static void registerEventListener(GenericEventListener receiver, Class<? extends Event> eventClass, Condition condition) {
        eventManager.registerEventListener(receiver, eventClass, condition);
    }

    /**
     * Registers an event listener, and binds it to a specific type of event and provides a condition.
     * @param context Any type of object, which will serve as a context for your event listeners. Useful when bulk-unregistering event listeners. (usage: unregisterAllEventListenersForContext("myContext") )
     * Note: Context-matching is done using object.equals(object), not object references.
     * @param receiver The callback object that will be called once the event is triggered. This may typically be an anonymous implementation of the class.
     * @param eventClass The type of Event that is subscribed to.
     * @param condition A condition that must be true for the EventListener to be called.
     */
    public static void registerEventListener(Object context, GenericEventListener receiver, Class<? extends Event> eventClass, Condition condition) {
        eventManager.registerEventListener(context, receiver, eventClass, condition);
    }

    /**
     * Unregisters an event listener bound to an event
     * @param receiver The EventListener registered for a particular event.
     * @param eventClass The type of Event that is subscribed to.
     */
    public static void unregisterEventListener(GenericEventListener receiver, Class<? extends Event> eventClass) {
        eventManager.unregisterEventListener(receiver, eventClass);
    }

    /**
     * Unregisters all event listeners associated with a specific context (event listeners registered with EventManager.registerEventListener("someContext", myEventListener...) )
     * @param context The object instance (or an object that .equals(object)==true) that was used to register the event listener.
     */
    public static void unregisterAllEventListenersForContext(Object context) {
        eventManager.unregisterAllEventListenersForContext(context);
    }

     /**
     * A blocking call, freezing the current thread execution until the event is triggered. May be used for thread synchronization.
     * @param event The type of Event that is subscribed to.
     * @param timeout The number of milliseconds before the blocking call will resume regardless of whether the event was triggered or not. If zero, it will never time out.
     */
    public static boolean waitUntilTriggered(Class<? extends Event> event, long timeout) {
        return eventManager.waitUntilTriggered(event, timeout);

    }

    /**
     * A blocking call, freezing the current thread execution until the event is triggered. May be used for thread synchronization.
     * @param eventClass The type of Event that is subscribed to.
     * @param timeout The number of milliseconds before the blocking call will resume regardless of whether the event was triggered or not. If zero, it will never time out.
     * @param condition A condition that must be true for the call to unblock.
     */
    public static boolean waitUntilTriggered(Class<? extends Event> eventClass, long timeout, Condition condition) {
        return eventManager.waitUntilTriggered(eventClass, timeout, condition);
    }

    /**
     * Triggers an event.
     * @param sender The object instance triggering the event.
     * @param event An instance of the type of Event that is triggered.
     */
    public static void triggerEvent(final Object sender, final Event event) {
        eventManager.triggerEvent(sender, event, null);
    }

     /**
     * Triggers an event.
     * @param sender The object instance triggering the event.
     * @param event An instance of the type of Event that is triggered.
     * @param conditionalExpression An object of any type. Will be checked by any event listeners providing Condition-objects.
     */
    public static void triggerEvent(final Object sender, final Event event, final Object conditionalExpression) {
        eventManager.triggerEvent(sender, event, conditionalExpression);
    }


    public static EventManagerExtension getEventManagerExtension() {
        return eventManager.getEventManagerExtension();
    }

    public static void setEventManagerExtension(EventManagerExtension eventManagerExtension) {
        eventManager.setEventManagerExtension(eventManagerExtension);
    }

}
