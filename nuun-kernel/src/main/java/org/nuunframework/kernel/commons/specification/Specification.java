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


public interface Specification<T> {

    /**
     * See if an object satisfies all the requirements expressed in this specification.
     * 
     * @param candidate the object being verified
     * @return {@code true} if the requirements are satisfied, otherwise {@code false}
     */
    boolean isSatisfiedBy(T candidate);


    /**
     * Construct a new specification that verifies if both this, as the provided,
     * specification has been satisfied.
     * 
     * @param other the specification we want to include
     * @return new composite specification, evaluates using a logical AND
     */
    Specification<T> and(Specification<? super T> other);

    /**
     * Construct a new specification that verifies if either this, or the provided,
     * specification has been satisfied.
     * 
     * @param other the specification we want to include
     * @return new composite specification, evaluates using a logical OR
     */
    Specification<T> or(Specification<? super T> other);

    /**
     * Construct a new specification that verifies if this specification remains unsatisfied.
     * 
     * @return new specification, negating the current specification
     */
    Specification<T> not();

}