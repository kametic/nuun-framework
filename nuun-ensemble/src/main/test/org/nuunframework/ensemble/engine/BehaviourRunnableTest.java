package org.nuunframework.ensemble.engine;

import static org.fest.assertions.Assertions.assertThat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.fest.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.nuunframework.ensemble.Ensemble;
import org.nuunframework.ensemble.Ensemble.DestinationRole;
import org.nuunframework.ensemble.Ensemble.SourceRole;
import org.nuunframework.ensemble.engine.Actor;
import org.nuunframework.ensemble.engine.EnsemblePlayer;
import org.nuunframework.ensemble.sample.Address;
import org.nuunframework.ensemble.sample.Person;
import org.nuunframework.ensemble.sample.PersonRepresentation;
import org.nuunframework.ensemble.util.Types;




public class BehaviourRunnableTest
{
    
    Ensemble<Person, PersonRepresentation> map;
    EnsemblePlayer underTest;
    Person épo;
    PersonRepresentation personRepresentation;
    
    
    @Before
    public void init() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
    {
        // The Map
        map = new Ensemble<Person, PersonRepresentation>()
        {
            @Override
            protected void play()
            {
             // @f:off
                d().fillSpouseName(s().getSpouse().getFirstname() , s().getSpouse().getLastname());
                d().fillFullname(s().getFirstname(), s().getLastname()); 
                d().fillFullAddress(
                     s().getAddress().getLine1  (),      
                     s().getAddress().getLine2  (),      
                     s().getAddress().getTown   (),      
                     s().getAddress().getCountry(),      
                    //s().getAddress().getCode()         
                    "97300"                              
                    ); 
                d().fillChildrenFirstName(s().getChildren());
                // @f:off
            }
        };

        // initialisation
        Method methodForConfigure = Types.methodFor(Ensemble.class, "configure"); 
        methodForConfigure.setAccessible(true);        
        methodForConfigure.invoke(map);
        
        // Actors 
        Address address = new Address("8 route de la madelein", "en face de la poste", "Cayenne", "Guyane Française", "97300");
        épo = new Person("epo", "jemba", address );
        Person jeanne = new Person("jeanne", "jemba", address );
        Person kwané = new Person("kwané", "jemba", address );
        Person térhé = new Person("térhé", "jemba", address );
        
        épo.setSpouse(jeanne);
        épo.setChildren(Collections.set(kwané , térhé));
        
        personRepresentation = new PersonRepresentation();
        Set<Actor> actors = Collections.set( new Actor(new Ensemble.DestinationRole(), personRepresentation) , new Actor(new Ensemble.SourceRole(), épo));

        // create the underTest
        underTest = new EnsemblePlayer(map.getRecording(), actors );
        System.err.println(map.getRecording());
    
    }
    
    @Test
    public void behaviourRunnable_should_work_normally ()
    {
        underTest.run();
        
        
        System.out.println(personRepresentation);
        
        assertThat(personRepresentation.getFullname()).isNotNull();
        assertThat(personRepresentation.getFullname()).isEqualTo("epo jemba");
        assertThat(personRepresentation.getSpousename()).isEqualTo("jeanne jemba");
        assertThat(personRepresentation.getFullAddress()).isEqualTo("8 route de la madelein\n" + 
        		"en face de la poste\n" + 
        		"Cayenne\n" + 
        		"Guyane Française\n" + 
        		"97300");
        
    }
    
    
    
}
