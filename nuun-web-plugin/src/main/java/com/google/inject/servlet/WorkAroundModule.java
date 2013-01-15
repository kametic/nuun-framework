package com.google.inject.servlet;

import org.nuunframework.kernel.annotations.KernelModule;

import com.google.inject.AbstractModule;


/**
 * 
 * Workaround to get the guice servlet working in explicit mode. 
 * This module will be installed by NuunWebPlugin 
 * 
 * @author Epo Jemba
 *
 */
public class WorkAroundModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(InternalServletModule.BackwardsCompatibleServletContextProvider.class);
    }
}