package fr.emn.fil;

import fr.emn.fil.model.Constraint;
import fr.emn.fil.model.Domain;
import fr.emn.fil.model.ForbiddenRegion;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

public class MainTest { 

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testComputeForbiddenRegion() throws Exception {
        Domain domain = new Domain(10, 10);

        Constraint ri = new Constraint(3, 5);
        domain.add(ri);

        Constraint rj = new Constraint(6, 7, 7, 8, 3, 2);
        domain.add(rj);

        ForbiddenRegion forbiddenRegion = Domain.computeForbiddenRegion(rj, ri.getWidth(), ri.getHeight());

        Constraint expectedConstraint = new Constraint(5, 8, 4, 8, 3, 2);
        Assert.assertEquals(forbiddenRegion.getxMax(), expectedConstraint.getxMax());
        Assert.assertEquals(forbiddenRegion.getxMin(), expectedConstraint.getxMin());
        Assert.assertEquals(forbiddenRegion.getyMax(), expectedConstraint.getyMax());
        Assert.assertEquals(forbiddenRegion.getyMin(), expectedConstraint.getyMin());
    }

    @Test
    public void testInitDomain() throws Exception {
        Domain domain = new Domain(10, 10);

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 5, 2, 2, 4, 4);
        domain.add(c1);

        Constraint c2 = new Constraint(1, 1, 0, 0, 2, 1);
        domain.add(c2);

        Constraint c3 = new Constraint(2, 2, 2, 2, 1, 4);
        domain.add(c3);

        Constraint c4 = new Constraint(3, 3, 0, 0, 3, 2);
        domain.add(c4);

        Constraint c5 = new Constraint(4, 8, 2, 3, 2, 2);
        domain.add(c5);

        // constrainte à placer par rapport aux autres
        Constraint c6 = new Constraint(0, 9, 2, 5, 3, 1);
        domain.addCleverly(c6);

//        Assert.assertArrayEquals(c6.getPosition(), new Placement(7, 2));
    }
} 
