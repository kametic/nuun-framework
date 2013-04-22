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
package org.nuunframework.kernel.commons.function;

import java.util.HashMap;
import java.util.Map;

class BlockContextInternal implements BlockContext
{

    private final Map<Object , Object > map;
    private int loopinfo;

    public BlockContextInternal()
    {
        map = new HashMap<Object, Object>();
        loopinfo = 0;
    }
    
    @Override
    public Map<Object, Object> map()
    {
        return map;
    }

    @Override
    public int loopInfo()
    {
        return loopinfo;
    }
    
    public void loopinfo(int loopinfo)
    {
        this.loopinfo = loopinfo;
    }

}
