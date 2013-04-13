package org.nuunframework.ensemble;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.nuunframework.ensemble.Ensemble;
import org.nuunframework.ensemble.sample.Person;
import org.nuunframework.ensemble.sample.PersonRepresentation;
import org.nuunframework.ensemble.util.Types;


public class EnsembleTest
{

    @Before
    public void init()
    {

    }

    @Test
    public void map_should_be_correct() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {

        // @f:off
        class holder { PersonRepresentation pr;  } // @f:on

        final holder holder = new holder();

        Ensemble<Person, PersonRepresentation> map = new Ensemble<Person, PersonRepresentation>()
        {
            @Override
            protected void play()
            {
             // @f:off
                d().fillFullname(s().getFirstname(), s().getLastname()); // + 3 calls                
                d().fillFullAddress(                      // + 1 call
                     s().getAddress().getLine1  (),      // + 2 calls
                     s().getAddress().getLine2  (),      // + 2 calls
                     s().getAddress().getTown   (),      // + 2 calls
                     s().getAddress().getCountry(),      // + 2 calls
                    //s().getAddress().getCode()         // -----------
                    "97300"                              // = 12 calls
                    ); 
                // @f:off
            }
        };

        Method methodForConfigure = Types.methodFor(Ensemble.class, "configure");        
        methodForConfigure.setAccessible(true);        
        methodForConfigure.invoke(map);

        System.err.println("" + map.getRecording() );
        
        
        assertThat(map.getRecording()).hasSize(12);
        
        // assertThat(holder.pr).isNotNull();
        // assertThat(holder.pr).isInstanceOf(PersonRepresentation.class);

        // holder.pr.getFullname();

    }

    @Test
    public void source_should_be_correct() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {

        // @f:off
        class holder { Person person; } // @f:on  
        final holder holder = new holder();

        Ensemble<Person, PersonRepresentation> map = new Ensemble<Person, PersonRepresentation>()
        {

            @Override
            protected void play()
            {
                holder.person = s();
            }
        };

        Method methodForConfigure = Types.methodFor(Ensemble.class, "play");
        methodForConfigure.setAccessible(true);
        methodForConfigure.invoke(map);

        assertThat(methodForConfigure).isNotNull();
        assertThat(holder.person).isNotNull();
        assertThat(holder.person).isInstanceOf(Person.class);

        // holder.person.getFirstname();

    }

    @Test
    public void destination_should_be_correct() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {

        // @f:off
        class holder { PersonRepresentation pr; } // @f:on  
        final holder holder = new holder();

        Ensemble<Person, PersonRepresentation> map = new Ensemble<Person, PersonRepresentation>()
        {

            @Override
            protected void play()
            {
                holder.pr = destination();
            }
        };

        Method methodForConfigure = Types.methodFor(Ensemble.class, "play");
        methodForConfigure.setAccessible(true);
        methodForConfigure.invoke(map);

        assertThat(methodForConfigure).isNotNull();
        assertThat(holder.pr).isNotNull();
        assertThat(holder.pr).isInstanceOf(PersonRepresentation.class);

        // holder.pr.getFullname();

    }

}
