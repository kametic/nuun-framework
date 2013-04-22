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
package org.nuunframework.kernel.plugin.request;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


/**
 * @author Epo Jemba
 *
 */
public class KernelParamsRequestBuilder implements Builder<Collection<KernelParamsRequest>>
{
    
    private Collection<KernelParamsRequest> requests;
    
    /**
     * 
     */
    public KernelParamsRequestBuilder()
    {
        requests = new HashSet<KernelParamsRequest>();
    }
    
    
    public KernelParamsRequestBuilder optional(String keyRequested)
    {
        
        requests.add(new KernelParamsRequest(KernelParamsRequestType.OPTIONAL, keyRequested));
        
        return this;
    }

    public KernelParamsRequestBuilder mandatory(String keyRequested)
    {
        
        requests.add(new KernelParamsRequest(KernelParamsRequestType.MANDATORY, keyRequested));
        
        return this;
    }
    
    
    
    @Override
    public Collection<KernelParamsRequest> build()
    {
        return Collections.unmodifiableCollection(requests);
    }
    
    @Override
    public void reset()
    {
        requests.clear();
    }
    
    
}
