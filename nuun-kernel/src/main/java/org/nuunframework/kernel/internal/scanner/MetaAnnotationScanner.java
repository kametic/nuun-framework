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

import org.nuunframework.kernel.commons.AssertUtils;
import org.reflections.scanners.AbstractScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaAnnotationScanner extends AbstractScanner
{

    Logger logger = LoggerFactory.getLogger(MetaAnnotationScanner.class);
            
    private final Class<? extends Annotation> annotationType;

    private final String metaAnnotationRegex;

    public MetaAnnotationScanner(Class<? extends Annotation> annotationType)
    {
        this.annotationType = annotationType;
        this.metaAnnotationRegex = null;
    }
    
    public MetaAnnotationScanner(String  metaAnnotationRegex)
    {
        this.metaAnnotationRegex = metaAnnotationRegex;
        this.annotationType = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void scan(Object cls)
    {
        final String className = getMetadataAdapter().getClassName(cls);
        try
        {
            Class<?> klass = Class.forName(className);
            
            if ( annotationType != null &&  AssertUtils.hasAnnotationDeep(klass, annotationType) && ! klass.isAnnotation() )
            {
                getStore().put(annotationType.getName(), className);
            }
            
            if ( metaAnnotationRegex != null &&  AssertUtils.hasAnnotationDeepRegex(klass, metaAnnotationRegex) && ! klass.isAnnotation() )
            {
                getStore().put(metaAnnotationRegex, className);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
