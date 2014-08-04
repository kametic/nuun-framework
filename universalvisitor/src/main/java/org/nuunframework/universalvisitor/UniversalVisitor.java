package org.nuunframework.universalvisitor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
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
import java.util.Queue;
import java.util.Set;

import org.nuunframework.universalvisitor.api.Filter;
import org.nuunframework.universalvisitor.api.Job;
import org.nuunframework.universalvisitor.api.MapReduce;
import org.nuunframework.universalvisitor.api.Mapper;
import org.nuunframework.universalvisitor.api.Metadata;
import org.nuunframework.universalvisitor.api.Reducer;
import org.nuunframework.universalvisitor.core.JobDefault;
import org.nuunframework.universalvisitor.core.MapReduceDefault;
import org.nuunframework.universalvisitor.core.NodeDefault;

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
	public <T> void visit(AnnotatedElement ae, Mapper<T> mapper) {
		visit(ae, (Filter) null, new MapReduceDefault<T>(mapper));
	}

	@SuppressWarnings("unchecked")
	public <T> void visit(Object o, Mapper<T> mapper) {
		visit(o, (Filter) null, new MapReduceDefault<T>(mapper));
	}
	
	@SuppressWarnings("unchecked")
	public <T> void visit(AnnotatedElement o, Filter filter, Mapper<T> mapper) {
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
	
	@SuppressWarnings("rawtypes")
	public void visit(AnnotatedElement ae, Filter filter, MapReduce<?> ...mapReduces) {
		visit(ae,filter,new JobDefault( mapReduces));
	}
	
	@SuppressWarnings({ "rawtypes" })
	public void visit(Object o, Filter filter, MapReduce ...mapReduces) {
		visit(o,filter,new JobDefault( mapReduces));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void visit(AnnotatedElement ae, Filter filter, Job job) {
		Set<Object> cache = new HashSet<Object>();
		ChainedNode node = ChainedNode.createRoot();
		if (filter == null) {
			filter = Filter.TRUE;
		}

		recursiveVisit(ae, cache, node, filter);
		doMapReduce(job, node);
	}

	/**
	 * @param job
	 * @param node
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void doMapReduce(Job<?> job, ChainedNode node) {
		for (node = node.next; node != null; node = node.next) {
			for ( MapReduce mapReduce : job.mapReduces()) {
				if (mapReduce.getMapper().handle(node.annotatedElement())) {
					Object t = mapReduce.getMapper().map(node);
					
					for (Reducer<Object, Object> reducer : mapReduce.getReducers()) {
						reducer.collect(t);
					}
				}
			}
		}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void visit(Object o, Filter filter, Job job) {

		Set<Object> cache = new HashSet<Object>();

		ChainedNode node = ChainedNode.createRoot();

		if (filter == null) {
			filter = Filter.TRUE;
		}

		recursiveVisit(o, cache, node, filter);

		doMapReduce(job, node);

	}
	
	private static class ChainedNode extends NodeDefault {
		ChainedNode next;

		protected ChainedNode(Object instance, AnnotatedElement annotatedElement, int level, ChainedNode next) {
			super(instance, annotatedElement, level);
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

		public ChainedNode append(Object o, AnnotatedElement ao, int level,Metadata metadata) {

			next(new ChainedNode(o, ao, level, null).metadata(metadata));

			return next;
		}
		

		@Override
		public ChainedNode metadata(Metadata metadata) {
			return (ChainedNode) super.metadata(metadata);
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
			String rep = String.format("%sChainedNode [ %s@%s , level=%s , annotatedElement=%s] \n%s", indentation ,  instance().getClass().getSimpleName(), Integer.toHexString(instance().hashCode()) , level()
					,annotatedElement(),next);
			return rep;
			
//			return "ChainedNode [instance()=" + instance() + ", level()="
//					+ level() + ", annotatedElement()=" + annotatedElement()
//					+ "]  ==> \n" + next;
		}

	}


	private void recursiveVisit(AnnotatedElement ae, Set<Object> cache, ChainedNode node, Filter filter) {

		int currentLevel = node.level() + 1;
		
		if (!cache.contains(ae)) {

			cache.add(ae);
			
			if (ae == null)
			{
				// ignore nulls
			}
			else if (Constructor.class.isAssignableFrom(ae.getClass()))
			{
//				visitConstructor((Constructor) ae, cache, node, currentLevel,filter);
			}
			else if (Method.class.isAssignableFrom(ae.getClass()))
			{
//				visitMethod((Method) ae, cache, node, currentLevel,filter);
			}
			else if (Field.class.isAssignableFrom(ae.getClass()))
			{
//				visitField((Field) ae, cache, node, currentLevel,filter);
			}
			else if (Package.class.isAssignableFrom(ae.getClass()))
			{
//				visitPackage((Package) ae, cache, node, currentLevel,filter);
			}
			else if ( Class.class.isAssignableFrom(ae.getClass()) && ae.getClass().isAnnotation() )
			{
				visitClass((Class<?>) ae, cache, node, currentLevel,filter);
			}
		
			 else
			{
				// visitObject(object, cache, node, currentLevel,filter);
				 throw new IllegalStateException("Can not visist " + ae);
			}
		}
	}

	
	private void doVisitPackage(AnnotatedElement ae, Package p, Set<Object> cache, ChainedNode node, int currentLevel, Filter filter, Metadata metadata) {
		ChainedNode current = node;
		
		// annotations on package
		for (Annotation annotation : p.getDeclaredAnnotations()) {
			doVisitAnnotation(p, annotation, cache, current, currentLevel, filter, metadata);
			current = current.last();
			
		}
		
	    // then Append the package it self
		current = current.append(ae, p, currentLevel, null);
	}
	
	
	private void doVisitAnnotation(AnnotatedElement ae, Annotation a, Set<Object> cache, ChainedNode node, int currentLevel, Filter filter, Metadata metadata) {
		
	}
	

	private void visitClass(Class<?> cläss, Set<Object> cache, ChainedNode node, int currentLevel,  Filter filter ) {
		visitClass( cläss, cache, node, currentLevel,  filter , (Metadata) null);
	}
		
		
	private void visitClass(Class<?> cläss, Set<Object> cache, ChainedNode node, int currentLevel, Filter filter, Metadata metadata) {
		Class<? extends Object> currentClass = cläss;
		ChainedNode current = node;

		if (!isJdkMember(currentClass)) {
			
            

			if (currentClass != null && !isJdkMember(currentClass)) {
				// package
				
				Package p = currentClass.getPackage();
				
				doVisitPackage(currentClass, p, cache, node, currentLevel, filter , metadata);
				
				// annotations
				
				
				// extends
				// implements
				// declared classes
				// constructors
				// methods
				// fields

				for (Constructor<?> c : currentClass.getDeclaredConstructors()) {
					if (!isJdkMember(c) && !c.isSynthetic()) {
						current = current.append(currentClass, c, currentLevel, metadata);
					}
				}
				//
				for (Method m : currentClass.getDeclaredMethods()) {
					if (!isJdkMember(m) && !m.isSynthetic()) {
						current = current.append(currentClass, m, currentLevel, metadata);
					}
				}

				for (Field f : currentClass.getDeclaredFields()) {
					if (!isJdkMember(f) && !f.isSynthetic()) {

						current = current.append(currentClass, f, currentLevel, metadata);

						if (filter != null && filter.retains(f)) {
							Object deeperObject = readField(f, currentClass);

							recursiveVisit(deeperObject, cache, current, filter);
							current = current.last();
						}
					}
				}
			}

		}
	}
	

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
			else if (object.getClass().isArray())
			{
				visitAllArray( object, cache, node,currentLevel, filter);
			}
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
		visitObject(object, cache, node, currentLevel, filter, null);
	}
	
	private <T> void visitConstructor(Constructor<T> ae, Set<Object> cache, ChainedNode node, int currentLevel,  Filter filter , Metadata metadata) {
		// Params
		//   Annotations
		// Exceptions
		Class<? extends Object> currentClass = ae.getClass();
		
		ChainedNode current = node;
		
		Class<?>[] family = getAllInterfacesAndClasses(currentClass);
		for (Class<?> elementClass : family) { // We iterate over all the family
												// tree of the current class
												//
			if (elementClass != null && !isJdkMember(elementClass)) {

				for (Constructor<?> c : elementClass.getDeclaredConstructors()) {
					if (!isJdkMember(c) && !c.isSynthetic()) {
						current = current.append(ae, c, currentLevel, metadata);
					}
				}
				//
				for (Method m : elementClass.getDeclaredMethods()) {
					if (!isJdkMember(m) && !m.isSynthetic()) {
						current = current.append(ae, m, currentLevel, metadata);
					}
				}

				for (Field f : elementClass.getDeclaredFields()) {
					if (!isJdkMember(f) && !f.isSynthetic()) {

						current = current.append(ae, f, currentLevel, metadata);

						if (filter != null && filter.retains(f)) {
							Object deeperObject = readField(f, ae);

							recursiveVisit(deeperObject, cache, current, filter);
							current = current.last();
						}
					}
				}
			}

		}
	}
	
	private void visitObject(Object object, Set<Object> cache, ChainedNode node, int currentLevel,  Filter filter , Metadata metadata) {

		Class<? extends Object> currentClass = object.getClass();
		
		if ( ! isJdkMember(currentClass)) {
			
			ChainedNode current = node;
			Class<?>[] family = getAllInterfacesAndClasses(currentClass);
			for (Class<?> elementClass : family)
			{ // We iterate over all the family tree of the current class
				//
				if  (elementClass != null &&  !isJdkMember(elementClass)) {
					
					for (Constructor<?> c : elementClass.getDeclaredConstructors()) {
						if (!isJdkMember(c)  && ! c.isSynthetic()) {
							current = current.append(object, c, currentLevel,metadata);
						}
					}
					//
					for (Method m : elementClass.getDeclaredMethods()) {
						if (!isJdkMember(m) && ! m.isSynthetic()  ) {
							current = current.append(object, m, currentLevel,metadata);
						}
					}
					
					for (Field f : elementClass.getDeclaredFields()) {
						if (!isJdkMember(f) && ! f.isSynthetic()  ) {
							
							current = current.append(object, f, currentLevel,metadata);
							
							if (filter != null && filter.retains(f)) {
								Object deeperObject = readField(f, object);
								
								recursiveVisit(deeperObject, cache, current, filter);
								current = current.last();
							}
						}
					}
				}
				
			}
		}
	}

	private void visitAllCollection(Collection<?> collection, Set<Object> cache, ChainedNode node, int currentLevel, Filter filter) {
		ChainedNode current = node;
		
		boolean indexable = collection instanceof List  || collection instanceof Queue;
		
		Object[] valArray = collection.toArray();
		for (int i = 0; i < valArray.length; i++) {
			Object value = valArray[i];
			if (value != null) {
				if (indexable) {
					visitObject(value, cache, current, currentLevel,filter,new Metadata(i));
				} else {
					visitObject(value, cache, current, currentLevel,filter);
				}
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
				visitObject(value, cache, current, currentLevel,filter,new Metadata(i));
				current = current.last();
			}
		}
	}

	private void visitAllMap(Map<?, ?> values, Set<Object> cache, ChainedNode pair, int currentLevel, Filter filter) {
		ChainedNode current = pair;
		for (Object thisKey : values.keySet()) {
			Object value = values.get(thisKey);
			if (value != null) {
				visitObject(thisKey, cache, current, currentLevel , filter );
				current = current.last();
				visitObject(value, cache, current, currentLevel , filter , new Metadata(thisKey));
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
