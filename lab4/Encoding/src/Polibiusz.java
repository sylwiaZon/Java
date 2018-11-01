public class Polibiusz implements Algorithm {
    public String crypt(String word) {
        word = word.toUpperCase();
        String[] response = new String[word.length()];
        int xValue;
        int yValue;
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i) > 'J') {
                xValue = (word.charAt(i) / 5) - 12;
                yValue = (word.charAt(i) % 5);
            }else if(word.charAt(i) < 'I'){
                xValue = (word.charAt(i) / 5) - 12;
                yValue = (word.charAt(i) % 5) + 1;
            }else{
                xValue = 2;
                yValue = 4;
            }

            response[i] = String.valueOf(xValue) + String.valueOf(yValue);
        }
        return stringToString(response);
    }
    public String decrypt(String word) {
        Character[] response = new Character[1];
        int xValue;
        int yValue;
        int codedNumber = (word.charAt(0) - '0') * 10 + word.charAt(1) - '0';
        if(codedNumber > 24) {
            xValue = ((codedNumber / 10) + 12) * 5;
            yValue = codedNumber % 10;
            response[0] = (char)(xValue + yValue);
        }else if(codedNumber < 24 ){
            xValue = ((codedNumber / 10) + 12) * 5;
            yValue = codedNumber % 10 - 1;
            response[0] = (char)(xValue + yValue);
        }else {
            response[0] = 'I';
        }

        return charToString(response);
    }
    private String charToString(Character[] word){
        String response = "";
        for(int i=0;i<word.length;i++){
            response = response + word[i];
        }
        return response;
    }
    private String stringToString(String[] word){
        String response = "";
        for(int i=0;i<word.length;i++){
            response = response + word[i] + " ";
        }
        return response;
    }
}
