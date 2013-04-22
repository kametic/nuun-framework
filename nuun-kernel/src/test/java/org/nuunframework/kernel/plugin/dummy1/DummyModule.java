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
/**
 * 
 */
package org.nuunframework.kernel.plugin.dummy1;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.util.Providers;


/**
 * @author Epo Jemba
 *
 */
public class DummyModule extends AbstractModule
{

    
    
    
    private final Collection<Class<?>> classes;


    /**
     * 
     */
    public DummyModule(Collection<Class<?>> classes)
    {
        this.classes = classes;
    }
    
    
    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    @Override
    protected void configure()
    {
        bind(DummyService.class).to(DummyServiceInternal.class);
        bind(DummyService.class).annotatedWith(MarkerSample3.class).to(DummyServiceInternal2.class);
        Provider ofNull = Providers.of(null);
        for (Class<?> klass : classes)
        {
            System.err.println("logger " + klass);
            if (klass.getAnnotation(Nullable.class)== null )
            {
                bind(klass);
            }
            else
            {
                bind(klass).toProvider( ofNull);
            }
        }
    }

}
