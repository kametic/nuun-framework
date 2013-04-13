package org.nuunframework.ensemble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.nuunframework.ensemble.engine.Context;
import org.nuunframework.ensemble.engine.EnsembleModule;
import org.nuunframework.ensemble.engine.EnsembleRecording;
import org.nuunframework.ensemble.engine.Role;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class Ensemble<R1, R2>
{
    public static Object NULL = UUID.randomUUID();
    public static class NULL{}

    final EnsembleRecording        recording          = new EnsembleRecording();
    protected final Injector       injector;
    protected final Context        context;
    protected final List<Class<?>> listClassRoles = new ArrayList<Class<?>>();
    protected final List<Role>     listRoles      = new ArrayList<Role>();
    protected final Map<Integer, Object> constants = new HashMap<Integer, Object>();

    /**
     * Creates a new Ensemble for the source and destination types {@code S} and {@code D}.
     * 
     * @throws IllegalArgumentException
     *             if {@code S} and {@code D} are not declared
     */
    protected Ensemble()
    {
        context = new Context(recording);
        injector = Guice.createInjector(new EnsembleModule(context));
        context.injector = injector;
    }

    public EnsembleRecording getRecording()
    {
        return (recording);
    }

    public Role[] roles()
    {
        Role[] roles = new Role[listRoles.size()];
        listRoles.toArray(roles);
        return roles;
    }

    public Class<?>[] roleClasses()
    {
        Class<?>[] clazzes = new Class[listClassRoles.size()];
        listClassRoles.toArray(clazzes);
        return clazzes;
    }
    
    // 
    
    protected <T> String $Null()
    {
       return NULL.toString();
    }
    
    @SuppressWarnings("unused")
    protected String $(String in)
    {
        Integer operationId = context.getOperationId();
        System.out.println("$(" + in + ") " + operationId);
        return null;
    }
    
    protected Long $(Long in)
    {
        Integer operationId = context.getOperationId();
        return null;
    }

    protected abstract void play();

}
