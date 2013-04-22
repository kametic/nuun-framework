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

import org.nuunframework.kernel.commons.specification.Specification;


/**
 * @author Epo Jemba
 *
 */
public class ClasspathScanRequest
{

    public final RequestType requestType;
    public final Object objectRequested;
    public final Specification<Class<?>> specification;

    public ClasspathScanRequest(RequestType requestType , Object keyRequested)
    {
        this.requestType = requestType;
        this.objectRequested = keyRequested;
        this.specification = null;
    }
    public ClasspathScanRequest(Specification<Class<?>> specification)
    {
        this.specification = specification;
        this.requestType = RequestType.VIA_SPECIFICATION;
        this.objectRequested = null;
    }
    
}
