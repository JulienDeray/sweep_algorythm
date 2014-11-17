/**
 * Evènement, représente le début ou la fin (et la taille) d'une région interdite
 * Created by Guillaume on 23/10/2014.
 */
package fr.emn.fil.model;

public class Event {
    /**
     * La limite basse de l'évènement
     */
    private Integer min;

    /**
     * La limite haute de l'évènement
     */
    private Integer max;

    /**
     * La position de l'évènement
     */
    private Integer position;

    /**
     * Contructeur par défault.
     *
     * @param min le minimum
     * @param max le maximum
     * @param position la position de l'évènement
     */
    public Event( Integer position,Integer min, Integer max) {
        this.min = min;
        this.max = max;
        this.position = position;
    }

    /**
     * Getters et Setters
     */

    public Integer getMin() {
        return min;
    }
    public Integer getMax() {
        return max;
    }
    public Integer getPosition() {
        return position;
    }
}
