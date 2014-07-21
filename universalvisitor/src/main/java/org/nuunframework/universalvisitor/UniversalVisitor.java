package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * UniversalVisitor is the main entrypoint. With it you can visit any object
 * instance.
 * <p>
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public class UniversalVisitor<T> {

	public void visit(Object o, Mapper<T> mapper) {
		visitn(o, null, mapper);
	}

	public void visit(Object o, Predicate predicate, Mapper<T> mapper) {
		visitn(o, predicate, mapper);
	}

	public <R> R visit(Object o, Mapper<T> mapper, Reducer<T, ?> reducer) {
		return visit(o, null, mapper, reducer);
	}

	public Object[] visitn(Object o, Mapper<T> mapper, Reducer<T, ?>... reducers) {
		return visitn(o, null, mapper, reducers);
	}

	@SuppressWarnings("unchecked")
	public <R> R visit(Object o, Predicate predicate, Mapper<T> mapper, Reducer<T, ?> reducer) {
		Reducer<T, ?>[] r = new Reducer[1];
		r[0] = reducer;
		return (R) visitn(o, predicate, mapper, r)[0];
	}

	@SuppressWarnings("unchecked")
	public Object[] visitn(Object o, Predicate predicate, Mapper<T> mapper, @SuppressWarnings("rawtypes") Reducer... reducers) {

		Set<Object> cache = new HashSet<Object>();

		ChainedNode node = ChainedNode.createRoot();

		if (predicate == null) {
			predicate = Predicate.TRUE;
		}

		recursiveVisit(o, cache, node, predicate);

		if (reducers == null) {
			reducers = new Reducer[0];
		}

		for (node = node.next; node != null; node = node.next) {
			if (mapper.handle(node.accessibleObject())) {
				T t = mapper.map(node);

				for (Reducer<T, ?> reducer : reducers) {
					reducer.collect(t);
				}
			}
		}

		List<Object> reducedResult = new ArrayList<Object>(reducers.length);

		for (int i = 0; i < reducers.length; i++) {
			reducedResult.add(i, reducers[i].reduce());
		}
		return reducedResult.toArray();
	}

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

	private void recursiveVisit(Object object, Set<Object> cache, ChainedNode node, Predicate predicate) {

		int currentLevel = node.level + 1;
		
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
