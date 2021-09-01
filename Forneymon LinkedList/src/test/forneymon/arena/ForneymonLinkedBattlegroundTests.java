package test.forneymon.arena;

import main.forneymon.arena.*;
import main.forneymon.fmtypes.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.Description;

import static org.junit.Assert.*;

import org.junit.AfterClass;

    /**
     * @author Brittany Steenbergen
     */
public class ForneymonLinkedBattlegroundTests {
    
    // =================================================
    // Test Configuration
    // =================================================
    
    // Global timeout to prevent infinite loops from
    // crashing the test suite
    // [!] Comment out the next 2 lines if you're using
    // the debugger!
    @Rule
    public Timeout globalTimeout = Timeout.seconds(1);
    
    // Grade record-keeping
    static int possible = 0, passed = 0;

    // Each time you pass a test, you get a point! Yay!
    // [!] Requires JUnit 4+ to run
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            passed++;
        }
    };
    
    // Used as the basic empty LinkedForneymonagerie to test; the @Before
    // method is run before every @Test
    LinkedForneymonagerie fm1;
    @Before
    public void init () {
        possible++;
        fm1 = new LinkedForneymonagerie();
    }
    
    // Used for grading, reports the total number of tests
    // passed over the total possible
    @AfterClass
    public static void gradeReport () {
        System.out.println("============================");
        System.out.println("Tests Complete");
        System.out.println(passed + " / " + possible + " passed!");
        if ((1.0 * passed / possible) >= 0.9) {
            System.out.println("[!] Nice job!"); // Automated acclaim!
        }
        System.out.println("============================");
    }
    
    
    // =================================================
    // Unit Tests
    // =================================================
    
    @Test
    public void testSize_t0() {
        assertEquals(0, fm1.size());
        fm1.collect(new Dampymon(1));
        assertEquals(1, fm1.size());
    }
    @Test
    public void testSize_t1() {
        assertEquals(0, fm1.size());
        fm1.collect(new Wormymon(1));
        fm1.collect(new Slimymon(1));
        assertEquals(2, fm1.size());
    }
    @Test
    public void testSize_t2() {
        assertEquals(0, fm1.size());
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(2));
        fm1.collect(new Leafymon(2));
        fm1.collect(new Zappymon(2));
        fm1.collect(new Wormymon(1));
        assertEquals(5, fm1.size());
    }
    
    @Test
    public void testCollect_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        assertTrue(fm1.containsType("Dampymon"));
        assertTrue(fm1.containsType("Burnymon"));
        assertTrue(!fm1.containsType("Zappymon"));
        assertEquals(2, fm1.size());
        assertEquals("Burnymon", fm1.get(1).getFMType());
    }
    
    @Test
    public void testCollect_t1() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        assertEquals(2, fm1.size());
        assertEquals(true, !fm1.empty());
    }
    
    @Test
    public void testCollect_t2() {
        boolean c1 = fm1.collect(new Dampymon(1)),
                c2 = fm1.collect(new Burnymon(2)),
                c3 = fm1.collect(new Zappymon(3)),
                c4 = fm1.collect(new Burnymon(2));
        assertEquals(0, fm1.getTypeIndex("Dampymon"));
        assertEquals(1, fm1.getTypeIndex("Burnymon"));
        assertEquals(2, fm1.getTypeIndex("Zappymon"));
        assertEquals(4, fm1.get(1).getLevel());
        assertTrue(c1);
        assertTrue(c2);
        assertTrue(c3);
        assertFalse(c4);
    }
    @Test
    public void testCollect_t3() {
        Dampymon d1 = new Dampymon(1);
        Dampymon d2 = new Dampymon(2);
        fm1.collect(d1);
        assertTrue(fm1.containsType("Dampymon"));
        fm1.collect(d2);
        assertEquals(3, fm1.get(0).getLevel());
        fm1.collect(d1);
        assertEquals(3, fm1.get(0).getLevel());
        assertEquals(1, fm1.size());
    }
    
    @Test
    public void testReleaseType_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        assertEquals(2, fm1.size());
        fm1.releaseType("Dampymon");
        assertEquals(1, fm1.size());
        assertTrue(fm1.containsType("Burnymon"));
        assertTrue(!fm1.containsType("Dampymon"));
    }
    @Test
    public void testReleaseType_t1() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(2));
        fm1.collect(new Leafymon(3));
        boolean rt1 = fm1.releaseType("Burnymon");
        assertEquals(2, fm1.size());
        assertEquals("Dampymon", fm1.get(0).getFMType());
        assertEquals("Leafymon", fm1.get(1).getFMType());
        assertTrue(!fm1.containsType("Burnymon"));
        boolean rt2 = fm1.releaseType("Burnymon");
        assertTrue(rt1);
        assertFalse(rt2);
    }
    
    @Test
    public void testGet_t0() {
        Dampymon d1 = new Dampymon(1);
        Burnymon b1 = new Burnymon(1);
        fm1.collect(d1);
        fm1.collect(b1);
        assertEquals(d1, fm1.get(0));
        assertEquals(b1, fm1.get(1));
    }
    
    @Test
    public void testRemove_t0() {
        Dampymon d1 = new Dampymon(1);
        Burnymon b1 = new Burnymon(1);
        fm1.collect(d1);
        fm1.collect(b1);
        assertEquals(2, fm1.size());
        fm1.remove(0);
        assertEquals(1, fm1.size());
        assertEquals(b1, fm1.get(0));
    }
    
    @Test
    public void testGetTypeIndexContainsType_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        assertEquals(0, fm1.getTypeIndex("Dampymon"));
        assertEquals(1, fm1.getTypeIndex("Burnymon"));
        assertEquals(-1, fm1.getTypeIndex("Leafymon"));
        assertTrue(fm1.containsType("Dampymon"));
        assertFalse(fm1.containsType("Zappymon"));
    }
    @Test
    public void testGetTypeIndexContainsType_t1() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(2));
        fm1.collect(new Leafymon(3));
        fm1.collect(new Zappymon(2));
        fm1.collect(new Slimymon(1));
        fm1.collect(new Wormymon(1));
        assertEquals(0, fm1.getTypeIndex("Dampymon"));
        assertEquals(1, fm1.getTypeIndex("Burnymon"));
        assertEquals(2, fm1.getTypeIndex("Leafymon"));
        assertEquals(3, fm1.getTypeIndex("Zappymon"));
        assertEquals(4, fm1.getTypeIndex("Slimymon"));
        assertEquals(5, fm1.getTypeIndex("Wormymon"));
        assertEquals(-1, fm1.getTypeIndex("Blahmymon"));
        assertTrue(fm1.containsType("Dampymon"));
        assertTrue(fm1.containsType("Wormymon"));
        assertFalse(fm1.containsType("TestForneymon5"));
    }
    
    @Test
    public void testRearrange_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        fm1.rearrange("Leafymon", 0);
        assertEquals(1, fm1.getTypeIndex("Dampymon"));
        assertEquals(2, fm1.getTypeIndex("Burnymon"));
        assertEquals(0, fm1.getTypeIndex("Leafymon"));
    }
    @Test
    public void testRearrange_t1() {
        Dampymon d1 = new Dampymon(1);
        Burnymon b1 = new Burnymon(1);
        Leafymon e1 = new Leafymon(1);
        fm1.collect(d1);
        fm1.collect(b1);
        fm1.collect(e1);
        fm1.rearrange("Leafymon", 0);
        fm1.collect(new Burnymon(1));
        fm1.collect(new Dampymon(1));
        fm1.collect(d1);
        fm1.collect(b1);
        fm1.collect(e1);
        assertEquals(1, fm1.getTypeIndex("Dampymon"));
        assertEquals(2, fm1.getTypeIndex("Burnymon"));
        assertEquals(0, fm1.getTypeIndex("Leafymon"));
        assertEquals(e1, fm1.get(0));
        assertEquals(d1, fm1.get(1));
        assertEquals(b1, fm1.get(2));
    }
    @Test
    public void testRearrange_t2() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        fm1.collect(new Zappymon(1));
        fm1.rearrange("Dampymon", 0);
        fm1.rearrange("Zappymon", 3);
        assertEquals(0, fm1.getTypeIndex("Dampymon"));
        assertEquals(1, fm1.getTypeIndex("Burnymon"));
        assertEquals(2, fm1.getTypeIndex("Leafymon"));
        assertEquals(3, fm1.getTypeIndex("Zappymon"));
    }
    
    @Test
    public void testClone_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        LinkedForneymonagerie dolly = fm1.clone();
        assertEquals(3, dolly.size());
        
        fm1.get(0).takeDamage(5, DamageType.BASIC);
        assertEquals(Dampymon.START_HEALTH - 5, fm1.get(0).getHealth());
        assertEquals(Dampymon.START_HEALTH, dolly.get(0).getHealth());
        
        fm1.rearrange("Leafymon", 0);
        assertEquals(0, fm1.getTypeIndex("Leafymon"));
        assertEquals(2, dolly.getTypeIndex("Leafymon"));
    }
    @Test
    public void testClone_t1() {
        Dampymon d1 = new Dampymon(1);
        fm1.collect(d1);
        fm1.collect(new Burnymon(2));
        fm1.collect(new Leafymon(3));
        fm1.collect(new Zappymon(2));
        fm1.collect(new Wormymon(1));
        fm1.collect(new Slimymon(1));
        LinkedForneymonagerie dolly = fm1.clone();
        dolly.collect(d1);
        fm1.collect(d1);
        assertEquals(6, dolly.size());
        assertEquals(1, fm1.get(0).getLevel());
        assertEquals(2, dolly.get(0).getLevel());
    }
    
    @Test
    public void testTrade_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Leafymon(1));
        fm1.trade(fm2);
        
        assertEquals(2, fm2.size());
        assertEquals(1, fm1.size());
        assertTrue(fm1.containsType("Leafymon"));
        assertTrue(!fm1.containsType("Dampymon"));
        assertTrue(fm2.containsType("Dampymon"));
        assertTrue(!fm2.containsType("Leafymon"));
    }
    @Test
    public void testTrade_t1() {
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        LinkedForneymonagerie fm3 = new LinkedForneymonagerie();
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(2));
        fm1.collect(new Leafymon(3));
        fm1.collect(new Zappymon(2));
        fm1.collect(new Wormymon(1));
        fm1.collect(new Slimymon(1));
        fm1.trade(fm2);
        fm2.trade(fm3);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
        assertEquals(6, fm3.size());
    }
    
    @Test
    public void testEquals_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Dampymon(1));
        fm2.collect(new Burnymon(1));
        
        assertEquals(fm1, fm2);
        fm2.rearrange("Burnymon", 0);
        assertNotEquals(fm1, fm2);
    }
    
    @Test
    public void testArena_t0() {
        fm1.collect(new Dampymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Dampymon(1));
        
        LinkedForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
    }
    @Test
    public void testArena_t1() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Burnymon(1));
        fm2.collect(new Dampymon(1));
        
        LinkedForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
    }
    @Test
    public void testArena_t2() {
        fm1.collect(new Dampymon(3));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Burnymon(3));
        fm2.collect(new Dampymon(1));
        fm2.collect(new Zappymon(1));
        
        LinkedForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(1, fm2.size());
    }
    @Test
    public void testArena_t3() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Zappymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Burnymon(5));
        fm2.collect(new Dampymon(5));
        
        LinkedForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(1, fm2.size());
    }
    @Test
    public void testArena_t4() {
        fm1.collect(new Dampymon(3));
        fm1.collect(new Wormymon(1));
        fm1.collect(new Leafymon(1));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Slimymon(3));
        fm2.collect(new Dampymon(1));
        fm2.collect(new Zappymon(1));
        
        LinkedForneymonArena.fight(fm1, fm2);
        assertEquals(2, fm1.size());
        assertEquals(0, fm2.size());
    }
    @Test
    public void testArena_t5() {
        fm1.collect(new Slimymon(3));
        fm1.collect(new Wormymon(1));
        fm1.collect(new Leafymon(5));
        fm1.collect(new Burnymon(2));
        LinkedForneymonagerie fm2 = new LinkedForneymonagerie();
        fm2.collect(new Slimymon(3));
        fm2.collect(new Dampymon(1));
        fm2.collect(new Zappymon(9));
        
        LinkedForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
    }
    
    @Test
    public void testIterator_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        LinkedForneymonagerie.Iterator it = fm1.getIterator();
        assertTrue(it.isValid());
        assertTrue(it.atStart());
        assertEquals("Dampymon", it.getCurrent().getFMType());
        it.next();
        assertEquals("Burnymon", it.getCurrent().getFMType());
        it.next();
        assertEquals("Leafymon", it.getCurrent().getFMType());
        assertTrue(it.atEnd());
        it.next();
        assertEquals("Dampymon", it.getCurrent().getFMType());
        it.prev();
        assertEquals("Leafymon", it.getCurrent().getFMType());
        it.prev();
        assertEquals("Burnymon", it.getCurrent().getFMType());
        it.prev();
        assertEquals("Dampymon", it.getCurrent().getFMType());
        fm1.remove(0);
        assertFalse(it.isValid());
    }
    @Test
    public void testIterator_t1() {
       fm1.collect(new Dampymon(1));
       fm1.collect(new Burnymon(1));
       fm1.collect(new Leafymon(1));
       LinkedForneymonagerie.Iterator it = fm1.getIterator();
       it.next();
       assertEquals("Burnymon", it.getCurrent().getFMType());
       it.removeCurrent();
       assertEquals("Dampymon", it.getCurrent().getFMType());
       assertEquals(2, fm1.size());
       assertFalse(fm1.containsType("Burnymon"));
    }
    @Test
    public void testIterator_t2() {
       fm1.collect(new Dampymon(1));
       fm1.collect(new Burnymon(1));
       fm1.collect(new Leafymon(1));
       LinkedForneymonagerie.Iterator it = fm1.getIterator();
       it.next();
       assertEquals("Burnymon", it.getCurrent().getFMType());
       fm1.remove(1);
       
       try {
           it.next();
           fail();
       } catch (Exception e) {
           if (! (e instanceof IllegalStateException)) {
               fail();
           }
       }
    }
    
}
