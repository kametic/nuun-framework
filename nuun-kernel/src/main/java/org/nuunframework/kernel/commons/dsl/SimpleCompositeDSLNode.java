/**
 * Copyright (C) 2013 Kametic <epo.jemba@kametic.com>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * or any later version
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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