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
package org.nuunframework.kernel.commons.function.sample.resource;

import org.nuunframework.kernel.commons.function.Predicate;

public enum Predicates implements Predicate<Resource>
{
    contains_letter
    {
        public boolean is(Resource each, Object... args)
        {
            CharSequence x = (CharSequence) args[0];
            return each != null && each.name.contains(x);
        }
    },
    ends_with
    {
        public boolean is(Resource each, Object... args)
        {
            CharSequence x = (CharSequence) args[0];
            return each != null && each.name.endsWith("" +x);
        }
    }

}