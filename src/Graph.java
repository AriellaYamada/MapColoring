import java.sql.Time;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class Graph {

    private int size;
    private LinkedList<Region> regions;
    private Stream<Region> stream;

    public Graph(int size) {
        this.size = size;
        regions = new LinkedList<Region>();
    }

    public int getSize() {
        return size;
    }

    public void insertVertex(Region r){
        regions.add(r);
    }

    public Region getRegion(String region) {
        return regions.stream()
                .filter(r -> r.getName().equals(region))
                .findFirst().get();
    }

    public Region getNextAdj(Region r) {
        if (r.hasAdjRegion()) {
            LinkedList<String> list = r.getAdjRegion();
            for (String n : list){
                Region next = getRegion(n);
                if(next.getColor() == 0)
                    return next;
            }
        }
        return null;
    }

    public Region getNextFC(Region r) {
        Comparator<Region> byRemainingColors = (r1, r2) -> Integer.compare(r1.nRemainingColors(), r2.nRemainingColors());
        return regions.stream().max(byRemainingColors).get();
    }

    public boolean fowardChecking () {
        for (Region r : regions)
            if (r.nRemainingColors() == 0) return false;
        return true;
    }

    public boolean verifyAdjRegions(Region region) {
        int color = region.getColor();
        if (region.hasAdjRegion()) {
            LinkedList<String> adjRegions = region.getAdjRegion();
            for (String r : adjRegions) {
                Region adj = getRegion(r);
                if (adj.getColor() == color)
                    return false;
            }
            return true;
        }
        return false;
    }

    public void refreshPossibleColors(Region region, int c) {
        region.setColor(c);
        if (region.hasAdjRegion()) {
            for (String name : region.getAdjRegion()) {
                Region r = getRegion(name);
                r.removeRemainingColors(c);
            }
        }
    }

    public void resetPossibleColors(Region region) {
        if (region.hasAdjRegion()) {
            for (String name : region.getAdjRegion()) {
                Region r = getRegion(name);
                r.insertRemainingColors(r.getColor());
            }
        }
        region.setColor(0);
    }

    public void backtraking(char h) {
        for (Region r : regions) {
            if (r.getColor() == 0) {
                //Escolha da heuristica
                switch (h) {
                    //Backtracking simples
                    case 'a':
                        recursiveBacktracking(r);
                        break;
                    //Backtracking com verificacao adiante
                    case 'b':
                        backtrakingFC(r);
                        break;
                    //Backtracking com verificacao adiante e MVR
                    case 'c':
                        break;
                    //Backtracking com verificacao adiante, MVR e grau
                    case 'd':
                        break;
                }
            }
        }
    }

    public boolean recursiveBacktracking(Region r) {
        for (int c = 1; c < 5; c++) {
            r.setColor(c);
            if(verifyAdjRegions(r)) {
                Region next = getNextAdj(r);
                if (next == null)
                    return true;
                else
                    return recursiveBacktracking(next);
            }
        }
        r.setColor(0);
        return false;
    }

    public boolean backtrakingFC(Region r) {
        for (Integer c : r.getRemainingColors()) {
            refreshPossibleColors(r, c);
            if(fowardChecking()) {
                Region next = getNextFC(r);
                if (next == null)
                    return true;
                else
                    return backtrakingFC(next);
            }
        }
        resetPossibleColors(r);
        return false;
    }

    public void printColors() {
        for (int i = 0; i < this.size;  i++) {
            regions.get(i).printColor();
        }
    }

    public void printGraph() {
        for (int i = 0; i < this.size;  i++) {
            regions.get(i).printRegion();
            //out.println("\n");
        }
    }
}
