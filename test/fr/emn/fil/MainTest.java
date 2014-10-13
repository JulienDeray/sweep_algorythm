package fr.emn.fil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

/** 
* Main Tester. 
* 
* @author <Authors name> 
* @since <pre>oct. 13, 2014</pre> 
* @version 1.0 
*/ 
public class MainTest { 

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public static void testComputeForbiddenRegion() throws Exception {
        PlacementRegion ri = new PlacementRegion(3, 5);
        PlacementRegion rj = new PlacementRegion(6, 7, 7, 8, 3, 2);

        PlacementRegion forbiddenRegion = Algorythm.computeForbiddenRegion(rj, ri);

        Assert.assertEquals(forbiddenRegion, new PlacementRegion(5, 8, 4, 8, 3, 2));
    }
} 
