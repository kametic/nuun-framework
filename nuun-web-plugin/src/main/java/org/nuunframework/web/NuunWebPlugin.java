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

import java.net.URL;
import java.util.Set;

import javax.servlet.ServletContext;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.reflections.util.ClasspathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.WorkAroundModule;

public class NuunWebPlugin extends AbstractPlugin
{

    private Logger         logger = LoggerFactory.getLogger(NuunWebPlugin.class);
    
    private WorkAroundModule module = null;
    private Set<URL> additionalClasspath = null;

    @Override
    public String name()
    {
        return "nuun-web-plugin";
    }

    @Override
    public Set<URL> computeAdditionalClasspathScan()
    {
        ServletContext servletContext = null;

        if (containerContext != null && ServletContext.class.isAssignableFrom(containerContext.getClass()))
        {

            servletContext = (ServletContext) containerContext;
            
            Set<URL> webCPUrls = ClasspathHelper.forWebInfLib(servletContext);
            URL forWebInfClasses = ClasspathHelper.forWebInfClasses(servletContext);
            webCPUrls.add(forWebInfClasses);
            
          this.additionalClasspath = webCPUrls;
            
        }   
        
        return  this.additionalClasspath != null ? this.additionalClasspath : super.computeAdditionalClasspathScan() ;
    }
}
