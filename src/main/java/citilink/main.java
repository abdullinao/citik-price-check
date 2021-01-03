package citilink;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * #говнокод
 */


public class main {
    private static prop prop2 = new prop();

    public static void main(String[] args) throws InterruptedException, MessagingException {


        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        Scanner sc = new Scanner(System.in);
        // Создаем экземпляр WebDriver
        // Следует отметить что скрипт работает с интерфейсом,
        // а не с реализацией.
        WebDriver driver = new FirefoxDriver();
        String loginUrl = prop2.getLoginpage();
        driver.get(loginUrl);
        Thread.sleep(4000);
        // Находим элемент по атрибуту name
        WebElement element = driver.findElement(By.xpath("//div[contains(@class,'Captcha__image')]"));

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        Object elementAttributes = executor.executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", element);

        System.out.println(elementAttributes.toString());

        while (driver.getCurrentUrl().equals(loginUrl)) {
            System.out.println("Жду пока пройдет авторизация");
            Thread.sleep(4000);
        }

        System.out.println("Пiймав на авторизации");

        // String searchUrl = "";

        int count = 0;
        try {

            while (1 == 1) {
//проверка что авторизация не слетела
                try {
                    element = driver.findElement(By.xpath("//div[contains(@class,'HeaderUserName__name')]"));
                } catch (NoSuchElementException n) {
                    element = driver.findElement(By.xpath("//div[contains(@class,'IconAndTextWithCount__text_mainHeader IconAndTextWithCount__text')]"));
                }
                String userNonFormated = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                String userFormated = userNonFormated.replaceAll("\\s+", "");
                System.out.println("авторизация под пользователем " + userFormated);
                System.out.println("Ожидаю пользователя " + prop2.getUserName() );
                if (!userFormated.equalsIgnoreCase(prop2.getUserName())) {
                    System.out.println("не залогинен");
                    SendMailBad();
                }

                count++;
                driver.get(prop2.getFilterUrl());

                System.out.println("Жду пока страница прогрузится");
                Thread.sleep(4000);

//выводит все товары со страницы
//        System.out.println("анализ хуялиз");
//
//        List<WebElement> products = (new WebDriverWait(driver, 10))
//                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className("ProductCardCategoryList__grid")));
//
//        for (WebElement elements : products) {
//            System.out.println("22222222");
//            System.out.println("Paragraph text:" + elements.getText());
//        }

                // System.out.println(products.get(0));
                System.out.println("ищу самую дешевую карточку...");

                element = driver.findElement(By.xpath("//a[contains(@class,' ProductCardVertical__name  Link js--Link Link_type_default')]"));
                String name = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                System.out.println("самая дешевая карта: " + name);

                element = driver.findElement(By.xpath("//span[contains(@class,'ProductCardVerticalPrice__price-current_current-price')]"));
                String price = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                String priceFormated = price.replaceAll("\\s+", "");
                System.out.println("цена: " + price.replaceAll("\\s+", ""));


                int priceInt = Integer.parseInt(priceFormated);


                if (priceInt < prop2.getMaxPrice()) {
                    System.out.println("отправка сообщения");
                    SendMailOk(priceInt, name);
                    System.out.println("успешных проверок " + count);
                    System.out.println("таймер 2 мин");
                    Thread.sleep(120000);
                } else {
                    System.out.println("нет карт дешевле " + prop2.getMaxPrice());
                    System.out.println("успешных проверок " + count);
                    System.out.println("таймер 2 мин");
                    Thread.sleep(120000);

                }


                // System.out.println(element);


                //  System.out.println("Введи логин:");
                //  String login = sc.nextLine();
                //  System.out.println("Введи пасс:");
                //  String pwd = sc.nextLine();
                //  System.out.println("Введи капчу:");
                //  String kaptcha = sc.nextLine();


                //  driver.findElement(By.name("login")).sendKeys("your value");

//         element = driver.findElement(By.name("login")); // you can use any locator
//        JavascriptExecutor jse = (JavascriptExecutor) driver;
//        jse.executeScript("arguments[0].value='"+login+"';", element);
//
//        element = driver.findElement(By.name("pass")); // you can use any; locator
//         jse = (JavascriptExecutor) driver;
//        jse.executeScript("arguments[0].value='"+pwd+"';", element);
//
//        element = driver.findElement(By.name("captcha")); // you can use any locator
//         jse = (JavascriptExecutor) driver;
//        jse.executeScript("arguments[0].value='"+kaptcha+"';", element);


//        element = driver.findElement(By.name("login"));
//        element.click();
//        element.submit();
//        element.sendKeys(login);

//        element = driver.findElement(By.name("pass"));
//        element.click();
//        element.sendKeys(pwd);
//
//        element = driver.findElement(By.name("captcha"));
//        element.click();
//        element.sendKeys(kaptcha);
            }
        } catch (Exception e) {

            SendMailBad();
        }
    }


