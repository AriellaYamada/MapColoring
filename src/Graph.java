import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.out;

public class Graph {

    private int size;
    private LinkedList<Region> regions;
    private Stream<Region> stream;
    private LinkedList<Region> ordered;
    private Comparator<Region> MVR = (r1, r2) -> Integer.compare(r1.nRemainingColors(), r2.nRemainingColors());
    private Comparator<Region> byDegree = (r1, r2) -> Integer.compare(r1.getAdjRegion().size(), r2.getAdjRegion().size());

    public Graph(int size) {
        this.size = size;
        this.regions = new LinkedList<Region>();
        this.ordered = new LinkedList<>();
    }

    public int getSize() {
        return size;
    }

    public void insertVertex(Region r){
        this.regions.add(r);
        this.ordered.add(r);
    }

    public Region getRegion(String region) {
        return regions.stream()
                .filter(r -> r.getName().equals(region))
                .findFirst().get();
    }

    //Retorna todas as regioes adjacentes
    public void getAdjRegions(Region r) {
        if (r.hasAdjRegion()) {
            LinkedList<String> list = r.getAdjRegion();
            this.ordered.clear();
            for (String n : list) {
                Region next = getRegion(n);
                this.ordered.add(next);
            }
        }
    }

    //Busca a proxima regiao adjacente de uma regiao dada
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

    //Busca a proxima regiao com maior quantidade de cores possiveis
    public Region getNextFC() {
        this.ordered.sort(MVR.reversed());
        return this.ordered.pop();
    }

    //Busca a proxima regiao com possibilidades mais restritas
    public Region getNextMVR() {
        return regions.stream().min(Comparator.comparing(r -> r.nRemainingColors())).get();
    }

    public Region getNextByMVRandDegree() {
        if (this.ordered.size() > 0) {
            this.ordered.sort(byDegree.reversed().thenComparing(MVR));
            return this.ordered.pop();
        }
        return null;
    }

    //Verifica se todas as regioes ainda possuem cores possiveis remanescentes
    public boolean fowardChecking () {
        for (Region r : regions)
            if (r.nRemainingColors() == 0) return false;
        return true;
    }

    //Verifica se todas as regioes adjacentes de uma regiao dada possuem cores diferentes
    public boolean verifyAdjRegions(Region region) {
        int color = region.getColor();
        if (region.hasAdjRegion()) {
            LinkedList<String> adjRegions = region.getAdjRegion();
            for (String r : adjRegions) {
                Region adj = getRegion(r);
                if (adj.getColor() == color)
                    return false;
            }
        }
        return true;
    }

    //Atualiza as cores possiveis remanescentes do grafo de acordo com uma coloracao feita
    public void refreshPossibleColors(Region region, int c) {
        region.setColor(c);
        if (region.hasAdjRegion()) {
            for (String name : region.getAdjRegion()) {
                Region r = getRegion(name);
                r.removeRemainingColors(c);
            }
        }
    }

    //Reverte uma coloracao feita caso ela nao seja valida
    public void resetPossibleColors(Region region) {
        if (region.hasAdjRegion()) {
            for (String name : region.getAdjRegion()) {
                Region r = getRegion(name);
                r.insertRemainingColors(r.getColor());
            }
        }
        this.ordered.add(region);
        region.setColor(0);
    }

    public void backtraking(char h) {
        for (Region r : regions) {
            if (r.getColor() == 0) {
                //Escolha de heuristica
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
                        backtrakingFCMVR(r);
                        break;
                    //Backtracking com verificacao adiante, MVR e grau
                    case 'd':
                        r = getNextByMVRandDegree();
                        backtrakingFCMVRByDegree(r);
                        break;
                }
            }
        }
    }

    //Backtraking recursivo simples
    public boolean recursiveBacktracking(Region r) {
        for (int c = 1; c < 5; c++) {
            r.setColor(c);
            getAdjRegions(r);
            if(verifyAdjRegions(r)) {
                Region next = this.ordered.pop();
                while (next != null && next.getColor() != 0)
                    next = this.ordered.pop();
                if (next == null)
                    return true;
                else
                    return recursiveBacktracking(next);
            }
        }
        r.setColor(0);
        return false;
    }

    //Backtraking com verificacao adiante
    public boolean backtrakingFC(Region r) {
        for (int c : r.getRemainingColors()) {
            refreshPossibleColors(r, c);
            if (fowardChecking()) {
                Region next = getNextFC();
                if (next.getColor() == 0)
                    return backtrakingFC(next);
                else return true;
            }
            resetPossibleColors(r);
        }
        return false;
    }

    //Backtraking com verificacao adiante e MVR
    public boolean backtrakingFCMVR(Region r) {
        for (int c : r.getRemainingColors()) {
            refreshPossibleColors(r, c);
            if (fowardChecking()) {
                Region next = getNextByMVRandDegree();
                if (next.getColor() == 0)
                    return backtrakingFCMVR(next);
                else return true;
            }
            resetPossibleColors(r);
        }
        return false;
    }

    //Backtraking com verificacao adiante, MVR e heuristica de grau
    public boolean backtrakingFCMVRByDegree(Region r) {
        if (r != null) {
            for (int c : r.getRemainingColors()) {
                refreshPossibleColors(r, c);
                if (fowardChecking()) {
                    Region next = getNextByMVRandDegree();
                    if (next != null && next.getColor() == 0)
                        return backtrakingFCMVRByDegree(next);
                    else return true;
                }
                resetPossibleColors(r);
            }
        }
        return false;
    }

    //Imprime as cores do mapa
    public void printColors() {
        for (int i = 0; i < this.size;  i++) {
            regions.get(i).printColor();
        }
    }

    //Imprime o grafo
    public void printGraph() {
        for (int i = 0; i < this.size;  i++) {
            regions.get(i).printRegion();
            //out.println("\n");
        }
    }
}
