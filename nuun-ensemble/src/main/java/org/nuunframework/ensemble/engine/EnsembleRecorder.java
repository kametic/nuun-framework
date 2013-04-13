package org.nuunframework.ensemble.engine;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.nuunframework.ensemble.util.Primitives;


public class EnsembleRecorder implements MethodInterceptor
{

    @Singleton
    private final Context context;

    EnsembleRecorder(Context context)
    {
        this.context = context;
    }

    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        Object[] arguments = invocation.getArguments();

        invocation.getThis();
        
        Integer operationId = context.getOperationIdAndIncrement();
        
        Object proceed = invocation.proceed();
        

        if ("hashCode() toString()".indexOf(methodName) == -1)
        {
            Role currentRole = null;
            if ( ! context.stackRole.isEmpty())
            {
                currentRole = context.stackRole.pop(); 
                System.out.println(" =================================== " + operationId);
                System.out.println(" poping (" + context.stackRole.size()  + ") "+ currentRole.getName() );
            }
            else
            {
                System.out.println(" ===================================" );
                System.out.println(" stack empty " );
            }
            
            if (arguments.length == 0)
            { // getter
                if (!Primitives.isJdkLangClass(method.getReturnType()))
                {
                    // Here we create another proxy
                    proceed = context.injector.getInstance(method.getReturnType());
                    context.stackRole.push( Roles.BLANK );
                }
            }
            else
            { // setter
                
            }

            // Get the invokerclass
            Class<?> invokerClass = null;

            if (invocation.getThis().getClass().getName().contains("EnhancerByGuice"))
            {
                invokerClass = invocation.getThis().getClass().getSuperclass();
            }
            else
            {
                invokerClass = invocation.getThis().getClass();
            }

            String currentRoleName = (  currentRole != null ? currentRole.getName() : null);
            
            EnsembleRecordItem item = new EnsembleRecordItem(invokerClass, method, method.getReturnType(), currentRoleName , arguments);

            // We record the item
            context.recording.record(item);

            System.out.println(method.getName() + "(" + display(arguments) + ")  return type "  + method.getReturnType().getSimpleName());

        }
        else if ("hashCode()".indexOf(methodName) >= 0)
        {
            System.out.println(" ===================================" );
            System.out.println(" HASHCODE " + context.stackRole.size());
            // if (hashcodes.containsKey(this1))
            // {
            // proceed = (int) hashcodes.get(this1);
            // }
            // else {
            proceed = nextInt();
            // hashcodes.put(this1, (Integer) proceed);
            // }
        } else {
            System.out.println(" ===================================" );
            System.out.println(" TOSTRING " + context.stackRole.size());
        }
        
        // we dismiss the current role 
        
        return proceed;
    }

    static final Map<Object, Integer> hashcodes = new ConcurrentHashMap<Object, Integer>();

    String display(Object... args)
    {
        StringBuilder b = new StringBuilder();
        for (Object arg : args)
        {
            b.append(arg).append(' ');
        }

        return b.toString();
    }

    static Integer proxyIds = 0;

    static int nextInt()
    {
        synchronized (proxyIds)
        {
            return proxyIds++;
        }

    }
}
