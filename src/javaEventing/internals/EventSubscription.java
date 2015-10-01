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
package javaEventing.internals;

import javaEventing.interfaces.Condition;
import javaEventing.interfaces.Event;
import javaEventing.interfaces.GenericEventListener;

public class EventSubscription {

        private GenericEventListener receiver;
        private Class<? extends Event> eventClass;
        private Condition condition;

        public EventSubscription(GenericEventListener receiver, Class<? extends Event> eventClass, Condition condition) {
            this.receiver = receiver;
            this.eventClass = eventClass;
            this.condition = condition;
        }

        public GenericEventListener getReceiver() {
            return receiver;
        }

        public Class<? extends Event> getEventClass() {
            return eventClass;
        }

        public int hashCode() {
            return (receiver.hashCode() + eventClass.getName()+"").hashCode();
        }

        public Condition getCondition() {
            return condition;
        }
    }