package org.nuunframework.kernel.commons.function;


public interface Block<T>
{
    void evaluate(BlockContext blockContext, T resource);
}