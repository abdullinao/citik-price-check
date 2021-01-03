package citilink;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.*;

public class main {
    private static prop prop2 = new prop();

    public static void main(String[] args) throws InterruptedException, MessagingException {


        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
        Scanner sc = new Scanner(System.in);
        // Создаем экземпляр WebDriver
        // Следует отметить что скрипт работает с интерфейсом,
        // а не с реализацией.
        WebDriver driver = new FirefoxDriver();
        String loginUrl = "https://www.citilink.ru/login/";
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

        String searchUrl = "https://www.citilink.ru/catalog/computers_and_notebooks/parts/videocards/?f=available.all%2Cdiscount.any%2C9368_29amdd1d1radeond1rxd16800%2C9368_29amdd1d1radeond1rxd16800xt%2C9368_29amdd1d1radeond1rxd16900xt%2C9368_29nvidiad1d1geforced1rtxd13060ti%2C9368_29nvidiad1d1geforced1rtxd13070%2C9368_29nvidiad1d1geforced1rtxd13080%2C9368_29nvidiad1d1geforced1rtxd13090&sorting=price_asc";
       while(1==1) {
           driver.get(searchUrl);

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


           if (priceInt < 50000) {
               SendMailOk(priceInt, name);
               System.out.println("sending email");
           } else {
               System.out.println("timer 4 min");
               Thread.sleep(240000);
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
        message.setSubject("[От бота] "+ price + " rub. "+"Дешевка в ситилинке!");

//добавляем текст письма
        message.setText("В ситилинке появилась дешевая карта! \n"
                + Name + " " + "цена: " + price +
                "ссылка  citilink точка ru/catalog/computers_and_notebooks/parts/videocards/?f=available.all%2Cdiscount.any%2C9368_29amdd1d1radeond1rxd16800%2C9368_29amdd1d1radeond1rxd16800xt%2C9368_29amdd1d1radeond1rxd16900xt%2C9368_29nvidiad1d1geforced1rtxd13060ti%2C9368_29nvidiad1d1geforced1rtxd13070%2C9368_29nvidiad1d1geforced1rtxd13080%2C9368_29nvidiad1d1geforced1rtxd13090&sorting=price_asc");

//указываем получателя
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("ceo@devcorp.ru"));


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
