package org.nuunframework.ensemble.engine;

import java.util.Stack;

public class EnsembleRecording extends Stack<EnsembleRecordItem>
{

    
    
    public void record(EnsembleRecordItem item)
    {
        push(item);
    }
    
    
    private static final long serialVersionUID = 1L;

}
