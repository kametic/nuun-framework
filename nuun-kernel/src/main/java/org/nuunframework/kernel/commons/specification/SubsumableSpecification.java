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
/**
 * Specification that can compare its logic to other specifications.
 * 
 * @author Jeroen van Schagen
 * @since 22-02-2011
 *
 * @param <T> type of candidates being checked on
 */
public interface SubsumableSpecification<T> extends Specification<T> {

    /**
     * Check whether this specification <i>subsumes</i> another specification, which
     * means that every satisfying candidate of this specification also satisfies the
     * other specification. It could be described using conventional notation:
     * <p>
     * {@code this specification -> other specification}
     * <p>
     * So, whenever this specification is {@code true}, the other specification is
     * also {@code true}.
     * 
     * @param other the other specification that should be checked
     * @return whether this specification subsumes the other specification
     */
    boolean subsumes(Specification<T> other);
    
}