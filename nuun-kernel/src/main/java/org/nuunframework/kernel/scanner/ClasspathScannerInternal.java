package org.nuunframework.kernel.scanner;

import static org.reflections.util.FilterBuilder.prefix;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.nuunframework.kernel.KernelException;
import org.nuunframework.kernel.annotations.Ignore;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Multimap;

class ClasspathScannerInternal implements ClasspathScanner
{

    private final List<String> packageRoots;
    private final boolean      reachAbstractClass;
    private Set<URL>           additionalClasspath;
    private Set<URL>           urls;

    public ClasspathScannerInternal(String... packageRoots_)
    {
        this(false, null, packageRoots_);

    }

    public ClasspathScannerInternal(boolean reachAbstractClass, String packageRoot, String... packageRoots_)
    {
        this.packageRoots = new LinkedList<String>();

        if (packageRoot != null)
        {
            this.packageRoots.add(packageRoot);
        }

        for (String packageRoot_ : packageRoots_)
        {
            this.packageRoots.add(packageRoot_);
        }
        this.reachAbstractClass = reachAbstractClass;
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public Collection<Class<?>> scanClasspathForAnnotation(Class<? extends Annotation> annotationType)
    {
        ConfigurationBuilder configurationBuilder = configurationBuilder();
        Set<URL> computeUrls = computeUrls();
        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypeAnnotationsScanner()));

//        Multimap<String, String> multimap = reflections.getStore().get(TypeAnnotationsScanner.class);
//        Collection<String> names = multimap.get(annotationType.getName());
//        Collection<Class<?>> typesAnnotatedWith = toClasses2(names);
        Collection<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotationType);

        if (typesAnnotatedWith == null)
        {
            typesAnnotatedWith = Collections.emptySet();
        }

        return (Collection) postTreatment((Collection) typesAnnotatedWith);
    }

    static class IgnorePredicate implements Predicate<Class<?>>
    {

        private final boolean reachAbstractClass;

        public IgnorePredicate(boolean reachAbstractClass)
        {
            this.reachAbstractClass = reachAbstractClass;
        }

        @Override
        public boolean apply(Class<?> clazz)
        {
            boolean toKeep = true;

            if ((Modifier.isAbstract(clazz.getModifiers()) && !reachAbstractClass) && (!clazz.isInterface()))
            {
                toKeep = false;
            }

            for (Annotation annotation : clazz.getAnnotations())
            {
                if (annotation.annotationType().equals(Ignore.class) || annotation.annotationType().getName().endsWith(".Ignore"))
                {
                    toKeep = false;
                    break;
                }
            }
            return toKeep;
        }
    }

    private Collection<Class<?>> postTreatment(Collection<Class<?>> set)
    {
        
        // Sanity Check : throw a KernelException if one of the returned classes are null
        for (Class<?> class1 : set)
        {
            if (null == class1)
            {
                throw new KernelException("Scanned classes results can not be null. Please check Integrity of the classes.");
            }
        }
        
        Collection<Class<?>> filtered = Collections2.filter(set, new IgnorePredicate(reachAbstractClass));

        return filtered;

    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    public java.util.Collection<java.lang.Class<?>> scanClasspathForAnnotationRegex(String annotationTypeName)
    {
        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new NameAnnotationScanner(annotationTypeName)));

        Store store = reflections.getStore();

        Multimap<String, String> multimap = store.get(NameAnnotationScanner.class);

        String key = null;
        for (String loopKey : multimap.keySet())
        {
            if (loopKey.matches(annotationTypeName))
            {
                key = loopKey;
            }
        }

        Collection<Class<?>> typesAnnotatedWith = null;
        if (key != null)
        {
            Collection<String> collectionOfString = multimap.get(key);
            typesAnnotatedWith = toClasses(collectionOfString);
        }

        if (typesAnnotatedWith == null)
        {
            typesAnnotatedWith = Collections.emptySet();
        }

        return (Collection) postTreatment((Collection) typesAnnotatedWith);
    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public Collection<Class<?>> scanClasspathForTypeRegex(String typeName)
    {
        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));

        Store store = reflections.getStore();

        Multimap<String, String> multimap = store.get(TypesScanner.class);

        Collection<String> collectionOfString = new HashSet<String>();

        for (String loopKey : multimap.keySet())
        {

            if (loopKey.matches(typeName))
            {
                collectionOfString.add(loopKey);
            }
        }

        Collection<Class<?>> types = null;

        if (collectionOfString.size() > 0)
        {
            types = toClasses(collectionOfString);
        }
        else
        {
            types = Collections.emptySet();
        }

        return (Collection) postTreatment((Collection) types);

    }

    @SuppressWarnings({})
    @Override
    public Collection<Class<?>> scanClasspathForSubTypeRegex(String subTypeName)
    {

        // Reflections reflections =
        // new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
        //
        // Collection<Class<?>> typesAnnotatedWith = reflections.getSubTypesOf(type)
        //
        // if (typesAnnotatedWith == null)
        // {
        // typesAnnotatedWith = Collections.emptySet();
        // }
        //
        // return (Collection) removeIgnore((Collection) typesAnnotatedWith);

        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));

        Store store = reflections.getStore();

        Multimap<String, String> multimap = store.get(TypesScanner.class);

        Collection<String> collectionOfString = new HashSet<String>();

        for (String loopKey : multimap.keySet())
        {

            if (loopKey.matches(subTypeName))
            {
                collectionOfString.add(loopKey);
            }
        }

        Collection<Class<?>> types = null;

        if (collectionOfString.size() > 0)
        {
            types = toClasses(collectionOfString);
        }
        else
        {
            types = Collections.emptySet();
        }

        // Then find subclasses of types

        Collection<Class<?>> finalClasses = new HashSet<Class<?>>();
        for (Class<?> class1 : types)
        {
            Collection<Class<?>> scanClasspathForSubTypeClass = scanClasspathForSubTypeClass(class1);
            finalClasses.addAll(scanClasspathForSubTypeClass);
        }

        // removed ignored already done
        return finalClasses;
        // return (Collection) removeIgnore((Collection)types);

    }

    @Override
    public Collection<String> scanClasspathForResource(String pattern)
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(computeUrls()).setScanners(new ResourcesScanner()));

        return reflections.getResources(Pattern.compile(pattern));

    }

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public Collection<Class<?>> scanClasspathForSubTypeClass(Class<?> subType)
    {
        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new SubTypesScanner()));

        Collection<?> typesAnnotatedWith = (Collection<?>) reflections.getSubTypesOf(subType);

        if (typesAnnotatedWith == null)
        {
            typesAnnotatedWith = Collections.emptySet();
        }

        return (Collection) postTreatment((Collection) typesAnnotatedWith);
    }

    public void setAdditionalClasspath(Set<URL> additionalClasspath)
    {
        this.additionalClasspath = additionalClasspath;

    }

    private ConfigurationBuilder configurationBuilder()
    {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        FilterBuilder fb = new FilterBuilder();
        
        for (String packageRoot : packageRoots)
        {
            fb.include(prefix(packageRoot) );
        }
        
        cb.filterInputsBy(fb);
        
        return cb;
    }
    
    private Set<URL> computeUrls()
    {
        if (urls == null)
        {
            urls = new HashSet<URL>();

            if (this.additionalClasspath != null)
            {
                urls.addAll(this.additionalClasspath);
            }

            urls.addAll(ClasspathHelper.forJavaClassPath());
//            urls.addAll(ClasspathHelper.forClassLoader(ClasspathHelper.classLoaders()));
        }

        return urls;
    }

    private <T> Collection<Class<? extends T>> toClasses2(Collection<String> names)
    {
        Collection classes = new HashSet();
        
        for (String name : names)
        {
            try
            {
                classes.add(Class.forName(name));
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        
        return classes;
    }
    private <T> Collection<Class<? extends T>> toClasses(Collection<String> names)
    {
        return ReflectionUtils.<T> forNames(names, this.getClass().getClassLoader());
    }

}
