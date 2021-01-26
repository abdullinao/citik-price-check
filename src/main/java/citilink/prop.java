package citilink;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class prop {

 private String mailProtocol;
 private String mailHost;
 private String mailSmtpAuth;
 private String mailUser;
 private String mailPass;
 private int maxPrice;
 private int minPrice;
 private int timer;
 private String loginpage;
 private String filter;
 private String userName;
 private String mailTo;
 private String apiKey;
 private String channelName;
 private String channelAdm;
 private String logChannelName;
 private String city;


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
            minPrice = Integer.parseInt(prop.getProperty("minPrice"));
            timer = Integer.parseInt(prop.getProperty("timer"));
            loginpage = prop.getProperty("loginPage");
            filter = prop.getProperty("filter");
            userName = prop.getProperty("userName");
            mailTo = prop.getProperty("mailTo");
            apiKey = prop.getProperty("apikey");
            channelName = prop.getProperty("channelName");
            channelAdm = prop.getProperty("channelAdm");
            logChannelName = prop.getProperty("logChannelName");
            city = prop.getProperty("city");


        } catch (IOException e) {
            System.out.println("Ошибка в программе: файл settings.properties не обнаружен!");
            e.printStackTrace();
        }


    }

    public String getApiKey() {
        return apiKey;
    }
    public int getminPrice() {
        return minPrice;
    }
    public String getCity() {
        return city;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelAdm() {
        return channelAdm;
    }
    public String getlogChannelName() {
        return logChannelName;
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
