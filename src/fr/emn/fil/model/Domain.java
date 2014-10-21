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

    public void addCleverly(Constraint constraint) {
        List<ForbiddenRegion> forbiddenRegions = getForbiddenRegionsFor( constraint );

        System.out.println(forbiddenRegions);
    }

    private List<ForbiddenRegion> getForbiddenRegionsFor(Constraint constraint) {
        List<ForbiddenRegion> forbiddenRegions = new ArrayList<>();

        for (Constraint c : constraints) {
            forbiddenRegions.add( computeForbiddenRegion(c, constraint.getWidth(), constraint.getHeight()) );
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
