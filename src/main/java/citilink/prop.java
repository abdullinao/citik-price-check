package citilink;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class prop {

    String mailProtocol;
    String mailHost;
    String mailSmtpAuth;
    String mailUser;
    String mailPass;

    public String getMailProtocol() {
        return mailProtocol;
    }

    public String getMailHost() {
        return mailHost;
    }

    public String getMailSmtpAuth() {
        return mailSmtpAuth;
    }

    public String getMailUser() {
        return mailUser;
    }

    public String getMailPass() {
        return mailPass;
    }

    public prop() {


        try
        {
            File file = new File("settings.properties");

            //Properties prop = new Properties();
            //prop.load(is);
            Properties prop = new Properties();
            prop.load(new FileReader(file));

            mailProtocol= prop.getProperty("mail.transport.protocol");
            mailHost= prop.getProperty("mail.host");
            mailSmtpAuth= prop.getProperty("mail.smtp.auth");
            mailUser= prop.getProperty("mail.user");
            mailPass= prop.getProperty("mail.password");



        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл settings.properties не обнаружен!");
            e.printStackTrace();
        }


    }
}
