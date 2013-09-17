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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuunframework.kernel.spi.configuration.NuunConfigurationConverter;
import org.nuunframework.kernel.spi.configuration.NuunDummyConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD , ElementType.ANNOTATION_TYPE
})
/**
 * 
 * This annotation mark a field as the args of the commandline.
 * <p>
 * Type must be an array of String? If a converter is provided an array of the converted class. 
 * 
 * @author epo.jemba@kametic.com
 *
 */
public @interface NuunArgs
{
    
    boolean mandatory () default false;
    
    /**
     * @return a converter that will allow transformation from String to Something else 
     * 
     * @return
     */
    Class<? extends NuunConfigurationConverter<?>> converter() default NuunDummyConverter.class;

}