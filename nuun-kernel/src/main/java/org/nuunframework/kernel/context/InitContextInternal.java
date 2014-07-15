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
package org.nuunframework.kernel.context;

import static com.google.common.base.Predicates.not;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.nuunframework.kernel.Kernel;
import org.nuunframework.kernel.KernelException;
import org.nuunframework.kernel.annotations.KernelModule;
import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.commons.specification.reflect.DescendantOfSpecification;
import org.nuunframework.kernel.internal.scanner.ClasspathScanner;
import org.nuunframework.kernel.internal.scanner.ClasspathScanner.Callback;
import org.nuunframework.kernel.internal.scanner.ClasspathScanner.CallbackResources;
import org.nuunframework.kernel.internal.scanner.ClasspathScannerFactory;
import org.nuunframework.kernel.internal.scanner.ClasspathStrategy;
import org.nuunframework.kernel.plugin.Plugin;
import org.nuunframework.kernel.plugin.request.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.inject.Module;
import com.google.inject.Scopes;
@SuppressWarnings("rawtypes")
public class InitContextInternal implements InitContext
{

    private Logger                                                       logger = LoggerFactory.getLogger(InitContextInternal.class);

    ClasspathScanner                                                     classpathScanner;

    private List<Class<?>>                                         parentTypesClassesToScan;
    private List<Class<?>>                                         ancestorTypesClassesToScan;
    private List<Class<?>>                                         typesClassesToScan;
    private List<String>                                           typesRegexToScan;
    private List<Specification<Class<?>>>                          specificationsToScan;
    private List<String>                                           resourcesRegexToScan;
    private List<String>                                           parentTypesRegexToScan;
    private List<Class<? extends Annotation>>                      annotationTypesToScan;
    private List<String>                                           annotationRegexToScan;

    private List<Class<?>>                                         parentTypesClassesToBind;
    private List<Class<?>>                                         ancestorTypesClassesToBind;
    private List<String>                                           parentTypesRegexToBind;
    private List<Specification<Class<?>>>                          specificationsToBind;
    private Map<Key , Object>                                       mapOfScopes;
    private List<Class<? extends Annotation>>                      annotationTypesToBind;
    private List<Class<? extends Annotation>>                      metaAnnotationTypesToBind;
    private List<String>                                           annotationRegexToBind;
    private List<String>                                           metaAnnotationRegexToBind;

    private List<String>                                           propertiesPrefix;

    private List<Module>                                           childModules;
    private List<Module>                                           childOverridingModules;
    private List<String>                                           packageRoots;
    private Set<URL>                                               additionalClasspathScan;
    private ClasspathStrategy                                      classpathStrategy;

    private Set<Class<?>>                                               classesToBind;
    private Map<Class<?> , Object>                                       classesWithScopes;

    private Collection<String>                                           propertiesFiles;

    private Map<String, String>                                    kernelParams;

    private Collection<Class<?>>                                         scanClasspathForSubType;
    private Collection<Class<?>>                                         scanClasspathForAncestorType;
    private Collection<Class<?>>                                         bindClasspathForSubType;
    private Collection<Class<?>>                                         bindClasspathForAncestorType;
    private Map<Class<?>, Collection<Class<?>>>                    mapSubTypes;
    private Map<Class<?>, Collection<Class<?>>>                    mapAncestorTypes;
    private Map<String, Collection<Class<?>>>                      mapSubTypesByName;
    private Map<String, Collection<Class<?>>>                      mapTypesByName;
    private Map<Specification, Collection<Class<?>>>               mapTypesBySpecification;
    private Map<Class<? extends Annotation>, Collection<Class<?>>> mapAnnotationTypes;
    private Map<String, Collection<Class<?>>>                      mapAnnotationTypesByName;
    private Map<String, Collection<String>>                        mapPropertiesFiles;
    private Map<String, Collection<String>>                        mapResourcesByRegex;

    private String initialPropertiesPrefix;

    private int roundNumber = 0;

