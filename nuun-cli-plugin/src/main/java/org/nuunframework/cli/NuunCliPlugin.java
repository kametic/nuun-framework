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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.ArrayUtils;
import org.nuunframework.cli.api.NuunCliHandler;
import org.nuunframework.kernel.commons.AssertUtils;
import org.nuunframework.kernel.commons.specification.AbstractSpecification;
import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.context.InitContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.InitState;
import org.nuunframework.kernel.plugin.PluginException;
import org.nuunframework.kernel.plugin.request.ClasspathScanRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class NuunCliPlugin extends AbstractPlugin
{

    private Logger                  logger = LoggerFactory.getLogger(NuunCliPlugin.class);
    private Specification<Class<?>> optionDefsSpecification;
    private Options optionsAggregated;
    private String[] lineArguments;
    private CommandLine commandLine;
    private Map<Class<?>, CommandLine> contextualCommandLineMap;
    private CommandLineParser providedParser;
    
    Specification<Class<?>> cliHandlerSpec = null;
    private Map<Class, Class> bindings;
    private Map<Class<?>, Options> byClassOptions;
    
    
    /**
     * 
     */
    public NuunCliPlugin()
    {
        bindings = Maps.newHashMap();
        contextualCommandLineMap = Maps.newHashMap();
        optionsAggregated = new Options();
    }

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
       // sanity check         
        if (lineArguments == null || lineArguments.length == 0)
            exception("Empty or Null command line has been provided to nuun-cli-plugin");
        
        
        // Parser initialization
        CommandLineParser parser = null;
        if (providedParser == null)
        {
            parser = new PosixParser();
        }
        else {
            parser = providedParser;
        }
        
        /////////// handler CliHandler
        {
            Collection<Class<?>> collection = initContext.scannedTypesBySpecification().get(cliHandlerSpec);
            
            for (Class<?> class1 : collection) {
                bindings.put(NuunCliHandler.class, class1);
                // for now only one CommandLineHandler
                break; 
            }
        }
        
        //////////// Define options and command lines
        
        
        Collection<Class<?>> collection = initContext.scannedTypesBySpecification().get(optionDefsSpecification);
        
        byClassOptions = createOptions(collection);
 
        
        boolean oneParsing = false;
        Map<Class<?>, ParseException> parsingErrors = Maps.newHashMap();
        for (Entry<Class<?>, Options> entry : byClassOptions.entrySet()) 
        {
            try
            {
                commandLine = parser.parse(  entry.getValue(), lineArguments);
                oneParsing = true;
                contextualCommandLineMap.put(entry.getKey(), commandLine);
            }
            catch (ParseException e)
            {
                parsingErrors.put(entry.getKey(), e);
//                logException("Error in the command line", e);
            }
        }
        if (! oneParsing ) 
        {
            logger.error("Errors occurs when parsing. List by context :  *********************************" );
            logger.error(" for commandLine : \""  + treat (lineArguments) + "\"");
            for (Entry<Class<?>, ParseException> entry : parsingErrors.entrySet())
            {
                logger.error("Current context : " + entry.getKey()); 
                logger.error("  message : " + entry.getValue().getMessage()); 
                
            }
        }
        
        
        return InitState.INITIALIZED;
    }

    /**
     * @param lineArguments2
     * @return
     */
    private String treat(String[] lineArguments2)
    {
        StringBuilder bulder = new StringBuilder();
        
        for (String string : lineArguments2)
        {
            bulder.append( string).append(' ');
        }
        return bulder.toString();
    }

    private Map<Class<?>, Options> createOptions(Collection<Class<?>> collection)
    {
        Set< String> shortOptions = Sets.newHashSet();
        Set< String> longOptions  = Sets.newHashSet();
        
        Map< Class<?>,Options> optionsMap = Maps.newHashMap();
        
        for (Class<?> class1 : collection)
        {
            logger.info("CLASS " + class1.getSimpleName());
            Set<Field> fields = annotatedFields(class1, NuunOption.class);
            Options internalOptions = new Options();
            for(Field field : fields)
            {
                logger.info("Class : " + field.getDeclaringClass().getSimpleName() + " / " + field.getName());
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
                
                internalOptions.addOption(option);
                // global one
                optionsAggregated.addOption(option);
                
                shortOptions.add(option.getOpt());
                longOptions.add(option.getLongOpt());
            }
            
            optionsMap.put(class1, internalOptions);
            
        }
        
        return optionsMap;
    }
    
    public void provideParser(CommandLineParser parser)
    {
        this.providedParser = parser;
        
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
        return new NuunCliModule(commandLine,this.optionsAggregated, contextualCommandLineMap  , byClassOptions ,bindings);
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
    private void logException (String message, Object...objects)
    {
        logger.error(message, objects);
        PluginException pluginException = null;
        if (objects != null && objects.length == 1 && ( Throwable.class.isAssignableFrom( objects[0].getClass())))
        {
            pluginException = new PluginException(message, (Throwable) objects[0]);
        }
        else 
        {
            pluginException = new PluginException(message, objects);
            
        }
        logger.error( "Nuun Cli Plugin Exception : ", pluginException);
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
            
            if (nuunOption == null) 
            {
                for (Annotation anno : field.getAnnotations() )
                {
                    if ( AssertUtils.hasAnnotationDeep(anno.annotationType(), NuunOption.class ) )
                    {
                        nuunOption = AssertUtils.annotationProxyOf(NuunOption.class, anno);
                        break;
                    }
                }
            }
            
            
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
            
            // nuun 
            OptionBuilder.withValueSeparator(nuunOption.valueSeparator());
            
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
            else {
                for (Annotation annotation : field.getAnnotations())
                {
                    if ( hasAnnotationDeep(annotation.annotationType(), annoClass))
                    {
//                        return true;
                        fields.add(field);
                    }
                }
            }
        }
        
        return fields;
    }

    /*
     * (non-Javadoc)
     * @see org.nuunframework.kernel.plugin.AbstractPlugin#classpathScanRequests()
     */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<ClasspathScanRequest> classpathScanRequests()
    {
        optionDefsSpecification = fieldAnnotatedWith(NuunOption.class);
        
        cliHandlerSpec = and (
                ancestorImplements(NuunCliHandler.class), //
                not(classIsInterface()) , //
                not(classIsAbstract()) // 
                ); 
        
        return classpathScanRequestBuilder() //
                .specification(optionDefsSpecification) //
                .specification(cliHandlerSpec)
                .build();
    }

    protected Specification<Class<?>> fieldAnnotatedWith (final Class<? extends Annotation> annotationClass)
    {
    	return new AbstractSpecification<Class<?>> ()
    	{
    		@Override
    		public boolean isSatisfiedBy(Class<?> candidate) {
    			
    			if (candidate != null)
    			{
    			    try
                    {
    			        for (Field field : candidate.getDeclaredFields())
    			        {
    			            if ( field.isAnnotationPresent(annotationClass) ) 
    			            {
    			                return true;
    			            }
    			            for (Annotation annotation : field.getAnnotations())
    			            {
    			                if ( hasAnnotationDeep(annotation.annotationType(), annotationClass))
    			                {
    			                    return true;
    			                }
    			            }
    			        }
                    }
                    catch (Throwable throwable)
                    {
                        logger.trace("fieldAnnotatedWith : " +candidate +  " missing " + throwable );
                    }
    			}
    			
    			return false;
    		}
    	};
    }
    
    boolean hasAnnotationDeep(   Class<? extends Annotation>   from ,  Class<? extends Annotation> toFind)
    {
        
        if (from.equals(toFind))
        {
            return true;
        }
        
        for (Annotation anno : from.getAnnotations())
        {
            Class<? extends Annotation> annoClass = anno.annotationType();
            if (!annoClass.getPackage().getName().startsWith("java.lang") && hasAnnotationDeep(annoClass, toFind))
            {
                return true;
            }
        }
        
        return false;
    }
    /*******************************************************************/
    /************* SPECIFICATIONS                     ******************/
    /*******************************************************************/
    
    protected Specification<Class<?>> classIsInterface() {
        return new AbstractSpecification<Class<?>>() {
            @Override
            public boolean isSatisfiedBy(Class<?> candidate) {
                
                return candidate != null && candidate.isInterface();
            }
        };
    }
    
    protected Specification<Class<?>> classIsAbstract() {
        return new AbstractSpecification<Class<?>>() {
            @Override
            public boolean isSatisfiedBy(Class<?> candidate) {

                return candidate != null && Modifier.isAbstract(candidate.getModifiers());
            }
        };
    }
    
    protected Specification<Class<?>> ancestorImplements (final Class<?> interfaceClass)
    {
        return new AbstractSpecification<Class<?>>() {

            @Override
            public boolean isSatisfiedBy(Class<?> candidate) {
                if (candidate == null) return false;
                
                boolean result = false;
                
                Class<?>[] allInterfacesAndClasses = getAllInterfacesAndClasses(candidate);

                for (Class<?> clazz : allInterfacesAndClasses) {
                    if (! clazz.isInterface()    )
                    {
                         for (Class<?> i : clazz.getInterfaces())
                         {
                             if (i.equals(interfaceClass))
                             {
                                result = true;
                                 break;
                             }
                         }
                    }
                }
                
                return result;
            }
            
        };
    }
    
   Class<?>[] getAllInterfacesAndClasses(Class<?> clazz) {
            return getAllInterfacesAndClasses(new Class[] { clazz } );
        }
        
         //This method walks up the inheritance hierarchy to make sure we get every class/interface that could
        //possibly contain the declaration of the annotated method we're looking for.
        @SuppressWarnings("unchecked")
     Class<?>[] getAllInterfacesAndClasses(Class<?>[] classes) {
        if (0 == classes.length) {
            return classes;
        } else {
            List<Class<?>> extendedClasses = new ArrayList<Class<?>>();
            // all interfaces hierarchy
            for (Class<?> clazz : classes) {
                if (clazz != null) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    if (interfaces != null) {
                        extendedClasses
                                .addAll((List<? extends Class<?>>) Arrays
                                        .asList(interfaces));
                    }
                    Class<?> superclass = clazz.getSuperclass();
                    if (superclass != null && superclass != Object.class) {
                        extendedClasses
                                .addAll((List<? extends Class<?>>) Arrays
                                        .asList(superclass));
                    }
                }
            }

            // Class::getInterfaces() gets only interfaces/classes
            // implemented/extended directly by a given class.
            // We need to walk the whole way up the tree.
            return (Class[]) ArrayUtils.addAll(classes,
                    getAllInterfacesAndClasses(extendedClasses
                            .toArray(new Class[extendedClasses.size()])));
        }
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
