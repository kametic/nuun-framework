package org.nuunframework.kernel.context;

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

import org.nuunframework.kernel.annotations.KernelModule;
import org.nuunframework.kernel.scanner.ClasspathScanner;
import org.nuunframework.kernel.scanner.ClasspathScannerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.inject.Module;

public class InitContextInternal implements InitContext
{

    private Logger                                                       logger = LoggerFactory.getLogger(InitContextInternal.class);

    ClasspathScanner                                                     classpathScanner;

    private final List<Class<?>>                                         parentTypesClassesToScan;
    private final List<Class<?>>                                         typesClassesToScan;
    private final List<String>                                           typesRegexToScan;
    private final List<String>                                           parentTypesRegexToScan;
    private final List<Class<? extends Annotation>>                      annotationTypesToScan;
    private final List<String>                                           annotationRegexToScan;

    private final List<Class<?>>                                         parentTypesClassesToBind;
    private final List<String>                                           parentTypesRegexToBind;
    private final List<Class<? extends Annotation>>                      annotationTypesToBind;
    private final List<String>                                           annotationRegexToBind;

    private final List<String>                                           propertiesPrefix;

    private final List<Module>                                           childModules;
    private final List<String>                                           packageRoots;
    private final Set<URL>                                               additionalClasspathScan;

    private List<Class<?>>                                               classesToBind;

    private Collection<String>                                           propertiesFiles;

    private final Map<String, String>                                    kernelParams;

    private Collection<Class<?>>                                         scanClasspathForSubType;
    private final Map<Class<?>, Collection<Class<?>>>                    mapSubTypes;
    // private final Map<Class<?> , Collection<Class<?>>> mapTypes;
    private final Map<String, Collection<Class<?>>>                      mapSubTypesByName;
    private final Map<String, Collection<Class<?>>>                      mapTypesByName;
    private final Map<Class<? extends Annotation>, Collection<Class<?>>> mapAnnotationTypes;
    private final Map<String, Collection<Class<?>>>                      mapAnnotationTypesByName;
    private final Map<String, Collection<String>>                        mapPropertiesFiles;

    private Object                                                       containerContext;

    /**
     * @param inPackageRoots
     */
    public InitContextInternal(String initialPropertiesPrefix, Map<String, String> kernelParams)
    {
        this.kernelParams = kernelParams;
        this.mapSubTypes = new HashMap<Class<?>, Collection<Class<?>>>();
        // this.mapTypes = new HashMap<Class<?>, Collection<Class<?>>>();
        this.mapSubTypesByName = new HashMap<String, Collection<Class<?>>>();
        this.mapTypesByName = new HashMap<String, Collection<Class<?>>>();
        this.mapAnnotationTypes = new HashMap<Class<? extends Annotation>, Collection<Class<?>>>();
        this.mapAnnotationTypesByName = new HashMap<String, Collection<Class<?>>>();
        this.mapPropertiesFiles = new HashMap<String, Collection<String>>();

        this.annotationTypesToScan = new LinkedList<Class<? extends Annotation>>();
        this.parentTypesClassesToScan = new LinkedList<Class<?>>();
        this.typesClassesToScan = new LinkedList<Class<?>>();
        this.typesRegexToScan = new LinkedList<String>();
        this.parentTypesRegexToScan = new LinkedList<String>();
        this.annotationRegexToScan = new LinkedList<String>();

        this.annotationTypesToBind = new LinkedList<Class<? extends Annotation>>();
        this.parentTypesClassesToBind = new LinkedList<Class<?>>();
        this.parentTypesRegexToBind = new LinkedList<String>();
        this.annotationRegexToBind = new LinkedList<String>();

        this.propertiesPrefix = new LinkedList<String>();
        this.childModules = new LinkedList<Module>();
        this.propertiesPrefix.add(initialPropertiesPrefix);
        this.packageRoots = new LinkedList<String>();
        this.additionalClasspathScan = new HashSet<URL>();
        // for (String packageRoot : inPackageRoots)
        // {
        // this.packageRoots.add(packageRoot);
        // }
    }

    private void initScanner()
    {
        String[] rawArrays = new String[this.packageRoots.size()];
        this.packageRoots.toArray(rawArrays);
        this.classpathScanner = new ClasspathScannerFactory().create( this.additionalClasspathScan , rawArrays);
        
    }

    class Class2Instance implements Function<Class<? extends Module>, Module>
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

