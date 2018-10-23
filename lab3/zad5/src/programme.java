import java.util.LinkedList;
import java.util.Properties;

public class programme {
    public static void main (String[] argv){
        try {
            LinkedList<String> to = new LinkedList<String>();
            to.add("mailTo@gmail.com");
            EmailMessage emailMessage = new EmailMessage.Builder("mail@mail.com",to).addSubject("Hello World").addContent("Mail!").build();
            emailMessage.send();
        } catch (MailException e) {
            e.printStackTrace();
        }

    }
}
