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
package org.nuunframework.cli;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;

/**
 * @author ejemba
 *
 */
public class NuunCliModule extends AbstractModule
{
    Logger logger = LoggerFactory.getLogger(NuunCliModule.class);
    
    private Options options;
    private CommandLine commandLine;
    private Map<Class, Class> bindings;

    private Map<Class<?>, CommandLine> contextualCommandLineMap;

    private Map<Class<?>, Options> byClassOptions;

    /**
     * @param contextualCommandLineMap 
     * @param byClassOptions 
     * @param bindings 
     * 
     */
    @SuppressWarnings("rawtypes")
    public NuunCliModule(CommandLine commandLine, Options options, Map<Class<?>, CommandLine> contextualCommandLineMap, Map<Class<?>, Options> byClassOptions, Map<Class, Class> bindings)
    {
        this.commandLine = commandLine;
        this.options = options;
        this.contextualCommandLineMap = contextualCommandLineMap;
        this.byClassOptions = byClassOptions;
        this.bindings = bindings;
    }
    
    
    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure()
    {
        bind(Options.class).toInstance(options);
        // Map<Class<?>, Options> byClassOptions
//        TypeLiteral typeLiteral= ;
        bind (new TypeLiteral<Map<Class<?>, Options>> () {} ).toInstance(this.byClassOptions);
        bindListener(Matchers.any(), new NuunCliTypeListener(contextualCommandLineMap));
        for( Entry<Class, Class> binding : bindings.entrySet()  )
        {
            logger.info(  String.format( "Binding %s to %s."   , binding.getKey() , binding.getValue()));
            bind(binding.getKey()).to(binding.getValue());
        }
    }

}
