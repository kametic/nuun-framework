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
package org.nuunframework.kernel.api.topology.objectgraph;

import java.util.Collection;

import org.nuunframework.kernel.api.topology.instance.CompositeInstance;
import org.nuunframework.kernel.api.topology.instance.Instance;
import org.nuunframework.kernel.api.topology.instance.SpecifiedInstances;
import org.nuunframework.kernel.api.topology.instance.TypedInstance;
import org.nuunframework.kernel.api.topology.reference.Reference;

/**
 * an ObjectGraph hold information between Instances thanks to ref.
 * 
 * 
 * @author ejemba
 */
public interface ObjectGraph
{
    Collection<Instance> instances();
    Collection<Reference> references();
    Instance instance(String name);
    Collection<Instance> instancesByRegex(String regex);
    
    
    Reference reference(String name);    
    Collection<Reference> referencesByTargetType(Class<?> klass);
    Collection<Reference> referencesByRegex(String regex);
    Collection<Reference> referencesTargetAssignableFrom(Class<?> klass);
    
    Collection<TypedInstance> instancesByClass(Class<?> klass);
    Collection<TypedInstance> instancesAssignableFrom(Class<?> klass);

    Collection<CompositeInstance> compositeInstances();
    Collection<SpecifiedInstances> specifiedInstances();
}
