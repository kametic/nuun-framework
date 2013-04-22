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

import org.fest.assertions.Assertions;
import org.junit.Test;

public class LoopInfoTest
{

    @Test
    public void testFirst()
    {
        int underTest = LoopInfo.FIRST;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isFalse();
    }
    @Test
    public void testAntepenultimate()
    {
        int underTest = LoopInfo.ANTEPENULTIMATE;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isFalse();
    }
    
    @Test
    public void testPenultimate()
    {
        int underTest = LoopInfo.PENULTIMATE;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isFalse();
    }
    
    @Test
    public void testLast()
    {
        int underTest = LoopInfo.LAST;
        
        Assertions.assertThat(LoopInfo.isFirst(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isTrue();
    }

    @Test
    public void testFirstAndLast()
    {
        int underTest = 0;
        underTest |= LoopInfo.FIRST;
        underTest |= LoopInfo.LAST;
        

        Assertions.assertThat(LoopInfo.isFirst(underTest)).isTrue();
        Assertions.assertThat(LoopInfo.isAntepenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isPenultimate(underTest)).isFalse();
        Assertions.assertThat(LoopInfo.isLast(underTest)).isTrue();
    }
    
    
    
    
    

}
