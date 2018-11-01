import java.io.*;
import java.util.Scanner;

public class Cryptographer {
    public static void cryptfile(String fileToEncode, String fileToSave, Algorithm algorithm){
        try {
            Scanner read = new Scanner(new File(fileToEncode));
            BufferedWriter write = new BufferedWriter(new FileWriter(fileToSave));
            String word;
            while(read.hasNext()) {
                word = read.next();
                if(!word.equals(" ")) {
                    String encoded = algorithm.crypt(word);
                    write.write(encoded);
                } else {
                    write.write(" ");
                }
            }
            write.close();
        }catch(IOException e){e.printStackTrace();}
    }
    public static void decryptfile(String fileToDecode, String fileToSave, Algorithm algorithm){
        try {
            Scanner read = new Scanner(new File(fileToDecode));
            BufferedWriter write = new BufferedWriter(new FileWriter(fileToSave));
            String word;
            while(read.hasNext()) {
                word = read.next();
                if(!word.equals(" ")) {
                    String decoded = algorithm.decrypt(word);
                    write.write(decoded);
                } else {
                    write.write(" ");
                }
            }
            write.close();
        }catch(IOException e){e.printStackTrace();}
    }
}
