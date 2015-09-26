import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import static java.lang.System.*;

public class FileReader {

    public String filename;
    public File file;
    public RandomAccessFile buffer;

    public FileReader (String filename) {
        this.filename = filename;
        try {
            this.file = new File(filename);
            this.buffer = new RandomAccessFile(this.file, "r");
        } catch (IOException e) {
            out.println("Erro na leitura da entrada");
        }
    }

    public String readLine() {
        try {
            return buffer.readLine();
        } catch (IOException e) {
            out.println("Erro na leitura do arquivo");
        }
        return null;
    }
}
