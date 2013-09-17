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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang.StringUtils;
import org.nuunframework.kernel.commons.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.MembersInjector;

/**
 * @author ejemba
 *
 */
public class NuunArgsMembersInjector<T> implements MembersInjector<T> 
{
    
    Logger logger = LoggerFactory.getLogger(NuunArgsMembersInjector.class);
    
    private Field field;
    private Annotation clonedAnno;
    private CommandLine commandLine;

    /**
     * 
     */
    public NuunArgsMembersInjector(Field field, CommandLine commandLine , Annotation clonedAnno)
    {
        this.field = field;
        this.commandLine = commandLine;
        this.clonedAnno = clonedAnno;
    }
    
    /* (non-Javadoc)
     * @see com.google.inject.MembersInjector#injectMembers(java.lang.Object)
     */
    @Override
    public void injectMembers(T instance)
    {
        String[] value = null;
        NuunArgs optionAnno = null;
        
        if (clonedAnno.annotationType() == NuunArgs.class)
        {
            optionAnno = this.field.getAnnotation(NuunArgs.class);
        }
        else 
        {
            optionAnno = AssertUtils.annotationProxyOf(NuunArgs.class, this.clonedAnno);
        }
        
        List<String> argList = commandLine.getArgList();
        int size = argList.size();
        value = new String[size];
        if (size > 0 )
        {
            int i = -1;
            for (String arg : argList)
            {
                value[++i] = arg;
            }
        }
        
        try
        {
            this.field.setAccessible(true);
            this.field.set(instance, value);
        }
        catch (IllegalArgumentException ex)
        {
            logger.error("Wrong argument or argument type during configuration injection", ex);
        }
        catch (IllegalAccessException ex)
        {
            logger.error("Illegal access during configuration injection", ex);
        }
        finally
        {
            this.field.setAccessible(false);
        }
    }

}
