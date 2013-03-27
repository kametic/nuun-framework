package org.nuunframework.nuun.jeromq;

import org.nuunframework.kernel.api.topology.ObjectGraph;
import org.nuunframework.nuun.jeromq.topology.AbstractJeroMqTopology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;

class JeroMqModule extends AbstractModule
{

    Logger logger = LoggerFactory.getLogger(JeroMqModule.class);
    
	private AbstractJeroMqTopology jeroMqTopology;
    
    
    public JeroMqModule(AbstractJeroMqTopology jeroMqTopology )
    {
		this.jeroMqTopology = jeroMqTopology;
    }
    
    @Override
    protected void configure()
    {
        ObjectGraph objectGraph = jeroMqTopology.generate();
//        
//        Collection<Instance> commandGateways = og.instancesAssignableFrom(.class);
//        
//        for (Instance gate : commandGateways)
//        {
//            Collection<Reference> referencesAssignableFrom = gate.referencesAssignableFrom(CommandBus.class);
//            
//            for (Reference bus : referencesAssignableFrom)
//            {
//                String gateName = gate.name();
//                String busName = bus.target().name();
//                
//                try
//                {
//                    Constructor<?>[] constructors = gate.type().getConstructors();
//                    
//                    for (Constructor<?> constructor : constructors)
//                    {
//                        System.err.println(" " +  constructor);
//                    }
//                    
//                    
//                }
//                catch (SecurityException e)
//                {
//                    e.printStackTrace();
//                }
//                
//                
//                
//            }
//            
//        }
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

}
