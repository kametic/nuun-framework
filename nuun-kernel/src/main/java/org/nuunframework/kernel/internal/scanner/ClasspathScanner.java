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

import java.lang.annotation.Annotation;
import java.util.Collection;

import org.nuunframework.kernel.commons.specification.Specification;

public interface ClasspathScanner
{

    void scanClasspathForAnnotation(Class<? extends Annotation> annotationType , Callback callback);

    void scanClasspathForAnnotationRegex(String annotationTypeName, Callback callback);

    void scanClasspathForMetaAnnotationRegex(String annotationTypeName, Callback callback);

    void scanClasspathForSubTypeClass(Class<?> subType, Callback callback);

    void scanClasspathForTypeRegex(String typeRegex, Callback callback);

    void scanClasspathForSubTypeRegex(String typeRegex, Callback callback);

    void scanClasspathForResource(String pattern, CallbackResources callback);

    void scanClasspathForSpecification(Specification<Class<?>> specification, Callback callback);

    void scanClasspathForMetaAnnotation(Class<? extends Annotation> annotationType, Callback callback);
    
    void doClasspathScan ();
    
    
    public static interface Callback 
    {
        public void callback(Collection<Class<?>> scanResult);
    }

    public static interface CallbackResources
    {
        public void callback(Collection<String> scanResult);
    }
    
}
