package testScripts;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class validateErrorMess {

	WebDriver driver;
	Properties prop;

	@BeforeMethod
	public void setUp() {

		prop = new Properties();
		String path = System.getProperty("user.dir") + "//src//test//resources//configfiles//config.properties";

		FileInputStream file;
		try {
			file = new FileInputStream(path);
			prop.load(file);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
	}

	@Test
	public void addToCart() throws InterruptedException {
		
		String url = prop.getProperty("url");

		driver.get(url);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Computers")));

		WebElement comp = driver.findElement(By.linkText("Computers"));

		Actions action = new Actions(driver);
		action.moveToElement(comp).perform();

		driver.findElement(By.linkText("Desktops")).click();

		wait.until(ExpectedConditions
				.elementToBeClickable(driver.findElement(By.xpath("//h1[contains(text(),'Desktops')]"))));

		driver.findElement(By.xpath("//a[1][contains(text(),'Build your own computer')]")).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("1 review(s)")));

		driver.findElement(By.id("add-to-cart-button-1")).click();
		Thread.sleep(3000);

//		String errormess = driver.findElement(By.xpath("//div[@class='bar-notification error']/p")).getText();
//		System.out.println(errormess);

		List<WebElement> mess = driver.findElements(By.xpath("//div[@class='bar-notification error']/p"));
		System.out.println(mess.size());

		for (WebElement e : mess) {
			String str = e.getText();
			System.out.println(str);

			if (str.equals("Please select RAM")) {
				Select sel = new Select(driver.findElement(By.id("product_attribute_2")));
				sel.selectByVisibleText("4GB [+$20.00]");
			}

			if (str.equals("Please select HDD")) {
				driver.findElement(By.xpath("//label[contains(text(), '400 GB [+$100.00]')]")).click();
			}
		}

		if (!(mess.isEmpty())) {

			driver.findElement(By.id("add-to-cart-button-1")).click();
			Thread.sleep(3000);

		}
	}
	
	@AfterMethod
	public void closeDriver() {
		driver.close();
	}
}
