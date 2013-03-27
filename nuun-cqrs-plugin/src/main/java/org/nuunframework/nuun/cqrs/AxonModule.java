package org.nuunframework.nuun.cqrs;

import java.lang.reflect.Constructor;
import java.util.Collection;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.nuunframework.kernel.api.Instance;
import org.nuunframework.kernel.api.ObjectGraph;
import org.nuunframework.kernel.api.ObjectGraphDef;
import org.nuunframework.kernel.api.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

class AxonModule extends AbstractModule
{

    Logger logger = LoggerFactory.getLogger(AxonModule.class);
    
    ObjectGraphDef objectGraphDef = new DefaultCqrsObjectGraph();
    
    public AxonModule()
    {
    }
    
    @Override
    protected void configure()
    {
        ObjectGraph og = objectGraphDef.generate();
        
        Collection<Instance> commandGateways = og.instancesAssignableFrom(CommandGateway.class);
        
        for (Instance gate : commandGateways)
        {
            Collection<Reference> referencesAssignableFrom = gate.referencesAssignableFrom(CommandBus.class);
            
            for (Reference bus : referencesAssignableFrom)
            {
                String gateName = gate.name();
                String busName = bus.target().name();
                
                try
                {
                    Constructor<?>[] constructors = gate.type().getConstructors();
                    
                    for (Constructor<?> constructor : constructors)
                    {
                        System.err.println(" " +  constructor);
                    }
                    
                    
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
                
                
                
            }
            
        }
    }
    
    
    @SuppressWarnings("unchecked")
    private <T> T newInstance(Class<?> klass)
    {
        T instance = null;

        try
        {
            instance = (T) klass.newInstance();
        }
        catch (InstantiationException e)
        {
            logger.error("Error when instantiating class " + klass, e);
        }
        catch (IllegalAccessException e)
        {
            logger.error("Error when instantiating class " + klass, e);
        }

        return instance;
    }
    
    public static void main(String[] args)
    {
        new AxonModule().configure();
    }

}
