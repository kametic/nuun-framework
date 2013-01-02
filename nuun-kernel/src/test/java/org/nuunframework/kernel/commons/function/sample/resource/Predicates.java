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