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
package org.nuunframework.spring;

import com.google.inject.Module;
import org.nuunframework.kernel.plugin.provider.DependencyInjectionProvider;
import org.springframework.context.ApplicationContext;

class InternalDependencyInjectionProvider implements DependencyInjectionProvider
{

    @Override
    public boolean canHandle(Class<?> injectionDefinition)
    {
        return (ApplicationContext.class.isAssignableFrom(injectionDefinition));
    }

    @Override
    public Module convert(Object injectionDefinition)
    {
        ApplicationContext context = ApplicationContext.class.cast(injectionDefinition);
        return new SpringModule(context);
    }

    @Override
    public Object kernelDIProvider()
    {
        return null;
    }

}