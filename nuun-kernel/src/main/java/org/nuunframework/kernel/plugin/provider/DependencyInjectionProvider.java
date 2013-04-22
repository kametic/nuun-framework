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
package org.nuunframework.kernel.plugin.provider;

import com.google.inject.Module;


public interface DependencyInjectionProvider
{
    
    boolean canHandle (Class<?> injectionDefinition);

    /**
     * Convert framework DI definition in nuun internal DI dependency guice module.
     *
     * @param injectionDefinition
     * @return Guice Plugin
     */
    Module convert(Object injectionDefinition);

    /**
     * This method will return a bridge from kernel to module with this DI.
     * For example a FactoryBean in the Spring world.
     *
     * @return an object able to deal with all kernel dependencies
     */
    Object kernelDIProvider ();

}
