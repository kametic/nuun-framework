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

import com.google.inject.MembersInjector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.nuunframework.kernel.commons.AssertUtils;
import org.nuunframework.kernel.plugin.PluginException;
import org.nuunframework.kernel.spi.configuration.NuunConfigurationConverter;
import org.nuunframework.kernel.spi.configuration.NuunProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs field injection of configuration data.
 * 
 * @author anatolich
 */
public class ConfigurationMembersInjector<T> implements MembersInjector<T>
{

    private static final String NO_PROPERTY_FOUND_LOG_MESSAGE = "No property {} found. Injecting default value";

    private static final Logger log = LoggerFactory.getLogger(ConfigurationMembersInjector.class);

    private final Field field;

    private final Configuration configuration;

    private Annotation clonedAnno;

    public ConfigurationMembersInjector(Field field, Configuration configuration, Annotation clonedAnno)
    {
        this.field = field;
        this.configuration = configuration;
        this.clonedAnno = clonedAnno;
    }

    @Override
    public void injectMembers(T instance)
    {
        NuunProperty injectConfigAnnotation = null;
        // The annotation is actual NuunProperty.class
        if (clonedAnno.annotationType() == NuunProperty.class)
        {
            injectConfigAnnotation = field.getAnnotation(NuunProperty.class);
        } 
        else
        { // The annotation has the NuunProperty annotation on it we proxy it
            injectConfigAnnotation = AssertUtils.annotationProxyOf(NuunProperty.class, clonedAnno);
        }
        
        String configurationParameterName = injectConfigAnnotation.value();
        
        // Pre verification //
        if (StringUtils.isEmpty(configurationParameterName)) 
        {
            log.error("Value for annotation {} on field {} can not be null or empty.", clonedAnno.annotationType() , field.toGenericString());
            throw new PluginException("Value for annotation %s on field %s can not be null or empty.", clonedAnno.annotationType() , field.toGenericString());
        }
        
        if (! configuration.containsKey(configurationParameterName)  && injectConfigAnnotation.mandatory())
        {
            throw new PluginException( "\"%s\" must be in one properties file for field %s.", configurationParameterName , field.toGenericString());
        }
        
        try
        {
            this.field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == Integer.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setInt(instance, configuration.getInt(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setInt(instance, injectConfigAnnotation.defaultIntValue());
                }
            } 
            else if (type == Integer.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Integer(configuration.getInt(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Integer(injectConfigAnnotation.defaultIntValue()));
                }
            }
            else if (type == Boolean.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setBoolean(instance, configuration.getBoolean(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setBoolean(instance, injectConfigAnnotation.defaultBooleanValue());
                }

            }
            else if (type == Boolean.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Boolean(configuration.getBoolean(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Boolean(injectConfigAnnotation.defaultBooleanValue()));
                }
                
            }
            else if (type == Short.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setShort(instance, configuration.getShort(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setShort(instance, injectConfigAnnotation.defaultShortValue());
                }
            }
            else if (type == Short.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Short(configuration.getShort(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Short(injectConfigAnnotation.defaultShortValue()));
                }
            }
            else if (type == Byte.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setByte(instance, configuration.getByte(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setByte(instance, injectConfigAnnotation.defaultByteValue());
                }
            }
            else if (type == Byte.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Byte(configuration.getByte(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Byte(injectConfigAnnotation.defaultByteValue()));
                }
            }
            else if (type == Long.TYPE )
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setLong(instance, configuration.getLong(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setLong(instance, injectConfigAnnotation.defaultLongValue());
                }
            }
            else if (type == Long.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Long( configuration.getLong(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Long(injectConfigAnnotation.defaultLongValue()));
                }
            }
            else if (type == Float.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setFloat(instance, configuration.getFloat(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setFloat(instance, injectConfigAnnotation.defaultFloatValue());
                }
            }
            else if (type == Float.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Float(configuration.getFloat(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Float(injectConfigAnnotation.defaultFloatValue()));
                }
            }
            else if (type == Double.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setDouble(instance, configuration.getDouble(configurationParameterName));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.setDouble(instance, injectConfigAnnotation.defaultDoubleValue());
                }
            }
            else if (type == Double.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Double(configuration.getDouble(configurationParameterName)));
                }
                else
                {
                    log.debug(NO_PROPERTY_FOUND_LOG_MESSAGE, configurationParameterName);
                    field.set(instance, new Double(injectConfigAnnotation.defaultDoubleValue()));
                }
            }
            else if (type == Character.TYPE)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.setChar(instance, configuration.getString(configurationParameterName).charAt(0));
                }
            }
            else if (type == Character.class)
            {
                if (configuration.containsKey(configurationParameterName))
                {
                    field.set(instance, new Character(configuration.getString(configurationParameterName).charAt(0)));
                }
            }
            else
            {
                Object property = getProperty(configurationParameterName, injectConfigAnnotation);
                field.set(instance, property);
            }
        }
        catch (IllegalArgumentException ex)
        {
            log.error("Wrong argument or argument type during configuration injection", ex);
        }
        catch (IllegalAccessException ex)
        {
            log.error("Illegal access during configuration injection", ex);
        }
        catch (InstantiationException ex)
        {
            log.error("Impossible to instantiate value converter", ex);
        }
        finally
        {
            this.field.setAccessible(false);
        }
    }

    private Object getProperty(String configurationParameterName, NuunProperty injectConfigAnnotation) throws InstantiationException,
    IllegalAccessException
    {
        String property;
        if (configuration.containsKey(configurationParameterName))
        {
            property = configuration.getString(configurationParameterName);
        }
        else
        {
            property = injectConfigAnnotation.defaultValue();
        }
        NuunConfigurationConverter<?> converter = injectConfigAnnotation.converter().newInstance();
        Object value = converter.convert(property);
        return value;
    }
}
