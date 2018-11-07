import java.io.*;
import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subtitles {
    public void delay(String in, String out, int delay, int fps){
        try {
            BufferedReader read = new BufferedReader(new FileReader(in));
            BufferedWriter write = new BufferedWriter(new FileWriter(out));
            String line;
            String values[];
            int i=0;
            int difference = delay / 1000 * fps;
            while ((line = read.readLine())!= null) {
                i++;
                values = validate(line,i);
                int startFrame = toInt(values[0]) + difference;
                int endFrame = toInt(values[1]) + difference;
                if(startFrame < 0) startFrame = 0;
                if(endFrame < 0) endFrame = 0;
                values[0] = toString(startFrame);
                values[1] = toString(endFrame);
                write.write('{' + values[0] + '}' + '{' + values[1] + '}' + values[2]);
            }
            write.close();
        }catch(Exception e){e.printStackTrace();}
    }
    private String[] validate(String line, int lineNumber) throws Exception {
        String values[] = new String[3];
        Pattern pattern = Pattern.compile("\\{([0-9]+)\\}\\{([0-9]+)\\}(.*)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            if (toInt(matcher.group(1)) <= toInt(matcher.group(2))){
                for(int i=0; i<3; i++){
                    values[i] = matcher.group(i+1);
                }
                return values;
            } else{
                throw new SequenceException(line,lineNumber);
            }
        } else {
            throw new FormatException(line, lineNumber);
        }
    }
    private int toInt(String val){
        int number = 0;
        for(int i=0; i<val.length(); i++){
            number = number * 10 + val.charAt(i) - '0';
        }
        return number;
    }
    private String toString(int val){
        StringBuilder number = new StringBuilder();
        number.append(val);
        return number.toString();
    }
}
