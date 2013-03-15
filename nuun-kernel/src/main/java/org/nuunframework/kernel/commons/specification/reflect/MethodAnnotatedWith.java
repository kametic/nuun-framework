package org.nuunframework.kernel.commons.specification.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nuunframework.kernel.commons.specification.AbstractSpecification;

public class MethodAnnotatedWith extends AbstractSpecification<Method> {

	private Class<? extends Annotation> annotation;

	public MethodAnnotatedWith(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	@Override
	public boolean isSatisfiedBy(Method candidate) {

		return candidate != null && candidate.isAnnotationPresent(annotation);
	}

}
