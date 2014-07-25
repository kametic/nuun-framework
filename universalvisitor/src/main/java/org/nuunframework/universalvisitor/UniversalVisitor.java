package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Mapper;
import org.nuunframework.universalvisitor.api.Node;
import org.nuunframework.universalvisitor.api.Predicate;
import org.nuunframework.universalvisitor.api.Reducer;

/**
 * UniversalVisitor is the main entrypoint. With it you can visit any object
 * instance.
 * <p>
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public class UniversalVisitor {

	@SuppressWarnings("unchecked")
	public <T> void visit(Object o, Mapper<T> mapper) {
		visit(o, (Predicate) null, new MapReduce<T>(mapper));
	}
	
	@SuppressWarnings("unchecked")
	public <T> void visit(Object o, Predicate predicate, Mapper<T> mapper) {
		visit(o, predicate, new MapReduce<T>(mapper));
	}

	public <T> void visit(Object o, Mapper<T> mapper, Reducer<T, ?>... reducers) {
		visit(o, (Predicate) null, new MapReduce<T>(mapper , reducers));
	}

	@SuppressWarnings("unchecked")
	public <T> void visit(Object o, Predicate predicate, Mapper<T> mapper, Reducer<T, ?> reducer) {
		
		visit(o, predicate, new MapReduce<T>(mapper , reducer));
	}

	public void visit(Object o,  MapReduce<?> ...mapReduces) {
		visit(o, null, mapReduces);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void visit(Object o, Predicate predicate, MapReduce<?> ...mapReduces) {

		Set<Object> cache = new HashSet<Object>();

		ChainedNode node = ChainedNode.createRoot();

		if (predicate == null) {
			predicate = Predicate.TRUE;
		}

		recursiveVisit(o, cache, node, predicate);

		for (node = node.next; node != null; node = node.next) {
			for ( MapReduce mapReduce : mapReduces) {
				if (mapReduce.getMapper().handle(node.accessibleObject())) {
					Object t = mapReduce.getMapper().map(node);
					
					for (Reducer<Object, Object> reducer : mapReduce.getReducers()) {
						reducer.collect(t);
					}
				}
			
			}
		}

	}
	
	
//	@SuppressWarnings("unchecked")
//	public Object[] visitReflectionN(AnnotatedElement o, Predicate predicate, Mapper<T> mapper, @SuppressWarnings("rawtypes") Reducer... reducers) {
//		
//		Set<Object> cache = new HashSet<Object>();
//
//		ChainedNode node = ChainedNode.createRoot();
//
//		if (predicate == null) {
//			predicate = Predicate.TRUE;
//		}
//
//		recursiveVisit(o, cache, node, predicate);
//
//		if (reducers == null) {
//			reducers = new Reducer[0];
//		}
//
//		for (node = node.next; node != null; node = node.next) {
//			if (mapper.handle(node.accessibleObject())) {
//				T t = mapper.map(node);
//
//				for (Reducer<T, ?> reducer : reducers) {
//					reducer.collect(t);
//				}
//			}
//		}
//
//		List<Object> reducedResult = new ArrayList<Object>(reducers.length);
//
//		for (int i = 0; i < reducers.length; i++) {
//			reducedResult.add(i, reducers[i].reduce());
//		}
//		return reducedResult.toArray();
//		
//	}

	private static class ChainedNode extends Node {
		ChainedNode next;

		protected ChainedNode(Object instance, AccessibleObject accessibleObject, int level, ChainedNode next) {
			super(instance, accessibleObject, level);
			this.next = next;
		}

		private void next(ChainedNode node) {
			if (next != null) {
				throw new IllegalStateException(
						"next pair can not be set twice.");
			}
			this.next = node;
		}

		public static ChainedNode createRoot() {
			return new ChainedNode(new Object(), null, -1, null);
		}

		public ChainedNode append(Object o, AccessibleObject ao, int level) {

			next(new ChainedNode(o, ao, level, null));

			return next;
		}

		public ChainedNode last() {
			if (this.next != null) {
				return this.next.last();
			} else {
				return this;
			}
		}

		@Override
		public String toString() {
			String indentation ="";
			for (int i = 0 ; i < level ; i ++) {
				indentation += "\t";
			} // instance()=
			String rep = String.format("%sChainedNode [ %s@%s , level=%s , accessibleObject=%s] \n%s", indentation ,  instance().getClass().getSimpleName(), Integer.toHexString(instance().hashCode()) , level()
					,accessibleObject(),next);
			return rep;
			
//			return "ChainedNode [instance()=" + instance() + ", level()="
//					+ level() + ", accessibleObject()=" + accessibleObject()
//					+ "]  ==> \n" + next;
		}

	}

//	private void recursiveVisitReflection(Class<?> klass, Set<Object> cache, ChainedNode node, Predicate predicate) { 
//	int currentLevel = node.level + 1;
//		
//		if (!cache.contains(klass)) {
//
//			cache.add(klass);
//
//			Package p = klass.getPackage();
//			
//			
//			if (klass == null) {
//				// ignore nulls
//			} else if (Collection.class.isAssignableFrom(klass.getClass())) {
//				visitAll((Collection<?>) klass, cache, node, currentLevel,predicate);
//			} else if (klass.getClass().isArray()) {
//				visitAll(Arrays.asList((Object[]) klass), cache, node,currentLevel,
//						predicate);
//			} else if (Map.class.isAssignableFrom(klass.getClass())) {
//				visitMap((Map<?, ?>) klass, cache, node,currentLevel, predicate);
//			} else {
//				visitObject(klass, cache, node, currentLevel,predicate);
//			}
//		}
//	}
	
	
	private void recursiveVisit(Object object, Set<Object> cache, ChainedNode node, Predicate predicate) {

		int currentLevel = node.level() + 1;
		
		if (!cache.contains(object)) {

			cache.add(object);

			if (object == null) {
				// ignore nulls
			} else if (Collection.class.isAssignableFrom(object.getClass())) {
				visitAll((Collection<?>) object, cache, node, currentLevel,predicate);
			} else if (object.getClass().isArray()) {
				visitAll(Arrays.asList((Object[]) object), cache, node,currentLevel,
						predicate);
			} else if (Map.class.isAssignableFrom(object.getClass())) {
				visitMap((Map<?, ?>) object, cache, node,currentLevel, predicate);
			} else {
				visitObject(object, cache, node, currentLevel,predicate);
			}
		}
	}

	private void visitObject(Object object, Set<Object> cache, ChainedNode node, int currentLevel,  Predicate predicate) {

		ChainedNode current = node;
		//
		for (Constructor<?> c : object.getClass().getConstructors()) {
			if (!isJdkMember(c)) {
				current = current.append(object, c, currentLevel);
			}
		}
		//
		for (Method m : object.getClass().getMethods()) {
			if (!isJdkMember(m)) {
			    current = current.append(object, m, currentLevel);
			}
		}
		
		for (Field f : object.getClass().getDeclaredFields()) {
			if (!isJdkMember(f)) {
				
				current = current.append(object, f, currentLevel);

				if (predicate != null && predicate.apply(f)) {
					Object deeperObject = readField(f, object);

					recursiveVisit(deeperObject, cache, current, predicate);
					current = current.last();
				}
			}
		}
	}

	private void visitAll(Collection<?> values, Set<Object> cache, ChainedNode pair, int currentLevel, Predicate predicate) {
		ChainedNode current = pair;
		
		Object[] valArray = values.toArray();
		for (int i = 0; i < valArray.length; i++) {
			Object value = valArray[i];
			if (value != null) {
				visitObject(value, cache, current, currentLevel,predicate);
				current = current.last();
			}
		}
	}

	private void visitMap(Map<?, ?> values, Set<Object> cache, ChainedNode pair, int currentLevel, Predicate predicate) {
		ChainedNode current = pair;
		for (Object thisKey : values.keySet()) {
			Object value = values.get(thisKey);
			if (value != null) {
				visitObject(value, cache, current, currentLevel , predicate);
				current = current.last();
			}
		}
	}
	
	private boolean isJdkMember(Member input) {
		return input.getDeclaringClass().getPackage().getName()
				.startsWith("java.") || input.getDeclaringClass().getPackage().getName().startsWith("javax.") ;
	}

	private Object readField(Field f, Object instance) {
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
