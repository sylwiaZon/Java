public class ROT11 implements Algorithm {
    public static Character[] alphabet;
    public static int rotation;
    public ROT11(){
        alphabet = new Character[52];
        for(int i=0;i<26;i++) {
            alphabet[i] = (char)('a' + i);
        }
        for(int i=0;i<26;i++) {
            alphabet[i] = (char)('A' + i);
        }
        rotation = 11;
    }
    public String crypt(String word) {
        Character[] response = new Character[word.length()];
        for(int i=0; i<word.length();i++){
            if(word.charAt(i) <= 'Z') {
                response[i] = (char) ((word.charAt(i) - 'A' + rotation) % 26 + 'A');
            } else {
                response[i] = (char) ((word.charAt(i) - 'a' + rotation) % 26 + 'a');
            }
        }
        return toString(response);
    }
    public String decrypt(String word) {
        Character[] response = new Character[word.length()];
        for(int i=0; i<word.length();i++){
            int rotate = word.charAt(i) - 'A' - rotation;
            if(rotate < 0 ){
                response[i] = (char)('Z' + rotate + 1);
            } else {
                response[i] = (char)('A' + rotate);
            }

        }
        return toString(response);
    }
    private String toString(Character[] word){
        String response = "";
        for(int i=0;i<word.length;i++){
            response = response + word[i];
        }
        return response;
    }
}
