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
package org.nuunframework.kernel.fixtures;

import org.nuunframework.kernel.Kernel;
import org.nuunframework.kernel.Kernel.KernelBuilderWithPluginAndContext;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

public class CoreITFixture extends AbstractFixture<Kernel> {

	private Kernel kernel = null;

	private String[] kernelParams = new String[0];
	private Module[] modules = new Module[0];
	private Plugin[] plugins = new Plugin[0];
    boolean spiActivated = true;

	public CoreITFixture() {
	}

	public CoreITFixture(String... kernelParams) {
		this.kernelParams = kernelParams;
	}
	
	public Injector  getInjector ()
	{
		return kernel.getMainInjector();
	}

	@Override
	protected Kernel createUnitUnderTest() {
        
		if (null == kernel) {
			KernelBuilderWithPluginAndContext kernelBuilder = Kernel.createKernel(kernelParams);
			
			kernelBuilder.withPlugins(createInternalPluginFromModules());

			kernelBuilder.withPlugins(plugins);
			
			if(! spiActivated)
			    kernelBuilder.withoutSpiPluginsLoader();

					
			kernel = kernelBuilder.build();
			
			kernel.init();
			kernel.start();
		}

		return kernel;
	}

	static class InternalModule extends AbstractModule {
		private Module[] internalModules;

		public InternalModule(Module[] modules) {
			internalModules = modules;
		}

		@Override
		protected void configure() {
			if (internalModules != null) {
				for (Module m : internalModules) {
					install(m);
				}
			}
		}
	}

	public static class InternalPlugin extends AbstractPlugin {
		private Module module;

		public InternalPlugin(Module module) {
			this.module = module;
		}

		@Override
		public String name() {
			return "fixture-internal-plugin";
		}

		@Override
		public Object dependencyInjectionDef() {
			return module;
		}
	}

	private Plugin[] createInternalPluginFromModules() {
		Plugin [] plugins = new Plugin[1];
		
		if (modules != null && modules.length > 0) {
			InternalPlugin plugin = new InternalPlugin(new InternalModule(modules));
			plugins[0] = plugin;
			
			return plugins;
		}
		
		return new Plugin[0];
	}

	@Override
	protected void after() {
		kernel.stop();
		super.after();
	}

	public static Builder createCoreFixture() {
		return new Builder();
	}

	public static class Builder {
		private String[] params = new String[0];
		private Module[] modules = new Module[0];
		private Plugin[] plugins = new Plugin[0];
		boolean spiActivated = true;

		
		public Builder withoutSpi()
		{
		    spiActivated = false;
		    return this;
		}
		
		public Builder withPlugins(Plugin... plugins)
		{
		    this.plugins = plugins;
		    return this;
		}
		
		public Builder withKernelParameters(String... params) {
			this.params = params;
			return this;
		}

		public Builder withModule(Module... modules) {
			this.modules = modules;
			return this;
		}

		public CoreITFixture build() {
			CoreITFixture cf = new CoreITFixture();
			
			if (params != null)
				cf.kernelParams = this.params;
			
			if (modules != null)
				cf.modules = this.modules;
			
			if (plugins != null)
			    cf.plugins = this.plugins;
			
			cf.spiActivated = this.spiActivated;
			

			return cf;
		}

	}

}
