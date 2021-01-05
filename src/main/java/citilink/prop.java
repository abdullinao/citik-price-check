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
    int maxPrice;
    int timer;
    String loginpage;
    String filter;
    String userName;
    String mailTo;

    public prop() {


        try {
            File file = new File("settings.properties");

            //Properties prop = new Properties();
            //prop.load(is);
            Properties prop = new Properties();
            prop.load(new FileReader(file));

            mailProtocol = prop.getProperty("mail.transport.protocol");
            mailHost = prop.getProperty("mail.host");
            mailSmtpAuth = prop.getProperty("mail.smtp.auth");
            mailUser = prop.getProperty("mail.user");
            mailPass = prop.getProperty("mail.password");
            maxPrice = Integer.parseInt(prop.getProperty("maxPrice"));
            timer = Integer.parseInt(prop.getProperty("timer"));
            loginpage = prop.getProperty("loginPage");
            filter = prop.getProperty("filter");
            userName = prop.getProperty("userName");
            mailTo = prop.getProperty("mailTo");


        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл settings.properties не обнаружен!");
            e.printStackTrace();
        }


    }


    public String getMailProtocol() {
        return mailProtocol;
    }

    public int getTimer() {
        return timer;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getUserName() {
        return userName;
    }

    public String getFilterUrl() {
        return filter;
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

    public int getMaxPrice() {
        return maxPrice;
    }

    public String getLoginpage() {
        return loginpage;
    }
}
