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

import static org.fest.assertions.Assertions.assertThat;
import static org.nuunframework.kernel.commons.function.ForEachCollection.foreach;
import static org.nuunframework.kernel.commons.function.sample.resource.Blocks.print;
import static org.nuunframework.kernel.commons.function.sample.resource.Blocks.print_uppercase;
import static org.nuunframework.kernel.commons.function.sample.resource.Predicates.contains_letter;
import static org.nuunframework.kernel.commons.function.sample.resource.Predicates.ends_with;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nuunframework.kernel.commons.function.sample.resource.Blocks;
import org.nuunframework.kernel.commons.function.sample.resource.Resource;

public class ForEachCollectionTest
{

    List<Resource> resources;

    @Before
    public void init()
    {
        resources = Arrays.asList( //
                resource("Jeanne Jemba", 100), //
                resource("Epo Jemba", 100), //
                resource("Adrien Lauer", 100), //
                resource("Emmanuel Vinel", 100), //
                resource("Marius Matéi", 100), //
                resource("Laurent Didier", 100), //
                resource("Yves Dautremay", 100) //
                );
    }

    @Test
    public void test()
    {
        // TODO rajouter select o dans foreach
        foreach(resources).select(contains_letter , "e")
           .forEachDo(ends_with , "Jemba", print_uppercase , print );
        // get(print_uppercase)
        // get(print)
        // getOtherwise()
        System.out.println("==================================");
        
        foreach(resources).select(contains_letter , "u").forEachDo(print);
        
        System.out.println("==================================");        
        foreach(resources).select(contains_letter , "y").forEachDo(print_uppercase);
        
        System.out.println("==================================");
        
        Integer number = foreach(resources).select().forEachDo(Blocks.count_elems).get();
        
        assertThat(number).isEqualTo(7);
        
    }

    private Resource resource(String name, Integer salary)
    {
        return new Resource(name, salary);
    }

}
