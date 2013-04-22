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
package org.nuunframework.kernel.sample;

import javax.inject.Inject;

import org.nuunframework.kernel.plugin.dummy1.Bean6;
import org.nuunframework.kernel.plugin.dummy1.Bean9;
import org.nuunframework.kernel.plugin.dummy1.DummyService;
import org.nuunframework.kernel.plugins.configuration.NuunProperty;


public class HolderForPlugin
{

    @NuunProperty("value1")
    public String       value;

    @Inject
    public DummyService dummyService;

    @Inject
    public Bean6        bean6;
    
    @Inject
    public Bean9        bean9;
}