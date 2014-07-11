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
package org.nuunframework.kernel.internal.scanner;

import static org.reflections.util.FilterBuilder.prefix;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.nuunframework.kernel.KernelException;
import org.nuunframework.kernel.annotations.Ignore;
import org.nuunframework.kernel.commons.AssertUtils;
import org.nuunframework.kernel.commons.specification.Specification;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.Store;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

class ClasspathScannerInternal implements ClasspathScanner
{

    Logger                          logger = LoggerFactory.getLogger(ClasspathScannerInternal.class);

    private final List<String>      packageRoots;
    private final boolean           reachAbstractClass;
    private final ClasspathStrategy classpathStrategy;
    private Set<URL>                additionalClasspath;
    private Set<URL>                urls;

    private final List<ScannerCommand> commands;

    public ClasspathScannerInternal(ClasspathStrategy classpathStrategy, String... packageRoots_)
    {
        this(classpathStrategy, false, null, packageRoots_);

    }

    public ClasspathScannerInternal(ClasspathStrategy classpathStrategy, boolean reachAbstractClass, String packageRoot, String... packageRoots_)
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
        this.classpathStrategy = classpathStrategy;
        commands = new ArrayList<ClasspathScannerInternal.ScannerCommand>();
    }

    static interface ScannerCommand 
    {
        void execute (Reflections reflections);
        Scanner scanner ();
    }
    
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public void scanClasspathForAnnotation(final Class<? extends Annotation> annotationType , final Callback callback)
    {
//        ConfigurationBuilder configurationBuilder = configurationBuilder();
//        Set<URL> computeUrls = computeUrls();
//        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypeAnnotationsScanner()));

        ScannerCommand command = new ScannerCommand()
        {
            @Override
            public Scanner scanner()
            {
                return new TypeAnnotationsScanner();
            }
            
            @Override
            public void execute(Reflections reflections)
            {
                Collection<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotationType);
                if (typesAnnotatedWith == null)
                {
                    typesAnnotatedWith = Collections.emptySet();
                }
                callback.callback((Collection) postTreatment((Collection) typesAnnotatedWith));
            }
        };
        
        queue(command);
                        
        
    }

    private void queue(ScannerCommand command)
    {
        commands.add(command);
    }

    @SuppressWarnings({
        "unchecked", "rawtypes"
    })
    @Override
    public void scanClasspathForMetaAnnotation(final Class<? extends Annotation> annotationType , final Callback callback)
    {
//        ConfigurationBuilder configurationBuilder = configurationBuilder();
//        Set<URL> computeUrls = computeUrls();
//        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypesScanner()));
        
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                Multimap<String, String> multimap = reflections.getStore().get(TypesScanner.class);
                Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
                for (String className : multimap.keys())
                {
                    Class<?> klass = toClass(className);
                    if (annotationType != null && klass != null && AssertUtils.hasAnnotationDeep(klass, annotationType) && !klass.isAnnotation())
                    {
                        typesAnnotatedWith.add(klass);
                    }
                }
                callback.callback((Collection) postTreatment((Collection) typesAnnotatedWith));
            }

            @Override
            public Scanner scanner()
            {
                return new TypesScanner();
            }

        });
        
        
    }


    @Override
    public void scanClasspathForMetaAnnotationRegex(final String metaAnnotationRegex , final Callback callback)
    {
//        ConfigurationBuilder configurationBuilder = configurationBuilder();
//        Set<URL> computeUrls = computeUrls();
//        Reflections reflections = new Reflections(configurationBuilder.addUrls(computeUrls).setScanners(new TypesScanner()));
        
        
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                Multimap<String, String> multimap = reflections.getStore().get(TypesScanner.class);
                
                Collection<Class<?>> typesAnnotatedWith = Sets.newHashSet();
                
                for( String className : multimap.keys())
                {
                    Class<?> klass = toClass(className);
                    if ( metaAnnotationRegex != null && klass != null&&  AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() )
//            if ( annotationType != null && klass != null &&  AssertUtils.hasAnnotationDeep(klass, annotationType) && ! klass.isAnnotation() )
                    {
                        typesAnnotatedWith.add(klass);
                    }
                }
                callback.callback((Collection) postTreatment((Collection) typesAnnotatedWith));
            }
            @Override
            public Scanner scanner()
            {
                return new TypesScanner();
            }
        });
    }

    
    private Class<?> toClass(String candidate)
    {
        try
        {
            return  Class.forName(candidate);
        }
        catch (Throwable e)
        {
            logger.debug("String to Class : " + e.getMessage() );
            
        }
        return null;
    }
    static class IgnorePredicate implements Predicate<Class<?>>
    {

        Logger                logger = LoggerFactory.getLogger(ClasspathScannerInternal.IgnorePredicate.class);

        private final boolean reachAbstractClass;

        public IgnorePredicate(boolean reachAbstractClass)
        {
            this.reachAbstractClass = reachAbstractClass;
        }

        @Override
        public boolean apply(Class<?> clazz)
        {

            logger.trace("Checking {} for Ignore", clazz.getName());

            boolean toKeep = true;

            if ((Modifier.isAbstract(clazz.getModifiers()) && !reachAbstractClass) && (!clazz.isInterface()))
            {
                toKeep = false;
            }

            for (Annotation annotation : clazz.getAnnotations())
            {
                logger.trace("Checking annotation {} for Ignore", annotation.annotationType().getName());
                if (annotation.annotationType().equals(Ignore.class) || annotation.annotationType().getName().endsWith("Ignore"))
                {
                    toKeep = false;
                }
                logger.trace("Result tokeep = {}.", toKeep);
                if (!toKeep)
                {
                    break;
                }
            }
            return toKeep;
        }
    }

    private Collection<Class<?>> postTreatment(Collection<Class<?>> set)
    {

        // Sanity Check : throw a KernelException if one of the returned classes is null
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
    @Override
    public void scanClasspathForAnnotationRegex(final String annotationTypeRegex, final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypeAnnotationsScanner()));

          queue(new ScannerCommand()
          {
              @Override
              public void execute(Reflections reflections)
              {
                  Store store = reflections.getStore();
                  
                  Multimap<String, String> multimap = store.get(TypeAnnotationsScanner.class);
                  
                  List<String> key = new ArrayList<String>();
                  for (String loopKey : multimap.keySet())
                  {
                      if (loopKey.matches(annotationTypeRegex))
                      {
                          key.add(loopKey);
                      }
                  }
                  
                  Collection<Class<?>> typesAnnotatedWith = new HashSet<Class<?>>();
                  
                  for (String k : key)
                  {
                      Collection<String> collectionOfString = multimap.get(k);
                      typesAnnotatedWith.addAll(toClasses(collectionOfString));
                  }
                  callback .callback((Collection) postTreatment((Collection) typesAnnotatedWith));
              }
              
              @Override
              public Scanner scanner()
              {
                  return new TypeAnnotationsScanner();
              }
              
          });
    }


    
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public void scanClasspathForTypeRegex(final String typeName, final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
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
                callback.callback((Collection) postTreatment((Collection) types));

            }

            @Override
            public Scanner scanner()
            {
                return new TypesScanner();
            }

        });


    }

    
    
    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public void scanClasspathForSpecification(final Specification<Class<?>> specification , final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
          queue(new ScannerCommand()
          {
              @Override
              public void execute(Reflections reflections)
              {
                  Store store = reflections.getStore();
                  Multimap<String, String> multimap = store.get(TypesScanner.class);
                  Collection<String> collectionOfString = multimap.keySet();
                  
                  Collection<Class<?>> types = null;
                  Collection<Class<?>> filteredTypes = new HashSet<Class<?>>();
                  
                  // Convert String to classes
                  if (collectionOfString.size() > 0)
                  {
                      types = toClasses(collectionOfString);
                  }
                  else
                  {
                      types = Collections.emptySet();
                  }
                  
                  // Filter via specification
                  for (Class<?> candidate : types)
                  {
                      if (specification.isSatisfiedBy(candidate))
                      {
                          filteredTypes.add(candidate);
                      }
                  }
                  
                  callback.callback((Collection) postTreatment((Collection) filteredTypes));
                  
              }
              
              @Override
              public Scanner scanner()
              {
                  return new TypesScanner();
              }
              
          });


    }

    @SuppressWarnings({})
    @Override
    public void scanClasspathForSubTypeRegex(final String subTypeName , final Callback callback)
    {

//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new TypesScanner()));
        
        
        queue(new ScannerCommand()
        {
            @Override
            public void execute(Reflections reflections)
            {
                // empty just add 
            }

            @Override
            public Scanner scanner()
            {
                return new SubTypesScanner();
            }
        });
        queue(new ScannerCommand()
        {
            @SuppressWarnings({
                    "rawtypes", "unchecked"
            })
            @Override
            public void execute(Reflections reflections)
            {
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
                for (Class<?> subType : types)
                {
                    // Collection<Class<?>> scanClasspathForSubTypeClass =
                    // scanClasspathForSubTypeClass(class1);
                    // ///////////////////////////////
                    Collection<?> typesAnnotatedWith = (Collection<?>) reflections.getSubTypesOf(subType);

                    if (typesAnnotatedWith == null)
                    {
                        typesAnnotatedWith = Collections.emptySet();
                    }
                    // ///////////////////////////////
                    finalClasses.addAll((Collection) postTreatment((Collection) typesAnnotatedWith));
                }

                // removed ignored already done
                callback.callback(finalClasses);
                // return (Collection) removeIgnore((Collection)types);

            }

            @Override
            public Scanner scanner()
            {
                return new TypesScanner();

            }
        });

    }
    

    @Override
    public void scanClasspathForResource(final String pattern , final CallbackResources callback)
    {
//        Reflections reflections = new Reflections(new ConfigurationBuilder().addUrls(computeUrls()).setScanners(new ResourcesScanner()));
          queue(new ScannerCommand()
          {
              @Override
              public void execute(Reflections reflections)
              {
                  Set<String> resources = reflections.getResources(Pattern.compile(pattern));
                  callback.callback(resources);
                  
              }
              
              @Override
              public Scanner scanner()
              {
                  return new ResourcesScanner();
              }
              
          });
    }
