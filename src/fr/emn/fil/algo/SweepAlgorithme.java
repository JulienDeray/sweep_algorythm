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
     * Trouver la position minimum ou maximum d'un rectangle par rapport à des contraintes
     *
     * @param rectangle Rectangle à positionner par rapport aux contraintes du domaine
     * @param calculeMin True si l'on cherche un minimum ou False si l'on cherche un maximum
     * @return la position minimale ou maximale trouvée
     */
    public static Position findMinimum(Constraint rectangle, List<Constraint> constraints, boolean calculeMin) {

        // Liste des régions interdites du rectangle
        List<ForbiddenRegion> forbiddenRegions = getForbiddenRegionsFor(rectangle, constraints);

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
                //Nous créons les évènements  définissant le début et la fin de chacune des régions interdites
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
            int minGlobal = getMinGlobal(forbiddenRegions, constraints);
            int maxGlobal = getMaxGlobal(forbiddenRegions, constraints);

            //Nous parcourons chaque case de chaque delta du domaine du rectangle, pour trouver les premiers emplacements libres
            do {
                //Initialise les cases du delta (0=disponible , 1=interdit)
                pStatus = makePstatus(minGlobal, maxGlobal, rectangle);

                if ( calculeMin ) {
                    //Nous allons à la colonne suivante
                    delta++;
                    //Nous remplissons le pStatus pour connaître les emplacements libres de la colonne
                    pStatus = handleEvent(minGlobal, delta, pStatus, qEvent, true);
                    // Etablissement de la condition d'atteinte de la limite de la contrainte
                    limiteNonAtteinte = delta <= rectangle.getxMax();
                }
                else {
                    //Nous allons à la colonne suivante
                    delta--;
                    //Nous remplissons le pStatus pour connaître les emplacements libres de la colonne
                    pStatus = handleEvent(minGlobal, delta, pStatus, qEvent, false);
                    // Etablissement de la condition d'atteinte de la limite de la contrainte
                    limiteNonAtteinte = delta >= rectangle.getxMin();
                }

                //Nous vérifions dans chaque case du PStatus s'il y a un/des emplacements disponibles
                for (int i = 0; i < pStatus.length; i++) {
                    Integer pStatu = pStatus[i];
                    if (pStatu.equals(0) && i + minGlobal >= rectangle.getyMin() && i + minGlobal <= rectangle.getyMax()) {
                        availableY.add(i + minGlobal);
                    }
                }
                //Tant qu'aucun emplacement n'a été trouvé ou que la limite n'a pas été atteinte
            } while (availableY.size() <= 0 && limiteNonAtteinte);
        }

        if ( availableY.size() == 0 ) {
            // Pas de position possible
            return null;
        }
        else if ( availableY.size() == 1) {
            //On retourne la seule position possible
            return new Position(delta, availableY.get(0));
        }
        else {
            //On random la valeur de l'emplacement choisi par rapport aux cases disponibles
            return new Position(delta, random(availableY));
        }
    }

    /**
     * Applique une rotation anti-horaire de 90° sur chaque contrainte
     * @param constraints Jeu de contraintes contenues dans le model
     * @return Jeu de contraintes avec une rotation anti-horaire de 90°
     */
    public static List<Constraint> invertXY( List<Constraint> constraints ) {
        List<Constraint> revertedConstraints = new ArrayList<>();
        for (Constraint constraint : constraints) {
            revertedConstraints.add(new Constraint( constraint.getyMin(), constraint.getyMax(), constraint.getxMin(), constraint.getxMax(), constraint.getHeight(), constraint.getWidth() ));
        }
        return revertedConstraints;
    }

    /**
     * Renvoit le minimum possible entre les contraintes et les forbidden regions
     * @param forbiddenRegions Régions interdites d'un rectangle par rapport aux contraintes
     * @param constraints Régions interdites d'un rectangle par rapport aux contraintes
     * @return le minimum possible
     */
    private static int getMinGlobal(List<ForbiddenRegion> forbiddenRegions, List<Constraint> constraints) {
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
     * @param forbiddenRegions Régions interdites d'un rectangle par rapport aux contraintes
     * @param constraints Régions interdites d'un rectangle par rapport aux contraintes
     * @return le maximum possible
     */
    private static int getMaxGlobal(List<ForbiddenRegion> forbiddenRegions, List<Constraint> constraints) {
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
     * @param availables liste d'integer
     * @return l'élément de la liste choisi aléatoirement
     */
    private static int random(List<Integer> availables) {
        Random ran = new Random();
        return availables.get( ran.nextInt(availables.size() ) );
    }

    /**
     * Construit et initialise le Pstatus, tableau indiquant les emplacements libres ou non d'une colonne
     * @param borneInferieure le minimum possible
     * @param BorneSuperieure le maximum possible
     * @param rectangle la liste des régions interdites
     * @return le Pstatus initialisé
     */
    private static Integer[] makePstatus(int borneInferieure, int BorneSuperieure, Constraint rectangle) {
        Integer[] pStatus = new Integer[BorneSuperieure - borneInferieure + 1];
        // Nous indiquons qu'aucune case n'est disponible par défaut
        for (int y = 0; y < pStatus.length; y++) {
            pStatus[y] = 1;
        }
            // Seul le domaine du rectangle est rendu accessible
            for ( int y = rectangle.getyMin(); y <= rectangle.getyMax(); y++ ) {
                pStatus[y - borneInferieure] = 0;
            }

        return pStatus;
    }

    /**
     * Incrémente chaque case du pStatus lorsque des évènements témoignent d'une interdiction
     *
     * @param delta le delta du pStatus à complété
     * @param pStatus où indiquer les emplacements libres ou occupés sur le delta
     * @param events les évènements de début et fin de forbidden régions
     * @param isMin True pour un minimum, False pour un maximum
     * @return le pStatus, mis à jour avec les emplacements libres(0) ou interdits(1+) du delta
     */
    private static Integer[] handleEvent(int yMin, Integer delta, Integer[] pStatus, List<Event> events, boolean isMin) {
        for (int i = 0; i < events.size() - 1; i += 2) {
            //Les limites d'une région interdite
            int minX;
            int maxX;
            if(isMin){
                minX = events.get(i).getPosition();
                maxX = events.get(i+1).getPosition();
            }else {
                maxX = events.get(i).getPosition();
                minX = events.get(i + 1).getPosition();
            }

            //Si le delta est compris dans une région interdite
            if ( delta >= minX && delta <= maxX ) {
                //Nous rendons les cases concernées indisponibles
                for ( int y = events.get(i).getMin(); y <= events.get(i).getMax(); y++ ) {
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
     * @return la liste des régions interdites de ce rectangle
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

        //Calcul des limites de la région interdite
        int xMin = rj.getxMax() - width + 1;
        int xMax = rj.getxMin() + rj.getWidth() - 1;
        int yMin = rj.getyMax() - height + 1;
        int yMax = rj.getyMin() + rj.getHeight() - 1;

        //Pas de région interdite
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
