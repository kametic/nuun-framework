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