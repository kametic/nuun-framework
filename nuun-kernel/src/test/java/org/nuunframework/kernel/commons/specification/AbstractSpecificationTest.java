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
package org.nuunframework.kernel.commons.specification;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.*;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

public class AbstractSpecificationTest
{

    
    private S1 s1;
    private S2 s2;

    static class S1 extends AbstractSpecification<Object>
    {
        @Override
        public boolean isSatisfiedBy(Object candidate)
        {
            return false;
        }
    }
    
    static class S2 extends AbstractSpecification<Object>
    {
        @Override
        public boolean isSatisfiedBy(Object candidate)
        {
            return false;
        }
    }
    
    @Before
    public void init ()
    {
        s1 = new S1();
        s2 = new S2();
    }
    
    @Test
    public void testHashCode()
    {
        assertThat(s1.hashCode()).isNotEqualTo(s2.hashCode());
    }

    @Test
    public void testEqualsObject()
    {
        assertThat(s1.equals(s2)).isFalse();
    }

}
