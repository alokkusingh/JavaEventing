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

import javaEventing.interfaces.Event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventActionListener implements ActionListener {

    private Object sender;
    private Event event;
    private Object conditionalExpression;
    private EventManagerInstance eventManager;

    public EventActionListener(EventManagerInstance eventManager, Object sender, Event event, Object conditionalExpression) {
        super();
        this.sender = sender;
        this.event = event;
        this.conditionalExpression = conditionalExpression;
        this.eventManager = eventManager;
    }

    public EventActionListener(EventManagerInstance eventManager, Object sender, Event event) {
        this(eventManager, sender, event, null);
    }

    public EventActionListener(Object sender, Event event) {
        this(EventManager.getEventManagerInstance(), sender, event, null);
    }

    public EventActionListener(Object sender, Event event, Object conditionalExpression) {
        this(EventManager.getEventManagerInstance(), sender, event, conditionalExpression);
    }

    public void actionPerformed(ActionEvent e) {
        if (conditionalExpression != null) {
            eventManager.triggerEvent(sender, event, conditionalExpression);
        } else {
            eventManager.triggerEvent(sender, event);
        }
    }
}