    /**
     * @param inPackageRoots
     */
    public InitContextInternal(String initialPropertiesPrefix, Map<String, String> kernelParams)
    {
        String classpathStrategyNameParam = kernelParams.get(Kernel.NUUN_CP_STRATEGY_NAME);
        String classpathStrategyAdditionalParam = kernelParams.get(Kernel.NUUN_CP_STRATEGY_ADD);
        String classpathStrategyDeduplicateParam = kernelParams.get(Kernel.NUUN_CP_STRATEGY_DEDUP);
        String classpathStrategyTrailingSlashParam = kernelParams.get(Kernel.NUUN_CP_STRATEGY_SLASH);
        String classpathStrategyThresholdParam = kernelParams.get(Kernel.NUUN_CP_STRATEGY_TH);

        this.classpathStrategy = new ClasspathStrategy(
                classpathStrategyNameParam == null ? ClasspathStrategy.Strategy.ALL : ClasspathStrategy.Strategy.valueOf(classpathStrategyNameParam.toUpperCase()),
                classpathStrategyAdditionalParam == null ? true : Boolean.parseBoolean(classpathStrategyAdditionalParam),
                classpathStrategyDeduplicateParam == null ? true : Boolean.parseBoolean(classpathStrategyDeduplicateParam),
                classpathStrategyTrailingSlashParam == null ? true : Boolean.parseBoolean(classpathStrategyTrailingSlashParam),
                classpathStrategyThresholdParam == null ? ClasspathStrategy.DEFAULT_THRESHOLD : Integer.parseInt(classpathStrategyThresholdParam)
        );
        this.packageRoots = new LinkedList<String>();
        this.initialPropertiesPrefix = initialPropertiesPrefix;
        this.kernelParams = kernelParams;
        this.childModules = new LinkedList<Module>();
        this.childOverridingModules = new LinkedList<Module>();
        classesToBind = new HashSet<Class<?>>();
        classesWithScopes = new HashMap<Class<?>, Object>();
        reset();
    }
    
    public void reset()
    {
        this.mapSubTypes = new HashMap<Class<?>, Collection<Class<?>>>();
        this.mapAncestorTypes = new HashMap<Class<?>, Collection<Class<?>>>();
        this.mapSubTypesByName = new HashMap<String, Collection<Class<?>>>();
        this.mapTypesByName = new HashMap<String, Collection<Class<?>>>();
        this.mapTypesBySpecification = new HashMap<Specification, Collection<Class<?>>>();
        this.mapAnnotationTypes = new HashMap<Class<? extends Annotation>, Collection<Class<?>>>();
        this.mapAnnotationTypesByName = new HashMap<String, Collection<Class<?>>>();
        this.mapPropertiesFiles = new HashMap<String, Collection<String>>();
        this.mapResourcesByRegex = new HashMap<String, Collection<String>>();
        
        this.annotationTypesToScan = new LinkedList<Class<? extends Annotation>>();
        this.parentTypesClassesToScan = new LinkedList<Class<?>>();
        this.ancestorTypesClassesToScan = new LinkedList<Class<?>>();
        this.typesClassesToScan = new LinkedList<Class<?>>();
        this.typesRegexToScan = new LinkedList<String>();
        this.specificationsToScan = new LinkedList<Specification<Class<?>>>(); 
        this.resourcesRegexToScan = new LinkedList<String>();
        this.parentTypesRegexToScan = new LinkedList<String>();
        this.annotationRegexToScan = new LinkedList<String>();
        
        this.annotationTypesToBind = new LinkedList<Class<? extends Annotation>>();
        this.metaAnnotationTypesToBind = new LinkedList<Class<? extends Annotation>>();
        this.parentTypesClassesToBind = new LinkedList<Class<?>>();
        this.ancestorTypesClassesToBind = new LinkedList<Class<?>>();
        this.parentTypesRegexToBind = new LinkedList<String>();
        this.specificationsToBind = new LinkedList<Specification<Class<?>>>();
        this.mapOfScopes = new HashMap<Key, Object>();
        this.annotationRegexToBind = new LinkedList<String>();
        this.metaAnnotationRegexToBind = new LinkedList<String>();
        
        this.propertiesPrefix = new LinkedList<String>();
        this.propertiesPrefix.add(initialPropertiesPrefix);
        this.additionalClasspathScan = new HashSet<URL>();
    }

