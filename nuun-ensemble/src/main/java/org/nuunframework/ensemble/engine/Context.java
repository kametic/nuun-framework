package org.nuunframework.ensemble.engine;

import java.util.Stack;

import com.google.inject.Injector;

public class Context {
    
    public Injector injector;
    public final Stack<Role>  stackRole;    
    public final EnsembleRecording recording;
    private int operationId = 0;
    private int argumentId = 0;
    
    
    public Context (EnsembleRecording stack )
    {
        this.recording = stack;
        stackRole = new Stack<Role>();
    }
    
    public Integer getArgumentIdAndIncrement()
    {
        return argumentId++;
    }
    
    public Integer getOperationIdAndIncrement()
    {
        argumentId = 0;
        return operationId++;
    }

    public Integer getOperationId()
    {
        return operationId;
    }
    
    public void incrementOperationId()
    {
        argumentId = 0;
        operationId++;
    }
}