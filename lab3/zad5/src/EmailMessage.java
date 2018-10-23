import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.LinkedList;
import java.util.Properties;

public class EmailMessage {
    private final String from;
    private final LinkedList<String> to;
    private final String subject;
    private final String content;
    private final String mimeType;
    private final LinkedList<String> cc;
    private final LinkedList<String> bcc;

    private EmailMessage(Builder _builder) {
        this.from = _builder.from;
        this.to = _builder.to;
        this.subject = _builder.subject;
        this.content = _builder.content;
        this.mimeType = _builder.mimeType;
        this.cc = _builder.cc;
        this.bcc = _builder.bcc;
    }

    public static class Builder {
        private String from;
        private LinkedList<String> to;
        private String subject;
        private String content;
        private String mimeType;
        private LinkedList<String> cc;
        private LinkedList<String> bcc;

        public Builder(String _from, LinkedList<String> _to) throws MailException {
            if (checkMail(_from)) {
                from = _from;
            } else {
                throw new MailException("Wrong from field");
            }
            if (checkMails(_to)) {
                to = _to;
            } else {
                throw new MailException("Wrong to field");
            }
        }

        private boolean checkMails(LinkedList<String> mails) {
            for (String mail : mails) {
                if (!checkMail(mail)) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkMail(String mail) {
            return mail.matches(".*@.*\\..*");
        }

        public Builder addSubject(String _subject) {
            subject = _subject;
            return this;
        }

        public Builder addContent(String _content) {
            content = _content;
            return this;
        }

        public Builder addMimeType(String _mimeType) {
            mimeType = _mimeType;
            return this;
        }

        public Builder addCc(LinkedList<String> _cc) throws MailException {
            if (checkMails(_cc)) {
                cc = _cc;
                return this;
            } else {
                throw new MailException("Wrong cc field");
            }
        }

        public Builder addBcc(LinkedList<String> _bcc) throws MailException {
            if (checkMails(_bcc)) {
                bcc = _bcc;
                return this;
            } else {
                throw new MailException("Wrong cc field");
            }
        }

        public EmailMessage build() {
            return new EmailMessage(this);
        }

    }
    public void send() {
        String password = new String("password");
        Properties properties = System.getProperties();
        String hostName = "smtp.gmail.com";
        properties.put("mail.smtp.host", hostName);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.debug", true);
        properties.put("mail.smtp.port", 465);
        properties.put("mail.smtp.socketFactory.port", 465);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.from));
            InternetAddress[] internetAddresses = new InternetAddress[to.size()];
            int i=0;
            for (String mail: to) {
                if(i < to.size()) {
                    internetAddresses[i] = new InternetAddress(mail);
                }
                i++;
            }
            message.setRecipients(Message.RecipientType.TO,internetAddresses);
            message.setSubject("Testing");
            message.setText(this.content);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}