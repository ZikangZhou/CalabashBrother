import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class IOTest {
    @Test
    public void bufferedReaderTest() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("test")));
            String line = reader.readLine();
            System.out.println(line.isEmpty());
            System.out.println("'"+line+"'");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
