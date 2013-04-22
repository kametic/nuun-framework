/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuunframework.kernel.plugin.request;

public enum BindingType
{
    /**
     * Binds the Implementation to itself. Equals binder.bind(Implementation.class);
     */
    IMPLEMENTATION,
    /**
     * Binds the Implementation to all implemented Interfaces. Equals: for(Interface interface:
     * implementedInterfaces) binder.bind(interface).to(implementation);
     */
    INTERFACES,
    /**
     * Binds the Implementation to the extended Super-Class. Equals:
     * binder.bind(superclass).to(implementation);
     */
    SUPER,
    /**
     * Binds the Implementation to the Classes specifed by @To(to={}) Equals: for(Class<?> class: toClasses)
     * binder.bind(class).to(implementation);
     */
    CUSTOM
}