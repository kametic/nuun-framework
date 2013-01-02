package org.nuunframework.kernel.sample;

import com.google.inject.AbstractModule;
   public class ModuleInError extends AbstractModule
    {

        /*
         * (non-Javadoc)
         * 
         * @see com.google.inject.AbstractModule#configure()
         */
        @Override
        protected void configure()
        {
            bind(HolderForError.class);
        }
    }