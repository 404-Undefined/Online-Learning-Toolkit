package sample;

import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MeetingHost {
    private String teacherAddress;
    private String teacherPassword;
    private String link;
    private String[] meetLinksList;

    public MeetingHost(String teacherAddress, String teacherPassword){
        this.teacherAddress = teacherAddress;
        this.teacherPassword = teacherPassword;
    }

    public String[] generateZoomLinks(int numLinks) {

        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");//put your driver exe here

        meetLinksList = new String[numLinks];

        final String baseUrl = "https://accounts.google.com/signin/oauth/identifier?response_type=code&access_type=offline&client_id=849883241272-ed6lnodi1grnoomiuknqkq2rbvd2udku.apps.googleusercontent.com&scope=profile%20email&redirect_uri=https%3A%2F%2Fzoom.us%2Fgoogle%2Foauth&state=https%3A%2F%2Fzoom.us%2Fgoogle%2Foauth&o2v=2&as=QRWd4a9xYfc09Hopyd3_EA&flowName=GeneralOAuthFlow";

        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setPosition(new Point(-2000, -2000));//put it to the left

        meetLinksList = new String[numLinks];
        for (int i = 0; i < numLinks; i++) {
            try {
                driver.get("https://accounts.google.com/signin/oauth/identifier?response_type=code&access_type=offline&client_id=849883241272-ed6lnodi1grnoomiuknqkq2rbvd2udku.apps.googleusercontent.com&scope=profile%20email&redirect_uri=https%3A%2F%2Fzoom.us%2Fgoogle%2Foauth&state=https%3A%2F%2Fzoom.us%2Fgoogle%2Foauth&o2v=2&as=QRWd4a9xYfc09Hopyd3_EA&flowName=GeneralOAuthFlow");
                Thread.sleep(1000);
                if(i == 0) {
                    (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='identifierId']")))).sendKeys(teacherAddress);
                    driver.findElement(By.id("identifierNext")).click();
                    (new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='password']")))).sendKeys(teacherPassword);
                    driver.findElement(By.id("passwordNext")).click();
                }
                driver.findElement(By.xpath("//html")).click();
                Thread.sleep(1000);
                new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='meetings']/div/div/div[2]/div[3]/div[1]/div[1]/a")));
                driver.findElement(By.xpath("//*[@id='meetings']/div/div/div[2]/div[3]/div[1]/div[1]/a")).click();
                new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"schedule_form\"]/div[4]/div/div[2]/button")));
                driver.findElement(By.xpath("//*[@id=\"schedule_form\"]/div[4]/div/div[2]/button")).click();
                Thread.sleep(1000);
                new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='info_form']/div[5]/div/div/span[1]")));
                link = driver.findElement(By.xpath("/html/body/div[1]/div[3]/div[3]/div[2]/div/div/div[2]/div[2]/form/div[5]/div/div/span[1]")).getText();
                System.out.println(link);
                meetLinksList[i] = link;

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        driver.close();
        return meetLinksList;
    }

    public String[] generateGoogleMeetLinks(int numLinks){
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver");//put your driver exe here

        final String baseUrl = "https://meet.google.com/new";
        ChromeOptions options = new ChromeOptions();

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().setPosition(new Point(-2000, 0));//put it to the left
        meetLinksList = new String[numLinks];
        for(int i = 0; i < numLinks; i++){
            try {
                driver.get(baseUrl);

                if(i == 0) { //for the first link, we need to log into google
                    new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='identifierId']"))).sendKeys(teacherAddress);
                    driver.findElement(By.id("identifierNext")).click();
                    new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='password']"))).sendKeys(teacherPassword);
                    driver.findElement(By.id("passwordNext")).click();
                }
                Thread.sleep(1000);
                new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"yDmH0d\"]/div[3]/div/div[2]/span")));
                driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[3]/div/div[2]/span")).click();//clicking in the middle of the page
                Thread.sleep(1000);
                new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"yDmH0d\"]/div[3]/div/div[2]/div[3]/div/span/span")));
                driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/div[3]/div/div[2]/div[3]/div/span/span")).click();//clicks dismiss
                //new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body")));


                Thread.sleep(1000);
                new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"yDmH0d\"]/c-wiz/div/div/div[4]/div[3]/div/div[2]/div/div[2]/div/div[1]/div[1]/div[2]/div/div[1]")));
                link = driver.findElement(By.xpath("//*[@id=\"yDmH0d\"]/c-wiz/div/div/div[4]/div[3]/div/div[2]/div/div[2]/div/div[1]/div[1]/div[2]/div/div[1]")).getText();
                meetLinksList[i] = link;//filling the list

            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(TimeoutException e){
                e.printStackTrace();
            }
        }
        driver.close();
        return meetLinksList;
    }
}
