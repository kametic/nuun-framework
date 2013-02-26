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
//    /**
//     * Request classes based on type of parent class
//     */
//    TYPE_OF_BY_CLASS , 
    /**
     * Request classes based on type of parent class
     */
    TYPE_OF_BY_REGEX_MATCH,
    /**
     * Request classes based on type of parent class
     */
    SUBTYPE_OF_BY_CLASS , 
    /**
     * Request classes based on type of parent class
     */
    SUBTYPE_OF_BY_REGEX_MATCH, 
    /**
     * Selection of the class is made via the Specification<Class> given.
     */
    VIA_SPECIFICATION, 
    
    /**
     * return resources
     */
    RESOURCES_REGEX_MATCH
    
    
    
//    ,ANNOTATION_TYPE_DEEP , ANNOTATION_SUFFIX_NAME_MATCH_DEEP , 
//     SUBTYPE_OF_BY_TYPE_DEEP ,    SUBTYPE_OF_SUFFIX_NAME_MATCH_DEEP 
    ;
    
}
