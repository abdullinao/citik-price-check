package citilink;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * #говнокод
 */


public class main {
    private static final prop prop2 = new prop();

    public static void main(String[] args) throws InterruptedException, MessagingException, IOException {


        System.setProperty("webdriver.gecko.driver", "geckodriver.exe");

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
        int count = 0;
        int banCount = 0;
        int downCount = 0;
        // String searchUrl = "";
        check(driver, element, count, banCount, downCount);

    }

    //first run 1 = true ; 0 = false
    public static void check(WebDriver driver, WebElement element, int count2, int banCount2, int downCount) throws IOException, MessagingException, InterruptedException {


        int count = count2;
        int downC = downCount;
        int banCount = banCount2;
        int timer = prop2.getTimer();
        String productCount = "";
        boolean productsEmpty;
        String priceFormated = "";
        String name = "";
        int priceInt;


        int minPrice = prop2.getminPrice();
        try {

            while (1 == 1) {
                try {
                    driver.get(prop2.getFilterUrl());
                    System.out.println("Жду пока страница прогрузится...");
                    Thread.sleep(5000);//можно сделать через веит, если захочешь
                } catch (Exception e) {
                    e.printStackTrace();
                    tgMessage(0, "null ", 0, "bad", "null", e.toString(), 0, "null", 0);

                }
//проверка на отсутсвтие товаров
                try {
                    element = driver.findElement(By.xpath("//h2[contains(@class,'ProductCardCategoryList__empty-list-title')]"));//ищем колво товаров в категории
                    productsEmpty = true;
                } catch (NoSuchElementException n) {
                    System.out.println("товары есть");
                    productsEmpty = false;
                }


                try {
                    element = driver.findElement(By.xpath("//div[contains(@class,'Subcategory__count js--Subcategory__count')]"));//ищем колво товаров в категории
                    String productCountUnFormated = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                    productCount = productCountUnFormated.replaceAll("\\s+", "");
                    productCount = productCount.replaceAll("товаров|товар|товара|а", "");//буква "а" непобедима
                } catch (NoSuchElementException n) {
                    System.out.println("не нашел товаров");
                    n.printStackTrace();
                }


                System.out.println("проверяю на бан...");

                try {
                    System.out.println("ищу элемент капча");
                    element = driver.findElement(By.xpath("//input[contains(@class,'captcha-input request-limit-page__captcha-input input__field_error')]"));//ищем бан путем поиска строки с капчей
                    System.out.println("ввожу буквы");
                    element.sendKeys("abcdefg");
                    System.out.println("отправляю");
                    element.submit();
                    System.out.println("нашел и попытался устранить бан");
                    banCount++;
                    System.out.println("ban coubt" + banCount);
                    tgMessage(0, "name", count, "ban", "null", "null", banCount, "null", 0);
                    Thread.sleep(5000);//можно сделать через веит, если захочешь

                } catch (NoSuchElementException n) {
                    System.out.println("не нашел бан");
                }


                //проверка города

                try {
                    System.out.println("ищу авторизацию");
                    element = driver.findElement(By.xpath("//button[contains(@class,'js--CitiesSearch-trigger MainHeader__open-text TextWithIcon')]"));//
                    //пользователя в случае, если есть авторизация на сайте
                } catch (NoSuchElementException n) {
                    System.out.println("не нашел город");
                }


                String cityUnFormated = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                String cityFormated = cityUnFormated.replaceAll("\\s+", "");
                System.out.println(cityFormated);


//проверка что авторизация не слетела
                try {
                    System.out.println("ищу авторизацию");
                    element = driver.findElement(By.xpath("//div[contains(@class,'HeaderUserName__name')]"));//сюда пишется имя
                    //пользователя в случае, если есть авторизация на сайте
                } catch (NoSuchElementException n) {
                    element = driver.findElement(By.xpath("//div[contains(@class,'IconAndTextWithCount__text_mainHeader IconAndTextWithCount__text')]"));
                }
                String userNonFormated = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                String userFormated = userNonFormated.replaceAll("\\s+", "");
                System.out.println("авторизирован под пользователем " + userFormated);
                System.out.println("Ожидаю пользователя " + prop2.getUserName());
                //проверка совпадает ли текущий пользователем с тем что в пропертях
                if (!userFormated.equalsIgnoreCase(prop2.getUserName())) {
                    System.out.println("не залогинен");
                    SendMail(0, "null", "bad");
                    tgMessage(0, "null", 0, "login", "null", "null", 0, "null", 0);
                }

                count++;


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

                try {
                    element = driver.findElement(By.xpath("//a[contains(@class,' ProductCardVertical__name  Link js--Link Link_type_default')]"));
                    name = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                    System.out.println("Самый дешевый товар фильтра: " + name);

                    element = driver.findElement(By.xpath("//span[contains(@class,'ProductCardVerticalPrice__price-current_current-price')]"));
                    String price = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerHTML;", element);
                    priceFormated = price.replaceAll("\\s+", "");
                    System.out.println("цена: " + price.replaceAll("\\s+", ""));
                    priceInt = Integer.parseInt(priceFormated);
                    tgMessage(priceInt, name, count, "info", cityFormated, "null", banCount, productCount, downCount);

                    if (priceInt < prop2.getMaxPrice() & priceInt > minPrice) {//тут происходит уведомление об успешном нахождении
                        System.out.println("отправка сообщения");
                        SendMail(priceInt, name, "ok");
                        tgMessage(priceInt, name, count, "ok", "null", "null", 0, "null", 0);

                        System.out.println("успешных проверок " + count);
                        System.out.println("таймер " + timer / 1000 + " сек");
                        Thread.sleep(timer);
                    } else {
                        System.out.println("нет карт дешевле " + prop2.getMaxPrice());
                        System.out.println("успешных проверок " + count);
                        System.out.println("таймер " + timer / 1000 + " сек");
                        Thread.sleep(timer);

                    }

                } catch (NoSuchElementException n) {
                    System.out.println("нет товаров");
                    tgMessage(0, "null ", count, "noProduct", "n", n.toString(), 0, "null", 0);

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
            e.printStackTrace();

            SendMail(0, "null", "bad");
            tgMessage(0, "null ", 0, "bad", "null", e.toString(), 0, "null", 0);


        }
        tgMessage(0, "null ", count, "reborn", "null", "s", 0, "null", 0);
        driver.get("https://yandex.ru/");
        Thread.sleep(5000);
        driver.navigate().refresh();
        downC++;
        check(driver, element, count, banCount, downC);
    }


    public static void SendMail(int price, String Name, String MesType) throws MessagingException {
        try {
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

            //генерация тела и заголовка в зависимости от типа
            if (MesType.equalsIgnoreCase("ok")) {
                //устанавливаем тему письма
                message.setSubject("[От бота] " + price + " руб! " + "Дешевка в ситилинке!");
//добавляем текст письма
                message.setText("В ситилинке появился дешевый товар по фильтру! \n"
                        + Name + "\n" + "цена: " + price +
                        "\nссылка(удалить пробел)    vk. cc/bWNbp9");
            } else {
                message.setSubject("[От бота] Нужно участие человека");
                message.setText("бот не работает!");
            }
//указываем получателя
            String[] mails = prop2.getMailTo().split(",");
            for (String mail : mails) {
                System.out.println("Готовлю письмо для " + mail);
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
            }
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(prop2.getMailTo()));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("ceo@devcorp.ru"));
//        message.addRecipient(Message.RecipientType.TO, new InternetAddress("info@devcorp.ru"));

//указываем дату отправления
            message.setSentDate(new Date());
            System.out.println("отправляю");
            Transport transport = session.getTransport();
            System.out.println("получил транспорт");
            transport.connect(prop2.getMailHost(), 465, prop2.getMailUser(), prop2.getMailPass());
            System.out.println("получил коннект");
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            System.out.println("отправил");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public static void SendMailBad() throws MessagingException {
//        System.out.println("отправляю емейл ошибки");
//        Properties MailProps = new Properties();
//        MailProps.put("mail.transport.protocol", prop2.getMailProtocol());
//        MailProps.put("mail.smtp.host", prop2.getMailHost());
//        MailProps.put("mail.smtp.auth", prop2.getMailSmtpAuth());
//        // MailProps.put("mail.smtp.sendpartial", "true");
//        MailProps.put("mail.smtp.ssl.enable", "true");
//        MailProps.put("mail.user", prop2.getMailUser());
//        MailProps.put("mail.password", prop2.getMailPass());
//
//        System.out.println("получаю сессию для почты");
//        Session session = Session.getDefaultInstance(MailProps);
//        //создаем сообщение
//        System.out.println("делаю письмо");
//        MimeMessage message = new MimeMessage(session);
//
////устанавливаем тему письма
//
//
////указываем получателя
//        String[] mails = prop2.getMailTo().split(",");
//
//        for (String mail : mails) {
//            System.out.println("Готовлю письмо для " + mail);
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
//        }
//       // message.addRecipient(Message.RecipientType.TO, new InternetAddress(prop2.getMailTo()));
//        //  message.addRecipient(Message.RecipientType.TO, new InternetAddress("ceo@devcorp.ru"));
//        //  message.addRecipient(Message.RecipientType.TO, new InternetAddress("info@devcorp.ru"));
//
////указываем дату отправления
//        message.setSentDate(new Date());
//        System.out.println("отправляю");
//        Transport transport = session.getTransport();
//        System.out.println("получил транспорт");
//        transport.connect(prop2.getMailHost(), 465, prop2.getMailUser(), prop2.getMailPass());
//        System.out.println("получил коннект");
//        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
//        System.out.println("отправил");
//    }


    public static void tgMessage(int price, String name, int count, String mesType, String city, String e,
                                 int banCount, String productCount, int downcount) throws IOException {
        try {
            String trash = "\n*****************************************\n";
            System.out.println("пытаюсь отправить сообщение в тг ");
            // String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String apiToken = prop2.getApiKey();
            String chatId = prop2.getChannelName();
            String channelAdm = prop2.getChannelAdm();
            String logChannelName = prop2.getlogChannelName();
            String text = null;
            String urlString = null;
            switch (mesType) {
                case "ok":
                    text = channelAdm + "\n" + price + "\n" + name;
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + chatId + "&text=";

                    break;
                case "info":
                    System.out.println("bancount tg" + banCount);
                    text = "Цена: " + price + "\nИмя: " + name +
                            ".\n" +
                            "проверок после подъема: " + count + ". " + "Обходов бана: " + banCount +
                            "\nТоваров в фильтре: " + productCount + "." + "\nРегион: " + city + "\nКритических ошибок: " + downcount;
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" +
                            logChannelName + "&text=";
                    break;
                case "bad":
                    text = trash + channelAdm + " упал(" + "\n" + e + trash;
                    if (text.length() > 3999) {
                        text = text.substring(0, 3998);
                    }
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + logChannelName + "&text=";

                    break;
                case "login":
                    text = trash + channelAdm + " слетела авторизация" + trash;
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + logChannelName + "&text=";

                    break;

                case "ban":
                    text = trash + channelAdm + " я смог обойти бан! бан пришел после " + count + " проверок" + trash;
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + logChannelName + "&text=";

                    break;

                case "reborn":
                    text = trash + channelAdm + " сделал попытку переподъёма!" + "\nПроверок после подъема: " + count + trash;
                    if (text.length() > 3999) {
                        text = text.substring(0, 3998);
                    }
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + logChannelName + "&text=";

                    break;

                case "noProduct":
                    text = trash + channelAdm + " нет товаров или произошла непредусмотренная ошибка. " + "Проверок после подъема: " + count + trash + "\nДля инфо:\n" + e;
                    if (text.length() > 3999) {
                        text = text.substring(0, 3998);
                    }
                    urlString = "https://api.telegram.org/bot" + apiToken + "/sendMessage?chat_id=" + logChannelName + "&text=";

                    break;

                default:
                    text = "бот никогда не должен вызвать это";
            }


//        if (mesType.equalsIgnoreCase("info")) {
//            text = "Самая дешевая карта:\n" + name + "\n" + price
//                    + "\n" +
//                    "проверок после подъема: " + count;
//        } else if (mesType.equalsIgnoreCase("ok")) {
//            text = channelAdm + "\n" + price + "\n" + name;
//        } else if (mesType.equalsIgnoreCase("bad")) {
//            text = channelAdm + " я упал";
//        } else { text="бот никогда не должен вызвать это";}
            System.out.print("сформировал ");
            // text = text.replace(" ", "%20");
            text = URLEncoder.encode(text, "UTF-8");
            //urlString = String.format(urlString, apiToken, chatId, text);

            urlString = urlString + text;


            URL myURL = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder results = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                results.append(line);
            }

            //  connection.disconnect();
            //    System.out.println(results.toString());
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        System.out.print("есть коннект ");
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }
//        String response = sb.toString();
            // System.out.println("tg: " + response);
        } catch (Exception e12) {
            e12.printStackTrace();
        }
    }

}
