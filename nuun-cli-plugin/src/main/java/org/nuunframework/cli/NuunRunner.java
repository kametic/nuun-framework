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

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.nuunframework.cli.api.NuunCliHandler;
import org.nuunframework.kernel.plugins.logs.NuunLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Message;

/**
 * @author ejemba
 *
 */
public class NuunRunner
{
    static Logger logger = LoggerFactory.getLogger(NuunRunner.class);
    
    public static Injector injector;
    
    public static final SeedCallable NUUN_CALLABLE = new SeedCallable();
    
    public static void main(String[] args) 
    {
        /* Debug mode activated ? */
        boolean inDebugMode = ! StringUtils.isBlank(System.getProperty("debug"));
        
        NuunCliService nuunCliService = new NuunCliService();
        
        int returnCode = -1;
        
        try {
            returnCode = nuunCliService.startSync(args, NUUN_CALLABLE  );
        } 
        catch (ConfigurationException e) 
        {
            logger.error( "Configuration error" , e);
            for(Message message: e.getErrorMessages())
            {
                for (Object source :  message.getSources() )
                {
                    if (source.getClass().equals(TypeLiteral.class)  && TypeLiteral.class.cast(source).getRawType().equals(SeedCallable.class))
                    {
                        logger.error("No CommandLineHandler was found.");
                    }
                }
            }
        } catch (Exception e) { 
            logger.error("Exception when executing command line", e);           
        } finally {
            // We exit with the matching return code.
            logger.info("Exiting the application");
            if (! inDebugMode)
                System.exit(returnCode);
        }
    }
    
    static class SeedCallable implements Callable<Integer>
    {
        @NuunLog
        Logger logger;
        
        @Inject 
        NuunCliHandler commandLineHandler;
        
        @Override
        public Integer call() throws Exception {
            logger.info("Starting Command Line Handler : " + commandLineHandler.name());
            try {
                return commandLineHandler.call();
            } finally {
                logger.info("Ending Command Line Handler. " + commandLineHandler.name());
            }
        }
    }
}
