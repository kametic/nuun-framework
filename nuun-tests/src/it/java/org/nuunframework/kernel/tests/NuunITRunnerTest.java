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
package org.nuunframework.kernel.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Inject;

/**
 * 
 * 
 * @author epo.jemba@kametic.com
 *
 */
@RunWith(NuunITRunner.class)
public class NuunITRunnerTest
{
    
    @Inject
    ServiceA serviceA;
    
    
    @Test
    public void test()
    {
        assertThat(serviceA).isNotNull();
        serviceA.doSomethingUsefull();
    }

}
