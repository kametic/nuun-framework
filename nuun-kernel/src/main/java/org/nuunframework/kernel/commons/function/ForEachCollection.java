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
package org.nuunframework.kernel.commons.function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.nuunframework.kernel.commons.AssertUtils;

public class ForEachCollection<T>
{

    public static <T> ForEachCollection<T> foreach(Collection<T> resources)
    {
        return new ForEachCollection<T>(resources);
    }

    private Collection<T>    resources = new ArrayList<T>();
    private SelectHolder<T>  selectHolder;
    private ForEachHolder<T> foreachHolder;

    public ForEachCollection(Collection<T> resources)
    {
        this.resources = resources;
    }

    public void add(T resource)
    {
        this.resources.add(resource);
    }

    static class SelectHolder<T>
    {
        public final Predicate<T> predicate;
        public final Object[]     args;

        public SelectHolder(Predicate<T> predicate, Object[] args)
        {
            this.predicate = predicate;
            this.args = args;
        }
    }

    static class ForEachHolder<T>
    {
        public final BlockContextInternal context;

        public final Block<T>             uniqBlock;

        public final Predicate<T>         ifPredicate;
        public final Object               predicateArgs;
        public final Block<T>             thenBlock;
        public final Block<T>             elseBlock;

        public ForEachHolder(BlockContextInternal context, Predicate<T> ifPredicate, Object predicateArgs, Block<T> thenBlock, Block<T> elseBlock)
        {
            this.context = context;
            this.ifPredicate = ifPredicate;
            this.predicateArgs = predicateArgs;
            this.thenBlock = thenBlock;
            this.elseBlock = elseBlock;
            // null
            this.uniqBlock = null;
        }

        public ForEachHolder(BlockContextInternal context, Block<T> uniqBlock)
        {
            this.context = context;
            this.uniqBlock = uniqBlock;
            // null
            this.ifPredicate = null;
            this.predicateArgs = null;
            this.thenBlock = null;
            this.elseBlock = null;
        }

        public void evaluate(T each)
        {
            if (uniqBlock != null)
            {
                uniqBlock.evaluate(context, each);
            }
            else
            {// alternate execution
                if (ifPredicate.is(each, predicateArgs))
                {
                    thenBlock.evaluate(context, each);
                }
                else
                {
                    elseBlock.evaluate(context, each);
                }
            }

        }

    }
    
    public static interface ResultBuilder
    {
        <T> T get();
    }
    
    public ResultBuilder forEachDo(final Block <T> block)
    {
        BlockContextInternal context = new BlockContextInternal();
        foreachHolder = new ForEachHolder<T>(context, block);
        iterate();
        
        
        ResultBuilder rb = new ResultBuilder()
        {
            @SuppressWarnings({
                    "unchecked", "hiding"
            })
            @Override
            public <T> T get()
            {
                T o = (T) foreachHolder.context.map().get(block);
                
                return o;
            }
        };
        
        return rb;
    }

    public ResultBuilder forEachDo(Predicate<T> predicate, Block<T> thenBlock, Block<T> elseBlock)
    {
        return forEachDo(predicate, null, thenBlock, elseBlock);
    }

    public ResultBuilder forEachDo(final Predicate<T> predicate, Object predicateArg, Block<T> thenBlock, Block<T> elseBlock)
    {
        BlockContextInternal context = new BlockContextInternal();

        foreachHolder = new ForEachHolder<T>(context, predicate, predicateArg, thenBlock, elseBlock);

        iterate();
        
        ResultBuilder rb = new ResultBuilder()
        {
            @SuppressWarnings("hiding")
            @Override
            public <T> T get()
            {
                @SuppressWarnings("unchecked")
                T o = (T) foreachHolder.context.map().get(predicate);
                
                return o;
            }
        };
        
        return rb;
        
    }

    private void iterate()
    {
        int count = 0;
        int size = resources.size();
        Iterator<T> it = resources.iterator();

        while (it.hasNext())
        {
            T each = it.next();
            foreachHolder.context.map().put("count", count);
            int loopinfo = updateLoopinfo(count, size);
            foreachHolder.context.loopinfo(loopinfo);
            // select
            if (selectHolder.predicate.is(each, selectHolder.args))
            {
                foreachHolder.evaluate(each);
            }
            count++;
        }
    }

    private int updateLoopinfo(int count, int size)
    {
        int loopinfo = 0;

        if (count == 0)
        {
            loopinfo |= LoopInfo.FIRST;
        }
        if (count == (size - 1))
        {
            loopinfo |= LoopInfo.LAST;
        }
        if (count == (size - 2))
        {
            loopinfo |= LoopInfo.PENULTIMATE;
        }
        if (count == (size - 3))
        {
            loopinfo |= LoopInfo.ANTEPENULTIMATE;
        }

        return loopinfo;
    }

    public ForEachCollection<T> select(Predicate<T> predicate, Object... args)
    {
        AssertUtils.assertNotNull(predicate, "predicate can not be null");
        this.selectHolder = new SelectHolder<T>(predicate, args);

        return this;
    }

    @SuppressWarnings("unchecked")
    public ForEachCollection<T> select()
    {
        this.selectHolder = new SelectHolder<T>(Predicate.TRUE, new Object[0]);
        return this;
    }
}