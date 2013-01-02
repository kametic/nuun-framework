package org.nuunframework.kernel.sample;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.nuunframework.kernel.plugin.dummy1.InterfaceWithCustom2Suffix;


public class HolderForInterface
{
    @Inject
    @Nullable
    public InterfaceWithCustom2Suffix customBean = null;
}