package org.nuunframework.kernel.commons.dsl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleCompositeDSLNode extends SimpleDSLNode implements CompositeDSLNode
{

    private List<DSLNode> nodes;
    private DSLNode       previous;

    public SimpleCompositeDSLNode(DSLNode inParent, String compositeName, String firstNodeName)
    {
        super(inParent, compositeName);
        nodes = new ArrayList<DSLNode>();
        nodes.add(previous = (DSLNode) new SimpleDSLNode(inParent, firstNodeName));
    }

    public void addNode(String name)
    {
        nodes.add(previous = new SimpleDSLNode(previous, name));
    }

    @Override
    public List<DSLNode> siblingNodes()
    {
        // TODO Auto-generated method stub
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public String toString()
    {
        String outName = "";

        for (DSLNode n : nodes)
        {
            outName += "," + n.toString();
        }

        return outName.substring(1);
    }

}