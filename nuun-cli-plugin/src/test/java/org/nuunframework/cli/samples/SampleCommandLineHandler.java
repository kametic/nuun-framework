package org.nuunframework.cli.samples;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.collections.functors.TruePredicate;
import org.nuunframework.cli.NuunArgs;
import org.nuunframework.cli.NuunOption;
import org.nuunframework.cli.api.NuunCliHandler;
import org.nuunframework.kernel.plugins.logs.NuunLog;
import org.slf4j.Logger;


/**
 * 
 * 
 * @author epo.jemba@kametic.com
 *
 */
public class SampleCommandLineHandler implements NuunCliHandler  {
	
	@NuunLog
	Logger logger;
	
	@NuunOption(opt = "a" , arg=false)
	private Boolean hasA;

	@NuunOption(opt = "b" , arg=true, required=true )
	private String b;
	
	@NuunOption(opt = "P" , args=true ,valueSeparator='=')
	private String P[];
	
	@NuunArgs
	private String[] arg; 
	
	
	@Override	
	public String name() {
		return "Test Application Handler";
	}

	@Override
	public Integer call() throws Exception {
		
		logger.info("EXECUTING !!");
		logger.info( "hasA = " + hasA  );
		logger.info( "b = " + b  );
		if (arg != null)
		{
			logger.info( "arg.length = " + arg.length  );
			logger.info( "arg = " + arg[0]  );
		}
		
		
		assertThat(hasA).isNotNull();
		assertThat(hasA).isTrue();
		
		assertThat(b).isNotNull();
		assertThat(b).isEqualTo("babar");
		
		assertThat(arg).isNotNull();
		assertThat(arg).containsExactly("zob");
		
		assertThat(P).isNotNull();
		assertThat(P).containsExactly("key1" , "value1" , "key2" , "value2");
		
		return 0;
	}
}