package org.nuunframework.ensemble.util;

import static org.junit.Assert.assertNull;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;
import org.nuunframework.ensemble.util.TypeResolver;

/**
 * @author Jonathan Halterman
 */

@SuppressWarnings("serial")
public class TypeResolverTest
{

    static class RepoImplA<A1, A2 extends Map<?, ?>> extends RepoImplB<A2, ArrayList<?>>
    {
    }

    static class RepoImplB<B1 extends Map<?, ?>, B2 extends List<?>> extends RepoImplC<B2, HashSet<?>, B1>
    {
    }

    static class RepoImplC<C1 extends List<?>, C2 extends Set<?>, C3 extends Map<?, ?>> implements IRepo<C3, C1, Vector<?>, C2>
    {
    }

    interface IRepo<I1, I2, I3, I4> extends Serializable, IIRepo<I1, I3>
    {
    }

    interface IIRepo<II1, II2>
    {
    }

    static class NonParameterizedType
    {
    }

    static class Foo extends Bar<ArrayList<?>>
    {
    }

    static class Bar<B extends List<?>> implements Baz<HashSet<?>, B>
    {
    }

    interface Baz<C1 extends Set<?>, C2 extends List<?>>
    {
    }

    static class SimpleRepo implements IIRepo<String, List<?>>
    {
    }

    static class Entity<ID extends Serializable, T, D, E>
    {

        ID id;

        void setId(List<ID> id)
        {
        }
    }

    static class SomeList extends ArrayList<Integer>
    {
    }

    static class SomeEntity extends Entity<Long, Object, Object, Object>
    {
    }

    @Test
    public void shouldResolveClass() throws Exception
    {
        Field field = Entity.class.getDeclaredField("id");
        Assert.assertEquals(TypeResolver.resolveClass(field.getGenericType(), SomeEntity.class), Long.class);
        
    }

    @Test
    public void shouldResolveArgumentForGenericType() throws Exception
    {
        Method mutator = Entity.class.getDeclaredMethod("setId", List.class);
        Assert.assertEquals(TypeResolver.resolveArgument(mutator.getGenericParameterTypes()[0], SomeEntity.class), Long.class);
        Assert.assertEquals(TypeResolver.resolveArguments(SomeEntity.class, Entity.class)[0] , Long.class);
        Assert.assertEquals(TypeResolver.resolveArguments(SomeEntity.class, Entity.class)[1] , Object.class);
        Assert.assertEquals(TypeResolver.resolveArguments(SomeEntity.class, Entity.class)[2] , Object.class);
        Assert.assertEquals(TypeResolver.resolveArguments(SomeEntity.class, Entity.class)[3] , Object.class);
    }

    @Test
    public void shouldResolveArgumentForList()
    {
        Assert.assertEquals(TypeResolver.resolveArgument(SomeList.class, List.class), Integer.class);
    }

    @Test
    public void shouldResolveArgumentsForBazFromFoo()
    {
        Class<?>[] typeArguments = TypeResolver.resolveArguments(Foo.class, Baz.class);
        assert typeArguments[0] == HashSet.class;
        assert typeArguments[1] == ArrayList.class;
    }

    @Test
    public void shouldResolveArgumentsForNonParameterizedType()
    {
        assertNull(TypeResolver.resolveArguments(NonParameterizedType.class, NonParameterizedType.class));
    }

    @Test
    public void shouldResolveArgumentsForIRepoFromRepoImplA()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplA.class, IRepo.class);
        Assert.assertEquals(types[0], Map.class);
        Assert.assertEquals(types[1], ArrayList.class);
        Assert.assertEquals(types[2], Vector.class);
        Assert.assertEquals(types[3], HashSet.class);
    }

    @Test
    public void shouldResolveArgumentsForRepoImplCFromRepoImplA()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplA.class, RepoImplC.class);
        Assert.assertEquals(types[0], ArrayList.class);
        Assert.assertEquals(types[1], HashSet.class);
        Assert.assertEquals(types[2], Map.class);
    }

    @Test
    public void shouldResolveArgumentsForRepoImplCFromRepoImplB()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplB.class, RepoImplC.class);
        Assert.assertEquals(types[0], Object.class);
        Assert.assertEquals(types[1], HashSet.class);
        Assert.assertEquals(types[2], Object.class);
    }

    @Test
    public void shouldResolveArgumentsForIRepoFromRepoImplB()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplB.class, IRepo.class);
        Assert.assertEquals(types[0], Map.class);
        Assert.assertEquals(types[1], List.class);
        Assert.assertEquals(types[2], Vector.class);
        Assert.assertEquals(types[3], HashSet.class);
    }

    @Test
    public void shouldResolveArgumentsForIRepoFromRepoImplC()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplC.class, IRepo.class);
        Assert.assertEquals(types[0], Object.class);
        Assert.assertEquals(types[1], Object.class);
        Assert.assertEquals(types[2], Vector.class);
        Assert.assertEquals(types[3], Object.class);
    }

    @Test
    public void shouldResolveArgumentsForIIRepoFromRepoImplA()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplA.class, IIRepo.class);
        Assert.assertEquals(types[0], Map.class);
        Assert.assertEquals(types[1], Vector.class);
    }

    @Test
    public void shouldResolveArgumentsForRepoImplBFromRepoImplA()
    {
        Class<?>[] types = TypeResolver.resolveArguments(RepoImplA.class, RepoImplB.class);
        Assert.assertEquals(types[0], Object.class);
        Assert.assertEquals(types[1], ArrayList.class);
    }

    @Test
    public void shouldResolveArgumentsForSimpleType()
    {
        Class<?>[] args = TypeResolver.resolveArguments(SimpleRepo.class, IIRepo.class);
        Assert.assertEquals(args[0], String.class);
        Assert.assertEquals(args[1], List.class);
    }
}
