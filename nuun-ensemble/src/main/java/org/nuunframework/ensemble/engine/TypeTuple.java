/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nuunframework.ensemble.engine;

/**
 * @author Epo Jemba
 */
public class TypeTuple {
  public final Class<?> types[];
  public final Object qualifier;
  private final int hashCode;

  private TypeTuple(Object qualifier , Class<?>... types) {
    this.types = types;
    hashCode = computeHashCode();
    this.qualifier = qualifier;
  }

    static public class TypeTupleBuilder
    {
        private Object innerQualifier = null;
        private Class<?>[] innerTypes;

        public TypeTupleBuilder(Class<?>... innerTypes)
        {
            this.innerTypes = innerTypes;
        }
        
        public TypeTupleBuilder withQualifier(Object qualifier)
        {
            this.innerQualifier = qualifier;
            return this;
        }

        public TypeTuple build()
        {
            return new TypeTuple(innerQualifier , innerTypes);
        }
    }
  
  public static TypeTupleBuilder of(Class<?>... types) {
    return new TypeTupleBuilder(types);
  }
  
//  public static TypeTuple of(Class<?>... types) {
//      return new TypeTuple(types);
//  }

  @Override
  public boolean equals(Object other) {
      if (other == this)
      return true;
    if (!(other instanceof TypeTuple))
      return false;
    
    TypeTuple otherKey = (TypeTuple) other;
    
    if (this.types.length != otherKey.types.length)  return false;
    if  (!(this.qualifier == null  &&  otherKey.qualifier == null))
    {
         if ( this.qualifier != null  &&  ! this.qualifier.equals(otherKey) )
             return false;
    }
    
    for (int i= 0 ; i < this.types.length ; i ++)
    {
        if (this.types[i] != otherKey.types[i])
            return false;
    }
    
    return true;
  }

  @Override
  public final int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
      
      StringBuilder s = new StringBuilder();
      for (Class<?> k : types)
      {
        s.append(k.getName()).append(" - ");
      }

      if (qualifier != null)
          s.append(qualifier.toString());
      
    return s.toString();
  }

  private int computeHashCode() {
      int h = 1;
      int pow = 1;
      
      for (Class<?> k : types)
      {
        h *= k.hashCode() * Math.pow(31, pow++);
      }
      
      if (qualifier != null)
          h += qualifier.hashCode();
      
    return h;
  }

}