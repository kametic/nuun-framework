package org.nuunframework.kernel.plugins.configuration;

import com.google.inject.MembersInjector;
import java.lang.reflect.Field;
import org.apache.commons.configuration.Configuration;
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

    public ConfigurationMembersInjector(Field field, Configuration configuration)
    {
        this.field = field;
        this.configuration = configuration;
        this.field.setAccessible(true);
    }

    @Override
    public void injectMembers(T instance)
    {
        NuunProperty injectConfigAnnotation = field.getAnnotation(NuunProperty.class);
        String configurationParameterName = injectConfigAnnotation.value();
        try
        {
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
        ConfigurationConverter<?> converter = injectConfigAnnotation.converter().newInstance();
        Object value = converter.convert(property);
        return value;
    }
}
