package org.nuunframework.rest;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

@SuppressWarnings("serial")
public class SampleGuiceContainer extends GuiceContainer {

	public static boolean initialized = false;

	@Inject
	public SampleGuiceContainer(Injector injector) {
		super(injector);
		initialized = true;
	}
	
}
