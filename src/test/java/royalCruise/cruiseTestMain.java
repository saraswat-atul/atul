package royalCruise;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.java.Util.driverSetup;
import com.java.Util.writingData;

import java.io.File;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class cruiseTestMain {
	
	public static WebDriver driver;
	public String baseURL = "https://www.royalcaribbean.com/alaska-cruises";

	public void createDriver() {
		driver = driverSetup.getWebDriver(baseURL);
	}

	public void handleSignupPopup() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
			wait.until(
					ExpectedConditions.visibilityOfElementLocated(By.className("notification-banner__section-close")));
			driver.findElement(By.className("notification-banner__section-close")).click();
		} catch (Exception e) {
			e.printStackTrace();
			driver.close();
		}
	}

	public void scrollThePage() {
		try {
			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			String jsSelector = "document.querySelector(\"#rciFooterGroup-1-4 > a\")";
			String script = "return " + jsSelector;
			WebElement elementTillWhereThePageScrolls = (WebElement) jsExecutor.executeScript(script);
			jsExecutor.executeScript(
					"arguments[0].scrollIntoView(" + "{behaviour: 'smooth', block:'center', inline:'center'});",
					elementTillWhereThePageScrolls);
		} catch (Exception e) {
			e.printStackTrace();
			driver.close();
		}
	}

	public void checkHolidayCruisePresence() {
		By elementHolidayCruise = By.xpath("//*[@id=\"rciFooterGroup-1-4\"]/a"); 
		List<WebElement> temp = driver.findElements(elementHolidayCruise);
		if (!temp.isEmpty()) {
			System.out.println("Element is Present");
			driver.findElement(elementHolidayCruise).click();
		} else {
			System.out.println("Element is Not Present");
		}
	}

	public void checkSearchOptionPresence() throws InterruptedException {
		Thread.sleep(10000);
		By elementSearchButton = By.id("rciSearchHeaderIcon");
		if (!driver.findElements(elementSearchButton).isEmpty()) {
			System.out.println("Search Option Element is Present");
			driver.findElement(elementSearchButton).click();
		} else {
			System.out.println("Search Option Element is not Present");
		}
	}

	public void enterSearchText(String searchText) {
		WebDriverWait searchWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		searchWait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath("//*[@id=\"rciHeader\"]/div/div/div[6]/div/div/div/div[1]/div")));
		driver.findElement(By.xpath("//*[@id=\"rciHeader\"]/div/div/div[6]/div/div/div/div[1]/div/input")).click();
		driver.findElement(By.xpath("//*[@id=\"rciHeader\"]/div/div/div[6]/div/div/div/div[1]/div/input"))
				.sendKeys(searchText, Keys.ENTER);
	}

	public void displaySearchResults() {

		// This Java code uses a regular expression to find and capture numbers with
		String searchResult = driver.findElement(By.xpath("//*[@id='siteSearchApp']/div[1]/div/div[2]/div")).getText();
		Pattern pattern = Pattern.compile("\\b(\\d{1,3}(,\\d{3})*)(\\.\\d+)?\\b");
		Matcher matcher = pattern.matcher(searchResult);
		int searchResultCount = 0;
		if (matcher.find()) {
			String extractedNumber = matcher.group(1);
			extractedNumber = extractedNumber.replaceAll(",", "");
			searchResultCount = Integer.parseInt(extractedNumber);
			System.out.println("Total no of Search Result: " + searchResultCount);
			writingData.writePackage(searchResultCount);
		}
		if (searchResultCount < 300000) {
			System.out.println("Number of search results displayed are less than 300000");
		} else {
			System.out.println("Number of search results displayed are more than 300000");
		}

	}

	public void cruisesDate(String year) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// Clicking on the find the cruise button/link.
			driver.findElement(By.xpath("//*[@class=\"headerTopNav__menu\"]/div")).click();
			
			// Clicking on the cruise Date filter button
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.findElement(By.xpath("//*[@id='filters-content']/button[1]/span")).click();
			List<WebElement> sizeOfYearsList = driver
					.findElements(By.xpath("//*[@id='dates-filter-wrap']/section/div[2]/div[2]/div/div/div"));
			int sizeOfYears = sizeOfYearsList.size();

			String month = "";
			for (int y = 1; y <= sizeOfYears; y++) {

				
				WebElement yearXpath = driver.findElement(
						By.xpath("//*[@id='dates-filter-wrap']/section/div[2]/div[2]/div/div/div[" + y + "]/div[1]"));
				//js.executeScript("document.evaluate('//*[@id='dates-filter-wrap']/section/div[2]/div[2]/div/div/div[" + y + "]/div[1]', document, null, XPathResult,null"))

				if (year.equalsIgnoreCase(yearXpath.getAttribute("value"))) {
					month = "//*[@id='dates-filter-wrap']/section/div[2]/div[2]/div/div/div[" + y + "]/div[2]/div";
					break;
				}
			}
			
			for (int i = 1; i <= 12; i++) {
				//WebElement monthButton = driver.findElement(By.xpath("//*[@id='dates-filter-wrap']/section/div[2]/div[2]/div/div/div[2]/div[2]/div["+i+"]/button"));
				WebElement monthButton = driver.findElement(By.xpath("//*[@id=\"dates-filter-wrap\"]/section/div[2]/div[2]/div/div/div[2]/div[2]/div["+i+"]"));
				Thread.sleep(5000);
				if (monthButton.isEnabled()) {
					js.executeScript("arguments[0].click();", monthButton);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void departurePort(String continent) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// Clicking on the Departure Port Button.
			driver.findElement(By.xpath("//*[@id='filter-modal']/div/section[1]/div/div/button[2]")).click();

			List<WebElement> sizeOfContinentList = driver.findElements(By
					.xpath("//*[@id='filter" + "-modal']/div/section[2]/div/div[2]/div/div[@class='MuiTypography-root "
							+ "MuiTypography-title5 components__SectionTitleContent-sc-zsi07w-9 "
							+ "jGuvEA sectionTitle css-17fghhl']"));
			int sizeOfContinent = sizeOfContinentList.size();
			String regions = "";
			for (int i = 1; i <= sizeOfContinent; i++) {
				By continentXpath = By
						.xpath("(//*[@id='filter-modal']/div/section[2]/div/child::div[2]/div/div[1])" + "[" + i + "]");
				if (continent.equalsIgnoreCase(driver.findElement(continentXpath).getText())) {
					
					regions = "//*[@id='filter-modal']/div/section[2]/div/div[2]/div[" + i + "]/div[2]/div";
					
					break;
				}
			}
			List<WebElement> regionSize = driver.findElements(By.xpath(regions));
			for (int i = 1; i <= regionSize.size(); i++) {
				WebElement regionButton = driver.findElement(By.xpath(regions + "[" + i + "]"));
				if (regionButton.isEnabled()) {
					js.executeScript("arguments[0].click();", regionButton);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void destinations() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// Clicking on the Destination Button.
			driver.findElement(By.xpath("//*[@id='filter-modal']" + "/div/section[1]/div/div/button[3]")).click();
			List<WebElement> sizeOfDestinationList = driver
					.findElements(By.xpath("//*[@id='filter-modal']" + "/div/section/div[2]/div[3]/div"));
			String destinations = "//*[@id='filter-modal']/div/section/div[2]/div[3]/div";
			for (int i = 1; i <= sizeOfDestinationList.size(); i++) {
				WebElement destinationButton = driver.findElement(By.xpath(destinations + "[" + i + "]"));
				if (destinationButton.isEnabled()) {
					destinationButton = driver.findElement(By.xpath(destinations + "/button" + "[" + i + "]"));
					js.executeScript("arguments[0].click();", destinationButton);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void numberOfNights() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// Click on the Number of Nights Button
			driver.findElement(By.xpath("//*[@id='filter-modal']" + "/div/section[1]/div/div/button[4]")).click();
			List<WebElement> sizeOfNumberOfNightsList = driver.findElements(
					By.xpath("//*[@id='filter" + "-modal']/div/section[2]/div[2]/section/section/div[2]/div"));
			String nights = "//*[@id='filter-modal']/div/section[2]/div[2]/section/section/div[2]/div";
			for (int i = 1; i <= sizeOfNumberOfNightsList.size(); i++) {
				WebElement nightSelectorButton = driver.findElement(By.xpath(nights + "[" + i + "]"));
				if (nightSelectorButton.isEnabled()) {
					js.executeScript("arguments[0].click();", nightSelectorButton);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ships() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			// Click on the Ships Button
			driver.findElement(By.xpath("//*[@id='filter-modal']" + "/div/section[1]/div/div/button[5]")).click();
			List<WebElement> sizeOfShipsList = driver
					.findElements(By.xpath("//*[@id='filter" + "-modal']/div/section[2]/div[2]/div[2]/div"));
			String ship = "//*[@id='filter-modal']/div/section[2]/div[2]/div[2]/div";
			for (int i = 1; i <= sizeOfShipsList.size(); i++) {
				WebElement shipSelectorButton = driver.findElement(By.xpath(ship + "[" + i + "]"));
				if (shipSelectorButton.isEnabled()) {
					js.executeScript("arguments[0].click();", shipSelectorButton);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showResult() {
		// Clicking the result button;
		driver.findElement(By.xpath("//*[@id='filter-modal']/section/button")).click();

	}  
	

	public void filterCruises(String year, String continent) {
		cruisesDate(year);
		departurePort(continent);
		destinations();
		numberOfNights();
		ships();
		showResult();
	}

	public void takeResultScreenshot() throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,500)", "");
		Thread.sleep(2000);
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = "C:\\Users\\2304008\\OneDrive - Cognizant\\Documents\\Eclipse_Workspace\\royalCruise\\Screenshot\\result.png";
		File trg = new File(path);
		FileUtils.copyFile(src, trg);
		System.out.println("Screenshot taken successfully");
	}

	public static void main(String[] args) throws Exception {
		cruiseTestMain ctm = new cruiseTestMain();
		ctm.createDriver();
		ctm.handleSignupPopup();
		ctm.scrollThePage();
		ctm.checkHolidayCruisePresence();
		ctm.checkSearchOptionPresence();
		String searchText = "Rhapsody of the Seas";
		ctm.enterSearchText(searchText);
		ctm.displaySearchResults();

		ctm.filterCruises("2024", "North America");
		ctm.takeResultScreenshot();

		Thread.sleep(3000);
		driver.quit();

	}

}