//  queue(new ScannerCommand()
//  {
//      @Override
//      public void execute(Reflections reflections)
//      {
//          
//      }
//      
//      @Override
//      public Scanner scanner()
//      {
//          return
//      }
//      
//  });

    @SuppressWarnings({
            "unchecked", "rawtypes"
    })
    @Override
    public void scanClasspathForSubTypeClass(final Class<?> subType , final Callback callback)
    {
//        Reflections reflections = new Reflections(configurationBuilder().addUrls(computeUrls()).setScanners(new SubTypesScanner()));
      queue(new ScannerCommand()
      {
          @Override
          public void execute(Reflections reflections)
          {
              Collection<?> typesAnnotatedWith = (Collection<?>) reflections.getSubTypesOf(subType);
              
              if (typesAnnotatedWith == null)
              {
                  typesAnnotatedWith = Collections.emptySet();
              }
              
              callback.callback ( (Collection) postTreatment((Collection) typesAnnotatedWith));
              
          }
          
          @Override
          public Scanner scanner()
          {
              return new SubTypesScanner();
          }
          
      });

    }
    
    /**
     * Unique reflections object. Unique scan
     */
    @Override
    public void doClasspathScan()
    {
        
        Scanner[] scanners = getScanners();
        
        ConfigurationBuilder configurationBuilder = configurationBuilder().addUrls(computeUrls()).setScanners ( scanners ) ;
        
        Reflections reflections = new Reflections(configurationBuilder);
        
        for(  ScannerCommand command : commands)
        {
            command.execute(reflections);
        }
    }

    private Scanner[] getScanners()
    {
        Map<Class<?> , Scanner> scannersByClass = Maps.newHashMap();
        for(  ScannerCommand command : commands)
        {
            Scanner scanner = command.scanner();
            
            if (!scannersByClass.containsKey(scanner.getClass()))
            {
                scannersByClass.put(scanner.getClass(), scanner);
            }
        }
        
        Collection<Scanner> scanners = scannersByClass.values();
        int size = scanners.size();
        
        Scanner[] arrayOfScanners = new Scanner[size];
        
        scanners.toArray(arrayOfScanners);

        return arrayOfScanners;
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
            fb.include(prefix(packageRoot));
        }
        

        cb.filterInputsBy(fb);

        return cb;
    }

    private Set<URL> computeUrls()
    {
        if (urls == null)
        {
            urls = new HashSet<URL>();

            switch (classpathStrategy.getStrategy())
            {
                case SYSTEM:
                    urls.addAll(ClasspathHelper.forJavaClassPath());
                    break;
                case CLASSLOADER:
                    urls.addAll(ClasspathHelper.forClassLoader());
                    break;
                case ALL:
                    urls.addAll(ClasspathHelper.forJavaClassPath());
                    urls.addAll(ClasspathHelper.forClassLoader());
                    break;
                case NONE:
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported classpath strategy " + classpathStrategy.toString());
            }

            if (classpathStrategy.isAdditional() && this.additionalClasspath != null)
            {
                urls.addAll(this.additionalClasspath);
            }
        }

        if (classpathStrategy.isDeduplicate())
        {
            this.urls = deduplicate(urls);
        }

        return urls;
    }

    private <T> Collection<Class<?>> toClasses2(Collection<String> names)
    {
        Collection<Class<?>> classes = new HashSet();

        for (String name : names)
        {
            try
            {
                classes.add((Class<T>) Class.forName(name));
            }
            catch (Exception e)
            {
                logger.warn("Error when converting " + name + " to class.", e);
            }
        }

        return classes;
    }

    private <T> Collection<Class<? extends T>> toClasses(Collection<String> names)
    {
        return ReflectionUtils.<T> forNames(names, this.getClass().getClassLoader());
    }

    private String findLongestSuffix(String a, String b, int threshold)
    {
        int i = a.length() - 1;
        int j = b.length() - 1;
        int k = -1;
        int l = 0;

        while (i >= 0 && j >= 0) {
            if (a.charAt(i) == '/')
            {
                k = i;
                l++;
            }

            if (a.charAt(i) != b.charAt(j))
            {
                return l > threshold ? a.substring(Math.max(i, k)) : null;
            }

            i--;
            j--;
        }

        return null;
    }

    Set<URL> deduplicate(Collection<URL> urlCollection)
    {
        List<URL> urlList = new ArrayList<URL>(urlCollection);
        Collections.sort(urlList, new Comparator<URL>()
        {
            @Override
            public int compare(URL url1, URL url2) {
                return new StringBuilder(url1.toExternalForm()).reverse().toString().compareTo(new StringBuilder(url2.toExternalForm()).reverse().toString());
            }
        });

        Set<URL> result = new HashSet<URL>();
        URL previous = null;

        for (URL current : urlList)
        {
            String longestSuffix = previous == null ? null : findLongestSuffix(current.toExternalForm(), previous.toExternalForm(), classpathStrategy.getThreshold());

            if (longestSuffix == null)
            {
                result.add(current);
            }

            previous = current;
        }

        return result;
    }
}
