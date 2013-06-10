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
package org.nuunframework.configuration.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuunframework.kernel.spi.configuration.NuunConfigurationConverter;
import org.nuunframework.kernel.spi.configuration.NuunDummyConverter;
import org.nuunframework.kernel.spi.configuration.NuunProperty;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@NuunProperty(value = "")
public @interface AnnoCustomProperty
{
    String value();
    boolean mandatory() default true;
    String defaultValue() default "";
    byte defaultByteValue() default 0;
    short defaultShortValue() default 0;
    int defaultIntValue() default 0;
    long defaultLongValue() default 0;
    float defaultFloatValue() default 0;
    double defaultDoubleValue() default 0.0;
    boolean defaultBooleanValue() default false; 
    Class<? extends NuunConfigurationConverter<?>> converter() default NuunDummyConverter.class;
}