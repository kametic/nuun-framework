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

import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.Message;

/**
 * @author epo.jemba@kametic.com
 *
 */
public class NuunRunnerDaemon
{
    static Logger logger = LoggerFactory.getLogger(NuunRunnerDaemon.class);
    
    public static Injector injector;
    
    public static final SeedCallable NUUN_CALLABLE = new SeedCallable();
    
    public static void main(String[] args) 
    {
        logger.info("Started in async");
        NuunCliService nuunCliService = new NuunCliService();
        
        int returnCode = -1;
        
        try {
            Future<Integer> startAsync = nuunCliService.startAsync(args, NUUN_CALLABLE  );
            returnCode = startAsync.get();
            logger.info("return code {}" , returnCode);
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
            
        }
    }
    
  
}
