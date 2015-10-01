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
package javaEventing.EventManagerTests;


import javaEventing.EventWatcher;
import javaEventing.MultiEventWatcher;
import javaEventing.interfaces.Condition;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import javaEventing.EventManagerInstance;
import javaEventing.EventObject;

public class EventingTests extends TestCase {

    public void testReceiveEvent() {

        EventManagerInstance instance = new EventManagerInstance();

        try {
            final BlockingQueue queue = new ArrayBlockingQueue(10);

            class MyEvent extends EventObject {
            }

            instance.registerEventListener(new GenericEventListener() {

                public void eventTriggered(Object sender, Event event) {
                    try {
                        queue.put(event);
                    } catch (InterruptedException e) {
                    }
                }
            }, MyEvent.class);

            instance.triggerEvent(this, new MyEvent());


            Event receivedEvent = null;


            receivedEvent = (MyEvent) queue.poll(1000, TimeUnit.MILLISECONDS);

            assertNotNull(receivedEvent);

        } catch (InterruptedException e) {
            assertTrue(false);
        }


    }

    public void testReceiveEventWithConditions() {
        EventManagerInstance instance = new EventManagerInstance();

        try {
            final BlockingQueue queue = new ArrayBlockingQueue(10);

            class MyEvent extends EventObject {
            }

            final Object parent = this;

            Condition condition = new Condition() {         // <-- I create an anonymous implementation of the Condition interface.

                public boolean matches(Object sender, Event event, Object conditionalExpression) {
                    return conditionalExpression.equals("someExpression")
                            && sender.equals(parent)
                            && event instanceof MyEvent;         // <-- I create my own conditional test. Here I return true if the Condition object EQUALS my expression.
                }
            };


            instance.registerEventListener(new GenericEventListener() {

                public void eventTriggered(Object sender, Event event) {
                    try {
                        queue.put(event);
                    } catch (InterruptedException e) {
                    }
                }
            }, MyEvent.class, condition);


            instance.triggerEvent(this, new MyEvent());

            Event receivedEvent = null;

            receivedEvent = (MyEvent) queue.poll(1000, TimeUnit.MILLISECONDS);

            assertNull(receivedEvent);

            instance.triggerEvent(this, new MyEvent(), "someExpression");

            receivedEvent = (MyEvent) queue.poll(1000, TimeUnit.MILLISECONDS);

            assertNotNull(receivedEvent);

        } catch (InterruptedException e) {
            assertTrue(false);
        }


    }

    public void testNotReceiveEvent() {
        EventManagerInstance instance = new EventManagerInstance();

        //tests that one do not receive events one have not subscribed to

        try {
            final BlockingQueue queue = new ArrayBlockingQueue(10);

            TestEvent testEvent = new TestEvent();

            instance.registerEventListener(new GenericEventListener() {

                public void eventTriggered(Object sender, Event event) {
                    try {
                        queue.put(event);
                    } catch (InterruptedException e) {
                    }
                }
            }, TestEvent.class);


            instance.triggerEvent(this, new TestEvent2());


            TestEvent receivedEvent = null;


            receivedEvent = (TestEvent) queue.poll(1000, TimeUnit.MILLISECONDS);


            assertNull(receivedEvent);



        } catch (InterruptedException e) {
            assertTrue(false);
        }


    }

