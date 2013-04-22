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
package org.nuunframework.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.nuunframework.kernel.Kernel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class NuunServletContextListener extends GuiceServletContextListener
{

    private Logger         logger = LoggerFactory.getLogger(NuunServletContextListener.class);

    private ServletContext servletContext;
    private Kernel         kernel;

    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);
    }

    @Override
    protected Injector getInjector()
    {

        List<String> params = new ArrayList<String>();
        Enumeration<?> initparams = servletContext.getInitParameterNames();
        while (initparams.hasMoreElements())
        {
            String keyName = (String) initparams.nextElement();
            if (keyName != null && !keyName.isEmpty())
            {
                String value = servletContext.getInitParameter(keyName);
                logger.info("Adding {}={} to NuunServletContextListener.", keyName, value);
                params.add(keyName);
                params.add(value);
            }
        }

        

        String[] paramsArray = new String[params.size()];
        params.toArray(paramsArray);

        kernel = Kernel.createKernel(paramsArray).withContainerContext(servletContext). build();

        kernel.init();

        kernel.start();

        return kernel.getMainInjector();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {

        kernel.stop();

        // Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());

        super.contextDestroyed(servletContextEvent);
    }
}