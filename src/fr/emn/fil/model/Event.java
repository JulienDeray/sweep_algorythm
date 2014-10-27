package fr.emn.fil.model;

/**
 * Created by Guillaume on 23/10/2014.
 */
public class Event {
    /**
     * La limite basse de l'évènement en ordonnée
     */
    private Integer yMin;

    /**
     * La limite haute de l'évènement en ordonnée
     */
    private Integer yMax;

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
        this.yMin = minY;
        this.yMax = maxY;
        this.positionX = positionX;
    }

    /**
     * Getters et Setters
     */

    public Integer getyMin() {
        return yMin;
    }
    public Integer getyMax() {
        return yMax;
    }
    public Integer getPositionX() {
        return positionX;
    }
}
