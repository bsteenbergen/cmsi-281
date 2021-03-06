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

public class ForneymonBattlegroundTests {
    
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
    
    // Used as the basic empty Forneymonagerie to test; the @Before
    // method is run before every @Test
    Forneymonagerie fm1;
    @Before
    public void init () {
        possible++;
        fm1 = new Forneymonagerie();
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
    public void testEmpty_t0() {
        assertEquals(true, fm1.empty());
    }
    
    @Test
    public void testEmpty_t1() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        assertEquals(false, fm1.empty());
    }
    
    @Test
    public void testContainsType_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        assertTrue(fm1.containsType("Dampymon"));
        assertTrue(fm1.containsType("Burnymon"));
        assertTrue(!fm1.containsType("Zappymon"));
    }
    
    @Test
    public void testSize_t0() {
        assertEquals(0, fm1.size());
        fm1.collect(new Dampymon(1));
        assertEquals(1, fm1.size());
    }

    @Test
    public void testCheckAndGrow() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        fm1.collect(new Zappymon(1));
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
        Dampymon d1 = new Dampymon(1);
        Dampymon d2 = new Dampymon(2);
        fm1.collect(d1);
        fm1.collect(d1);
        assertTrue(fm1.containsType("Dampymon"));
        assertEquals(1, fm1.get(0).getLevel());
        fm1.collect(d2);
        assertEquals(3, fm1.get(0).getLevel());
        assertEquals(1, fm1.size());
    }
    
    @Test
    public void testCollect_t2() {
        Burnymon b1 = new Burnymon(1);
        Burnymon b2 = new Burnymon(2);
        fm1.collect(b1);
        assertTrue(fm1.containsType("Burnymon"));
        assertEquals(1, fm1.get(0).getLevel());
        fm1.collect(b2);
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
    public void testClone_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        Forneymonagerie dolly = fm1.clone();
        assertEquals(3, dolly.size());
        
        fm1.get(0).takeDamage(5, DamageType.BASIC);
        assertEquals(Dampymon.START_HEALTH - 5, fm1.get(0).getHealth());
        assertEquals(Dampymon.START_HEALTH, dolly.get(0).getHealth());
        
        fm1.rearrange("Leafymon", 0);
        assertEquals(0, fm1.getTypeIndex("Leafymon"));
        assertEquals(2, dolly.getTypeIndex("Leafymon"));
    }
    
    
    @Test
    public void testTrade_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
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
    public void testEquals_t0() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Dampymon(1));
        fm2.collect(new Burnymon(1));
        
        assertEquals(fm1, fm2);
        fm2.rearrange("Burnymon", 0);
        assertNotEquals(fm1, fm2);
    }
    
    @Test
    public void testArena_t0() {
        fm1.collect(new Dampymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Dampymon(1));

        ForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
    }
    @Test
    public void testArena_t1() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Burnymon(1));
        fm2.collect(new Dampymon(1));
        
        ForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
    }
    @Test
    public void testArena_t2() {
        fm1.collect(new Dampymon(3));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Leafymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Burnymon(3));
        fm2.collect(new Dampymon(1));
        fm2.collect(new Zappymon(1));
        
        ForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(1, fm2.size());
    }
    @Test
    public void testArena_t3() {
        fm1.collect(new Dampymon(1));
        fm1.collect(new Burnymon(1));
        fm1.collect(new Zappymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Burnymon(5));
        fm2.collect(new Dampymon(5));
        
        ForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(1, fm2.size());
    }
    @Test
    public void testArena_t4() {
        fm1.collect(new Dampymon(3));
        fm1.collect(new Wormymon(1));
        fm1.collect(new Leafymon(1));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Slimymon(3));
        fm2.collect(new Dampymon(1));
        fm2.collect(new Zappymon(1));
        
        ForneymonArena.fight(fm1, fm2);
        assertEquals(2, fm1.size());
        assertEquals(0, fm2.size());
    }
    @Test
    public void testArena_t5() {
        fm1.collect(new Slimymon(3));
        fm1.collect(new Wormymon(1));
        fm1.collect(new Leafymon(5));
        fm1.collect(new Burnymon(2));
        Forneymonagerie fm2 = new Forneymonagerie();
        fm2.collect(new Slimymon(3));
        fm2.collect(new Dampymon(1));
        fm2.collect(new Zappymon(9));
        
        ForneymonArena.fight(fm1, fm2);
        assertEquals(0, fm1.size());
        assertEquals(0, fm2.size());
    }
}
