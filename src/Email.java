package sample;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Email {
    private String senderEmail;
    private String senderPassword;

    public Email(String senderEmail, String senderPassword) {
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
    }

    public void sendEmail(String[] recipientAddresses, String content) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true"); //Gmail requires authentication
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com"); //Gmail specific
        properties.put("mail.smtp.port", "587"); //Gmail specific

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });


        String emailsString = String.join(",", recipientAddresses); //convert the String Array to String separated by commas
        try {
            MimeMessage message = new MimeMessage(session);

            //InternetAddress[] myBccList = InternetAddress.parse();
            InternetAddress[] myCcList = InternetAddress.parse(emailsString);

            message.setFrom(new InternetAddress(senderEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(senderEmail)); //send to the sender as well
            //message.addRecipients(Message.RecipientType.BCC,myBccList); //add BCC recipients
            message.addRecipients(Message.RecipientType.CC,myCcList); //add CC recipients
            message.setSubject("Video call invite");
            message.setContent(content,"text/html");

            Transport.send(message); //send the email!

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //There are email validation APIs
    public boolean authentiate() {
        return true; //TODO: Attempt to log in with this.senderEmail and this.senderPassword, check if it's valid or not
    }
}
