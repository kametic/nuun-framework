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
package org.nuunframework.cli.samples;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.nuunframework.cli.NuunOption;

/**
 * 
 * 
 * @author epo.jemba@kametic.com
 *
 */
@NuunOption(longOpt = "", opt = "")
@Retention(RetentionPolicy.RUNTIME)
@Target( ElementType.FIELD  )
public @interface FwkOption
{
    String opt();
    String longOpt();
    String description() default "";
    boolean required() default false; 
    boolean arg() default false; 
    boolean args() default false; 
    boolean optionalArg() default false; 
    String argName() default "";
    char valueSeparator() default ',';
}
