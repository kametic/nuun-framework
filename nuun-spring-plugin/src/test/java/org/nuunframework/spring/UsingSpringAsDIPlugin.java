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
package org.nuunframework.spring;

import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.spring.sample.Service3Internal;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

public class UsingSpringAsDIPlugin extends AbstractPlugin
{

    @Override
    public String name()
    {
        return "using-spring-as-di-plugin";
    }
    
    
    @Override
    public Object dependencyInjectionDef()
    {
        ClassPathXmlApplicationContext parentCtx = new  ClassPathXmlApplicationContext("context.xml");
        
        StaticApplicationContext dynCtx = new StaticApplicationContext();        
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(Service3Internal.class);
        beanDef.setScope("prototype");
        dynCtx.registerBeanDefinition("service3", beanDef );
        
        dynCtx.setParent(parentCtx);        
        dynCtx.refresh();
        
        

        return dynCtx;
    }
    
}
