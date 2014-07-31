package org.nuunframework.universalvisitor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
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

import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Mapper;
import org.nuunframework.universalvisitor.api.Node;
import org.nuunframework.universalvisitor.api.Filter;
import org.nuunframework.universalvisitor.api.Reducer;
import org.nuunframework.universalvisitor.core.MapReduceDefault;

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

//	@SuppressWarnings("unchecked")
//	public <T> void visit(Class<?> o, Mapper<T> mapper) {
////		visit(o, (Predicate) null, new MapReduceDefault<T>(mapper));
//	}
//
//	@SuppressWarnings("unchecked")
//	public <T> void visit(Object o, Mapper<T> mapper) {
//		visit(o, (Predicate) null, new MapReduceDefault<T>(mapper));
//	}
	
	@SuppressWarnings("unchecked")
	public <T> void visit(Class<?> o, Filter filter, Mapper<T> mapper) {
		visit(o, filter, new MapReduceDefault<T>(mapper));
	}
	@SuppressWarnings("unchecked")
	public <T> void visit(Object o, Filter filter, Mapper<T> mapper) {
		visit(o, filter, new MapReduceDefault<T>(mapper));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void visit(Object o, Mapper<T> mapper, Reducer... reducers) {
		visit(o, (Filter) null, new MapReduceDefault<T>(mapper , reducers));
	}

	@SuppressWarnings("unchecked")
	public <T> void visit(Object o, Filter filter, Mapper<T> mapper, Reducer<T, ?> reducer) {
		visit(o, filter, new MapReduceDefault<T>(mapper , reducer));
	}

	public void visit(Object o,  MapReduce<?> ...mapReduces) {
		visit(o, null, mapReduces);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void visit(Class<?> o, Filter filter, MapReduce<?> ...mapReduces) {
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void visit(Object o, Filter filter, MapReduce ...mapReduces) {

		Set<Object> cache = new HashSet<Object>();

		ChainedNode node = ChainedNode.createRoot();

		if (filter == null) {
			filter = Filter.TRUE;
		}

		recursiveVisit(o, cache, node, filter);

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
			next = node;
		}

		public static ChainedNode createRoot() {
			return new ChainedNode(new Object(), null, -1, null);
		}

		public ChainedNode append(Object o, AccessibleObject ao, int level) {

			next(new ChainedNode(o, ao, level, null));

			return next;
		}

		public ChainedNode last() {
			if (next != null) {
				return next.last();
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
	
	
	private void recursiveVisit(Object object, Set<Object> cache, ChainedNode node, Filter filter) {

		int currentLevel = node.level() + 1;
		
		if (!cache.contains(object)) {

			cache.add(object);

			if (object == null)
			{
				// ignore nulls
			}
			else if (Collection.class.isAssignableFrom(object.getClass()))
			{
				visitAllCollection((Collection<?>) object, cache, node, currentLevel,filter);
			}
			else if (object.getClass().isArray() /* && ! object.getClass().getComponentType().isPrimitive() */)
			{
				visitAllArray( object, cache, node,currentLevel, filter);
			}
//			else if (object.getClass().isArray()  &&   object.getClass().getComponentType().isPrimitive() )
//			{
//				visitAllArrayPrimitives( object, cache, node,currentLevel, predicate);
//			}
			else if (Map.class.isAssignableFrom(object.getClass()))
			{
				visitAllMap((Map<?, ?>) object, cache, node,currentLevel, filter);
			}
			 else
			{
				visitObject(object, cache, node, currentLevel,filter);
			}
		}
	}

	private void visitObject(Object object, Set<Object> cache, ChainedNode node, int currentLevel,  Filter filter) {

		Class<? extends Object> currentClass = object.getClass();
		
		if ( ! isJdkMember(currentClass)) {
			
			ChainedNode current = node;
			Class<?>[] family = getAllInterfacesAndClasses(currentClass);
			for (Class<?> elementClass : family)
			{ // We iterate over all the family tree of the current class
				//
				System.out.println("===============================");
				System.out.println(" => " + elementClass.getSimpleName());
				if  (elementClass != null &&  !isJdkMember(elementClass)) {
					
					for (Constructor<?> c : elementClass.getDeclaredConstructors()) {
						if (!isJdkMember(c)  && ! c.isSynthetic()) {
							System.out.println("   => " + c.getName());
							current = current.append(object, c, currentLevel);
						}
					}
					//
					for (Method m : elementClass.getDeclaredMethods()) {
						if (!isJdkMember(m) && ! m.isSynthetic()  ) {
							System.out.println("   => " + m.getName());
							current = current.append(object, m, currentLevel);
						}
					}
					
					for (Field f : elementClass.getDeclaredFields()) {
						if (!isJdkMember(f) && ! f.isSynthetic()  ) {
							System.out.println("   => " + f.getName());
							
							current = current.append(object, f, currentLevel);
							
							if (filter != null && filter.retains(f)) {
								Object deeperObject = readField(f, object);
								
								recursiveVisit(deeperObject, cache, current,
										filter);
								current = current.last();
							}
						}
					}
				}
				
			}
		}
	}

	private void visitAllCollection(Collection<?> values, Set<Object> cache, ChainedNode node, int currentLevel, Filter filter) {
		ChainedNode current = node;
		
		Object[] valArray = values.toArray();
		for (int i = 0; i < valArray.length; i++) {
			Object value = valArray[i];
			if (value != null) {
				visitObject(value, cache, current, currentLevel,filter);
				current = current.last();
			}
		}
	}
	private void visitAllArray(Object arrayObject, Set<Object> cache, ChainedNode node, int currentLevel, Filter filter) {
		ChainedNode current = node;
		
		int l = Array.getLength(arrayObject);
		for (int i = 0; i < l; i++) {
			Object value = Array.get(arrayObject, i);
			if (value != null) {
				visitObject(value, cache, current, currentLevel,filter);
				current = current.last();
			}
		}
	}
//	private void visitAllArrayPrimitives(Object arrayObject, Set<Object> cache, ChainedNode node, int currentLevel, Predicate predicate) {
//		ChainedNode current = node;
//
//		int l = Array.getLength(arrayObject);
//		for (int i = 0; i < l; i++) {
//			Object value = Array.get(arrayObject, i);
//			if (value != null) {
//				visitObject(value, cache, current, currentLevel,predicate);
//				current = current.last();
//			}
//		}
//	}

	private void visitAllMap(Map<?, ?> values, Set<Object> cache, ChainedNode pair, int currentLevel, Filter filter) {
		ChainedNode current = pair;
		for (Object thisKey : values.keySet()) {
			Object value = values.get(thisKey);
			if (value != null) {
				visitObject(value, cache, current, currentLevel , filter);
				current = current.last();
			}
		}
	}
	
	private boolean isJdkMember(Member input) {
		return isJdkMember(input.getDeclaringClass());
	}
	
	private boolean isJdkMember(Class<?> input) {
		return input.getPackage().getName()
				.startsWith("java.") || input.getPackage().getName().startsWith("javax.") ;
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
	
	 /**
     * Returns all the interfaces and classes implemented or extended by a class.
     *
     * @param clazz The class to search from.
     * @return The array of classes and interfaces found.
     */
    private Class<?>[] getAllInterfacesAndClasses(Class<?> clazz) {
        return getAllInterfacesAndClasses(new Class[]{clazz});
    }

    /**
     * This method walks up the inheritance hierarchy to make sure we get every
     * class/interface extended or implemented by classes.
     *
     * @param classes The classes array used as search starting point.
     * @return the found classes and interfaces.
     */
    @SuppressWarnings("unchecked")
    private  Class<?>[] getAllInterfacesAndClasses(Class<?>[] classes) {
        if (0 == classes.length) {
            return classes;
        } else {
            List<Class<?>> extendedClasses = new ArrayList<Class<?>>();
            // all interfaces hierarchy
            for (Class<?> clazz : classes) {
                if (clazz != null) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    if (interfaces != null) {
                        extendedClasses.addAll(Arrays.asList(interfaces));
                    }
                    Class<?> superclass = clazz.getSuperclass();
                    if (superclass != null && superclass != Object.class) {
                        extendedClasses.addAll(Arrays.asList(superclass));
                    }
                }
            }

            // Class::getInterfaces() gets only interfaces/classes
            // implemented/extended directly by a given class.
            // We need to walk the whole way up the tree.
            return concat(classes, getAllInterfacesAndClasses(extendedClasses.toArray(new Class[extendedClasses.size()])));
        }
    }
    
    @SuppressWarnings("rawtypes")
	private Class[] concat(Class[] A, Class[] B) {
    	   int aLen = A.length;
    	   int bLen = B.length;
    	   Class[] C= new Class[aLen+bLen];
    	   System.arraycopy(A, 0, C, 0, aLen);
    	   System.arraycopy(B, 0, C, aLen, bLen);
    	   return C;
    	}

}
