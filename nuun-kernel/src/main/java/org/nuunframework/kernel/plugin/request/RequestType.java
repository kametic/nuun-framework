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
/**
 * 
 */
package org.nuunframework.kernel.plugin.request;


/**
 * @author Epo Jemba
 *
 */
public enum RequestType
{
    /**
     * Request classes based on annotation class type
     */
    ANNOTATION_TYPE , 
    /**
     * Request classes based on annotation regex match on name
     */
    ANNOTATION_REGEX_MATCH,
    
    /**
     * Request classes annotated with annotation annotated by this annotation class type 
     */
    META_ANNOTATION_TYPE , 
    /**
     * Request classes annotated with annotation annotated by this annotation regex name
     */
    META_ANNOTATION_REGEX_MATCH,

    /**
     * Request classes based on type of parent class
     */
    TYPE_OF_BY_REGEX_MATCH,
    /**
     * Request classes based on type of direct parent class
     */
    SUBTYPE_OF_BY_CLASS , 
    /**
     * Request classes based on type of direct parent class
     */
    SUBTYPE_OF_BY_REGEX_MATCH,
    
    /**
     * Request classes based on type of ancestor class
     */
    SUBTYPE_OF_BY_TYPE_DEEP,
    /**
     * Selection of the class is made via the Specification<Class> given.
     */
    VIA_SPECIFICATION, 
    
    /**
     * return resources
     */
    RESOURCES_REGEX_MATCH
    
    
    
//    ,ANNOTATION_TYPE_DEEP , ANNOTATION_SUFFIX_NAME_MATCH_DEEP , 
//      ,    SUBTYPE_OF_SUFFIX_NAME_MATCH_DEEP 
    ;
    
}
