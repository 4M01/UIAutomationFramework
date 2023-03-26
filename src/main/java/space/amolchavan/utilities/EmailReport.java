package space.amolchavan.utilities;

import space.amolchavan.driverFactory.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static space.amolchavan.reporting.ExtentReporter.now;


public class EmailReport {
    String url = "";
    public EmailReport(String resultPath) {
        this.url = resultPath;
        createScreenshotForEmail();
    }

    private void createScreenshotForEmail() {
        try {
            WebDriver driver = new DriverFactory().createInstance("chrome");
            driver.manage().window().maximize();
            driver.get(url);
            driver.findElement(By.id("nav-dashboard")).click();
            String subject = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/ul[2]/li[1]/a/span")).getText();
            String imagePath = DriverUtils.takeFullPageScreenShot(driver);
            driver.close();
            driver.quit();
            createEmailBody(url, subject, imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createEmailBody(String link, String subject, String imagePath) {
        String from = "2amolchavan@gmail.com";
        String pass = "************";
        String[] to = { "amolchavan10@gmail.com" }; // list of recipient email addresses
        String body = "Link to the public result page - "+link;
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            String formattedDate = now("dd MMM yyyy hh:mm:ss");
            message.setSubject(subject + " execution results dated on "+formattedDate );
//            message.setText(body);
            MimeMultipart multipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            String htmlText = "Hi Team, <br> Test execution is completed for "+subject+". <H4>"+body+"</H4><img src=\"cid:image\">";
            messageBodyPart.setContent(htmlText, "text/html");
            multipart.addBodyPart(messageBodyPart);
            // second part (the image)
            messageBodyPart = new MimeBodyPart();
            DataSource fds = new FileDataSource(imagePath);
            messageBodyPart.setDataHandler(new DataHandler(fds));
            messageBodyPart.setHeader("Content-ID", "<image>");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtps");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }

    }



    public void sendEmailTo(String s) {
    }
}
