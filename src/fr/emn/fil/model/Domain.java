/**
 * Domaine, plan à deux dimensions de taille (x,y) possédant des contraintes.
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 20/10/14
 * Time: 12:37
 */
package fr.emn.fil.model;

import fr.emn.fil.algo.SweepAlgorithme;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Domain {
    private int x;
    private int y;

    private List<Constraint> constraints;

    public Domain() {
        this.constraints = new ArrayList<>();
    }

    /**
     * Méthode ajoutant une contrainte au domaine (modifiant les bornes si besoin)
     * @param constraint JJeu de contraintes contenues dans le modeleu de contraintes contenues dans le model
     */
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
        this.x = (this.x < constraint.getxMax()) ? constraint.getxMax() : this.x;

        this.y = (this.y < constraint.getyMax()) ? constraint.getyMax() : this.y;
    }

    /**
     * Réévalue la borne xMin de chaque rectangle du domaine
     * @return nombre de rectangles déplacés
     */
    public int nonOverLapLeft(){
        int bornesModifiees = 0;
        // Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : constraints) {
                // Nous calculons l'emplacement minimum de ce rectangle
                Position newPosition = SweepAlgorithme.findMinimum(rectangle, constraints, true, true);
                // Si un minimum plus petit que le précédent a été trouvé
                if( newPosition != null && newPosition.getX() > rectangle.getxMin() ){
                    // Nous corrigeons la borne inférieur X de la contrainte
                   rectangle.setxMin(newPosition.getX());
                   bornesModifiees++;
                }
        }
        return bornesModifiees;
    }

    /**
     * Réévalue la borne xMax de chaque rectangle du domaine
     * @return nombre de rectangles déplacés
     */
    public int nonOverLapRight(){
        int bornesModifiees = 0;
        // Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : constraints) {
            //Nous calculons l'emplacement maximum de ce rectangle
            Position newPosition = SweepAlgorithme.findMinimum(rectangle, constraints, false, true);
            // Si un maximum plus petit que le précédent a été trouvé
            if( newPosition != null && newPosition.getX() < rectangle.getxMax() ){
                //Nous corrigeons la borne inférieur X de la contrainte
                rectangle.setxMax(newPosition.getX());
                bornesModifiees++;
            }
        }
        return bornesModifiees;
    }

    /**
     * Réévalue la borne yMax de chaque rectangle du domaine
     * @return nombre de rectangles déplacés
     */
    public int nonOverLapTop(){
        int bornesModifiees = 0;
        // Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : constraints) {
            //Nous calculons l'emplacement minimum de ce rectangle
            Position newPosition = SweepAlgorithme.findMinimum(rectangle, constraints, true, false);
            // Si un minimum plus petit que le précédent a été trouvé
            if( newPosition != null && newPosition.getX() > rectangle.getxMin() ){
                // Nous corrigeons la borne inférieur X de la contrainte
                rectangle.setxMin(newPosition.getX());
                bornesModifiees++;
            }
        }
        return bornesModifiees;
    }

    /**
     * Réévalue la borne yMax de chaque rectangle du domaine
     * @return nombre de rectangles déplacés
     */
    public int nonOverLapBottom() {
        int bornesModifiees = 0;
        // Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : constraints) {
            // Nous calculons l'emplacement minimum de ce rectangle
            Position newPosition = SweepAlgorithme.findMinimum(rectangle, constraints, false, false);
            // Si un minimum plus petit que le précédent a été trouvé
            if( newPosition != null && newPosition.getX() > rectangle.getyMax() ){
                //Nous corrigeons la borne inférieur X de la contrainte
                rectangle.setyMax(newPosition.getX());
                bornesModifiees++;
            }
        }
        return bornesModifiees;
    }

    /**
     * Getter des contraintes du domaine
     *@return la liste des contraintes du domaine
     */
    public List<Constraint> getConstraints(){
        return this.constraints;
    }
}
