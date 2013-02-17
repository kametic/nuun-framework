package org.nuunframework.kernel.plugin;


/**
 * @author Epo Jemba
 *
 */
public class PluginException extends RuntimeException
{

    public PluginException()
    {
    }

    public PluginException(String message , Object... params)
    {
        super( String.format(message, params) );
    }

    public PluginException(String message ,  Throwable cause)
    {
        super(message , cause);
    }

    public PluginException(String message ,  Throwable cause, Object... params)
    {
        super(String.format(message, params), cause);
    }
    
    
    
    private static final long serialVersionUID = -5031708737156896850L;
}
