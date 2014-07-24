package org.nuunframework.universalvisitor.api;

import static org.junit.Assert.*;

import java.lang.reflect.AccessibleObject;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class MapReduceTest {

	@Test
	public void test() {
		Assertions.assertThat(new MapReduce<String>(new MyMapper()).getReducers()).isNotNull();
		Assertions.assertThat(new MapReduce<String>(new MyMapper()).getReducers()).isEmpty();
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
