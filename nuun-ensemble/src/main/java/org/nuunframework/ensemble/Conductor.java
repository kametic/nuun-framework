package org.nuunframework.ensemble;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.fest.util.Collections;
import org.nuunframework.ensemble.engine.Actor;
import org.nuunframework.ensemble.engine.EnsemblePlayer;
import org.nuunframework.ensemble.engine.EnsembleRecording;
import org.nuunframework.ensemble.engine.Role;
import org.nuunframework.ensemble.engine.TypeTuple;
import org.nuunframework.ensemble.util.Assert;
import org.nuunframework.ensemble.util.Types;


public class Conductor
{
    
    @SuppressWarnings("rawtypes")
    Map<TypeTuple, Ensemble> tuples;
    
    public Conductor()
    {        
        tuples = new HashMap<TypeTuple, Ensemble>();
    }
    

    public boolean addEnsemble(Ensemble ensemble)
    {
        TypeTuple key = TypeTuple.of(ensemble.roleClasses()).build(); 
        tuples.put(key, ensemble);
        
        return (Boolean) true;
    }
    
    
    @SuppressWarnings("rawtypes")
    public <D> void conduct(Object... inputs) { 
        
        // Get input types 
        Class<?> types[] = new Class[inputs.length];
        for (int i = 0 ; i < inputs.length ; i ++)
        {
            if (! (inputs[i] instanceof Class))
            {
                types[i] = inputs[i].getClass();
            }
            else
            {
                types[i] = (Class<?>) inputs[i];
            }
        }
        
        // Retrieve the ensemble from this key/combination 
        TypeTuple key = TypeTuple.of( types ).build();        
        
        Ensemble ensemble = tuples.get(key);   
        
        Assert.notNull(ensemble, "No ensemble for this formation");

        // Let's listen the ensemble play
        Method methodForConfigure = Types.methodFor(Ensemble.class, "play");        
        methodForConfigure.setAccessible(true);        
        try
        {
            methodForConfigure.invoke(ensemble); // ensemble.play()
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Error when configuring Ensemble", e);
        }        
        
        // Compute the set of actors (role + instance)        
        Role[] roles = ensemble.roles();        
        Set<Actor> actors = new HashSet<Actor>();        
        for (int i = 0 ; i < inputs.length ; i++)
        {
            Actor actor = new Actor(roles[i], inputs[i]);
            actors.add(actor);
        }
            
       EnsemblePlayer player = new EnsemblePlayer(ensemble , actors );
       
       player.run();
            
     }
    

}
