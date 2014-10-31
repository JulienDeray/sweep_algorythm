package fr.emn.fil;

import fr.emn.fil.model.Constraint;
import fr.emn.fil.model.Domain;
import fr.emn.fil.model.ForbiddenRegion;
import fr.emn.fil.model.Position;
import org.hamcrest.CoreMatchers;
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
        Domain domain = new Domain();

        Constraint ri = new Constraint(3, 5);
        domain.addConstraint(ri);

        Constraint rj = new Constraint(6, 7, 7, 8, 3, 2);
        domain.addConstraint(rj);

        ForbiddenRegion forbiddenRegion = Domain.computeForbiddenRegion(rj, ri.getWidth(), ri.getHeight());

        Constraint expectedConstraint = new Constraint(5, 8, 4, 8, 3, 2);
        Assert.assertEquals(expectedConstraint.getxMax(), forbiddenRegion.getxMax());
        Assert.assertEquals(expectedConstraint.getxMin(), forbiddenRegion.getxMin());
        Assert.assertEquals(expectedConstraint.getyMax(), forbiddenRegion.getyMax());
        Assert.assertEquals(expectedConstraint.getyMin(), forbiddenRegion.getyMin());
    }

    @Test
    public void testFindMinimum() throws Exception {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(1, 4, 2, 4, 2, 1);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(4, 4, 6, 6, 3, 1);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(2, 4, 8, 9, 1, 1);
        domain.addConstraint(c3);

        Constraint c4 = new Constraint(7, 7, 1, 1, 1, 3);
        domain.addConstraint(c4);

        // constrainte à placer par rapport aux autres
        Constraint c5 = new Constraint(1, 8, 1, 8, 5, 4);
        Position domainMinimum = domain.findMinimum(c5);

        Assert.assertThat(domainMinimum, CoreMatchers.anyOf(CoreMatchers.is(new Position(3, 7)), CoreMatchers.is(new Position(3, 8))));
    }

    @Test
     public void testNonOverLapLeft() throws Exception {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 5, 2, 2, 4, 4);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(1, 1, 0, 0, 2, 1);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(2, 2, 2, 2, 1, 4);
        domain.addConstraint(c3);

        Constraint c4 = new Constraint(3, 3, 0, 0, 3, 2);
        domain.addConstraint(c4);

        Constraint c5 = new Constraint(4, 8, 2, 3, 2, 2);
        domain.addConstraint(c5);

        Constraint c6 = new Constraint(0, 9, 2, 5, 3, 1);
        domain.addConstraint(c6);

        domain.nonOverLapLeft();

        Assert.assertEquals(8, domain.getConstraints().get(domain.getConstraints().size() - 1).getxMin());
    }

    @Test
    public void testNonOverLapLeft2() throws Exception {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 3, 1, 3, 3, 3);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(4, 6, 0, 4, 3, 2);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 8, 0, 4, 2, 2);
        domain.addConstraint(c3);

        // On recalcule les bornes
        domain.nonOverLapLeft();

        Assert.assertEquals(3,domain.getConstraints().get(domain.getConstraints().size()-1).getxMin());
    }

    @Test
    public void testNonOverLapLeft3() throws Exception {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 5, 2, 2, 4, 4);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(1, 1, 0, 0, 2, 1);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(2, 2, 2, 2, 1, 4);
        domain.addConstraint(c3);

        Constraint c4 = new Constraint(3, 3, 0, 0, 3, 2);
        domain.addConstraint(c4);

        Constraint c5 = new Constraint(4, 8, 2, 3, 2, 2);
        domain.addConstraint(c5);

        Constraint c6 = new Constraint(0, 9, 2, 5, 3, 1);
        domain.addConstraint(c6);

        domain.nonOverLapLeft();

        //R1 : la borne xMin passe de 0 à 3
        Assert.assertEquals(3,domain.getConstraints().get(0).getxMin());
        //R5 : la borne xMin passe de 4 à 7
        Assert.assertEquals(7,domain.getConstraints().get(domain.getConstraints().size()-1).getxMin());
        //R6 : la borne xMin passe de 0 à 7
        Assert.assertEquals(7,domain.getConstraints().get(domain.getConstraints().size()-2).getxMin());
    }
} 
