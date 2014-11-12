/**
 * Contraintes, zones (délimitées en abcisse et ordonnée) où placer des rectangles de largeur width et de hauteur height.
 *
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 13/10/14
 * Time: 08:56
 */
package fr.emn.fil.model;

public class Constraint {

    //Arguments
    private int xMin;
    private int xMax;

    private int yMin;
    private int yMax;

    private int height;
    private int width;

    //Constructeurs
    public Constraint(int xMin, int xMax, int yMin, int yMax, int width, int height) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.height = height;
        this.width = width;
    }

    public Constraint(int width, int height) {
        this.height = height;
        this.width = width;
    }

    // Getters - Setters

    public int getxMin() { return xMin; }

    public void setxMin(int x) { this.xMin = x; }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int x) { this.xMax = x; }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int y) { this.yMin = y; }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int y) { this.yMax = y; }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    // Méthodes

    /**
     * Retourne le détail des arguments de la contrainte sous forme de String
     * @return strinf du détail
     */
    @Override
    public String toString() {
        return "Rectangle{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    /**
     * Teste l'égalité de deux contraintes/rectangles
     * @param o la contrainte à comparer
     * @return le résultat du test d'égalité
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Constraint)) return false;

        Constraint that = (Constraint) o;

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (xMax != that.xMax) return false;
        if (xMin != that.xMin) return false;
        if (yMax != that.yMax) return false;
        if (yMin != that.yMin) return false;

        return true;
    }
}
