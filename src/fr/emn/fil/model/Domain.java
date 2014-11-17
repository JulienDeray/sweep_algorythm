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
                Position newPosition = SweepAlgorithme.findMinimum(rectangle, constraints, true);
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
     * Réévalue la borne yMin de chaque rectangle du domaine
     * @return nombre de rectangles déplacés
     */
    public int nonOverLapBottom(){
        int bornesModifiees = 0;

        // Inversement des axes
        List<Constraint> revertedConstraints = SweepAlgorithme.invertXY(constraints);

        // Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : revertedConstraints) {
            //Nous calculons l'emplacement minimum de ce rectangle
            Position newPosition = SweepAlgorithme.findMinimum(rectangle, revertedConstraints, true);
            // Si un minimum plus petit que le précédent a été trouvé
            if( newPosition != null && newPosition.getX() > rectangle.getxMin() ){
                // Nous corrigeons la borne inférieur X de la contrainte
                rectangle.setxMin(newPosition.getX());
                bornesModifiees++;
            }
        }

        constraints = SweepAlgorithme.invertXY(revertedConstraints);
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
            Position newPosition = SweepAlgorithme.findMinimum(rectangle, constraints, false);
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
    public int nonOverLapTop() {
        int bornesModifiees = 0;

        // Inversement des axes
        List<Constraint> revertedConstraints = SweepAlgorithme.invertXY(constraints);

        // Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : revertedConstraints) {
            // Nous calculons l'emplacement minimum de ce rectangle
            Position newPosition = SweepAlgorithme.findMinimum(rectangle, revertedConstraints, false);
            // Si un minimum plus petit que le précédent a été trouvé
            if( newPosition != null && newPosition.getX() < rectangle.getxMax() ){
                //Nous corrigeons la borne inférieur X de la contrainte
                rectangle.setxMax(newPosition.getX());
                bornesModifiees++;
            }
        }
        constraints = SweepAlgorithme.invertXY(revertedConstraints);
        return bornesModifiees;
    }

    /**
     * Getter des contraintes du domaine
     *@return la liste des contraintes du domaine
     */
    public List<Constraint> getConstraints(){
        return this.constraints;
    }

    /**
     * Recalcule les bornes de toutes les contraintes
     */
    public int nonOverLap() {
        // Recalcul de toutes les bornes tant qu'elles ont besoin d'être modifiées
        int nbIter = 0;
        while (nonOverLapLeft() != 0 || nonOverLapRight() != 0 || nonOverLapTop() != 0 || nonOverLapBottom() != 0) {
            nbIter++;
        }
        return nbIter;
    }

    /**
     * Recalcule les bornes de toutes les contraintes
     */
    public int nonOverLapBis() {
        // Recalcul de toutes les bornes tant qu'elles ont besoin d'être modifiées
        int res = 0;
        int i = 1;
        while ( i > 0 ) {
            i = 0;
            i += nonOverLapLeft();
            i += nonOverLapRight();
            i += nonOverLapTop();
            i += nonOverLapBottom();
            res += i;
        }
        return res;
    }
}
