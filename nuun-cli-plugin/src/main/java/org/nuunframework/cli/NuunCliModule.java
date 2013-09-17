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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * @author ejemba
 *
 */
public class NuunCliModule extends AbstractModule
{
    
    private Options options;
    private CommandLine commandLine;

    /**
     * 
     */
    public NuunCliModule(CommandLine commandLine, Options options)
    {
        this.commandLine = commandLine;
        this.options = options;
    }
    
    
    /* (non-Javadoc)
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure()
    {
        bind(Options.class).toInstance(options);
        bindListener(Matchers.any(), new NuunCliTypeListener(commandLine));
    }

}
