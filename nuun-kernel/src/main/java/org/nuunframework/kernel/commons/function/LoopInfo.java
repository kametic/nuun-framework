package org.nuunframework.kernel.commons.function;

public class LoopInfo
{

    public LoopInfo()
    {
    }

    public static final int NONE            = 0x0000;
    public static final int FIRST           = 0x0001;
    public static final int ANTEPENULTIMATE = 0x0004;
    public static final int PENULTIMATE     = 0x0008;
    public static final int LAST            = 0x0010;

    public static boolean isFirst(int loop)
    {
        return (loop & FIRST) != 0;
    }

    public static boolean isAntepenultimate(int loop)
    {
        return (loop & ANTEPENULTIMATE) != 0;
    }

    public static boolean isPenultimate(int loop)
    {
        return (loop & PENULTIMATE) != 0;
    }

    public static boolean isLast(int loop)
    {
        return (loop & LAST) != 0;
    }
    public static boolean isNone(int loop)
    {
        return (loop & NONE) != 0;
    }

    
    public static String toString(int loop)
    {
      return toString(loop, new StringBuilder()).toString();
    }
    
    static StringBuilder toString(int loop, StringBuilder r)
    {
      r.append(toString(loop, new StringBuffer()));
      return r;
    }
    
    static StringBuffer toString(int loop, StringBuffer r)
    {
        if (isFirst(loop))
        {
            r.append("first ");
        }
        if (isAntepenultimate(loop))
        {
            r.append("antepenultimate ");
        }
        if (isPenultimate(loop))
        {
            r.append("penultimate ");
        }
        if (isLast(loop))
        {
            r.append("last ");
        }
        if (isNone(loop))
        {
            r.append("none ");
        }
        
        
        
        
        return r;
    }

}
