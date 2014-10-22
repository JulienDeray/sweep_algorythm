/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 20/10/14
 * Time: 12:37
 */
package fr.emn.fil.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Domain {
    private int x;
    private int y;

    private List<Constraint> constraints;

    public Domain(int y, int x) {
        this.y = y;
        this.x = x;
        this.constraints = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void add(Constraint constraint) {
        constraints.add(constraint);
    }

    public Position findMinimum(Constraint constraint) throws Exception {
        List<ForbiddenRegion> forbiddenRegions = getForbiddenRegionsFor( constraint );

        int availableX = -1;
        List<Integer> availableYs = null;
        Integer[] qEventInit = initQevent(y, forbiddenRegions);

        for ( int delta = constraint.getxMin(); delta < constraint.getxMax(); delta++ ) {
            Integer[] qEvent = qEventInit;
            qEvent = fillQevent(forbiddenRegions, qEvent, delta);

            availableYs = find0(qEvent);
            if ( availableYs.size() != 0 ) {
                availableX = delta;
                break;
            }
        }

        if ( availableYs != null ) {
            if ( availableYs.size() == 1 ) {
                return new Position( availableX, availableYs.get(0) );
            }
            else {
                Random rand = new Random();
                return new Position( availableX, availableYs.get( rand.nextInt(availableYs.size()) ) );
            }
        }
        else {
            throw new Exception("No available position for this constraint");
        }
    }

    private static Integer[] initQevent(int yMax, List<ForbiddenRegion> forbiddenRegions) {
        Integer[] qEvent = new Integer[yMax];

        for (int y = 0; y < qEvent.length; y++) {
            qEvent[y] = 1;
        }
        for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
            for ( int y = forbiddenRegion.getyMin(); y < forbiddenRegion.getyMax(); y++ ) {
                qEvent[y] = 0;
            }
        }

        return qEvent;
    }

    private List<Integer> find0(Integer[] qEvent) {
        List<Integer> res = new ArrayList<>();
        for (int y = 0; y < qEvent.length; y++) {
            Integer integer = qEvent[y];

            if ( integer == 0 ) {
                res.add(y);
            }
        }
        return res;
    }

    private static Integer[] fillQevent(List<ForbiddenRegion> forbiddenRegions, Integer[] qEvent, int delta) {
        for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
            if ( forbiddenRegion != null && forbiddenRegion.getxMin() <= delta && forbiddenRegion.getxMax() >= delta ) {
                for ( int involvedY = forbiddenRegion.getyMin(); involvedY < forbiddenRegion.getyMax(); involvedY++ ) {
                    qEvent[involvedY] += 1;
                }
            }
        }
        return qEvent;
    }

    private List<ForbiddenRegion> getForbiddenRegionsFor(Constraint constraint) {
        List<ForbiddenRegion> forbiddenRegions = new ArrayList<>();

        for (Constraint c : constraints) {
            ForbiddenRegion forbiddenRegion = computeForbiddenRegion(c, constraint.getWidth(), constraint.getHeight());
            if ( forbiddenRegion != null ) {
                forbiddenRegions.add( forbiddenRegion );
            }
        }

        return forbiddenRegions;
    }

    /**
     *
     * @param rj Constrainte que l'on veut placer
     * @param width Largeur de la contrainte comparée (ri)
     * @param height Hauteur de la constrainte comparée (ri)
     * @return La région interdite de rj par rapport à ri
     */
    public static ForbiddenRegion computeForbiddenRegion(Constraint rj, int width, int height) {

        int xMin = rj.getxMax() - width + 1;
        int xMax = rj.getxMin() + rj.getWidth() - 1;
        int yMin = rj.getyMax() - height + 1;
        int yMax = rj.getyMin() + rj.getHeight() - 1;

        if (xMin > xMax || yMin > yMax) {
            return null;
        }
        else {
            return new ForbiddenRegion(xMin, xMax, yMin, yMax);
        }
    }
}
