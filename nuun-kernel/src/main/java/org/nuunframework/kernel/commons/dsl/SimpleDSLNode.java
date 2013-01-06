package org.nuunframework.kernel.commons.dsl;

public class SimpleDSLNode implements DSLNode
{

    private static ThreadLocal<DSLNode> tl = new ThreadLocal<DSLNode>();

    final private DSLNode               parent;
    final private DSLToken              token;

    public SimpleDSLNode(DSLNode inParent, String inName, boolean thread)
    {
        this.parent = inParent;
        this.token = new DSLToken(inName);
        if (thread)
            tl.set(this);

    }

    public SimpleDSLNode(DSLNode inParent, String inName)
    {
        this(inParent, inName, true);
    }

    @Override
    public DSLNode parent()
    {
        return parent;
    }

    @Override
    public String toString()
    {
        return token.value.toString();
    }

    protected final String getCurrentName()
    {
        return methodNameFromTrace(Thread.currentThread().getStackTrace());
    }

    private final String methodNameFromTrace(StackTraceElement e[])
    {
        int l = e.length;
        String methodName = e[l - 2].getMethodName();
        if (methodName.equals("<init>"))
        {
            methodName = e[l - 3].getMethodName();
        }
        return methodName;
    }

    @Override
    public DSLToken token()
    {
        return token;
    }

    public static DSLNode lastThreadNode()
    {
        return tl.get();
    }
}