
package org.nuunframework.universalvisitor;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Mapper;
import org.nuunframework.universalvisitor.api.Node;
import org.nuunframework.universalvisitor.api.Predicate;
import org.nuunframework.universalvisitor.api.Reducer;
import org.nuunframework.universalvisitor.sample.Alphabet;
import org.nuunframework.universalvisitor.sample.collections.H;
import org.nuunframework.universalvisitor.sample.collections.I;
import org.nuunframework.universalvisitor.sample.collections.J;
import org.nuunframework.universalvisitor.sample.collections.K;
import org.nuunframework.universalvisitor.sample.collections.L;
import org.nuunframework.universalvisitor.sample.multiplereducers.D;
import org.nuunframework.universalvisitor.sample.multiplereducers.E;
import org.nuunframework.universalvisitor.sample.multiplereducers.F;
import org.nuunframework.universalvisitor.sample.multiplereducers.G;
import org.nuunframework.universalvisitor.sample.mutatormapper.M;
import org.nuunframework.universalvisitor.sample.nulls.N;
import org.nuunframework.universalvisitor.sample.simple.A;
import org.nuunframework.universalvisitor.sample.simple.B;
import org.nuunframework.universalvisitor.sample.simple.C;

/**
 *
 * 
 */
public class UniversalVisitorTest {

	UniversalVisitor underTest;
	
	A a;
	
	D d;
	
	H h;
	
	M m;
	
	N n;
	
	@SuppressWarnings("serial")
	@Before
	public void init () {
		underTest = new UniversalVisitor();
		a = new A();
		B b = new B();
		C c = new C();
		a.name = "zob";
		a.b = b;
		b.c = c;
		c.a = a;
		
		d = new D();
		D dNew = new D();
		E e = new E();
		F f = new F();
		G g = new G();
		
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
		
		///////////////////
		h = new H();
		
		h.is = new ArrayList<I>(){{
			add(new I());
			add(new I());
			add(new I());
		}};
		h.js = new HashMap<String, J>(){{
			put("k0", new J());
			put("k1", new J());
			put("k2", new J());
		}};
		h.ks = new K[]{new K() , new K() , new K()};
		h.ls = new ArrayList<L>() {{
			L l = new L();
			l.ks = new HashSet<K>(){{
				add(new K());
				add(new K());
				add(new K());
			}};
			add(l);
			l = new L();
			l.ks = new HashSet<K>(){{
				add(new K());
				add(new K());
				add(new K());
			}};
			add(l);
			l = new L();
			l.ks = new HashSet<K>(){{
				add(new K());
				add(new K());
				add(new K());
			}};
			add(l);
			
		}};
		/////
		
		m = new M();
		//
		n = new N();
		
	}
	
	@Test
	public void simple_case() {
		
		MyPredicate predicate = new MyPredicate();
		MyMapper mapper = new MyMapper();
		SumReducer reducer = new SumReducer();
		
		underTest.visit(a,predicate, mapper , reducer);
		
		assertThat(mapper.getCount()).isEqualTo(3);
		assertThat(reducer.reduce()).isEqualTo(3);
		
		assertThat(mapper.getMaxLevel()).isEqualTo(2);
	}
	
	@Test
	public void nullity_case() {
		
		MyPredicate predicate = new MyPredicate();
		MyMapper mapper = new MyMapper();
		
		
		underTest.visit(n,predicate, mapper);
		
	}
	

	@Test
	public void multiple_reducer () {
		
		MyMapper2 mapper = new MyMapper2();
		SumReducer sumReducer = new SumReducer();
		MeanReducer meanReducer = new MeanReducer();
		MyPredicate2 predicate = new MyPredicate2();
		
		
		MapReduce<Integer> mapReduce = new MapReduce <Integer>(mapper ,sumReducer, meanReducer);
		underTest.visit(d, predicate ,  mapReduce);
		
		assertThat(sumReducer.reduce()).isEqualTo(111110);
		assertThat(meanReducer.reduce()).isEqualTo(22222);
		
	}
	
	@Test
	public void collections_reducers () {
		MyPredicate predicate = new MyPredicate();
		MyMapper2 mapper = new MyMapper2();
		SumReducer sumReducer = new SumReducer();
		
		underTest.visit(h,predicate, mapper , sumReducer);
		
		assertThat(sumReducer.reduce()).isEqualTo(1233);
		
	}
	
	@Test
	public void mutator_mapper () {
		UniversalVisitor underTest2 = new UniversalVisitor();
		MyPredicate predicate = new MyPredicate();
		MutatorMapper mapper = new MutatorMapper();
		
		assertThat(m.getSalary()).isEqualTo(10000);
		
		underTest2.visit(m,predicate, mapper );
		
		assertThat(m.getSalary()).isEqualTo(100000);
	}
	
	static class MutatorMapper implements Mapper<Void> {

		@Override
		public boolean handle(AccessibleObject object) {
			return object instanceof Field && ((Field) object).getType().equals(Integer.class);
		}

		@Override
		public Void map(Node node) {
			
			Field f = (Field) node.accessibleObject();
			
			Integer value = null;
			try {
				value = (Integer) f.get(node.instance());
				System.out.println("value " + value);
				f.set(node.instance(), value * 10);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
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

		@Override
		public boolean handle(AccessibleObject object) {
			return object instanceof Field && ((Field) object).getType().equals(Integer.class);
		}
		
	}
	
	static class MyMapper implements Mapper<Integer> {
		
		int counter = 0;
		int maxLevel = 0;

		@Override
		public Integer map(Node node) {
			System.out.println("node " + node.accessibleObject() +   " -> " + node.instance() + " type = " + node.instance().getClass());
			
			counter ++;
			maxLevel = Math.max(maxLevel, node.level());
			return new Integer(1);
		}
		
		public int getCount() {
			return counter;
		}
		
		public int getMaxLevel() {
			return maxLevel;
		}
		
		@Override
		public boolean handle(AccessibleObject object) {
			return object instanceof Field &&  ((Field)   object) .getType(). getAnnotation(Alphabet.class) != null;
		}
		
	}
	
	static class SumReducer implements Reducer<Integer, Integer> {
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
	
	static class MeanReducer implements Reducer<Integer, Integer> {
		int counter = 0;
		int sum =0;
		@Override
		public void collect(Integer input) {
			counter++;
			sum +=  input;
		}
		@Override
		public Integer reduce() {
			return sum / counter;
		}
	}
	
	
	
	static class MyPredicate implements Predicate {

		@Override
		public boolean apply(Field input) {
			
			return true  ;
		}
		
	}
	
	static class MyPredicate2 implements Predicate {

		@Override
		public boolean apply(Field input) {
			
			return input.getType().getAnnotation(Alphabet.class) != null;
		}
		
	}
	
	

}
