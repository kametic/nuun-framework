package org.nuunframework.ensemble.engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.nuunframework.ensemble.Ensemble;

@SuppressWarnings("rawtypes")
public class EnsemblePlayer implements Runnable
{

    private final EnsembleRecording   playerRecording;
    private final Map<String, Object> globalRegistry;
    private final Ensemble ensemble;

    public EnsemblePlayer(Ensemble ensemble, Set<Actor> actors)
    {
        this.ensemble = ensemble;
        EnsembleRecording recording = ensemble.getRecording();
        
        // We reverse the stack
        playerRecording = new EnsembleRecording();
        for (int i = recording.size() - 1; i >= 0; i--)
        {
            this.playerRecording.push(recording.elementAt(i));
        }

        // We register the actor
        this.globalRegistry = new ConcurrentHashMap<String, Object>();
        for (Actor actor : actors)
        {
            this.globalRegistry.put(actor.role.getName(), actor.instance);
        }
    }

    public void run()
    {
        // Execution context //
        Stack<Object> localActorStack = new Stack<Object>();
        int operationId = -1;
        EnsembleRecordItem pop = null;
        while (!playerRecording.empty())
        {
            operationId++;
            
            System.out.println("-------------------------------- ");
            System.out.println("OperationId " + operationId);
            pop = playerRecording.pop();
            Method m = pop.method;
            Object item = null;

            Object actorInstance = null;
            if (pop.roleName != null)
            {
                actorInstance = globalRegistry.get(pop.roleName);
            }

            if (actorInstance == null)
            { // we look in the local stack
                actorInstance = localActorStack.pop();
                if (actorInstance == null)
                {
                    throw new IllegalStateException("local stack should not be empty");
                    // or it would mean we handle static call in the
                }
            }

            // COnstruct arguments either from local stack either from constant
            Object[] localArgs = new Object[pop.arguments.length];
            for (int i = localArgs.length - 1; i >= 0; i--)
            {
                Object object = pop.arguments[i];
                if (object == null)
                {
                    localArgs[i] = localActorStack.pop();
                }
                else
                {
                    localArgs[i] = treat(object);
                }

            }
            // 
            item = invokeWithoutException(m, actorInstance, localArgs);

            if (!(pop.method.getReturnType().equals(Void.TYPE) || pop.method.getReturnType().equals(Void.class)))
            {
                localActorStack.push(item);
            }
        }

    }

    private Object treat(Object object)
    {

        Object o = object;

        if ( object.equals( Ensemble.NULL) || object.equals( Ensemble.NULL.toString()))
        {
            o = null;
        }
        
        
        return o;
    }

    private Object invokeWithoutException(Method m, Object self, Object... args)
    {
        Object o = null;
        try
        {
            o = m.invoke(self, args);
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("> " + m);
            for (int i = 0; i < args.length; i++)
            {
                System.err.println("> " + args[i]);
            }
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return o;

    }

}
