package org.nuunframework.universalvisitor.api;

import java.lang.reflect.AccessibleObject;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.nuunframework.universalvisitor.core.MapReduceDefault;

public class MapReduceTest {

	@SuppressWarnings("unchecked")
	@Test
	public void test() {
		Assertions.assertThat(new MapReduceDefault<String>(new MyMapper()).getReducers()).isNotNull();
		Assertions.assertThat(new MapReduceDefault<String>(new MyMapper()).getReducers()).isEmpty();
	}
	
	
	static class MyMapper implements Mapper<String> {

		@Override
		public boolean handle(AccessibleObject object) {
			return false;
		}

		@Override
		public String map(Node node) {
			return null;
		}
	}

}