    private void initScanner()
    {
        String[] rawArrays = new String[this.packageRoots.size()];
        this.packageRoots.toArray(rawArrays);
        this.classpathScanner = new ClasspathScannerFactory().create(this.classpathStrategy, this.additionalClasspathScan , rawArrays);
        
    }
    
    class IsModuleOverriding implements Predicate<Class<? extends Module>>
    {

        @Override
        public boolean apply(Class<? extends Module> input)
        {
            KernelModule annotation = input.getAnnotation(KernelModule.class);
            
            return annotation.overriding();
        }
        
    }
    
    class ModuleClass2Instance implements Function<Class<? extends Module>, Module>
    {

        /*
         * (non-Javadoc)
         * @see com.google.common.base.Function#apply(java.lang.Object)
         */
        @Override
        public Module apply(Class<? extends Module> classpathClass)
        {
            try
            {
                return (Module) classpathClass.newInstance();
            }
            catch (InstantiationException e)
            {
                logger.warn("Error when instantiating module " + classpathClass, e);
            }
            catch (IllegalAccessException e)
            {
                logger.warn("Error when instantiating module " + classpathClass, e);
            }
            return null;
        }
    }

    
    public void executeRequests()
    {
        initScanner();
        
        { // bind modules
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<? >> scanResult)
                {
                    
                    Collection<Class<? extends Module>> scanResult2 = (Collection) scanResult;
                    FluentIterable<Module> nominals = FluentIterable.from(scanResult2).filter( not(new IsModuleOverriding())  ).transform(new ModuleClass2Instance());
                    FluentIterable<Module> overriders = FluentIterable.from(scanResult2).filter( new IsModuleOverriding() ).transform(new ModuleClass2Instance());
                    
                    childModules.addAll(nominals.toSet());
                    childOverridingModules.addAll(overriders.toSet());
                }
            };
            this.classpathScanner.scanClasspathForAnnotation(KernelModule.class , callback); // OK

        }

        // CLASSES TO SCAN
        for (final Class<?> parentType : this.parentTypesClassesToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    mapSubTypes.put(parentType, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSubTypeClass(parentType , callback); // OK
        }

        for (final Class<?> parentType : this.ancestorTypesClassesToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    mapAncestorTypes.put(parentType, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSpecification(new DescendantOfSpecification(parentType) , callback); // ok
        }

        // for (Class<?> type : this.typesClassesToScan)
        // {
        // scanClasspathForSubType = this.classpathScanner.scanClasspathForTypeClass(type);
        // // clässes.addAll(scanClasspathForSubType);
        // this.mapTypes.put(type, scanClasspathForSubType);
        // }

        for (final String typeName : this.parentTypesRegexToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapSubTypesByName.put(typeName, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSubTypeRegex(typeName, callback); // OK
        }
        
        for (final String typeName : this.typesRegexToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapTypesByName.put(typeName, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForTypeRegex(typeName, callback); // OK
        }
        
        for (final Specification<Class<?>> spec : this.specificationsToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs
                @Override
                public void callback(Collection<Class<?>> scanResult)
                {
                    mapTypesBySpecification.put(spec, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSpecification(spec, callback); // OK
        }

        for (final Class<? extends Annotation> annotationType : this.annotationTypesToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    mapAnnotationTypes.put(annotationType, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForAnnotation(annotationType , callback);// ok
        }

        for (final String annotationName : this.annotationRegexToScan)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    mapAnnotationTypesByName.put(annotationName, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForAnnotationRegex(annotationName,callback); // ok
            
        }

        // CLASSES TO BIND
        // ===============
        for (final Class<?> parentType : this.parentTypesClassesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.SUBTYPE_OF_BY_CLASS;
                    addScopeToClasses( scanResult , scope(requestType , parentType ) , classesWithScopes);
                    
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSubTypeClass(parentType , callback); // OK
            
        }
        for (final Class<?> ancestorType : this.ancestorTypesClassesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.SUBTYPE_OF_BY_TYPE_DEEP;
                    addScopeToClasses( scanResult , scope(requestType , ancestorType ) , classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSpecification(new DescendantOfSpecification(ancestorType) , callback); // OK
        }

        // TODO vérifier si ok parent types vs type. si ok changer de nom
        for (final String typeName : this.parentTypesRegexToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.SUBTYPE_OF_BY_REGEX_MATCH;
                    addScopeToClasses( scanResult , scope(requestType , typeName ) , classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForTypeRegex(typeName,callback); // ok
        }

        for (final Specification<Class<?>>  spec : this.specificationsToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.VIA_SPECIFICATION;
                    addScopeToClasses(scanResult, scope(requestType , spec ) , classesWithScopes);
                    
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForSpecification(spec,callback); // ok
        }

        for (final Class<? extends Annotation> annotationType : this.annotationTypesToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.ANNOTATION_TYPE;
                    addScopeToClasses( scanResult , scope(requestType , annotationType ) , classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForAnnotation(annotationType,callback); // OK
        }

        for (final Class<? extends Annotation> metaAnnotationType : this.metaAnnotationTypesToBind) 
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.META_ANNOTATION_TYPE;
                    addScopeToClasses( scanResult , scope(requestType , metaAnnotationType ) , classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForMetaAnnotation(metaAnnotationType,callback); // ok
        }

        for (final String annotationNameRegex : this.annotationRegexToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.ANNOTATION_REGEX_MATCH;
                    addScopeToClasses( scanResult , scope(requestType , annotationNameRegex ) , classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForAnnotationRegex(annotationNameRegex,callback); // ok
        }

        for (final String metaAnnotationNameRegex : this.metaAnnotationRegexToBind)
        {
            Callback callback = new Callback()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<Class<?>> scanResult)
                { 
                    RequestType requestType = RequestType.META_ANNOTATION_REGEX_MATCH;
                    addScopeToClasses( scanResult , scope(requestType , metaAnnotationNameRegex ) , classesWithScopes);
                    classesToBind.addAll(scanResult);
                }
            };
            this.classpathScanner.scanClasspathForMetaAnnotationRegex(metaAnnotationNameRegex,callback); // ok
        }

        // Resources to scan
        
        for (final String regex : this.resourcesRegexToScan)
        {
            CallbackResources callback = new CallbackResources()
            {
                @Override
                public void callback(Collection<String> scanResult)
                {
                    mapResourcesByRegex.put(regex, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForResource(regex , callback); // OK
        }
        
        // PROPERTIES TO FETCH
        propertiesFiles = new HashSet<String>();
        for (final String prefix : this.propertiesPrefix)
        {
            CallbackResources callback = new CallbackResources()
            { // executed only after the classpath scan occurs 
                @Override
                public void callback(Collection<String> scanResult)
                { 
                    propertiesFiles.addAll(scanResult);
                    mapPropertiesFiles.put(prefix, scanResult);
                }
            };
            this.classpathScanner.scanClasspathForResource(prefix + ".*\\.properties" , callback); // OK
        }
        
        // ACTUALLY LAUNCH THE SEARCH 
        this.classpathScanner.doClasspathScan();
    }

    private Object scope( RequestType requestType , Object spec)
    {
        Object scope = this.mapOfScopes.get( key( requestType ,  spec));
        if (null == scope) scope = Scopes.NO_SCOPE;
        return scope;
    }
    
    private void addScopeToClasses(Collection<Class<?>> classes , Object scope, Map<Class<?>, Object> inClassesWithScopes)
    {
        for (Class<?> klass : classes)
        {
            if (!inClassesWithScopes.containsKey(klass)  && scope != null )
            {
                inClassesWithScopes.put(klass, scope);
            }
            else
            {
                Object insideScope = inClassesWithScopes.get(klass);
                if ( ! insideScope.equals(scope))
                {
                    String format = String.format("Class %s is already associated with scope %s but  %s", klass.getName() , insideScope , scope );
                    logger.error(format);
                    throw new KernelException(format);
                }
            }
        }
    }
    
    public void addClasspathsToScan(Set<URL> paths)
    {
        if (paths != null && paths.size() > 0)
        {
            this.additionalClasspathScan.addAll( paths );
        }
    }
    public void addClasspathToScan(URL path)
    {
        if (path != null )
        {
            this.additionalClasspathScan.add( path );
        }
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass()
    {
        return Collections.unmodifiableMap(this.mapSubTypes);
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass()
    {
        return Collections.unmodifiableMap(this.mapAncestorTypes);
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex()
    {
        return Collections.unmodifiableMap(this.mapSubTypesByName);
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedTypesByRegex()
    {
        return Collections.unmodifiableMap(this.mapTypesByName);
    }

    @Override
    public Map<Specification, Collection<Class<?>>> scannedTypesBySpecification()
    {
        return Collections.unmodifiableMap(this.mapTypesBySpecification);
    }

    @Override
    public Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass()
    {
        return Collections.unmodifiableMap(this.mapAnnotationTypes);
    }

    @Override
    public Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex()
    {
        return Collections.unmodifiableMap(this.mapAnnotationTypesByName);
    }

    @Override
    public Map<String, Collection<String>> mapPropertiesFilesByPrefix()
    {
        return Collections.unmodifiableMap(this.mapPropertiesFiles);
    }
    
    @Override
    public Map<String, Collection<String>> mapResourcesByRegex()
    {
        return Collections.unmodifiableMap(this.mapResourcesByRegex);
    }

    public void addPropertiesPrefix(String prefix)
    {
        this.propertiesPrefix.add(prefix);
    }

    public void addPackageRoot(String root)
    {
        this.packageRoots.add(root);
    }

    public void addParentTypeClassToScan(Class<?> type)
    {
        this.parentTypesClassesToScan.add(type);
    }

    public void addAncestorTypeClassToScan(Class<?> type)
    {
        this.ancestorTypesClassesToScan.add(type);
    }

    public void addResourcesRegexToScan(String regex)
    {
        this.resourcesRegexToScan.add(regex);
    }

    public void addTypeClassToScan(Class<?> type)
    {
        this.typesClassesToScan.add(type);
    }

    private Key key(RequestType type , Object key)
    {
        return new Key(type, key);
    }
    
    public void addParentTypeClassToBind(Class<?> type , Object scope)
    {
        updateScope(key ( RequestType.SUBTYPE_OF_BY_CLASS ,  type), scope);
        this.parentTypesClassesToBind.add(type);
    }

    public void addAncestorTypeClassToBind(Class<?> type , Object scope)
    {
        updateScope(key ( RequestType.SUBTYPE_OF_BY_TYPE_DEEP ,  type), scope);
        this.ancestorTypesClassesToBind.add(type);
    }

    public void addTypeRegexesToScan(String type)
    {
        this.typesRegexToScan.add(type);
    }
    
    public void addSpecificationToScan(Specification<Class<?>> specification)
    {
        this.specificationsToScan.add(specification);
    }

    public void addParentTypeRegexesToScan(String type)
    {
        this.parentTypesRegexToScan.add(type);
    }

    public void addTypeRegexesToBind(String type , Object scope)
    {
        updateScope(key ( RequestType.TYPE_OF_BY_REGEX_MATCH,  type), scope);
        this.parentTypesRegexToBind.add(type);
    }
    /**
     * @category bind
     * @param specification
     */
//    public void addSpecificationToBind(Specification<Class<?>> specification)
//    {
//        this.specificationsToBind.add(specification);
//        this.mapSpecificationScope.put(specification, Scopes.NO_SCOPE);
//    }

    private void updateScope ( Key key , Object scope)
    {
        if (scope != null)
        {
            this.mapOfScopes.put(key, scope);
        }
        else
        {
            this.mapOfScopes.put(key, Scopes.NO_SCOPE);
        }
            
        
    }
    
    public void addSpecificationToBind(Specification<Class<?>> specification , Object scope)
    {
        this.specificationsToBind.add(specification);
        updateScope(key ( RequestType.VIA_SPECIFICATION ,  specification), scope);
    }

    public void addAnnotationTypesToScan(Class<? extends Annotation> types)
    {
        this.annotationTypesToScan.add(types);
    }

    public void addAnnotationTypesToBind(Class<? extends Annotation> types , Object scope)
    {
        this.annotationTypesToBind.add(types);
        updateScope(key ( RequestType.ANNOTATION_TYPE ,  types), scope);
    }

    public void addMetaAnnotationTypesToBind(Class<? extends Annotation> types , Object scope)
    {
        this.metaAnnotationTypesToBind.add(types);
        updateScope(key ( RequestType.META_ANNOTATION_TYPE ,  types), scope);
    }

    public void addAnnotationRegexesToScan(String names)
    {
        this.annotationRegexToScan.add(names);
    }

    public void addAnnotationRegexesToBind(String names, Object scope)
    {
        this.annotationRegexToBind.add(names);
        updateScope(key ( RequestType.ANNOTATION_REGEX_MATCH ,  names), scope);
    }
    public void addMetaAnnotationRegexesToBind(String names, Object scope)
    {
        this.metaAnnotationRegexToBind.add(names);
        updateScope(key ( RequestType.META_ANNOTATION_REGEX_MATCH,  names), scope);
    }

    public void addChildModule(Module module)
    {
        this.childModules.add(module);
    }

    public void addChildOverridingModule(Module module)
    {
        this.childOverridingModules.add(module);
    }

//    public void setContainerContext(Object containerContext)
//    {
//        this.containerContext = containerContext;
//    }

    // INTERFACE KERNEL PARAM USED BY PLUGIN IN INIT //

//    public Object containerContext()
//    {
//        return this.containerContext;
//    }

    @Override
    public String getKernelParam(String key)
    {
        return kernelParams.get(key);
    }

    @Override
    @SuppressWarnings({
            "unchecked"
    })
    public Collection<Class<?>> classesToBind()
    {
        return (Collection) Collections.unmodifiableSet(this.classesToBind);
    }
    
    @SuppressWarnings({"unchecked"})
    public Map<Class<?> , Object> classesWithScopes ()
    {
        return  (Map) Collections.unmodifiableMap(classesWithScopes );
    }

    @Override
    @SuppressWarnings({
            "unchecked"
    })
    public List<Module> moduleResults()
    {
        return (List) Collections.unmodifiableList(this.childModules);
    }

    @Override
    @SuppressWarnings({
        "unchecked"
    })
    public List<Module> moduleOverridingResults()
    {
        return (List) Collections.unmodifiableList(this.childOverridingModules);
    }

    @SuppressWarnings({
            "unchecked"
    })
    @Override
    public Collection<String> propertiesFiles()
    {
        return (Collection) Collections.unmodifiableCollection(this.propertiesFiles);
    }
    
    @Override
    public Collection<? extends Plugin> pluginsRequired()
    {
        return Collections.emptySet();
    }
    
    public Collection<? extends Plugin> dependentPlugins ()
    {
        return Collections.emptySet();
    }
    
    @Override
    public int roundNumber()
    {
        return this.roundNumber;
    }
    
    public void roundNumber(int roundNumber)
    {
        this.roundNumber  = roundNumber;
        
    }
    
    
    static class Key
    {
        private final RequestType type;
        private final Object key;
        
        public Key(RequestType type , Object key)
        {
            this.type = type;
            this.key = key;
        }
        
        @Override
        public boolean equals(Object obj)
        {
            Key key2 = (Key)obj;
            return new EqualsBuilder().append(this.type, key2.type ).append( this.key, key2.key).isEquals() ;
        }
        
        @Override
        public int hashCode()
        {
            return new HashCodeBuilder().append(this.type).append( this.key).toHashCode();
        }
    }

}