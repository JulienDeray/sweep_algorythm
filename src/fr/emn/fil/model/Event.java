package fr.emn.fil.model;

/**
 * Created by Guillaume on 23/10/2014.
 */
public class Event {
    /**
     * La limite basse de l'évènement en ordonnée
     */
    private Integer minY;

    /**
     * La limite haute de l'évènement en ordonnée
     */
    private Integer maxY;

    /**
     * La position de l'évènement en abcisse
     */
    private Integer positionX;

    /**
     * Contructeur par défault.
     *
     * @param minY
     * @param maxY
     * @param positionX
     */
    public Event( Integer positionX,Integer minY, Integer maxY) {
        this.minY = minY;
        this.maxY = maxY;
        this.positionX = positionX;
    }

    /**
     * Getters et Setters
     */

    public Integer getMinY() {
        return minY;
    }

    public void setMinY(Integer minY) {
        this.minY = minY;
    }

    public Integer getMaxY() {
        return maxY;
    }

    public void setMaxY(Integer maxY) {
        this.maxY = maxY;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public void setPositionX(Integer positionX) {
        this.positionX = positionX;
    }
}
