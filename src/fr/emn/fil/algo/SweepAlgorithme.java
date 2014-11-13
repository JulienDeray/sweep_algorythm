/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 13/11/14
 * Time: 15:43
 */
package fr.emn.fil.algo;

import fr.emn.fil.model.Constraint;
import fr.emn.fil.model.Event;
import fr.emn.fil.model.ForbiddenRegion;
import fr.emn.fil.model.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SweepAlgorithme {

    /**
     * Trouver la position minimum ou maximum
     *
     * @param rectangle Rectangle à positionner par rapport aux contraintes du domaine
     * @param calculeMin True si l'on calcule le minimum ou le maximum
     * @return la liste d'&eacute;v&egrave;nements
     */
    public static Position findMinimum(Constraint rectangle, List<Constraint> constraints, boolean calculeMin, boolean calculeAbscisse) {

        List<Constraint> usedConstraints;

        if ( calculeAbscisse ) {
            usedConstraints = constraints;
        }
        else {
            usedConstraints = invertXY( constraints );
        }

        // Liste des régions interdites du rectangle
        List<ForbiddenRegion> forbiddenRegions = getForbiddenRegionsFor(rectangle, usedConstraints);

        // Liste qui contient les évènements
        List<Event> qEvent = new ArrayList<>();

        //Définition du point de départ de l'algorithme
        int delta;
        if ( calculeMin ) {
            delta = rectangle.getxMin() - 1;
        }
        else {
            delta = rectangle.getxMax() + 1;
        }

        //Liste des cases disponibles dans la colonne delta
        List<Integer> availableY = new ArrayList<>();

        //Dans le cas où il n'y a pas de forbidden régions
        if (forbiddenRegions.isEmpty()) {
            //Les extrêmes sont inchangés
            for (int i = rectangle.getyMin(); i < rectangle.getyMax()+1 ; i++) {
                availableY.add(i);
            }
            if ( calculeMin ) {
                delta = rectangle.getxMin();
            }
            else{
                delta = rectangle.getxMax();
            }

        }
        else {
            //Nous cherchons à redéfinir la borne dans le domaine possible de la contrainte
            boolean limiteNonAtteinte;

            for (ForbiddenRegion forbiddenRegion : forbiddenRegions) {
                //Si il y un début ou fin de forbidden région sur ce delta, alors nous créons un Event correspondant
                Event eventMin = new Event(forbiddenRegion.getxMin(), forbiddenRegion.getyMin(), forbiddenRegion.getyMax());
                qEvent.add(eventMin);
                Event eventMax = new Event(forbiddenRegion.getxMax(), forbiddenRegion.getyMin(), forbiddenRegion.getyMax());
                qEvent.add(eventMax);
            }

            // Dans le cas du calcul d'un maximum, nous retournons la queue des évènements
            if ( !calculeMin ) {
                qEvent = reverse( qEvent );
            }

            //A présent que les évènements sont définis, nous cherchons à trouver le premier emplacement libre

            // Vecteur associé à une colonne, indiquant les cases bloquées par des régions interdites
            Integer[] pStatus;

            //Calcul des zones limites du Pstatus
            int yMinOfFR = getMinY(forbiddenRegions, usedConstraints);
            int yMaxOfFR = getMaxY(forbiddenRegions, usedConstraints);

            //Nous parcourons chaque colonne, puis chaque case pour trouver les emplacements libres par colonne
            do {
                //Initialise chaque case de la colonne à 0
                pStatus = makePstatus(yMinOfFR, yMaxOfFR, forbiddenRegions);

                if ( calculeMin ) {
                    //Nous allons à la colonne suivante
                    delta++;
                    //Nous remplissons le pStatus pour connaître les emplacements libres de la colonne
                    pStatus = handleEvent(yMinOfFR, delta, pStatus, qEvent, true);
                    // Etablissement de la condition d'atteinte de la limite de la contrainte
                    limiteNonAtteinte = delta <= rectangle.getxMax();
                }
                else {
                    delta--;
                    //Nous remplissons le pStatus pour connaître les emplacements libres de la colonne
                    pStatus = handleEvent(yMinOfFR, delta, pStatus, qEvent, false);
                    // Etablissement de la condition d'atteinte de la limite de la contrainte
                    limiteNonAtteinte = delta >= rectangle.getxMin();
                }

                //Nous vérifions dans chaque case du PStatus s'il y a un/des emplacements
                for (int i = 0; i < pStatus.length; i++) {
                    Integer pStatu = pStatus[i];
                    if (pStatu.equals(0) && i + yMinOfFR >= rectangle.getyMin() && i + yMinOfFR <= rectangle.getyMax()) {
                        availableY.add(i + yMinOfFR);
                    }
                }
            } while (availableY.size() <= 0 && limiteNonAtteinte);
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
     * Applique une rotation anti-horaire de 90° sur chaque contrainte
     * @param constraints Jeu de contraintes contenues dans le model
     * @return Jeu de contraintes avec une rotation anti-horaire de 90°
     */
    private static List<Constraint> invertXY( List<Constraint> constraints ) {
        List<Constraint> revertedConstraints = new ArrayList<>();
        for (Constraint constraint : constraints) {
            revertedConstraints.add(new Constraint( constraint.getxMin(), constraint.getxMax(), constraint.getyMin(), constraint.getyMax(), constraint.getHeight(), constraint.getWidth() ));
        }
        return revertedConstraints;
    }

    /**
     * Renvoit le plus petit y des forbidden regions
     * @param forbiddenRegions Régions interdites du rectangle par rapport aux contraintes
     * @param constraints Jeu de contraintes contenues dans le model
     * @return Plus petit Y contenu dans les forbidden regions
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
     * @param forbiddenRegions les régions interdites
     * @param constraints Jeu de contraintes contenues dans le modella liste des contraintes
     * @return le maximum des ordonnées
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
     * @return l'élément de la liste choisi aléatoirement
     */
    private static int randomY(List<Integer> availablesY) {
        Random ran = new Random();
        return availablesY.get( ran.nextInt(availablesY.size() ) );
    }

    /**
     * Construit et initialise le Pstatus, tableau indiquant les emplacements libres ou non d'une colonne
     * @param yMinOfFR le minimum en y entre toutes les régions interdites
     * @param yMaxOfFR le maximum en y entre toutes les régions interdites
     * @param forbiddenRegions la liste des régions interdites
     * @return le Pstatus initialisé
     */
    private static Integer[] makePstatus(int yMinOfFR, int yMaxOfFR, List<ForbiddenRegion> forbiddenRegions) {
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
     * @param isMin true pour un minimum, false pour un maximum
     * @return le pStatus, contient les emplacements libres ou occupés de la colonne delta
     */
    private static Integer[] handleEvent(int yMin, Integer delta, Integer[] pStatus, List<Event> events, boolean isMin) {
        for (int i = 0; i < events.size() - 1; i += 2) {
            int minX;
            int maxX;
            if(isMin){
                minX = events.get(i).getPositionX();
                maxX = events.get(i+1).getPositionX();
            }else {
                maxX = events.get(i).getPositionX();
                minX = events.get(i + 1).getPositionX();
            }

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
    private static List<ForbiddenRegion> getForbiddenRegionsFor(Constraint rectangle, List<Constraint> constraints) {
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
     * Retourne la liste donnée en paramètre
     * @param list  liste à retourner
     * @return la liste retournée
     */
    private static List<Event> reverse(List<Event> list) {
        if(list.size() > 1) {
            Event value = list.remove(0);
            reverse(list);
            list.add(value);
        }
        return list;
    }
}
