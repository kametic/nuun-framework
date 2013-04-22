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
public class NotSpecification<T> extends AbstractSpecification<T> {
    private final Specification<T> delegate;

    /**
     * Construct a new {@link NotSpecification}.
     * @param delegate the specification to negate
     */
    public NotSpecification(Specification<T> delegate) {
        super();
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate specification cannot be null.");
        }
        this.delegate = delegate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(T candidate) {
        return !delegate.isSatisfiedBy(candidate);
    }

}