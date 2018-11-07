public class programme {
    public static void main(String[] argv){
        try {
            Subtitles subtitles = new Subtitles();
            if(argv.length < 5) {
                throw new Exception("Wrong parameters");
            }
            String inFile = argv[1];
            String outFile = argv[2];
            int delay = Integer.parseInt(argv[3]);
            int framerate = Integer.parseInt(argv[4]);
            subtitles.delay(inFile, outFile, delay, framerate);
        }
        catch(Exception e){e.printStackTrace();}
        catch(Throwable e){System.err.println("Unknown exception in line: " + e.getStackTrace()[0].getLineNumber());}
    }
}
