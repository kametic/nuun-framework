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

import org.apache.commons.cli.Option;

@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.FIELD , ElementType.ANNOTATION_TYPE
})
public @interface NuunOption
{
    /**
     * @return the identification string of the Option.
     */
    String opt();

    /**
     * logOption is mandatory among all
     * @return an alias and more descriptive identification string
     */
    String longOpt();

    /**
     * @return a description of the function of the option
     */
    String description() default "";

    /**
     * @return a flag to say whether the option must appear on the command line.
     */
    boolean required() default false;

    /**
     * @return a flag to say whether the option takes an argument
     */
    boolean arg() default false;

    /**
     * @return a flag to say whether the option takes more than one argument
     */
    boolean args() default false;
    
    /**
     * @return explicit number of args for this option. 
     */
    int numArgs() default Option.UNINITIALIZED;

    /**
     * @return a flag to say whether the option's argument is optional
     */
    boolean optionalArg() default false;

    /**
     * @return the name of the argument value for the usage statement
     */
    String argName() default "";

    /**
     * @return the character value used to split the argument string, that is used in conjunction with
     *         multipleArgs e.g. if the separator is ',' and the argument string is 'a,b,c' then there are
     *         three argument values, 'a', 'b' and 'c'.
     */
    char valueSeparator() default ',';

}