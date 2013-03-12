package org.nuunframework.kernel.stereotype.sample;

import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ConcernInterceptor implements MethodInterceptor
{
    
    List<String> list;
    
    private String concernName;

    public ConcernInterceptor(List<String> list, String concernName  )
    {
        this.list = list;
        this.concernName = concernName;
    }
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        list.add("pre " + concernName);
        invocation.proceed();
        list.add("post " + concernName);
        
        return null;
    }

}
