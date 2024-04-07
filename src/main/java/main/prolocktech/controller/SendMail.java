package main.prolocktech.controller;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class SendMail {
    private final String from = "prolocktechming@gmail.com";
    private final String password = "ilhctgcobniuovfr";
    public String sendMail(String to){
        // Cài đặt thuộc tính cho SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//SMTP yêu cầu xác thực
        props.put("mail.smtp.starttls.enable", "true");//Kích hoạt TLS (Transport Layer Security) cho kết nối SMTP.
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.host", "smtp.gmail.com");//Cài đặt máy chủ SMTP

        //Tạo cổng vào từ email, password có sẵn
        Authenticator auth = new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        };
        //Tạo phiên làm việc
        Session session = Session.getInstance(props, auth);

        //Tạo 1 tin nhắn
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            //người gửi
            mimeMessage.setFrom(new InternetAddress(from));
            //Đặt người nhận
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //Tiêu đề
            mimeMessage.setSubject("Your OTP");
            //Nội dung
            Random random = new Random();
            String otp = String.valueOf(random.nextInt(1000, 9999));
            mimeMessage.setContent("Your OTP is: " + otp, "text/html");
            //Gửi email
            Transport.send(mimeMessage);
            return otp;
        } catch (MessagingException e) {
            System.out.println("Error:" + e.getMessage());
        }
        return null;
    }
}
