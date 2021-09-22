package ch.eddjos.qualitool.updatecache;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UpdateCacheTest extends TestCase {

    @Test
    public void test(){
        UpdateCache<String> uc=new UpdateCache<>(10);
        uc.update(1,1,"X");
        assertEquals(0,uc.getUpdates(-1,1).getVersionNr());
        assertTrue(uc.getUpdates(-1,1).getData().contains("X"));
        assertEquals(1,uc.getUpdates(-1,1).getData().size());
        uc.update(1,2,"B");
        uc.update(1,3,"C");
        uc.update(2,1,"D");
        uc.update(1,1,"A");
        assertEquals(4,uc.getUpdates(-1,1).getVersionNr());
        assertTrue(uc.getUpdates(-1,1).getData().contains("A"));
        assertTrue(uc.getUpdates(-1,1).getData().contains("B"));
        assertTrue(uc.getUpdates(-1,1).getData().contains("C"));
        assertEquals(3,uc.getUpdates(-1,1).getData().size());

        assertEquals(4,uc.getUpdates(-1,2).getVersionNr());
        assertTrue(uc.getUpdates(-1,2).getData().contains("D"));
        assertEquals(1,uc.getUpdates(-1,2).getData().size());

        assertEquals(4,uc.getUpdates(-1,3).getVersionNr());
        assertEquals(0,uc.getUpdates(-1,3).getData().size());

        assertNull(uc.getUpdates(-100,3));

        uc.update(1,2,"B");
        uc.update(1,3,"C");
        uc.update(2,1,"D");
        uc.update(1,1,"A");
        uc.update(1,2,"B");
        uc.update(1,3,"C");
        uc.update(2,1,"D");
        uc.update(1,1,"A");
        assertNull(uc.getUpdates(-1,1));

    }

}