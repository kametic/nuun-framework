package org.nuunframework.ensemble;

import static org.fest.assertions.Assertions.assertThat;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.nuunframework.ensemble.api.Soloist;
import org.nuunframework.ensemble.engine.TypeTuple;

public class ConductorTest
{

    Conductor conductorUt;

    @Before
    public void init()
    {
        conductorUt = new Conductor();
    }

    @Test
    public void testAddEnsemble()
    {
        conductorUt.addEnsemble(new Swap1());
        assertThat(conductorUt.tuples).hasSize(1);

        Class<?> c[] = new Class[] {
            Swappable.class
        };

        TypeTuple key = TypeTuple.of(c).build();
        Assertions.assertThat(key).isNotNull();

        Ensemble ensemble = conductorUt.tuples.get(key);

        assertThat(ensemble).isNotNull();
        assertThat(ensemble).isInstanceOf(Swap1.class);
    }

    @Test
    public void testConduct()
    {
        conductorUt.addEnsemble(new Swap1());

        Swappable swap1 = new Swappable();
        swap1.setA("a");
        swap1.setB("b");
        swap1.setTmp("not null");
        conductorUt.conduct(swap1);
        
        assertThat(swap1.getA()).isEqualTo("b");
        assertThat(swap1.getB()).isEqualTo("a");
        assertThat(swap1.getTmp()).isEqualTo(null);
    }

    public static abstract class Swap<R> extends Soloist<R>
    {

        protected R swaper()
        {
            return actor1();
        }

    }

    public static class Swap1 extends Swap<Swappable>
    {
        @Override
        protected void play()
        {
//            swaper().setTmp($("titi"));
            swaper().setTmp(swaper().getA());
            swaper().setA(swaper().getB());
            swaper().setB(swaper().getTmp());
            swaper().setTmp($("toto"));
        }
    }

    public static class Swappable
    {
        protected String a;
        protected String b;
        protected String tmp;

        public String getA()
        {
            return a;
        }

        public void setA(String a)
        {
            this.a = a;
        }

        public String getB()
        {
            return b;
        }

        public void setB(String b)
        {
            this.b = b;
        }

        public String getTmp()
        {
            return tmp;
        }

        public void setTmp(String tmp)
        {
            this.tmp = tmp;
        }

    }

}
