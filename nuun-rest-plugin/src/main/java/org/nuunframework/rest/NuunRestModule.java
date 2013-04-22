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
package org.nuunframework.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;


class NuunRestModule extends JerseyServletModule
{
    
    private final String uriPattern;
    private boolean enablePojoMappingFeature = true;
	private Class<? extends HttpServlet> jerseyCustomClass;

    public NuunRestModule(String uriPattern)
    {
        this.uriPattern = uriPattern;
    }

    public NuunRestModule(String uriPattern, boolean enablePojoMappingFeature, Class<? extends HttpServlet> jerseyCustomClass )
    {
        this(uriPattern);
        this.enablePojoMappingFeature = enablePojoMappingFeature;
		this.jerseyCustomClass = jerseyCustomClass;
    }
    
    @Override
    protected void configureServlets()
    {
        Map<String, String> initParams = new HashMap<String, String>();
        
        if (enablePojoMappingFeature)
        {
            initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        }
        else 
        {
            initParams.put("com.sun.jersey.api.json.POJOMappingFeature", "false");
        }
        bind(GuiceContainer.class);
        if (jerseyCustomClass == null) 
        {
        	serve(uriPattern).with(GuiceContainer.class,initParams);
        }
        else 
        {
        	bind(jerseyCustomClass);
        	serve(uriPattern).with(jerseyCustomClass , initParams);
        }
    }
}
