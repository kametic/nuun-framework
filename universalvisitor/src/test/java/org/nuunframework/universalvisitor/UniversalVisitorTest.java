
package org.nuunframework.universalvisitor;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.nuunframework.universalvisitor.Mapper;
import org.nuunframework.universalvisitor.Node;
import org.nuunframework.universalvisitor.Predicate;
import org.nuunframework.universalvisitor.Reducer;
import org.nuunframework.universalvisitor.UniversalVisitor;
import org.nuunframework.universalvisitor.sample.multiplereducers.D;
import org.nuunframework.universalvisitor.sample.multiplereducers.E;
import org.nuunframework.universalvisitor.sample.multiplereducers.F;
import org.nuunframework.universalvisitor.sample.multiplereducers.G;
import org.nuunframework.universalvisitor.sample.simple.A;
import org.nuunframework.universalvisitor.sample.simple.B;
import org.nuunframework.universalvisitor.sample.simple.C;

/**
 *
 * 
 * @author epo.jemba@ext.mpsa.com
 *
 */
public class UniversalVisitorTest {

	UniversalVisitor<Integer> underTest;
	
	A a;
	
	D d;
	
	@Before
	public void init () {
		underTest = new UniversalVisitor<Integer>();
		a = new A();
		B b = new B();
		C c = new C();
		a.name = "a";
		a.b = b;
		b.c = c;
		c.a = a;
		
		d = new D();
		D dNew = new D();
		E e = new E();
		F f = new F();
		G g =new G();
		
		d.e = e;
		d.dValue = 10;
		dNew.dValue = 100000;
		e.f = f;
		e.eValue = 100;
		f.g = g;
		f.fValue = 1000;
		g.dCycle = d;
		g.dNew = dNew;
		g.gValue = 10000;
		
	}
	
	@Test
	public void simple_case() {
		
		MyMapper mapper = new MyMapper();
		MyReducer reducer = new MyReducer();
		MyPredicate predicate = new MyPredicate();
		
		underTest.visit(a,predicate, mapper , reducer);
		
		assertThat(mapper.getCount()).isEqualTo(4);
		assertThat(reducer.reduce()).isEqualTo(4);
		
		assertThat(mapper.getMaxLevel()).isEqualTo(2);
	}
	
	@Test
	public void multiple_reducer () {
		
		MyMapper2 mapper = new MyMapper2();
		MyReducer sumReducer = new MyReducer();
		MyPredicate2 predicate = new MyPredicate2();
		
		
		underTest.visitn(d, predicate , mapper, sumReducer);
		
		assertThat(sumReducer.reduce()).isEqualTo(111110);
		
	}
	
	static class MyMapper2 implements Mapper<Integer> {

		@Override
		public Integer map(Node node)  {
			Field f = (Field) node.accessibleObject();
			
			Integer value = null;
			try {
				value = (Integer) f.get(node.instance());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return value;
		}
		
	}
	
	static class MyMapper implements Mapper<Integer> {
		
		int counter = 0;
		int maxLevel = 0;

		@Override
		public Integer map(Node node) {
			counter ++;
			maxLevel = Math.max(maxLevel, node.level);
			return new Integer(1);
		}
		
		public int getCount() {
			return counter;
		}
		
		public int getMaxLevel() {
			return maxLevel;
		}
		
		
	}
	
	static class MyReducer implements Reducer<Integer, Integer> {
		int counter = 0;
		@Override
		public void collect(Integer input) {
			counter = counter + input;
		}
		@Override
		public Integer reduce() {
			return counter;
		}
	}
	
	
	
	static class MyPredicate implements Predicate {

		@Override
		public boolean apply(AccessibleObject input) {
			
			return input instanceof Field  ;
		}
		
	}
	
	static class MyPredicate2 implements Predicate {

		@Override
		public boolean apply(AccessibleObject input) {
			
			return input instanceof Field && ! ((Field) input).getType().equals(Integer.class) ;
		}
		
	}
	
	

}
