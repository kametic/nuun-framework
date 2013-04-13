package org.nuunframework.ensemble.api;

import org.nuunframework.ensemble.Ensemble;
import org.nuunframework.ensemble.Ensemble.NULL;
import org.nuunframework.ensemble.engine.Role;
import org.nuunframework.ensemble.engine.RoleImpl;
import org.nuunframework.ensemble.util.Assert;
import org.nuunframework.ensemble.util.TypeResolver;

public abstract class Soloist<R1> extends Ensemble<R1,NULL>
{
    protected R1 instance1;
    public final Class<R1> roleClass1;
    
    protected RoleImpl role1;
    
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public Soloist()
    {
        super();
        Class<? extends Soloist> subclass = getClass();
        Class<?>[] typeArguments = TypeResolver.resolveArguments(subclass, Soloist.class);
        Assert.notNull(typeArguments, "Must declare role type argument <R> for Soloist");
        roleClass1 =   (Class<R1>) typeArguments[typeArguments.length - 1];
        instance1 = injector.getInstance(roleClass1);
        role1 = new RoleImpl("role1");
        listRoles.add(role1);
        listClassRoles.add(roleClass1);
    }
    
    
    public R1 actor1()
    {
        this.context.stackRole.push(role1);
        return instance1;
    }
    public Role role1()
    {
        return role1;
    }
    

    
    
 

}
