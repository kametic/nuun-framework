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
package org.nuunframework.kernel.api.topology.instance;

import java.util.Collection;

import org.nuunframework.kernel.api.topology.TopologyElement;
import org.nuunframework.kernel.api.topology.reference.Reference;

public interface Instance extends TopologyElement
{

    /**
     * @return the reference associated with the instance.
     */
    Collection<Reference> references();

    /**
     * 
     * @param klass the klass 
     * 
     * @return the reference where target type is assignable with klass.
     */
    Collection<Reference> referencesAssignableFrom(Class<?> klass); 
    
    /**
     * 
     * @param regex
     * 
     * @return references where name matches regex. 
     */
    Collection<Reference> referencesByRegex(String regex);

}
