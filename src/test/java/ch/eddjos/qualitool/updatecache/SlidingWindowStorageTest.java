package ch.eddjos.qualitool.updatecache;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SlidingWindowStorageTest extends TestCase {

    @Test
    public void basicTest(){
        SlidingWindowStorage<Integer> store = new SlidingWindowStorage<>(Integer.class, 5);
        store.put(1);
        System.out.println(store.get()[0]);
        assertEquals(1, store.get().length);
        assertEquals(1, store.get()[0].intValue());
        store.put(2);
        assertEquals(2, store.get().length);
        assertEquals(1, store.get()[0].intValue());
        assertEquals(2, store.get()[1].intValue());
        store.put(3);
        assertEquals(3, store.get().length);
        assertEquals(1, store.get()[0].intValue());
        assertEquals(2, store.get()[1].intValue());
        assertEquals(3, store.get()[2].intValue());
        store.put(4);
        assertEquals(4, store.get().length);
        assertEquals(1, store.get()[0].intValue());
        assertEquals(2, store.get()[1].intValue());
        assertEquals(3, store.get()[2].intValue());
        assertEquals(4, store.get()[3].intValue());
        store.put(5);
        assertEquals(5, store.get().length);
        assertEquals(1, store.get()[0].intValue());
        assertEquals(2, store.get()[1].intValue());
        assertEquals(3, store.get()[2].intValue());
        assertEquals(4, store.get()[3].intValue());
        assertEquals(5, store.get()[4].intValue());

        store.put(6);
        assertEquals(5, store.get().length);
        assertEquals(2, store.get()[0].intValue());
        assertEquals(3, store.get()[1].intValue());
        assertEquals(4, store.get()[2].intValue());
        assertEquals(5, store.get()[3].intValue());
        assertEquals(6, store.get()[4].intValue());

        store.put(7);
        assertEquals(5, store.get().length);
        assertEquals(3, store.get()[0].intValue());
        assertEquals(4, store.get()[1].intValue());
        assertEquals(5, store.get()[2].intValue());
        assertEquals(6, store.get()[3].intValue());
        assertEquals(7, store.get()[4].intValue());

        store.put(8);
        store.put(9);
        store.put(10);
        store.put(11);
        assertEquals(5, store.get().length);
        assertEquals(7, store.get()[0].intValue());
        assertEquals(8, store.get()[1].intValue());
        assertEquals(9, store.get()[2].intValue());
        assertEquals(10, store.get()[3].intValue());
        assertEquals(11, store.get()[4].intValue());


    }



}