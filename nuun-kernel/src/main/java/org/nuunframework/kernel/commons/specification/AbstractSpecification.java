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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class AbstractSpecification<T> implements Specification<T>
{

    /**
     * do not remove this simple object insure the specification is unique
     */
    private Object id = new Object();
    
    // Chaining

    /**
     * {@inheritDoc}
     */
    @Override
    public AndSpecification<T> and(Specification<? super T> rhs)
    {
        return new AndSpecification<T>(this, rhs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrSpecification<T> or(Specification<? super T> rhs)
    {
        return new OrSpecification<T>(this, rhs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NotSpecification<T> not()
    {
        return new NotSpecification<T>(this);
    }

    // Object behaviour

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Object getId()
    {
        return id;
    }

    public void setId(Object id)
    {
        this.id = id;
    }

}