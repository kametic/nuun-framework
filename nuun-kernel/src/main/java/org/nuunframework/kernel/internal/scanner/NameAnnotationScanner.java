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

import java.util.List;

import org.reflections.scanners.AbstractScanner;

class NameAnnotationScanner extends AbstractScanner
{
    
    private final String annotationRegexName;

    public NameAnnotationScanner(String annotationRegexName)
    {
        this.annotationRegexName = annotationRegexName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void scan(Object cls)
    {
        final String className = getMetadataAdapter().getClassName(cls);
        
        for (String annotationType : (List<String>) getMetadataAdapter().getClassAnnotationNames(cls))
        {
            if (  annotationType.matches(annotationRegexName) )
            { 
                getStore().put(annotationType, className);
            }
        }

    }

}
