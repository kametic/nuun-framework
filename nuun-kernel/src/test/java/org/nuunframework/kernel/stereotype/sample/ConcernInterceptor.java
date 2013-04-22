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
package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ConcernInterceptor implements MethodInterceptor
{
    
    List<String> list;
    
    private String concernName;

    public ConcernInterceptor(List<String> list, String concernName  )
    {
        this.list = list;
        this.concernName = concernName;
    }
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        list.add("pre " + concernName);
        invocation.proceed();
        list.add("post " + concernName);
        
        return null;
    }

}
