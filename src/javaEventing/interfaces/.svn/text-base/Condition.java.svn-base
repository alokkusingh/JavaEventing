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

/**
 * Inherit this interface in order to define your own conditions. A condition may be provided together with any events you wish to subscribe to. Only if the event is triggered toghether with the correct condition will you be notified.
 */
public interface Condition {
    boolean matches(Object sender, Event event, Object conditionalExpression);
}