    public static void SendMailOk(int price, String Name) throws MessagingException {
        Properties MailProps = new Properties();
        MailProps.put("mail.transport.protocol", prop2.getMailProtocol());
        MailProps.put("mail.smtp.host", prop2.getMailHost());
        MailProps.put("mail.smtp.auth", prop2.getMailSmtpAuth());
        // MailProps.put("mail.smtp.sendpartial", "true");
        MailProps.put("mail.smtp.ssl.enable", "true");
        MailProps.put("mail.user", prop2.getMailUser());
        MailProps.put("mail.password", prop2.getMailPass());

        System.out.println("получаю сессию для почты");
        Session session = Session.getDefaultInstance(MailProps);
        //создаем сообщение
        System.out.println("делаю письмо");
        MimeMessage message = new MimeMessage(session);

//устанавливаем тему письма
        message.setSubject("[От бота] " + price + " руб! " + "Дешевка в ситилинке!");

//добавляем текст письма
        message.setText("В ситилинке появилась дешевая карта! \n"
                + Name + "\n" + "цена: " + price +
                "\nссылка(удалить пробелы)    vk . cc/bWNbp9");

//указываем получателя
        String[] mails = prop2.getMailTo().split(",");

        for (String mail : mails) {
            System.out.println("Готовлю письмо для " + mail);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        }
        //message.addRecipient(Message.RecipientType.TO, new InternetAddress(prop2.getMailTo()));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("ceo@devcorp.ru"));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("info@devcorp.ru"));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("mashkin.efim@otr.ru"));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("mashkinefim@yandex.ru"));

//указываем дату отправления
        message.setSentDate(new Date());
        System.out.println("отправляю");
        Transport transport = session.getTransport();
        System.out.println("получил транспорт");
        transport.connect(prop2.getMailHost(), 465, prop2.getMailUser(), prop2.getMailPass());
        System.out.println("получил коннект");
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        System.out.println("отправил");
    }


    public static void SendMailBad() throws MessagingException {
        System.out.println("отправляю емейл ошибки");
        Properties MailProps = new Properties();
        MailProps.put("mail.transport.protocol", prop2.getMailProtocol());
        MailProps.put("mail.smtp.host", prop2.getMailHost());
        MailProps.put("mail.smtp.auth", prop2.getMailSmtpAuth());
        // MailProps.put("mail.smtp.sendpartial", "true");
        MailProps.put("mail.smtp.ssl.enable", "true");
        MailProps.put("mail.user", prop2.getMailUser());
        MailProps.put("mail.password", prop2.getMailPass());

        System.out.println("получаю сессию для почты");
        Session session = Session.getDefaultInstance(MailProps);
        //создаем сообщение
        System.out.println("делаю письмо");
        MimeMessage message = new MimeMessage(session);

//устанавливаем тему письма
        message.setSubject("[От бота] Нужно участие человека");

//добавляем текст письма
        message.setText("бот не работает!");

//указываем получателя
        String[] mails = prop2.getMailTo().split(",");

        for (String mail : mails) {
            System.out.println("Готовлю письмо для " + mail);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        }
       // message.addRecipient(Message.RecipientType.TO, new InternetAddress(prop2.getMailTo()));
        //  message.addRecipient(Message.RecipientType.TO, new InternetAddress("ceo@devcorp.ru"));
        //  message.addRecipient(Message.RecipientType.TO, new InternetAddress("info@devcorp.ru"));
        //message.addRecipient(Message.RecipientType.TO, new InternetAddress("mashkin.efim@otr.ru"));
        // message.addRecipient(Message.RecipientType.TO, new InternetAddress("mashkinefim@yandex.ru"));

//указываем дату отправления
        message.setSentDate(new Date());
        System.out.println("отправляю");
        Transport transport = session.getTransport();
        System.out.println("получил транспорт");
        transport.connect(prop2.getMailHost(), 465, prop2.getMailUser(), prop2.getMailPass());
        System.out.println("получил коннект");
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        System.out.println("отправил");
    }
}
