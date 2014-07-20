package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * UniversalVisitor is the main entrypoint. With it you can visit any object instance.
 * <p>
 * 
 * 
 * 
 * @author Epo Jemba
 * @author Pierre Thirouin
 *
 */
public class UniversalVisitor < T > {

	public void visit(Object o , Mapper<T> mapper ) {
		visit(o,null, mapper, null);
	}

	public void visit(Object o ,  Predicate predicate , Mapper<T> mapper   ) {
		visit(o,predicate , mapper, null);
	}
	
	public <R> R visit(Object o , Mapper<T> mapper ,  Reducer<T, R>  reducer  ) {
		return visit(o,null, mapper, reducer);
	}
	
	public <R> R[] visitn(Object o , Mapper<T> mapper ,  Reducer<T, R>...  reducers  ) {
		return visitn(o,null, mapper, reducers);
	}	
	
	public <R> R visit(Object o ,  Predicate predicate , Mapper<T> mapper ,  Reducer<T, R>  reducer  ) {
		Reducer[] r = new Reducer[1];
		r[0] = reducer;
		return (R) visitn (o , predicate , mapper , r)[0];
	}
	
	public <R> R[] visitn(Object o ,  Predicate predicate , Mapper<T> mapper ,  Reducer<T, R>...  reducers  ) {
		
		Set<Object> cache = new HashSet<Object>();
		
		ChainedNode node = ChainedNode.createRoot();
		
		if (predicate == null) {
			predicate = Predicate.TRUE;
		}
		
		recursiveVisit(o, cache ,node , predicate);
		
		if (reducers == null) reducers = new Reducer[0];
		
		for (node = node.next ; node != null &&  node.next  != null   ; node = node.next  ) {
//			if (! predicate.apply( node.accessibleObject()) ) {
				T t = mapper.map(node);
				
				for ( Reducer<T,R> reducer : reducers) {
					reducer.collect(t);
				}			
//			}
		}
		
//		R[] reducedResults = new R[reducers.length];
		List<R> reducedResult = new ArrayList<R>(reducers.length);
		
		for (int i = 0 ; i < reducers.length ; i ++ ) {
			reducedResult.add(i, reducers[i].reduce());
		}
		return (R[]) reducedResult.toArray( );
	}
	

	
	private static class ChainedNode extends Node {
		ChainedNode next;
		
		protected ChainedNode(Object instance, AccessibleObject accessibleObject, int level, ChainedNode next) {
			super(instance,accessibleObject , level);
			this.next = next;
		}
		
		private void next(ChainedNode node) {
			if (next != null) {
				throw new IllegalStateException("next pair can not be set twice.");
			}
			this.next = node;
		}
		
		public static ChainedNode createRoot() {
			return  new ChainedNode(new Object(), null, -1 ,null);
		}
		
		public ChainedNode append(Object o , AccessibleObject ao , int level) {
			
			next(new ChainedNode(o,ao, level,null));
			
			return next;
		}
		
		public ChainedNode last() {
			if (this.next != null) {
				return this.next.last();
			} else {
				return this;
			}
		}
	}
	
	private void recursiveVisit(Object object, Set<Object> cache, ChainedNode pair, Predicate predicate) {
		
		if (!cache.contains(object)) {

			cache.add(object);
			
			if (object == null) {
				// ignore nulls
			} else if (Collection.class.isAssignableFrom(object.getClass())) {
				visitAll((Collection<?>) object, cache,pair,predicate);
			} else if (object.getClass().isArray()) {
				visitAll(Arrays.asList((Object[]) object), cache,pair,predicate);
			} else if (Map.class.isAssignableFrom(object.getClass())) {
				visitMap((Map<?, ?>) object, cache,pair,predicate);
			} else {
				visitObject(object, cache,pair,predicate);
			}
		}
	}
	
	private void visitObject(Object object,  Set<Object> cache, ChainedNode pair, Predicate predicate) {

		int currentLevel = pair.level + 1;
		ChainedNode current = pair;
		//
		for (Constructor<?> c : object.getClass().getConstructors()) {
			if ( predicate != null  &&  predicate.apply(c)) {
				current = current.append(  object , c  , currentLevel);
			}
		}
		//
		for (Method m : object.getClass().getMethods()) {
			if ( predicate != null  &&  predicate.apply(m)) {
				current = current.append(  object , m  , currentLevel);
			}
		}
		
		for (Field f : object.getClass().getFields()) {
			
			if ( predicate != null  &&  predicate.apply(f)) {
				
				current = current.append(  object , f  , currentLevel);
				
				Object deeperObject = readField(f, object);
				
				recursiveVisit(deeperObject , cache, current, predicate);
				
				current = current.last();
				
			}
		}
	}

	private void visitAll(Collection<?> values, Set<Object> cache, ChainedNode pair, Predicate predicate) {
		Object[] valArray = values.toArray();
		for (int i = 0; i < valArray.length; i++) {
			Object value = valArray[i];
			if (value != null) {
				visitObject(value, cache,pair,predicate);
			}
		}
	}

	private void visitMap(Map<?, ?> values, Set<Object> cache, ChainedNode pair, Predicate predicate) {
		for (Object thisKey : values.keySet()) {
			Object value = values.get(thisKey);
			if (value != null) {
				visitObject(value, cache,pair,predicate);
			}
		}
	}
	
	private Object readField(Field f , Object instance) {
		Object o = null;
		
		try {
			o = f.get(instance);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return o;
	}

	
}
