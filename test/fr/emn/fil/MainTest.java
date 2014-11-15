package fr.emn.fil;

import fr.emn.fil.algo.SweepAlgorithme;
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
    public void testComputeForbiddenRegion() {
        Domain domain = new Domain();

        Constraint ri = new Constraint(3, 5);
        domain.addConstraint(ri);

        Constraint rj = new Constraint(6, 7, 7, 8, 3, 2);
        domain.addConstraint(rj);

        ForbiddenRegion forbiddenRegion = SweepAlgorithme.computeForbiddenRegion(rj, ri.getWidth(), ri.getHeight());

        Constraint expectedConstraint = new Constraint(5, 8, 4, 8, 3, 2);
        Assert.assertEquals(expectedConstraint.getxMax(), forbiddenRegion.getxMax());
        Assert.assertEquals(expectedConstraint.getxMin(), forbiddenRegion.getxMin());
        Assert.assertEquals(expectedConstraint.getyMax(), forbiddenRegion.getyMax());
        Assert.assertEquals(expectedConstraint.getyMin(), forbiddenRegion.getyMin());
    }

    @Test
    public void testFindMinimum() {
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
        Position domainMinimum = SweepAlgorithme.findMinimum(c5, domain.getConstraints(), true);

        Assert.assertThat(domainMinimum, CoreMatchers.anyOf(CoreMatchers.is(new Position(3, 7)), CoreMatchers.is(new Position(3, 8))));
    }

    @Test
    public void testNonOverLapLeft1() {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 1, 1, 3, 3);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(4, 6, 0, 4, 3, 2);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 8, 0, 3, 2, 2);
        domain.addConstraint(c3);

        // On recalcule les bornes
        domain.nonOverLapLeft();

        // R3 : la borne xMin passe de 0 à 3
        Assert.assertEquals(3, domain.getConstraints().get(2).getxMin());
    }

    @Test
    public void testNonOverLapLeft2() {
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

        // R1 : la borne xMin passe de 0 à 3
        Assert.assertEquals(3, domain.getConstraints().get(0).getxMin());

        // R5 : la borne xMin passe de 4 à 7
        Assert.assertEquals(7, domain.getConstraints().get(4).getxMin());

        // R6 : la borne xMin passe de 0 à 7
        Assert.assertEquals(7, domain.getConstraints().get(5).getxMin());

    }

    @Test
    public void testNonOverLapRight1() {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 1, 1, 3, 3);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(4, 6, 0, 4, 3, 2);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 8, 0, 4, 2, 2);
        domain.addConstraint(c3);

        // On recalcule les bornes
        domain.nonOverLapRight();

        // R2 : la borne xMax reste à 6
        Assert.assertEquals(6, domain.getConstraints().get(1).getxMax());

        // R3 : la borne xMax reste à 8
        Assert.assertEquals(8, domain.getConstraints().get(2).getxMax());
    }

    @Test
    public void testNonOverLapRight2() {
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

        domain.nonOverLapRight();

        // R1 : la borne xMax passe de 5 à 4
        Assert.assertEquals(4, domain.getConstraints().get(0).getxMax());

        // R5 : la borne xMax reste à 8
        Assert.assertEquals(8, domain.getConstraints().get(4).getxMax());

        // R6 : la borne xMax reste à 9
        Assert.assertEquals(9, domain.getConstraints().get(5).getxMax());
    }

    @Test
    public void testNonOverLapTop1() {
        Domain domain = new Domain();

        // Contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 3, 3, 3, 3);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(1, 1, 0, 5, 2, 2);
        domain.addConstraint(c2);

        // On recalcule les bornes
        domain.nonOverLapTop();

        // R1 : la borne yMax reste à 1
        Assert.assertEquals(3, domain.getConstraints().get(0).getyMax());

        // R2 : la borne yMax passe de 5 à 1
        Assert.assertEquals(1, domain.getConstraints().get(1).getyMax());
    }

    @Test
    public void testNonOverLapTop2() {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 1, 1, 3, 3);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(4, 6, 0, 4, 3, 2);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 8, 0, 4, 2, 2);
        domain.addConstraint(c3);

        // On recalcule les bornes
        domain.nonOverLapTop(); // TODO : ajouter des assert ici  <--

        // R2 : la borne yMax reste à 4
        Assert.assertEquals(4, domain.getConstraints().get(1).getyMax());

        // R3 : la borne yMax reste à 4
        Assert.assertEquals(4, domain.getConstraints().get(2).getyMax());
    }

    @Test
    public void testNonOverLapTop3() {
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

        domain.nonOverLapTop();

        // R1 : la borne xMax reste à 2
        Assert.assertEquals(2, domain.getConstraints().get(0).getyMax());

        // R5 : la borne xMax reste à 3
        Assert.assertEquals(3, domain.getConstraints().get(4).getyMax());

        // R6 : la borne xMax reste à 5
        Assert.assertEquals(5, domain.getConstraints().get(5).getyMax());
    }

    @Test
    public void testNonOverLapBottom1() {
        Domain domain = new Domain();

        // Contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 1, 1, 3, 2);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(1, 1, 0, 5, 2, 2);
        domain.addConstraint(c2);

        // On recalcule les bornes
        domain.nonOverLapBottom();

        // R1 : la borne yMin reste à 1
        Assert.assertEquals(1, domain.getConstraints().get(0).getyMin());

        // R2 : la borne yMin passe de 0 à 3
        Assert.assertEquals(3, domain.getConstraints().get(1).getyMin());
    }

    @Test
    public void testNonOverLapBottom2() {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 1, 1, 3, 3);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(4, 6, 0, 4, 3, 2);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 8, 0, 4, 2, 2);
        domain.addConstraint(c3);

        // On recalcule les bornes
        domain.nonOverLapBottom(); // TODO : ajouter des assert ici  <--

        // R2 : la borne yMin reste à 0
        Assert.assertEquals(0, domain.getConstraints().get(1).getyMin());

        // R3 : la borne yMin reste à 0
        Assert.assertEquals(0, domain.getConstraints().get(2).getyMin());
    }

    @Test
    public void testNonOverLapBottom3() {
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

        domain.nonOverLapBottom();

        // R1 : la borne yMin reste à 2
        Assert.assertEquals(2, domain.getConstraints().get(0).getyMin());

        // R5 : la borne yMin reste à 2
        Assert.assertEquals(2, domain.getConstraints().get(4).getyMin());

        // R6 : la borne yMin reste à 2
        Assert.assertEquals(2, domain.getConstraints().get(5).getyMin());
    }

    @Test
     public void testNonOverLapLeftBottom() {
        Domain domain = new Domain();

        Constraint c1 = new Constraint(0, 5, 0, 5, 2, 2);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(0, 7, 3, 8, 4, 4);
        domain.addConstraint(c2);

        // lance le multiLap
        domain.nonOverLap();

        // R1 passe en (0, 0)
        Assert.assertEquals(0, domain.getConstraints().get(0).getxMin());
        Assert.assertEquals(0, domain.getConstraints().get(0).getyMin());

        // la constrainte de scale à la taille du rectangle à la fin ?
        Assert.assertEquals(2, domain.getConstraints().get(0).getxMax());
        Assert.assertEquals(2, domain.getConstraints().get(0).getyMax());

        // R2 passe en (0, 3)
        Assert.assertEquals(0, domain.getConstraints().get(1).getxMin());
        Assert.assertEquals(3, domain.getConstraints().get(1).getyMin());

        // la constrainte de scale à la taille du rectangle à la fin ?
        Assert.assertEquals(4, domain.getConstraints().get(1).getxMax());
        Assert.assertEquals(7, domain.getConstraints().get(1).getyMax());
    }

    @Test
    public void testNonOverLapMultiple1() {
        Domain domain = new Domain();

        Constraint c1 = new Constraint(4, 7, 0, 5, 3, 1);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(0, 5, 0, 4, 3, 3);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 7, 3, 8, 1, 3);
        domain.addConstraint(c2);

        // lance le multiLap
        domain.nonOverLap();
        // aucune idée du résultat par contre ... il faudrait le lancer plusieurs fois pour voir si on a le même résultat et si il est plausible.

        boolean tru ;
    }

    @Test
    public void testNonOverLapMultiple2() {
        Domain domain = new Domain();

        // contraintes placées arbitrairement
        Constraint c1 = new Constraint(0, 0, 0, 0, 3, 1);
        domain.addConstraint(c1);

        Constraint c2 = new Constraint(0, 0, 2, 2, 3, 1);
        domain.addConstraint(c2);

        Constraint c3 = new Constraint(0, 0, 1, 1, 1, 1);
        domain.addConstraint(c3);

        Constraint c4 = new Constraint(0, 0, 1, 1, 1, 1);
        domain.addConstraint(c4);

        Constraint c5 = new Constraint(2, 2, 1, 1, 1, 1);
        domain.addConstraint(c5);

        Constraint c6 = new Constraint(0, 2, 0, 2, 1, 1);
        domain.addConstraint(c6);

        domain.nonOverLap();

        // R6 : la borne xMin passe de 0 à 1
        Assert.assertEquals(1, domain.getConstraints().get(5).getxMin());

        // R6 : la borne xMax passe de 2 à 1
        Assert.assertEquals(1, domain.getConstraints().get(5).getxMax());

        // R6 : la borne yMin passe de 0 à 1
        Assert.assertEquals(1, domain.getConstraints().get(5).getyMin());

        // R6 : la borne yMax passe de 2 à 1
        Assert.assertEquals(1, domain.getConstraints().get(5).getyMax());
    }

}
