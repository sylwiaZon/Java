public class FormatException extends Exception{
    public FormatException(String line, int lineNumber){
        super("Wrong format in line: " + lineNumber + "  " + line);
    }
}
