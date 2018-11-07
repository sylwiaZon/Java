public class SequenceException extends Exception{
    SequenceException(String line, int lineNumber){
        super("Wrong sequence in line: " + lineNumber + "  " + line);
    }
}
