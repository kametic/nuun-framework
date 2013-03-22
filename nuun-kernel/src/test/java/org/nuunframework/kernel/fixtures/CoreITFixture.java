package org.nuunframework.kernel.fixtures;

import org.nuunframework.kernel.Kernel;
import org.nuunframework.kernel.plugin.AbstractPlugin;
import org.nuunframework.kernel.plugin.Plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;

public class CoreITFixture extends AbstractFixture<Kernel> {

	private Kernel kernel = null;

	private String[] kernelParams = new String[0];
	private Module[] modules = new Module[0];

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
			kernel = Kernel.createKernel(kernelParams) 
					.withPlugins(createInternalPluginFromModules()).build();
			
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

			return cf;
		}

	}

}