    public void testGenericEvent() {
        EventManagerInstance instance = new EventManagerInstance();

        //Here we don't bother with defining a new Event type by declaring a class and making in inherit Event, we simply use the EventObject class directly.

        final BlockingQueue queue = new ArrayBlockingQueue(10);


        instance.registerEventListener(new GenericEventListener() {
            public void eventTriggered(Object sender, Event event) {
                try {
                    queue.put(event);
                } catch (InterruptedException e) {
                }
            }
        }, EventObject.class);


        instance.triggerEvent(this, new EventObject());


        Event receivedEvent = null;


        try {

            receivedEvent = (Event) queue.poll(1000, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        assertNotNull(receivedEvent);

    }

    public void testUnregisterEventListener() {
        EventManagerInstance instance = new EventManagerInstance();
        //tests that one do not receive events one have not subscribed to

        try {
            final BlockingQueue queue = new ArrayBlockingQueue(10);

            TestEvent testEvent = new TestEvent();

            GenericEventListener eventListener = new GenericEventListener() {

                public void eventTriggered(Object sender, Event event) {
                    try {
                        queue.put(event);
                    } catch (InterruptedException e) {
                    }
                }
            };

            instance.registerEventListener(eventListener, TestEvent.class);

            instance.unregisterEventListener(eventListener, TestEvent.class);

            instance.triggerEvent(this, new TestEvent());

            TestEvent receivedEvent = null;

            receivedEvent = (TestEvent) queue.poll(1000, TimeUnit.MILLISECONDS);

            assertNull(receivedEvent);

        } catch (InterruptedException e) {
            assertTrue(false);
        }


    }

    public void testConditionalEvents() {
        EventManagerInstance instance = new EventManagerInstance();

        try {
            final BlockingQueue queue = new ArrayBlockingQueue(10);

            final TestEvent testEvent = new TestEvent();

            final String expression = "hubba";

            final Object parent = this;


            Condition condition = new Condition() {

                public boolean matches(Object sender, Event event, Object conditionalExpression) {
                    return expression.equals(conditionalExpression)
                            && sender.equals(parent)
                            && event instanceof TestEvent;
                }
            };


            instance.registerEventListener(new GenericEventListener() {
                public void eventTriggered(Object sender, Event event) {
                    try {
                        queue.put(event);
                    } catch (InterruptedException e) {
                    }
                }
            }, TestEvent.class, condition);


            instance.triggerEvent(this, new TestEvent(), expression);

            TestEvent receivedEvent = null;

            receivedEvent = (TestEvent) queue.poll(1000, TimeUnit.MILLISECONDS);
            assertNotNull(receivedEvent);
            queue.clear();

            instance.triggerEvent(this, new TestEvent(), "somethingTotallyDifferent!");

            receivedEvent = (TestEvent) queue.poll(1000, TimeUnit.MILLISECONDS);
            assertNull(receivedEvent);

        } catch (InterruptedException e) {
            assertTrue(false);
        }


    }

    public void testEventWatcher() {
        EventManagerInstance instance = new EventManagerInstance();

        TestEvent testEvent = new TestEvent();

        EventWatcher eventWatcher = new EventWatcher(instance, TestEvent.class);

        assertFalse(eventWatcher.hasBeenTriggered());

        instance.triggerEvent(this, new TestEvent());

        doSleep(200);     //give the event time to progagate

        assertTrue(eventWatcher.hasBeenTriggered());

        assertFalse(eventWatcher.hasBeenTriggered()); //has now allready been consumed

    }

    public void testEventWatcherSynchronous() throws Exception {
        EventManagerInstance instance = new EventManagerInstance();

        EventWatcher eventWatcher = new EventWatcher(instance, TestEvent.class);

        assertFalse(eventWatcher.hasBeenTriggered());


        instance.triggerEvent(this, new TestEvent());


        boolean triggered = eventWatcher.waitUntilTriggered(500);

        //eventWatcher.waitUntilTriggered(500);//should produce an error stacktrace printout


        assertTrue(triggered);

        eventWatcher.reEnableEventWatcher();
        assertFalse(eventWatcher.hasBeenTriggered()); //has not been re-triggered

        triggered = eventWatcher.waitUntilTriggered(200); //WILL timeout, no new event triggered

        assertFalse(triggered);


    }

    public void testEventWatcherUnregisterEvent() throws Exception {
        EventManagerInstance instance = new EventManagerInstance();

        EventWatcher eventWatcher = new EventWatcher(instance, TestEvent.class);

        assertFalse(eventWatcher.hasBeenTriggered());

        instance.triggerEvent(this, new TestEvent());

        boolean triggered = eventWatcher.waitUntilTriggered(500);

        assertTrue(triggered);

        eventWatcher.unregisterEvent();
        instance.triggerEvent(this, new TestEvent());
        triggered = eventWatcher.waitUntilTriggered(500);

        assertFalse(triggered);

    }

    public void testReuseEventWatcher() throws Exception {
        EventManagerInstance instance = new EventManagerInstance();

        EventWatcher eventWatcher = new EventWatcher(instance, TestEvent.class);

        assertFalse(eventWatcher.hasBeenTriggered());

        instance.triggerEvent(this, new TestEvent());

        boolean triggered = eventWatcher.waitUntilTriggered(500);

        assertTrue(triggered);

        instance.triggerEvent(this, new TestEvent());

        triggered = eventWatcher.waitUntilTriggered(500);

        assertTrue(triggered);

    }

    public void testEventWatcherWaitThenUnregister() throws Exception {
        EventManagerInstance instance = new EventManagerInstance();

        EventWatcher eventWatcher = new EventWatcher(instance, TestEvent.class);

        assertFalse(eventWatcher.hasBeenTriggered());

        instance.triggerEvent(this, new TestEvent("hoho"));

        boolean triggered = eventWatcher.waitUntilTriggeredThenUnregister(500);

        assertTrue(triggered);

        instance.triggerEvent(this, new TestEvent());

        triggered = eventWatcher.waitUntilTriggered(500);

        assertFalse(triggered);

    }

    public void testBlockingWaits() throws Exception {

        EventManagerInstance instance = new EventManagerInstance();

        triggerDelayed(instance, new TestEvent(), null);

        boolean triggered = instance.waitUntilTriggered(TestEvent.class, 1000);

        assertTrue(triggered);


        triggerDelayed(instance, new TestEvent(), null);

        triggered = instance.waitUntilTriggered(TestEvent.class, 1);   //only one millisec wait

        assertFalse(triggered);   //should be false, the event should be triggered after the wait has timed out

    }

    public void testBlockingWaitsWithCondition() throws Exception {
        EventManagerInstance instance = new EventManagerInstance();
        final String expression = "something";

        Condition condition = new Condition() {

            public boolean matches(Object sender, Event event, Object conditionalExpression) {
                return expression.equals(conditionalExpression);
            }
        };

        triggerDelayed(instance, new TestEvent(), expression);

        boolean triggered = instance.waitUntilTriggered(TestEvent.class, 1000, condition);

        assertTrue(triggered);


        triggerDelayed(instance,new TestEvent(), null);

        triggered = instance.waitUntilTriggered(TestEvent.class, 500, condition);

        assertFalse(triggered);   //wrong (no) condition

    }

    public void testMultiEventWatcher() {
        EventManagerInstance instance = new EventManagerInstance();

        MultiEventWatcher multiEventWatcher = new MultiEventWatcher(instance);
        multiEventWatcher.addEvent(TestEvent.class);
        multiEventWatcher.addEvent(TestEvent2.class);

        String payload = "testing";
        triggerDelayed(instance, new TestEvent2(payload), null);
        multiEventWatcher.waitForAnyEventThenUnregister(500);

        assertTrue(multiEventWatcher.hasBeenTriggered());
        assertTrue(multiEventWatcher.getTriggeredEvent().getClass() == TestEvent2.class);
        assertTrue(multiEventWatcher.getTriggeredEventPayload().toString().equals(payload));


        triggerDelayed(instance, new TestEvent(), null);
        multiEventWatcher.reEnableMultiEventWatcher(); //re-use
        multiEventWatcher.waitForAnyEventThenUnregister(500);

        assertTrue(multiEventWatcher.hasBeenTriggered());
        assertTrue(multiEventWatcher.getTriggeredEvent().getClass() == TestEvent.class);


        multiEventWatcher.reEnableMultiEventWatcher();
        multiEventWatcher.waitForAnyEventThenUnregister(1);

        assertFalse(multiEventWatcher.hasBeenTriggered());

    }

    public void testMultiEventWatcherWaitForALLevents() {
        EventManagerInstance instance = new EventManagerInstance();

        MultiEventWatcher multiEventWatcher = new MultiEventWatcher(instance);
        multiEventWatcher.addEvent(TestEvent.class);
        multiEventWatcher.addEvent(TestEvent2.class);

        String payload = "testing";
        triggerDelayed(instance, new TestEvent2(payload), null);

        multiEventWatcher.waitForAllEvents(500);

        assertFalse(multiEventWatcher.hasBeenTriggered()); //only one of the events has been triggered

        //trigger the other event as well
        triggerDelayed(instance, new TestEvent(), null);

        multiEventWatcher.waitForAllEvents(500);

        assertTrue(multiEventWatcher.hasBeenTriggered());   //all events have been triggered
        assertTrue(multiEventWatcher.getTriggeredEvent().getClass() == TestEvent.class); //the last one triggered

    }

    public void testContexts() {
        EventManagerInstance instance = new EventManagerInstance();

        final List<String> receivedEvents = new ArrayList<String>();

        instance.registerEventListener("myContext1", new GenericEventListener() {

            public void eventTriggered(Object sender, Event event) {
                receivedEvents.add("myContext1");
            }
        }, EventObject.class);

        instance.registerEventListener("myContext2", new GenericEventListener() {

            public void eventTriggered(Object sender, Event event) {
                receivedEvents.add("myContext2");
            }
        }, EventObject.class);

        instance.triggerEvent(this, new EventObject());

        doSleep(500);

        assertTrue(receivedEvents.size() == 2); //Two events should have been received (the same event by two listeners)

        receivedEvents.clear();

        instance.unregisterAllEventListenersForContext("myContext1"); //only context1 is unregistered

        instance.triggerEvent(this, new EventObject());

        doSleep(1000);

        assertTrue(receivedEvents.size() == 1); //now only one event should have been received

        assertTrue(receivedEvents.get(0).equals("myContext2"));


    }

    public void testPerformance() {
        EventManagerInstance instance = new EventManagerInstance();

        instance.unregisterAllEventSubscriptions();

        class State {

            public int noOfTriggeredEvent = 0;
        }

        final State state = new State();

        instance.registerEventListener(new GenericEventListener() {

            public void eventTriggered(Object sender, Event event) {
                state.noOfTriggeredEvent += 1;
            }
        }, EventObject.class);


        System.out.println("Entering pretest..");
        long preEvents = 1000;
        for (int j = 0; j < preEvents; j++) {
            instance.triggerEvent(this, new EventObject());
        }

        while (state.noOfTriggeredEvent < preEvents) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        state.noOfTriggeredEvent = 0;
        System.out.println("Starting test..");

        long seconds = 2;
        long start = System.currentTimeMillis();

        int i = 0;
        while (System.currentTimeMillis() < start + (seconds * 1000)) {
            i += 1;
            instance.triggerEvent(this, new EventObject());
        }

        System.out.println("Received events per second = " + state.noOfTriggeredEvent / seconds + ", sent events per second = " + i / seconds);
    }

    private void triggerDelayed(final EventManagerInstance instance, final Event event, final Object condition) {
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(200);
                    if (condition != null) {
                        instance.triggerEvent(this, event, condition);
                    } else {
                        instance.triggerEvent(this, event);
                    }
                } catch (InterruptedException e) {
                }
            }
        }).start();
    }

    private void doSleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }

    class TestEvent extends EventObject {

        public TestEvent() {
        }

        public TestEvent(Object source) {
            super(source);
        }
    }

    class TestEvent2 extends EventObject {

        public TestEvent2() {
        }

        public TestEvent2(Object source) {
            super(source);
        }
    }
}
