package fr.emn.fil;

public class Main {

    public static void main(String[] args) {
	    PlacementRegion ri = new PlacementRegion(3, 5);
	    PlacementRegion rj = new PlacementRegion(6, 7, 7, 8, 3, 2);

        Region res = Algorythm.computeForbiddenRegion(rj, ri);

        System.out.println(res);
    }
}
