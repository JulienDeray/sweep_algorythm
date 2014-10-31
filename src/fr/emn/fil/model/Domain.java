/**
 * Domaine, plan à deux dimensions de taille (x,y) possédant des contraintes.
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

    public Domain() {
        this.constraints = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Méthode ajoutant une contrainte au domaine (modifiant les bornes si besoin)
     * @param constraint
     */
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
        this.x = (this.x < constraint.getxMax()) ? constraint.getxMax() : this.x;

        this.y = (this.y < constraint.getyMax()) ? constraint.getyMax() : this.y;
    }

    /**
     * Méthode supprimant une contrainte au domaine (modifiant les bornes si besoin)
     * @param constraint
     */
    public void removeConstraint(Constraint constraint){
        constraints.remove(constraint);
        redefinirLimites();
    }

    /**
     * Redéfinit les limites x et y du domaines afin d'être optimal pour acceuillir toutes les contraintes
     */
    private void redefinirLimites() {
        for(Constraint contrainte : constraints) {
            this.x = (this.x < contrainte.getxMax()) ? contrainte.getxMax() : this.x;
            this.y = (this.y < contrainte.getyMax()) ? contrainte.getyMax() : this.y;
        }
    }

    /**
     * Prend tous les rectangles du domaine et essaye de les replacer le plus à gauche possible
     * @return nombre de rectangles déplacés
     */
    public int nonOverLapLeft(){
        int bornesModifiees = 0;
        //Nous bouclons sur chaque rectangle du domaine
        for (Constraint rectangle : constraints) {
                //Nous calculons l'emplacement minimum de ce rectangle
                Position newPosition = findMinimum(rectangle);
                // Si un minimum plus petit que le précédent a été trouvé
                if( newPosition != null && newPosition.getX() > rectangle.getxMin()){
                    //Nous corrigeons la borne inférieur X de la contrainte
                   rectangle.setxMin(newPosition.getX());
                   bornesModifiees++;
                }
        }
        return bornesModifiees;
    }


    /**
     * Permet de créer la liste des évènements (débuts et fins des forbidden régions)
     *
     * @param rectangle
     * @return la liste d'évènements
     */
    public Position findMinimum(Constraint rectangle) {
        // Liste des régions interdites du rectangle
        List<ForbiddenRegion> forbiddenRegions = getForbiddenRegionsFor(rectangle);
        // Liste qui contient les évènements
        List<Event> qEvent = new ArrayList<>();

        //La coordonnée en abcisse du minimum
        Integer delta = rectangle.getxMin() - 1;

        //Liste des cases disponibles dans la colonne delta
        List<Integer> availableY = new ArrayList<>();

        //Dans le cas où il n'y a pas de forbidden régions
        if (forbiddenRegions.isEmpty()) {
            for (int i = rectangle.getyMin(); i < rectangle.getyMax()+1 ; i++) {
                availableY.add(i);
            }
            delta = rectangle.getxMin();
        }else{
            //Nous cherchons à trouver le minimum dans le domaine possible de la contrainte
            for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
                //Si il y un début ou fin de forbidden région sur ce delta, alors nous créons un Event correspondant
                Event eventMin = new Event(forbiddenRegion.getxMin(), forbiddenRegion.getyMin(), forbiddenRegion.getyMax());
                qEvent.add(eventMin);
                Event eventMax = new Event(forbiddenRegion.getxMax(), forbiddenRegion.getyMin(), forbiddenRegion.getyMax());
                qEvent.add(eventMax);
            }

            //A présent que les évènements sont définis, nous cherchons à trouver l'emplacement libre minimum
            // Vecteur associé à une colonne, indiquant les cases bloquées par des régions interdites
            Integer[] pStatus;

            //Nous parcourons chaque colonne, puis chaqaue case pour trouver les emplacements libres

            int yMinOfFR = getMinY(forbiddenRegions, constraints);
            int yMaxOfFR = getMaxY(forbiddenRegions, constraints);

            do {
                //Initialise chaque case de la colonne à 0
                pStatus = makePstatus(yMinOfFR, yMaxOfFR, forbiddenRegions);

                //Nous allons à la colonne suivante
                delta++;

                //Nous remplissons le pStatus pour connaître les emplacements libres de la colonne
                pStatus = handleEvent(yMinOfFR, delta, pStatus, qEvent);

                //Nous vérifions dans chaque case du PStatus s'il y a un/des emplacements
                for (int i = 0; i < pStatus.length; i++) {
                    Integer pStatu = pStatus[i];
                    if (pStatu.equals(0) && i + yMinOfFR >= rectangle.getyMin() && i + yMinOfFR <= rectangle.getyMax()) {
                        availableY.add(i + yMinOfFR);
                    }
                }
            } while (availableY.size() <= 0 && delta <= rectangle.getxMax());
        }

        //On random la valeur de y par rapport aux y disponibles
        if ( availableY.size() == 0 ) {
            return null;
        }
        else if ( availableY.size() == 1) {
            return new Position(delta, availableY.get(0));
        }
        else {
            return new Position(delta, randomY(availableY));
        }
    }

    /**
     * Renvoit le plus petit y des forbidden régions
     * @param forbiddenRegions
     * @param constraints
     * @return
     */
    private static int getMinY(List<ForbiddenRegion> forbiddenRegions, List<Constraint> constraints) {
        int minY = forbiddenRegions.get(0).getyMin();
        for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
            int min = forbiddenRegion.getyMin();
            if ( min < minY ) {
                minY = min;
            }
        }
        for (Constraint constraint : constraints) {
            int min = constraint.getyMin();
            if ( min < minY ) {
                minY = min;
            }
        }
        return minY;
    }

    /**
     * Renvoit le plus grand x des forbidden régions
     * @param forbiddenRegions
     * @param constraints
     * @return
     */
    private static int getMaxY(List<ForbiddenRegion> forbiddenRegions, List<Constraint> constraints) {
        int maxY = forbiddenRegions.get(0).getyMax();
        for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
            int max = forbiddenRegion.getyMax();
            if ( max > maxY ) {
                maxY = max;
            }
        }
        for (Constraint constraint : constraints) {
            int max = constraint.getyMax();
            if ( max > maxY ) {
                maxY = max;
            }
        }
        return maxY;
    }

    /**
     * Renvoit un entier au hasard parmis une liste d'entiers
     * @param availablesY liste d'integer
     * @return
     */
    private int randomY(List<Integer> availablesY) {
        Random ran = new Random();
        return availablesY.get( ran.nextInt(availablesY.size() ) );
    }

    /**
     * Construit et initialise le Pstatus, tableau indiquant les emplacements libres ou non d'une colonne
     * @param yMinOfFR
     * @param yMaxOfFR
     * @param forbiddenRegions
     * @return
     */
    private Integer[] makePstatus(int yMinOfFR, int yMaxOfFR, List<ForbiddenRegion> forbiddenRegions) {
        Integer[] pStatus = new Integer[yMaxOfFR - yMinOfFR + 1];

        for (int y = 0; y < pStatus.length; y++) {
            pStatus[y] = 1;
        }
        for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
            for ( int y = forbiddenRegion.getyMin(); y <= forbiddenRegion.getyMax(); y++ ) {
                pStatus[y - yMinOfFR] = 0;
            }
        }

        return pStatus;
    }

    /**
     * Remplit le pStatus avec le nombre de régions interdites, par case (0,1,2,etc)
     *
     * @param delta la colonne à étudier
     * @param pStatus où indiquer les emplacements libres ou occupés
     * @param events les évènements de début et fins de forbidden régions
     * @return le pStatus, contient les emplacements libres ou occupés de la colonne delta
     */
    public Integer[] handleEvent(int yMin, Integer delta, Integer[] pStatus, List<Event> events) {
        for (int i = 0; i < events.size() - 1; i += 2) {
            int minX = events.get(i).getPositionX();
            int maxX = events.get(i+1).getPositionX();

            if ( delta >= minX && delta <= maxX ) {
                for ( int y = events.get(i).getyMin(); y <= events.get(i).getyMax(); y++ ) {
                    pStatus[y - yMin] += 1;
                }
            }
        }
        return pStatus;
    }

    /**
     * Calcule puis retourne la liste des régions interdites du rectangle par rapport aux contraintes
     *
     * @param rectangle le rectangle que l'on veut placer.
     * @return la liste des régions interdites
     */
    private List<ForbiddenRegion> getForbiddenRegionsFor(Constraint rectangle) {
        List<ForbiddenRegion> forbiddenRegions = new ArrayList<>();

        for (Constraint c : constraints) {
            if(!c.equals(rectangle)) {
                ForbiddenRegion forbiddenRegion = computeForbiddenRegion(c, rectangle.getWidth(), rectangle.getHeight());
                if (forbiddenRegion != null) {
                    forbiddenRegions.add(forbiddenRegion);
                }
            }
        }

        return forbiddenRegions;
    }

    /**
     *Permet de calculer la région interdite d'un rectangle dont on connait la hauteur et largeur par rapport à une contrainte rJ
     *
     * @param rj Constrainte à prendre en compte pour placer le rectangle
     * @param width Largeur du rectangle à placer (ri)
     * @param height Hauteur du rectangle à placer (ri)
     * @return La région interdite de ri par rapport à rj
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

    /**
     * Getter des contraintes du domaine
     *@return la liste des contraintes du domaine
     */
    public List<Constraint> getConstraints(){
        return this.constraints;
    }



}
