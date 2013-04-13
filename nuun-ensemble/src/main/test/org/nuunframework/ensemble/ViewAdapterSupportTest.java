package org.nuunframework.ensemble;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;
import org.nuunframework.ensemble.Conductor;
import org.nuunframework.ensemble.Conductor;
import org.nuunframework.ensemble.Ensemble;


public class ViewAdapterSupportTest
{

    private static Conductor conductor;

    @BeforeClass
    public static void init()
    {
        conductor = new Conductor();

        conductor.addEnsemble(new Ensemble<Person, PersonRepresentation>()
        {

            @Override
            protected void play()
            {
                d().setTheName1(s().getName());
                d().setTheLine1(s().getAddress().getLine1());
                d().setTheLine2(s().getAddress().getLine2());
                d().setTheLine3(s().getAddress().getLine3());
            }
        });

        conductor.addEnsemble(new Ensemble<PersonRepresentation, Person>()
        {

            @Override
            protected void play()
            {
                d().setName(s().getTheName1());
                d().getAddress().setLine1( s().getTheLine1());
                // d().getAddress().setLine2(s().getTheLine2());
                // d().getAddress().setLine3(s().getTheLine3());
            }
        });

    }

//    // @Test
//    public void assembleViewAdapter_shouldwork()
//    {
//        Conductor underTest = new Conductor();
//
//        Person person = new Person("epo", "10 route de la madeleine", "cayenne", "97300");
//
//        BeanViewAdapter bva = underTest.<BeanViewAdapter> assembleViewAdapter(person, BeanViewAdapter.class);
//
//        assertThat(bva.getName()).isEqualTo("epo");
//
//        assertThat(bva.theline1()).isEqualTo("10 route de la madeleine");
//
//        assertThat(bva.theline3()).isEqualTo("97300");
//
//    }

    // @Test
    public void testModelMapper_way1()
    {

        Person person = new Person("epo", "10 route de la madeleine", "cayenne", "97300");

        PersonRepresentation personRepresentation = conductor.conduct(person, PersonRepresentation.class);

        assertThat(personRepresentation.getTheName1()).isEqualTo("epo");
        assertThat(personRepresentation.getTheLine1()).isEqualTo("10 route de la madeleine");
        assertThat(personRepresentation.getTheLine3()).isEqualTo("97300");

    }

    @Test
    public void testModelMapper_way2()
    {

        PersonRepresentation personRepresentation = new PersonRepresentation();
        personRepresentation.setTheLine1("line1");
        // personRepresentation.setTheLine2("line2");
        // personRepresentation.setTheLine2("line3");
        personRepresentation.setTheName1("name1");

        Person person = conductor.conduct(personRepresentation, Person.class);

        assertThat(person.getName()).isEqualTo("name1");
        assertThat(person.getAddress().getLine1()).isEqualTo("line1");
        // assertThat(personRepresentation.getTheLine3()).isEqualTo("97300");

    }

    public static class Person
    {

        private String  name;
        private Address address;

        public Person()
        {
            address = new Address();
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setAddress(Address address)
        {
            this.address = address;
        }

        public Person(String name, String line1, String line2, String line3)
        {
            this.name = name;
            this.address = new Address(line1, line2, line3);
        }

        public String getName()
        {
            return name;
        }

        public Address getAddress()
        {
            return this.address;
        }
    }

    public static class Address
    {

        private String line1;
        private String line2;
        private String line3;

        public Address()
        {
        }

        public Address(String line1, String line2, String line3)
        {
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
        }

        public String getLine1()
        {
            return this.line1;
        }

        public String getLine2()
        {
            return this.line2;
        }

        public String getLine3()
        {
            return this.line3;
        }

        public void setLine1(String line1)
        {
            this.line1 = line1;
        }

        public void setLine2(String line2)
        {
            this.line2 = line2;
        }

        public void setLine3(String line3)
        {
            this.line3 = line3;
        }

    }

    public static class PersonRepresentation
    {

        private String theName1;
        private String theName2;
        private String theLine1;
        private String theLine2;
        private String theLine3;

        public String getTheName1()
        {
            return theName1;
        }

        public void setTheName1(String theName1)
        {
            this.theName1 = theName1;
        }

        public String getTheName2()
        {
            return theName2;
        }

        public void setTheName2(String theName2)
        {
            this.theName2 = theName2;
        }

        public String getTheLine1()
        {
            return theLine1;
        }

        public void setTheLine1(String theLine1)
        {
            this.theLine1 = theLine1;
        }

        public String getTheLine2()
        {
            return theLine2;
        }

        public void setTheLine2(String theLine2)
        {
            this.theLine2 = theLine2;
        }

        public String getTheLine3()
        {
            return theLine3;
        }

        public void setTheLine3(String theLine3)
        {
            this.theLine3 = theLine3;
        }

    }

//    public static interface BeanViewAdapter
//    {
//
//        @inner("name")
//        public String getName();
//
//        @inner("name")
//        public String thename2();
//
//        @inner("address.line1")
//        public String theline1();
//
//        @inner("address.line3")
//        public String theline3();
//
//    }
//
//    public static interface Struct
//    {
//
//        @inner("name")
//        public BeanViewAdapter toto();
//
//    }

}
