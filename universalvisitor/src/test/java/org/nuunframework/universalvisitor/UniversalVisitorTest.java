
package org.nuunframework.universalvisitor;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nuunframework.universalvisitor.api.Filter;
import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Mapper;
import org.nuunframework.universalvisitor.api.Metadata;
import org.nuunframework.universalvisitor.api.Node;
import org.nuunframework.universalvisitor.api.Reducer;
import org.nuunframework.universalvisitor.core.MapReduceDefault;
import org.nuunframework.universalvisitor.sample.Alphabet;
import org.nuunframework.universalvisitor.sample.collections.H;
import org.nuunframework.universalvisitor.sample.collections.I;
import org.nuunframework.universalvisitor.sample.collections.J;
import org.nuunframework.universalvisitor.sample.collections.K;
import org.nuunframework.universalvisitor.sample.collections.L;
import org.nuunframework.universalvisitor.sample.issues.Issue1;
import org.nuunframework.universalvisitor.sample.issues.Issue2;
import org.nuunframework.universalvisitor.sample.levels.L1;
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
	
	Issue1 issue1;
	
	Issue2 issue2;
	
	L1 l1;
	
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
		//
		issue1 = new Issue1();
		//
		issue2 = new Issue2();
		//
		l1 = new L1();
		
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
		
		
		underTest.visit(String.class,predicate, mapper);
		underTest.visit(n,predicate, mapper);
		
	}
	

	@Test
	public void multiple_reducer () {
		
		MyMapper2 mapper = new MyMapper2();
		SumReducer sumReducer = new SumReducer();
		MeanReducer meanReducer = new MeanReducer();
		MyPredicate2 predicate = new MyPredicate2();
		
		
		MapReduce<Integer> mapReduce = new MapReduceDefault <Integer>(mapper ,sumReducer, meanReducer);
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
	
	@Test
	public void issue1 () {
		NopMap nopMap = new NopMap();
		
		underTest.visit(issue1, nopMap);
	}
	
	@Test
	public void issue2 () {
		CountMapper nopMap = new CountMapper();
//		System.out.println("============================== ISSUE2 ===================================");
		underTest.visit(issue2, nopMap);
		
		assertThat(nopMap.methods).contains("interface1M","interface2M","interface3M","issue2Private" , "issue2Package", "issue2Protected" , "issue2Public" , "parentPrivate" , "parentProtected", "parentPackage" , "parentPublic");
//		System.out.println(nopMap.methods);
		assertThat(nopMap.fields).contains("interface1F","interface2F","interface3F", "issue2Private" , "issue2Package", "issue2Protected" , "issue2Public" , "parentPrivate" , "parentProtected", "parentPackage" , "parentPublic");
//		System.out.println(nopMap.fields);
		System.out.println(nopMap.node);
	}
	
	
	@Test
	public void checkLevel () {
		CheckLevelMap mapper = new CheckLevelMap();
		underTest.visit(l1, mapper);
	}
	
	class CheckLevelMap implements Mapper<Void> {

		@Override
		public boolean handle(AnnotatedElement object) {
			return object instanceof Field  /*|| object instanceof Constructor*/;
		}

		@Override
		public Void map(Node node) {

			String indentation = "";
			for (int i = 0; i < node.level(); i++) {
				indentation += "\t";
			}
			
			if (node.annotatedElement() instanceof Field) {
				Field f = (Field) node.annotatedElement();
				
				String value = "";
				if (f.getType().equals(String.class)  || f.getType().equals(Integer.class)) {
					value = " = \"" + readField(f, node.instance()) + "\"";
				}
				
				Metadata metadata = node.metadata();
				if (metadata == null) {
					metadata = new Metadata();
				}
				System.out.println(indentation + "|" +node.level() +  "|"+"  "    +f.getName() + metadata + value + " from " + f.getDeclaringClass().getSimpleName()) ;
			}
			if (node.annotatedElement() instanceof Constructor) {
				Constructor c = (Constructor) node.annotatedElement();
				System.out.println(indentation + "|" + node.level() +  "|"+ node.metadata()+" "    +c.getDeclaringClass().getSimpleName() + "()");
			}

			return null;
		}
	}
	
	static class NopMap implements Mapper<Void> {

		@Override
		public boolean handle(AnnotatedElement object) {
			return true;
		}

		@Override
		public Void map(Node node) {
			System.out.println("Current Node : " + node.annotatedElement());
			return null;
		}
		
	}
	
	static class CountMapper implements Mapper<Void> {
		
		List<String> fields = new ArrayList<String>();
		List<String> methods = new ArrayList<String>();
		List<String> constructors = new ArrayList<String>();
		
		Node node = null;
		
		@Override
		public boolean handle(AnnotatedElement object) {
			return true;
		}
		
		@Override
		public Void map(Node node) {
			if (this.node == null) {
				this.node = node;
			}
			if (node.annotatedElement() instanceof Field) {
				fields.add(((Field) node.annotatedElement()).getName() );
			}
			if (node.annotatedElement() instanceof Method) {
				methods.add(((Method) node.annotatedElement()).getName() );
			}
			if (node.annotatedElement() instanceof Constructor) {
				constructors.add(((Constructor) node.annotatedElement()).getName() );
			}
			
			
			return null;
		}
		
	}
	
	static class MutatorMapper implements Mapper<Void> {

		@Override
		public boolean handle(AnnotatedElement object) {
			return object instanceof Field && ((Field) object).getType().equals(Integer.class);
		}

		@Override
		public Void map(Node node) {
			
			Field f = (Field) node.annotatedElement();
			
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
			Field f = (Field) node.annotatedElement();
			
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
		public boolean handle(AnnotatedElement object) {
			return object instanceof Field && ((Field) object).getType().equals(Integer.class);
		}
		
	}
	
	static class MyMapper implements Mapper<Integer> {
		
		int counter = 0;
		int maxLevel = 0;

		@Override
		public Integer map(Node node) {
			System.out.println("node " + node.annotatedElement() +   " -> " + node.instance() + " type = " + node.instance().getClass());
			
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
		public boolean handle(AnnotatedElement object) {
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
	
	
	
	static class MyPredicate implements Filter {

		@Override
		public boolean retains(Field input) {
			
			return true  ;
		}
		
	}
	
	static class MyPredicate2 implements Filter {

		@Override
		public boolean retains(Field input) {
			
			return input.getType().getAnnotation(Alphabet.class) != null;
		}
		
	}

	public Object readField(Field f, Object instance) {
		Object o = null;
		try {
			f.setAccessible(true);
			o = f.get(instance);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return o;
	}
	

}
