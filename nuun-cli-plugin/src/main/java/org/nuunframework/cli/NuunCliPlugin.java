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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.PluginException;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public class NuunCliPlugin extends AbstractPlugin
{

    private Logger                  logger = LoggerFactory.getLogger(NuunCliPlugin.class);
    private Specification<Class<?>> optionDefsSpecification;
    private Options options;
    private String[] lineArguments;
    private CommandLine commandLine;

    @Override
    public String name()
    {
        return "nuun-cli-plugin";
    }

    /*
     * (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#init(org.nuunframework.kernel.context.InitContext)
     */
    @Override
    public InitState init(InitContext initContext)
    {
        Set< String> shortOptions = Sets.newHashSet();
        Set< String> longOptions  = Sets.newHashSet();
        
        Collection<Class<?>> collection = initContext.scannedTypesBySpecification().get(optionDefsSpecification);
        options = new Options();
        
        for (Class<?> class1 : collection)
        {
            Set<Field> fields = annotatedFields(class1, NuunOption.class);
            for(Field field : fields)
            {
                logger.debug(">> " +  field);
                
                Option option = createOptionFromField(field);
                
                if (  !Strings.isNullOrEmpty(option.getOpt()) &&  shortOptions.contains(option.getOpt()))
                {
                    exception("Short option " + option.getOpt() + " already exists!" );
                }
                
                if ( !Strings.isNullOrEmpty(option.getLongOpt()) && longOptions.contains(option.getLongOpt()))
                {
                    exception("Long option " + option.getLongOpt() + " already exists!" );
                }
                
                if ( Strings.isNullOrEmpty(option.getOpt()) && Strings.isNullOrEmpty(option.getLongOpt()))
                {
                    exception("NuunOption defined on " + field + " has no opt nor longOpt." );
                }
                
                options.addOption(option);
                
                shortOptions.add(option.getOpt());
                longOptions.add(option.getLongOpt());
            }
        }
        
        // Option is initialized
        
        if (lineArguments == null || lineArguments.length == 0)
            exception("Empty or Null command line has been provided to nuun-cli-plugin");
        
        
        // Parse Command Line
        CommandLineParser parser = new BasicParser();
        
        try
        {
            commandLine = parser.parse( options, lineArguments);
        }
        catch (ParseException e)
        {
            exception("Error in the command line", e);
        }
        
        
        return InitState.INITIALIZED;
    }
    
    
    /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#provideContainerContext(java.lang.Object)
     */
    @Override
    public void provideContainerContext(Object containerContext)
    {
        lineArguments = (String[]) containerContext;
    }
    
    /* (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#dependencyInjectionDef()
     */
    @Override
    public Object dependencyInjectionDef()
    {
        return new NuunCliModule(commandLine,this.options);
    }
    
    
    private void exception (String message, Object...objects)
    {
        logger.error(message, objects);
        if (objects != null && objects.length == 1 && ( Throwable.class.isAssignableFrom( objects[0].getClass())))
        {
            throw new PluginException(message, (Throwable) objects[0]);
        }
        else 
        {
            throw new PluginException(message, objects);
            
        }
    }
    
    private Option createOptionFromField (Field field )
    {
        Option option = null;
        // Cli Option Builder is completly static :-/
        // so we synchronized it ...
        synchronized ( OptionBuilder.class )
        {
            // reset the builder creating a dummy option
            OptionBuilder.withLongOpt("dummy");
            OptionBuilder.create();
            
            // 
            NuunOption nuunOption = field.getAnnotation(NuunOption.class);
            
            // longopt
            if ( ! Strings.isNullOrEmpty(nuunOption.longOpt()))
            {
                OptionBuilder.withLongOpt( nuunOption.longOpt());
            }
            // description
            if ( ! Strings.isNullOrEmpty(nuunOption.description()))
            {
                OptionBuilder.withDescription(nuunOption.description());
            }
            // required
            OptionBuilder.isRequired((nuunOption.required()));
            
            // arg
            OptionBuilder.hasArg(( nuunOption.arg () ));
            
            // args
            if (nuunOption.args())
            {
                if (nuunOption.numArgs() > 0)
                {
                    OptionBuilder.hasArgs(nuunOption.numArgs());
                }
                else 
                {
                    OptionBuilder.hasArgs();
                }
            }
            // is optional 
            if ( nuunOption.optionalArg() ) 
            {
                OptionBuilder.hasOptionalArg()  ;
            }
            
            // opt 
            if ( ! Strings.isNullOrEmpty(nuunOption.opt()))
            {
                option = OptionBuilder.create(nuunOption.opt());
            }
            else 
            {
                option = OptionBuilder.create();
            }
        }
        
        return option;
        
    }
    
    
    private Set<Field> annotatedFields(Class<?> class_ , Class<? extends Annotation> annoClass)
    {
        Set<Field> fields = new HashSet<Field>();
        for(Field field : class_.getDeclaredFields())
        {
            if (field.isAnnotationPresent(annoClass))
            {
                fields.add(field);
            }
        }
        
        return fields;
    }

    /*
     * (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#classpathScanRequests()
     */
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        optionDefsSpecification = fieldAnnotatedWith(NuunOption.class);
        
        return classpathScanRequestBuilder().specification(optionDefsSpecification).build();
    }

//    class OptionsDefSpecification extends AbstractSpecification<Class<?>>
//    {
//        public OptionsDefSpecification()
//        {
//        }
//
//        @Override
//        public boolean isSatisfiedBy(Class<?> candidate)
//        {
//            return (candidate != null && candidate.isAnnotation() &&     
//
//                    ( candidate.isAnnotationPresent(NuunOption.class))  /*||  annotationAnnotatedWith(candidate, MetaNuunOption.class) */ );
//        }
//        
//        
//        private boolean annotationAnnotatedWith (Class<?> candidate, Class<? extends Annotation> anno) 
//        {
//            for (Annotation a : candidate.getAnnotations())
//            {
//                if (a.annotationType().getAnnotation(anno) != null)
//                {
//                    return true;
//                }
//            }
//            
//            return false;
//        }
//    }

}
