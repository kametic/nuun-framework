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
package org.nuunframework.kernel.context;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.nuunframework.kernel.commons.specification.Specification;
import org.nuunframework.kernel.plugin.Plugin;

import com.google.inject.Module;

/**
 * 
 * @author Epo Jemba
 *
 */
@SuppressWarnings("rawtypes")
public interface InitContext
{

    public abstract Map<Class<?>, Collection<Class<?>>> scannedSubTypesByParentClass();

    public abstract Map<Class<?>, Collection<Class<?>>> scannedSubTypesByAncestorClass();
    
    public abstract Map<String, Collection<Class<?>>> scannedSubTypesByParentRegex();

    public abstract Map<Class<? extends Annotation>, Collection<Class<?>>> scannedClassesByAnnotationClass();

    public abstract Map<String, Collection<Class<?>>> scannedClassesByAnnotationRegex();

    public abstract Map<String, Collection<String>> mapPropertiesFilesByPrefix();

    public abstract String getKernelParam(String key);

    public abstract Collection<Class<?>> classesToBind();

    public abstract List<Module> moduleResults();
    
    public abstract List<Module> moduleOverridingResults();

    public abstract Collection<String> propertiesFiles();

    public abstract Map<String, Collection<Class<?>>> scannedTypesByRegex();

    public abstract Map<String, Collection<String>> mapResourcesByRegex();

    public abstract Map<Specification, Collection<Class<?>>> scannedTypesBySpecification();
    
    /**
     * Return instances of the Plugin asked by the plugin
     * 
     * @return the instances of the plugin declared required by the method Plugin.pluginDependenciesRequired()
     */
    public abstract Collection<? extends Plugin> pluginsRequired ();

    
    /**
     * Returns instances of the plugins that become dependent on this plugin.
     * 
     * @return
     */
    public abstract Collection<? extends Plugin> dependentPlugins ();

    /**
     * Round Number of the initialization.
     * 
     * @return
     */
    public abstract int roundNumber();

}
