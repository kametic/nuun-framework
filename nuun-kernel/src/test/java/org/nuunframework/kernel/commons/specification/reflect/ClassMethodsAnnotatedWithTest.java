package org.nuunframework.kernel.commons.specification.reflect;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class ClassMethodsAnnotatedWithTest {

	private ClassMethodsAnnotatedWith underTest;


	@Retention(RetentionPolicy.RUNTIME)
	static @interface MyAnno
	{
		
	}
	static interface Z1 {}
	static class A1 {
		@MyAnno
		void inita1 () {}
	}
	
	static class B1 extends A1 implements Z1{
		void initb1 () {}
	}
	
	static class C1 extends B1{
		void initc1 () {}
	}

	////////////////////////////
	
	static interface A2 {
		@MyAnno
		void inita ();
	}
	
	static interface B2 extends A2 {
		void initb ();
	}
	
	static class C2 implements B2{
		public void initc () {}

		@Override
		public void inita() {}

		@Override
		public void initb() {}
	}
	
	
	@Before
	public void init ()
	{
		underTest = new ClassMethodsAnnotatedWith(MyAnno.class);
	}
	
	@Test
	public void ancestor_find_should_work_properly ()
	{
		assertThat(underTest.getAllInterfacesAndClasses(C2.class)).containsOnly(A2.class , B2.class , C2.class) ;
		
		assertThat(underTest.getAllInterfacesAndClasses(C1.class)).containsOnly(A1.class , B1.class , C1.class , Z1.class);
	}
	
	
	@Test
	public void specification_should_work_fine () 
	{
		for (Class<?> itf : C2.class.getInterfaces())
		{
			System.out.println(itf);
		}
		System.out.println("");
		for (Method m : C2.class.getMethods())
		{
			
			System.out.println(m + " " + m.isAnnotationPresent(MyAnno.class));
		}
		
		assertThat( underTest.isSatisfiedBy(B2.class)).isTrue();
		assertThat( underTest.isSatisfiedBy(A2.class)).isTrue();
		assertThat( underTest.isSatisfiedBy(C2.class)).isTrue();
	}

}
