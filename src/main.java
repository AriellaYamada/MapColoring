import java.util.Scanner;
import static java.lang.System.*;

public class main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(in);

        FileReader buffer = new FileReader(scanner.nextLine());
        String[] fline = buffer.readLine().split(" ");
        int size = Integer.parseInt(fline[0]);
        char heuristic = fline[1].charAt(0);

        //Inicializacao do grafo
        Graph graph = new Graph(size);
        for (int i = 0; i < size; i++) {
            String line = buffer.readLine();
            String[] name = line.split(":");
            String[] aux = name[name.length - 1].split("\\.");
            String[] edges;
            if (aux.length > 0) {
                edges = aux[0].split(",");
                int n = 0;
                for (String a : edges) {
                    edges[n] = a.substring(1);
                    n++;
                }
            } else {
                edges = null;
            }
            Region region = new Region(name[0], edges); //Cria uma nova regiao
            graph.insertVertex(region);                 //Insere regiao no grafo
        }

        graph.backtraking(heuristic);

        graph.printColors();
    }
}