    @SuppressWarnings("rawtypes")
    public void executeRequests()
    {
        initScanner();

        classesToBind = new LinkedList<Class<?>>();
        { // bind modules
            Collection<Class<?>> scanClasspathForModules = this.classpathScanner.scanClasspathForAnnotation(KernelModule.class);
            @SuppressWarnings("unchecked")
            Collection<Module> modules = Collections2.transform((Collection) scanClasspathForModules, new Class2Instance());
            this.childModules.addAll(modules);
            // clässes.addAll(scanClasspathForModules);
        }

        // CLASSES TO SCAN
        for (Class<?> parentType : this.parentTypesClassesToScan)
        {
            scanClasspathForSubType = this.classpathScanner.scanClasspathForSubTypeClass(parentType);
            // clässes.addAll(scanClasspathForSubType);
            this.mapSubTypes.put(parentType, scanClasspathForSubType);
        }

        // for (Class<?> type : this.typesClassesToScan)
        // {
        // scanClasspathForSubType = this.classpathScanner.scanClasspathForTypeClass(type);
        // // clässes.addAll(scanClasspathForSubType);
        // this.mapTypes.put(type, scanClasspathForSubType);
        // }

        for (String typeName : this.parentTypesRegexToScan)
        {
            Collection<Class<?>> scanClasspathForTypeName = this.classpathScanner.scanClasspathForSubTypeRegex(typeName);
            // clässes.addAll(scanClasspathForTypeName);
            this.mapSubTypesByName.put(typeName, scanClasspathForTypeName);
        }
        for (String typeName : this.typesRegexToScan)
        {
            Collection<Class<?>> scanClasspathForTypeName = this.classpathScanner.scanClasspathForTypeRegex(typeName);
            // clässes.addAll(scanClasspathForTypeName);
            this.mapTypesByName.put(typeName, scanClasspathForTypeName);
        }

        for (Class<? extends Annotation> annotationType : this.annotationTypesToScan)
        {
            Collection<Class<?>> scanClasspathForAnnotation = this.classpathScanner.scanClasspathForAnnotation(annotationType);
            // clässes.addAll(scanClasspathForAnnotation);
            this.mapAnnotationTypes.put(annotationType, scanClasspathForAnnotation);
        }

        for (String annotationName : this.annotationRegexToScan)
        {
            Collection<Class<?>> scanClasspathForAnnotation = this.classpathScanner.scanClasspathForAnnotationRegex(annotationName);
            // clässes.addAll(scanClasspathForAnnotation);
            this.mapAnnotationTypesByName.put(annotationName, scanClasspathForAnnotation);
        }

        // CLASSES TO BIND
        // ===============
        for (Class<?> parentType : this.parentTypesClassesToBind)
        {
            scanClasspathForSubType = this.classpathScanner.scanClasspathForSubTypeClass(parentType);
            classesToBind.addAll(scanClasspathForSubType);
        }

        for (String typeName : this.parentTypesRegexToBind)
        {
            Collection<Class<?>> scanClasspathForTypeName = this.classpathScanner.scanClasspathForTypeRegex(typeName);
            classesToBind.addAll(scanClasspathForTypeName);
        }

        for (Class<? extends Annotation> annotationType : this.annotationTypesToBind)
        {
            Collection<Class<?>> scanClasspathForAnnotation = this.classpathScanner.scanClasspathForAnnotation(annotationType);
            classesToBind.addAll(scanClasspathForAnnotation);
        }

        for (String annotationName : this.annotationRegexToBind)
        {
            Collection<Class<?>> scanClasspathForAnnotation = this.classpathScanner.scanClasspathForAnnotationRegex(annotationName);
            classesToBind.addAll(scanClasspathForAnnotation);
        }

        // PROPERTIES TO FETCH
        propertiesFiles = new HashSet<String>();
        for (String prefix : this.propertiesPrefix)
        {
            Collection<String> propertiesFilesTmp = this.classpathScanner.scanClasspathForResource(prefix + ".*\\.properties");
            propertiesFiles.addAll(propertiesFilesTmp);
            this.mapPropertiesFiles.put(prefix, propertiesFilesTmp);
        }
    }
    
    public void addClasspathsToScan(Set<URL> paths)
    {
        if (paths != null && paths.size() > 0)
        {
            this.additionalClasspathScan.addAll( paths );
        }
    }

    @Override
    public Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass()
    {
        return Collections.unmodifiableMap(this.mapSubTypes);
    }

    // @Override
    // public Map<Class<?> , Collection<Class<?>>> scannedTypesByClass()
    // {
    // return Collections.unmodifiableMap(this.mapTypes);
    // }

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

    public void addTypeClassToScan(Class<?> type)
    {
        this.typesClassesToScan.add(type);
    }

    public void addParentTypeClassToBind(Class<?> type)
    {
        this.parentTypesClassesToBind.add(type);
    }

    public void addTypeRegexesToScan(String type)
    {
        this.typesRegexToScan.add(type);
    }

    public void addParentTypeRegexesToScan(String type)
    {
        this.parentTypesRegexToScan.add(type);
    }

    public void addTypeRegexesToBind(String type)
    {
        this.parentTypesRegexToBind.add(type);
    }

    public void addAnnotationTypesToScan(Class<? extends Annotation> types)
    {
        this.annotationTypesToScan.add(types);
    }

    public void addAnnotationTypesToBind(Class<? extends Annotation> types)
    {
        this.annotationTypesToBind.add(types);
    }

    public void addAnnotationRegexesToScan(String names)
    {
        this.annotationRegexToScan.add(names);
    }

    public void addAnnotationRegexesToBind(String names)
    {
        this.annotationRegexToBind.add(names);
    }

    public void addChildModule(Module module)
    {
        this.childModules.add(module);
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
            "unchecked", "rawtypes"
    })
    public List<Class<?>> classesToBind()
    {
        return (List) Collections.unmodifiableList(this.classesToBind);
    }

    @Override
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public List<Module> moduleResults()
    {
        return (List) Collections.unmodifiableList(this.childModules);
    }

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    @Override
    public Collection<String> propertiesFiles()
    {
        return (Collection) Collections.unmodifiableCollection(this.propertiesFiles);
    }

}