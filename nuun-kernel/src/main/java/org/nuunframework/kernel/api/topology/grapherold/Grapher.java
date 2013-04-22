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
package org.nuunframework.kernel.api.topology.grapherold;

import org.nuunframework.kernel.commons.specification.Specification;



public interface Grapher
{
    /**
     * Declaring a new instance to be part of the Graph 
     *  
     *  @param name The name of the instance
     *  @param type the type of the instance
     * @return
     */
    InstanceBuilder newInstance ( String name  );
    InstanceBuilder newInstance ( String name , Class<?> type );
    
    InstanceBuilder newInstance ( String name , Specification<Class<?>> typeSubset );
    
    /**
     * 
     * Declaring a new reference to be part of the graph
     * 
     * @param referenceName the name of the reference. Has to be unique among all objects graph.
     * @return
     */
    ReferenceBuilder newReference(String referenceName); 
    
    
    
    
}
