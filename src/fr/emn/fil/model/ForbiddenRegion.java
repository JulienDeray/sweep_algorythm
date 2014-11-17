/**
 * Région interdite, zone interdite pour un placement entre deux rectangles.
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 21/10/14
 * Time: 12:53
 */
package fr.emn.fil.model;

public class ForbiddenRegion {
    private int xMin;
    private int xMax;

    private int yMin;
    private int yMax;

    public ForbiddenRegion(int xMin, int xMax, int yMin, int yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public int getxMin() {
        return xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMin() {
        return yMin;
    }

    public int getyMax() {
        return yMax;
    }

    /**
     * Contenu d'une région interdite mis en String
     * @return la String de la région interdite
     */
    @Override
    public String toString() {
        return "ForbiddenRegion{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                '}';
    }
}
