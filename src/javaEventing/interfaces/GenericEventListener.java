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
package javaEventing.interfaces;

import java.util.EventListener;

/**
 * Implement this interface to create an event listener. This functions as a call-back object that will be notified once the event is triggered. One may typically choose to create anonymous impelementations of this interface.
 */
public interface GenericEventListener extends EventListener {

    void eventTriggered(Object sender, Event event);
}
