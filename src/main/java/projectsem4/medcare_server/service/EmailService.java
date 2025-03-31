package projectsem4.medcare_server.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@EnableAsync
public class EmailService {

  @Value("${spring.mail.host}")
  private String host;
  @Value("${spring.mail.port}")
  private String port;
  @Value("${spring.mail.username}")
  private String usernameemail;
  @Value("${spring.mail.password}")
  private String passwordemail;
  @Value("${spring.mail.properties.mail.smtp.auth}")
  private String auth;
  @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private String enable;

  @Async
  public void sendEmail(String to, String subject, String body) {
    Properties properties = new Properties();
    properties.put("mail.smtp.auth", auth);
    properties.put("mail.smtp.starttls.enable", enable);
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", port);

    final String username = usernameemail;
    final String password = passwordemail;

    Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(subject);
      // message.setText(body);

      String htmlContent = "<html>" +
          "<body style='font-family: Arial, sans-serif;'>" +
          "<h1 style='color: rgb(105, 153, 168);'>Welcome to Medcare!</h1>" +
          "<p style='color: #264653;'>Thank you for joining us.</p>" +
          "<div>" + body + "</div>" + // Chèn nội dung body vào đây
          "<p style='font-size: 14px; color: #333;'>For any festivals, please contact directly at phone number: 0987654321.</p>"
          +
          "<img src='https://firebasestorage.googleapis.com/v0/b/medcare-9db1e.appspot.com/o/logo1blue.png?alt=media' "
          +
          "alt='Medcare Image' style='width:40%; max-width:300px; margin:auto; magin-top:4rem;' />" +
          "</body>" +
          "</html>";
      message.setContent(htmlContent, "text/html; charset=UTF-8");
      // Gửi email
      Transport.send(message);
      System.out.println("Email sent successfully!");

    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
