/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 13/10/14
 * Time: 08:56
 */
package fr.emn.fil;

public class PlacementRegion {
    private int xMin;
    private int xMax;

    private int yMin;
    private int yMax;

    private int height;
    private int width;

    public PlacementRegion(int xMin, int xMax, int yMin, int yMax, int width, int height) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.height = height;
        this.width = width;
    }

    public PlacementRegion(int width, int height) {
        this.height = height;
        this.width = width;
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

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlacementRegion)) return false;

        PlacementRegion that = (PlacementRegion) o;

        if (height != that.height) return false;
        if (width != that.width) return false;
        if (xMax != that.xMax) return false;
        if (xMin != that.xMin) return false;
        if (yMax != that.yMax) return false;
        if (yMin != that.yMin) return false;

        return true;
    }
}
