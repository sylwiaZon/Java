import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Programme {
    public static void main(String [] argv){
        Programme programme = new Programme();
        String coding = null;
        String code = null;
        System.out.println("Encode(1) or Decode(2)?");
        coding = programme.read();
        System.out.println("ROT11(1) or Polibiusz(2)?");
        code = programme.read();
        if(code.equals("1")) { //ROT11
            if(coding.equals("1")) { //Encode
                Cryptographer.cryptfile(argv[1],argv[2],new ROT11());
            }
            else if(coding.equals("2")) { //Decode
                Cryptographer.decryptfile(argv[1],argv[2],new ROT11());
            }
        }
        else if(code.equals("2")) { //Polibiusz
            if(coding.equals("1")) { //Encode
                Cryptographer.cryptfile(argv[1],argv[2],new Polibiusz());
            }
            else if(coding.equals("2")) { //Decode
                Cryptographer.decryptfile(argv[1],argv[2],new Polibiusz());
            }
        }
    }
    public String read(){
        String input = null;
        try
        {
            InputStreamReader rd = new InputStreamReader(System.in);
            BufferedReader bfr = new BufferedReader(rd);
            input = bfr.readLine();
        } catch(IOException e){e.printStackTrace();}
        return input;
    }
}
