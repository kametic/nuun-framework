/**
 * 
 */
package org.nuunframework.kernel;


/**
 * @author Epo Jemba
 *
 */
public class KernelException extends RuntimeException
{

    public KernelException()
    {
    }

    public KernelException(String message , Object... params)
    {
        super( String.format(message, params) );
    }

    public KernelException(String message ,  Throwable cause)
    {
        super(message , cause);
    }

    public KernelException(String message ,  Throwable cause, Object... params)
    {
        super(String.format(message, params), cause);
    }
    
    
    
    private static final long serialVersionUID = -5031708737156896850L;
}
