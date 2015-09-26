import java.util.LinkedList;
import java.util.List;
import static java.lang.System.out;

public class Region {

    private String name;
    private int color;
    private LinkedList<String> adjRegion;
    private List<Integer> remainingColors;

    //Construtor
    public Region(String name, String[] edges) {
        this.name = name;
        this.color = 0;
        adjRegion = new LinkedList<String>();
        if (edges != null) {
            for(int i = 0; i < edges.length; i++){
                adjRegion.add(edges[i]);
            }
        }
        this.remainingColors = new LinkedList<>();
        for (int i = 1; i < 5; i++) {
            this.remainingColors.add(i);
        }
    }

    public String getName(){return this.name;}

    public void setColor(int color) {
        this.color = color;
        this.remainingColors.clear();
        this.remainingColors.add(color);
    }

    public void resetRemainingColors() {
        this.remainingColors.clear();
        for (int c = 1; c < 5; c++)
            this.remainingColors.add(c);
    }

    public void resetRemainingColors(int c) { this.remainingColors.add(c); }

    public int getColor() {return this.color;}

    public LinkedList<String> getAdjRegion() {return this.adjRegion;}

    public List<Integer> getRemainingColors() { return this.remainingColors; }

    public void insertRemainingColors(Integer color) { this.remainingColors.add(color); }

    public void removeRemainingColors(Integer color) { this.remainingColors.remove(color); }

    public int nRemainingColors() { return this.remainingColors.size(); }

    public boolean hasAdjRegion() { return (adjRegion.size() > 0); }

    public void printColor() {
        String c = "";
        switch (color) {
            case 1:
                c = "Azul";
                break;
            case 2:
                c = "Amarelo";
                break;
            case 3:
                c = "Verde";
                break;
            case 4:
                c = "Vermelho";
                break;
        }
        out.print(name + ": " + c + ".\n");
    }

    public void printRegion() {
        out.print(name + ": ");
        for(int i = 0; i < adjRegion.size(); i++){
            out.print(adjRegion.get(i) + " ");
        }
    }
}
