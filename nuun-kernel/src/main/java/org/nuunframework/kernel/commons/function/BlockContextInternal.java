package org.nuunframework.kernel.commons.function;

import java.util.HashMap;
import java.util.Map;

class BlockContextInternal implements BlockContext
{

    private final Map<Object , Object > map;
    private int loopinfo;

    public BlockContextInternal()
    {
        map = new HashMap<Object, Object>();
        loopinfo = 0;
    }
    
    @Override
    public Map<Object, Object> map()
    {
        return map;
    }

    @Override
    public int loopInfo()
    {
        return loopinfo;
    }
    
    public void loopinfo(int loopinfo)
    {
        this.loopinfo = loopinfo;
    }

}
