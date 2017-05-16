package com.utils;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.resource.PropertiesCache;
/**
 * 
 * @author shalabh.b.srivastava
 *
 */
public class GenericMethods  {
	static Logger log = Logger.getLogger(GenericMethods.class.getName());
	String currentTestCaseName = "";
	private static  WebDriver driver = null;
	private static WebDriverWait driverWait;
	private static Map<String, Map<String, String>> locatorMap = new HashMap<String, Map<String, String>>();
	private static Map<String, Map<String, String>> locatorValuesMap = new HashMap<String, Map<String, String>>();

	public static HashMap<String, String> locators = null;
	public static HashMap<String, String> locatorsValue = null;
	
	public static Map<String, Map<String, String>> testCaseMap = new HashMap<String, Map<String, String>>();
	public Map<String, String> dataMap;
	
	/**
	 * constructor accepting webdriver
	 * @param webdriver
	 */
	public GenericMethods(WebDriver webdriver ,Map<String, String> dataMap) {
		//super(webdriver,dataMap);
		driver = webdriver;
		this.dataMap=dataMap;
		
	}

	/**
	 * Default Constructor
	 */
	public GenericMethods() {

	}
	/**
	 * Description : Sets the value to Textbox fields
	 * @param elementName
	 * @param pageName
	 * @param recordId
	 */
	
	public void setValueInTextBox(String elementName, String pageName,	String recordId) {
		WebElement element = getElement(elementName, pageName);
		String setText= dataMap.get(pageName+recordId+elementName);
		element.sendKeys(setText);
		log.info("Value (" + setText + ") "+"Entered in Page (" +pageName +") "+ "Element ("+elementName+")");
	}

	/**
	 * Description : Performs click oparation on webelemnt
	 * @param elementName
	 * @param pageName
	 */
	public void clickElement(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		element.click();
		log.info("Clicked Page (" +pageName +") "+ "Element ("+elementName+")");
	}

	/**
	 * Forms WebElemnet based on the locator  
	 * @param elementName
	 * @param pageName
	 * @return
	 */

	public WebElement getElement(String elementName, String pageName) {
		String locator = locatorMap.get(pageName).get(elementName);
		String locatorValue = locatorValuesMap.get(pageName).get(elementName);

		if(waitForPageLoad()){

			if ("ID".equalsIgnoreCase(locator)) {
				return driver.findElement(ById.id(locatorValue));
			} else if ("XPATH".equalsIgnoreCase(locator)) {
				return driver.findElement(ById.xpath(locatorValue));
			} else if ("tagName".equalsIgnoreCase(locator)) {
				return driver.findElement(ById.tagName(locatorValue));
			} else if ("className".equalsIgnoreCase(locator)) {
				return driver.findElement(ByClassName.className(locatorValue));
			} else if ("LinkText".equalsIgnoreCase(locator)) {
				return driver.findElement(ByLinkText.linkText(locatorValue));
			} else if ("cssSelector".equalsIgnoreCase(locator)) {
				return driver.findElement(ByCssSelector.cssSelector(locatorValue));

			}
		}

		return null;
	}

	public WebElement getElementForScreenShot(String elementName, String pageName) {
		String locator = locatorMap.get(pageName).get(elementName);
		String locatorValue = locatorValuesMap.get(pageName).get(elementName);


		if ("ID".equalsIgnoreCase(locator)) {
			return driver.findElement(ById.id(locatorValue));
		} else if ("XPATH".equalsIgnoreCase(locator)) {
			return driver.findElement(ById.xpath(locatorValue));
		} else if ("tagName".equalsIgnoreCase(locator)) {
			return driver.findElement(ById.tagName(locatorValue));
		} else if ("className".equalsIgnoreCase(locator)) {
			return driver.findElement(ByClassName.className(locatorValue));
		} else if ("LinkText".equalsIgnoreCase(locator)) {
			return driver.findElement(ByLinkText.linkText(locatorValue));
		} else if ("cssSelector".equalsIgnoreCase(locator)) {
			return driver.findElement(ByCssSelector.cssSelector(locatorValue));
		}
		return null;
	}

	/**
	 * @Description : Wait for page load.
	 * @return boolean
	 */
	public boolean waitForPageLoad() {

		driverWait = new WebDriverWait(driver,10);

		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
			
			public Boolean apply(WebDriver driver) {
				try {
					return ((Long)((JavascriptExecutor)driver).executeScript("return jQuery.active") == 0);
				}
				catch (Exception e) {
					// no jQuery present
					return true;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor)driver).executeScript("return document.readyState")
						.toString().equals("complete");
			}
		};
		log.info("Loading Complete");
		return driverWait.until(jQueryLoad) && driverWait.until(jsLoad);
	}

	/**
	 * Reads all the Elements from page data and stores in Hashmap
	 * @param currentTestCaseName
	 * @param path
	 */
	public static Map<String, String> loadPageData(String path) {
		Map<String, String> tesDataValuesMap = new HashMap<String, String>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream inputStream = Files.newInputStream(Paths.get(path).toAbsolutePath()); 
			Document document = db.parse(inputStream);
			NodeList nodeList1 = document.getElementsByTagName("page");
			
			for (int i = 0; i < nodeList1.getLength(); i++) {
				Element question = (Element) nodeList1.item(i);
				NodeList optionList = question.getElementsByTagName("record");
				for (int j = 0; j < optionList.getLength(); ++j) {

					for (int k = 0; k <= optionList.item(j).getAttributes().getLength()-1; k++) {
						StringBuffer mapkey = new StringBuffer();
						mapkey = mapkey.append(nodeList1.item(i).getAttributes().getNamedItem("name").getNodeValue());
						mapkey.append(optionList.item(j).getAttributes().getNamedItem("id").getNodeValue());
						mapkey.append(optionList.item(j).getAttributes().item(k).getNodeName());
						tesDataValuesMap.put(mapkey.toString(),optionList.item(j).getAttributes().item(k).getNodeValue());
					}

				}
			}

		}
		catch (Exception e) {
			System.out.println(e);
		}
		return tesDataValuesMap;
	}
	

	/**
	 * Reads Object repository and stores elements  of page as key value pair in map
	 */
	public static void loadObjectRepository() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(Files.newInputStream(Paths.get("src/com/resource/ObjectRepository.xml").toAbsolutePath()));
					
			locators = new HashMap<String, String>();
			locatorsValue = new HashMap<String, String>();

			NodeList nodeList1 = document.getElementsByTagName("page");
			for (int i = 0; i < nodeList1.getLength(); i++) {
				Element question = (Element) nodeList1.item(i);

				NodeList optionList = question.getElementsByTagName("element");
				for (int j = 0; j < optionList.getLength(); ++j) {
					locators.put(optionList.item(j).getAttributes()
							.getNamedItem("name").getNodeValue(), optionList
							.item(j).getAttributes().getNamedItem("Descriptor")
							.getNodeValue());
					locatorsValue.put(optionList.item(j).getAttributes()
							.getNamedItem("name").getNodeValue(), optionList
							.item(j).getAttributes().getNamedItem("value")
							.getNodeValue());

				}
				locatorMap.put(nodeList1.item(i).getAttributes().getNamedItem("name").getNodeValue(), locators);
				locatorValuesMap.put(nodeList1.item(i).getAttributes().getNamedItem("name").getNodeValue(), locatorsValue);
			}

		} catch (Exception e) {
		}
	}

	/**
	 *  Creates instance of webdriver based on the passed browser parameter 
	 * @return WebDriver
	 */
	public static WebDriver getDriver(String browserType) {
		if (browserType.equals("chrome")) {
			driver = initChromeDriver();
		} else if (browserType.equals("ie")) {
			driver = initIEDriver();
		} else {
			driver = initFirefoxDriver();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;

	}
	private static WebDriver initChromeDriver() {
		System.setProperty("webdriver.chrome.driver", PropertiesCache.getInstance().getProperty("service"));
		WebDriver driver = new ChromeDriver();
		log.info("Chrome Launched");
		return driver;
	}

	private static WebDriver initIEDriver() {
		System.setProperty("webdriver.ie.driver", PropertiesCache.getInstance().getProperty("service"));

		DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true); 
		WebDriver driver = new InternetExplorerDriver(capabilities);
		log.info("IE Launched");
		return driver;
	}
	private static WebDriver initFirefoxDriver() {
		System.setProperty("webdriver.gecko.driver", PropertiesCache.getInstance().getProperty("service"));
		WebDriver driver = new FirefoxDriver();
		log.info("FireFox Launched");
		return driver;
	}
	
	/**
	 *  @Description : Focus on newly opened window.
	 *  @return : boolean
	 */
	public boolean focuson_openwindow()
	{
		try
		{
			//String handle = driver.getWindowHandle();
			for (String winHandle : driver.getWindowHandles()) 
			{
				driver.switchTo().window(winHandle); 
			}
			log.info("Focus on Window");
			return true;
		}
		catch(Exception e)
		{
			return false;
		}  
	}

	/**
	 * @Description : Checks if is alert present.
	 * @return boolean
	 */
	public boolean isAlertPresent() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.alertIsPresent());
			log.info("Alert Present");
			return true;
		} // try
		catch (Exception Ex) {
			return false;
		} 
	} 

	/**
	 * @Description : Dismiss alert.
	 */
	public void dismissAlert() {
		try {
			isAlertPresent();
			Alert currentAlert = driver.switchTo().alert();
			currentAlert.dismiss();
		} // try
		catch (Exception e) {

		}
	}
	
	/**
	 *  @Description : Accept alert.
	 */
	public void acceptAlert() {
		try {

			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();

		} 
		catch (Exception e) {

		}

	}

	/**
	 *  @Description : Handling alerts.
	 *  @return : boolean
	 */
	public boolean Alert_handle(int popuptype, String message)
	{ 
		//popuptype = 0 --> cancel
		//popuptype = 1 --> accept	
		//popuptype = 2 --> send_text
		//popuptype = 3 --> get_text
		try
		{
			if(driver.switchTo().alert() != null)
			{
				switch(popuptype)
				{
				case 0:
					driver.switchTo().alert().dismiss(); 
				case 1:
					driver.switchTo().alert().accept();
				case 2:
					driver.switchTo().alert().sendKeys(message);
				case 3:
					driver.switchTo().alert().getText();
				}	
			}
		} 
		catch(Exception e)
		{
			return false; 
		}
		return true;
	}
	
	/**
	 *  @Description : Get system current date.
	 *  @return : String
	 */
     public String getSystemDate(){
		
		DateFormat df = new SimpleDateFormat("M/d/yyyy");
		// Instantiate a Date object
	      Date date = new Date();

	     return df.format(date);   
	}
     

	/**
	 *  @Description : Switch to alert.
	 */
	public void switchToAlert() {
		try {
			driver.switchTo().alert();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @Description : Open URL.
	 * @param String: url
	 * @return boolean
	 */
	public boolean openURL(String url) {
		try {

			driver.get(url);
			maxiWindow();

		} catch (Exception ex) {
			return false;
		}

		return true;
	}

	/**
	 * @Description : closeBrowser : close Browser
	 * @return void
	 */
	public void closeBrowser() {
		try {
			driver.close();

		} catch (Exception ex) {

		}

	}


	/**
	 * @Description : Checks if is element enabled.
	 * @param Element : elem
	 * @return boolean
	 */
	public boolean isElementEnabled(String elementName, String pageName) {
		try {
			WebElement element = getElement(elementName, pageName);
			//takeScreenShot(elementName,pageName);
			element.isEnabled();

		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * @Description : Checks if is element displayed.
	 * @param Element : elem
	 * @return boolean
	 */
	public boolean isElementDisplayed(String elementName, String pageName) {
		try {
			WebElement element = getElement(elementName, pageName);
			element.isDisplayed();

		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * @Description : Gets the all from dropdown.
	 * @param Element : elem
	 * @return the all from dropdown
	 */
	public List<WebElement> getAllFromDropdown(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		Select dropdown = new Select(element);
		log.info("Drop Down values returned");
		return dropdown.getOptions();
	}

	/**
	 * @Description : Action press key.
	 * @param key : key
	 * @return boolean
	 */
	public boolean actionPressKey(Keys key, String elementName, String pageName) {
		try {
			WebElement element = getElement(elementName, pageName);
			Actions actions = new Actions(driver);
			actions.sendKeys(key).perform();
			log.info("Enter Key Pressed");
			actions.keyDown(key).keyDown(key).build().perform();
			actions.click(element).build().perform();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * @Description :Gets the selected option.
	 * @param Element : elem
	 * @return String
	 */
	public String getSelectedOption(String elementName, String pageName) {
		try {
			WebElement element = getElement(elementName, pageName);
			Select sel = new Select(element);
			String text = sel.getFirstSelectedOption().getText();
		//	takeScreenShot(elementName,pageName);
			log.info("Drop Down Value (" + text + ") "+"Selected in Page (" +pageName +") "+ "Element ("+elementName+")");
			return text;

		} catch (Exception ex) {
			return null;
		}

	}


	/**
	 * @Description :Action mouse over.
	 * @param Element :element
	 * @return boolean
	 */
	public boolean actionMouseOver(String elementName, String pageName) {
		try {
			WebElement element = getElement(elementName, pageName);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).perform();
			//takeScreenShot(elementName,pageName);
		} catch (Exception ex) {
			return false;
		}
		return true;

	}

	/**
	 * @Description :Toggle check box.
	 * @param Element : element
	 * @param String : action
	 * @return boolean
	 */
	public boolean toggleCheckBox(String elementName, String pageName,String recordId) {
		try {
			WebElement element = getElement(elementName, pageName);
		//	takeScreenShot(elementName,pageName);
			//String action = getPageDetailsData(pageName, elementName, recordId);
			//if ((action.equalsIgnoreCase("Check") && !isSelected(elementName, pageName)) || (action.equalsIgnoreCase("Uncheck") && isSelected(elementName, pageName))) {
			//	element.click();
			//}

			element.click();

		} 
		catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description :Select values from dropdown.
	 * @param Element : element
	 * @param String elementName, String pageName,String recordId
	 * @return boolean
	 */
	public boolean selectValueFromDropdown(String elementName, String pageName,String recordId) {
		try {
			WebElement element = getElement(elementName, pageName);
			//String subStrings = getPageDetailsData(pageName, elementName, recordId);
			String text;
			Select sel = new Select(element);
			List<WebElement> options = sel.getOptions();
			Actions act = new Actions(driver);
			//sel.deselectAll();
			act.keyDown(Keys.CONTROL).build().perform();

			for (WebElement opt : options) {
				text = element.getText().trim();
				//if (text.equalsIgnoreCase(subStrings)) {
					//takeScreenShot(elementName,pageName);
					//opt.click();                       
					//log.info("Drop Down Value (" + text + ") "+"Selected in Page (" +pageName +") "+ "Element ("+elementName+")");
					//break;
				//}
			}

			act.keyUp(Keys.CONTROL).build().perform();


		} catch (Exception ex) {
			return false;
		}
		return true;

	}

	/**
	 * @Description : Gets the text.
	 * @param Element: element
	 * @return String
	 */
	public String getText(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		//takeScreenShot(elementName,pageName);
		String text = element.getText().trim();      
		log.info("Value (" + text + ") "+"fetched in Page (" +pageName +") "+ "Element ("+elementName+")");
		return text;
	}
	/**
	 * @Description : Checks if is text displayed.
	 * @param Element: elem
	 * @return boolean
	 */
	public boolean isTextDisplayed(String elementName, String pageName) {
		try {
			String text = getText(elementName, pageName);
			if (text.isEmpty() || text.length() == 0) {
				return false;
			} else {
				return true;
			}

		} catch (Exception ex) {

			return false;
		}

	}

	/**
	 *  @Description :Close all windows.
	 */
	public static void closeAllWindows() {
		try {

			driver.quit();

		} catch (Exception ex) {

		}

	}

	/**
	 * @Description :Gets the title.
	 * @return String
	 */
	public String getTitle() {
		try {
			log.info("Page Title (" + driver.getTitle()+")");
			return (driver.getTitle());

		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * @Description : Checks if is selected.
	 * @param Element: elem
	 * @return boolean
	 */
	public boolean isSelected(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		//takeScreenShot(elementName,pageName);
		if (element.isSelected()){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * @Description : Checks if is enabled.
	 * @param Element: elem
	 * @return boolean
	 */
	public boolean isEnabled(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		//takeScreenShot(elementName,pageName);
		if (element.isEnabled()){
			return true;
		}
		else {
			return false;
		}
	}



	/**
	 * @Description : Gets the innerHTML of a webelement.
	 * @param Element: element
	 * @return String
	 */
	public String getinnerHTML(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		//takeScreenShot(elementName,pageName);
		String text = element.getAttribute("innerHTML").trim();   
		return text;
	}

	/**
	 * @Description : Gets the innerText of a webelement.
	 * @param Element: element
	 * @return String
	 */
	public String getinnerText(String elementName, String pageName) {
		WebElement element = getElement(elementName, pageName);
		//takeScreenShot(elementName,pageName);
		String text = element.getAttribute("innerText").trim();   
		return text;
	}

	/**
	 * @Description :Press enter.
	 * @return boolean
	 *//*
	public boolean pressEnter() {
		try {
			Actions act = new Actions(driver);
			act.keyDown(Keys.ENTER);
			act.keyUp(Keys.ENTER);
			log.info("Enter Pressed");
		} catch (Exception ex) {

			return false;
		}

		return true;
	}*/
	  /**
     * @Description :  Method Returns the date before N days from current date in Allstate specific format
     * @param int: n
     * @return the string
     */
    public String getPreviousDate(int n , String format) {
        
        DateFormat dateFormat = new SimpleDateFormat(format);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -n);
        java.util.Date todate1 = cal.getTime();
        String s = dateFormat.format(todate1);

        //String accDate = s.substring(8, 10) + "-" + s.substring(5, 7) + "-" + s.substring(0, 4);

        return (s);
    }

	/**
	 * @Description : Gets the current year.
	 * @return String
	 */
	public String getCurrentYear(){
		try {

		Calendar now = Calendar.getInstance();   
			int year = now.get(Calendar.YEAR);  
			return (Integer.toString(year));

	} catch (Exception ex) {
			return null;
		}

}

	/**
	 * @Description : Gets the future date.
 * @return String
	 */
	public String getFutureDate(int noOfDays) {
	try {

			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, noOfDays);
			 Date date = now.getTime(); 
			return (dateFormat.format(date));

	} catch (Exception ex) {
			return null;
		}

	}
	 
    /**
     * @Description : getModifiedDate
     * @param: String: strDate
     * @param int : Day Count
     * @throws Exception 
     */
    public static String getModifiedDate(String strDate, int dayCount) throws Exception {

    	if(strDate!=null && strDate!="" && strDate.contains("/")) {
    		String date[] = strDate.split("/");
    		if(date[0].length()==1) {
    			date[0]="0"+date[0];
    		}
    		if(date[1].length()==1) {
    			date[1]="0"+date[1];
    		}

    		strDate =date[0] + "/" +date[1] +"/"+date[2] ;
    	}

    	DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    	Date date = dateFormat.parse(strDate);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DATE, dayCount); 
    	
    	Date modifiedDate = cal.getTime();

    	return dateFormat.format(modifiedDate);
    }
    
	/**
	 * @Description : Gets the current date.
	 * @return String
	 */
	public String getCurrentDate() {
		try {

			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date date = new Date();
			return (dateFormat.format(date));

		} catch (Exception ex) {
			return null;
		}

	}
 

	public void deleteCookies() {

		if (driver instanceof RemoteWebDriver)
			((RemoteWebDriver) driver).manage().deleteAllCookies();	
	}

	/**
	 * @Description: Maximizes the window 
	 */
	public  void maxiWindow() {
		driver.manage().window().maximize();
		log.info("WebPage Maximized");
	}


	/**
	 * @Description :Sets the implicit time out.
	 * @param Stirng : seconds, web driver 
	 */
	public void setImplicitTimeOut() {
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
		log.info("Set implicit wait of 5000 ms");
	}

	/**
	 * @Description : Method for switching to the child window
	 */
	public void switchToChildWindow() {
		try {

			if(waitForPageLoad()){

				String parentWinID = driver.getWindowHandle();
				Set<String> allWinIDs = driver.getWindowHandles();

				for (String win : allWinIDs) {
					if (!win.equalsIgnoreCase(parentWinID)) {
						driver.switchTo().window(win);
						break;
					}
				}
				log.info("Switched to child window");

			}} catch (Exception e) {
				e.printStackTrace();
			}

	}

	/**
	 * @Description : Method for switching window
	 */
	public void switchWindow() {

		log.info("***** In method 'switchWindow()' ");
		Set<String> handles = driver.getWindowHandles();
		System.out.println("No. of windows is: " + handles.size());

		for (String winHan : handles) {
			System.out.println(winHan);
			System.out.println(driver.getTitle());
			driver.switchTo().window(winHan);
		}
	} 

	/*
	 *//**
	 * @Description :Sets the implicit time out.
	 * @param Stirng : seconds 
	 */
	public void setImplicitTimeOut(int seconds) {

		driver.manage().timeouts().implicitlyWait(seconds * 1000, TimeUnit.MILLISECONDS);
	}

	/*   *//**
	 * @Description :Sets the page load time out.
	 * @param String :seconds 
	 */
	public void setPageLoadTimeOut(int seconds) {

		driver.manage().timeouts().pageLoadTimeout(seconds * 1000, TimeUnit.MILLISECONDS);
	}

	/*    *//**
	 * @Description :Sets the script load time out.
	 * @param String:seconds
	 */
	public void setScriptLoadTimeOut(int seconds) {

		driver.manage().timeouts().setScriptTimeout(seconds * 1000, TimeUnit.MILLISECONDS);
	}

	/**
	 * @Description : Switch to child window.
	 * @param String :parentWinID
	 */
	public void switchToChildWindow(String parentWinID) {
		try {
			waitForPageLoad();
			System.out.println("parent Id "+parentWinID);	
			Set<String> allWinIDs = driver.getWindowHandles();

			for (String win : allWinIDs) {
				if (!win.equalsIgnoreCase(parentWinID)) {
					driver.switchTo().window(win);
					break;
				}
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @Description : Gets the current window ID.
	 * @return the current window ID
	 */
	public String getCurrentWindowID() {
		try {

			String winID = driver.getWindowHandle();
			log.info("Window ID ("+winID+")");
			return winID;
		}

		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @Description : getAllWindowIDs : Get All Window IDs
	 * @return : Set<String>
	 */
	public Set<String> getAllWindowIDs() {
		try {

			log.info("Returned All Window IDs");
			return driver.getWindowHandles();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @Description : Window Switch Type.
	 * @return enum
	 */
	private enum WindowSwitchType {

		/** The by wintitle. */
		BY_WINTITLE,
		/** The by winurl. */
		BY_WINURL,
		/** The by frame. */
		BY_FRAME,
		/** The by parentframe. */
		BY_PARENTFRAME,
		/** The by default. */
		BY_DEFAULT,
		/** The by winclose. */
		BY_WINCLOSE,
		/** The by alert. */
		BY_ALERT,
		/** The by windowdialog */
		BY_WINDOWDIALOG_TITLE,
		/** The by frame index */
		BY_FRAME_INDEX,
		/** The by winID. */
		BY_WINID;
	}

	/**
	 * @Description : Switch to.
	 * @param String:switchType , switchExpValue 
	 * @return  boolean
	 */
	public boolean switchTo(String switchType, String switchExpValue) {
		try {
			switch (WindowSwitchType.valueOf(switchType)) {
			case BY_WINTITLE:
				return switchWindow(switchType, switchExpValue);
			case BY_WINURL:
				return switchWindow(switchType, switchExpValue);
			case BY_WINDOWDIALOG_TITLE:
				return switchWindowDialog(switchType, switchExpValue);
			case BY_FRAME:
				driver.switchTo().defaultContent();
				driver.switchTo().frame(switchExpValue);
				break;
			case BY_FRAME_INDEX:
				driver.switchTo().defaultContent();
				driver.switchTo().frame(Integer.parseInt(switchExpValue));
				break;
			case BY_PARENTFRAME:
				driver.switchTo().parentFrame();
				break;
			case BY_DEFAULT:
				driver.switchTo().defaultContent();
				break;
			case BY_WINCLOSE:
				driver.close();
				break;
			case BY_ALERT:
				WebDriverWait alertWait = new WebDriverWait(driver, 5);
				alertWait.until(ExpectedConditions.alertIsPresent());
				Alert alert = driver.switchTo().alert();
				alert.accept();
				break;
			case BY_WINID:
				driver.switchTo().window(switchExpValue);
				break;
			default:
				throw new IllegalArgumentException("Parameter switchtype should be BY_WINTITLE| BY_WINURL|BY_FRAME|BY_PARENTFRAME|BY_DEFAULT|BY_ALERT");
			}

		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * @Description : Switch window.
	 * @param String : switchType ,winExpValue
	 * @return boolean
	 * @throws Exception 
	 */
	private boolean switchWindow(String switchType, String winExpValue) throws Exception {

		log.info("***** In method 'switchWindow()' for: " + switchType + " Window Name:" + winExpValue + " *****");

		waitForPageLoad();
		TimeUnit.SECONDS.sleep(2);

		boolean bSwitchWindow = false;
		String winActValue = "";
		Set<String> availableWindows = driver.getWindowHandles();
		if (!availableWindows.isEmpty()) {
			for (String windowId : availableWindows) {
				if (switchType.equalsIgnoreCase("BY_WINTITLE")) {
					winActValue = driver.switchTo().window(windowId).getTitle().trim().toLowerCase();
				} else {
					winActValue = driver.switchTo().window(windowId).getCurrentUrl().trim().toLowerCase();
				}

				winExpValue = winExpValue.trim().toLowerCase();
				if (winActValue.contains(winExpValue)) {
					bSwitchWindow = true;
					log.info("Window '" + winExpValue + "' switched successfully!!");
					driver.manage().window().maximize();
					break;
				}
			}

			log.info("***** Exit method 'switchWindow()' for: " + switchType + " Window Name:" + winExpValue + " *****");
		}

		waitForPageLoad();

		return bSwitchWindow;
	}

	/**
	 * @Description : Switch window with no title.
	 * @return boolean
	 * @throws InterruptedException
	 */
	public boolean switchWindowWithNoTitle() throws InterruptedException {

		log.info("***** In method 'switchWindowWithNoTitle()' *****");

		waitForPageLoad();
		TimeUnit.SECONDS.sleep(2);

		Set<String> availableWindows = driver.getWindowHandles();
		availableWindows = driver.getWindowHandles();
		String winTitle;
		Boolean bSwitchWindow = false;

		for (String windowId : availableWindows) {
			winTitle = driver.switchTo().window(windowId).getTitle().trim();
			if (winTitle.length() == 0) {
				log.info("Window '" + winTitle + "' switched successfully!!");
				driver.switchTo().window(windowId);
				bSwitchWindow = true;
				break;
			}
		}	
		waitForPageLoad();

		log.info("***** Exit method 'switchWindowWithNoTitle()' *****");
		return bSwitchWindow;
	}

	/**
	 * @Description : Switch window dialog.
	 * @param String :switchType ,winExpValue
	 * @return boolean
	 */
	public boolean switchWindowDialog(String switchType, String winExpValue) {

		try {

			log.info("***** In method 'switchWindowDialog()' for: " + switchType + " Window Name:" + winExpValue + " *****");

			waitForPageLoad();
			TimeUnit.SECONDS.sleep(2);

			String currentWindowId = driver.getWindowHandle();

			for (String winHandle : driver.getWindowHandles()) {
				String actualWindowTitle = driver.switchTo().window(winHandle).getTitle();
				if (actualWindowTitle.contains(winExpValue) && !currentWindowId.equals(winHandle)) {
					driver.switchTo().window(winHandle);
					break;
				}
			}
			// Thread.sleep(1000);
			waitForPageLoad();
			log.info("***** EXIT method 'switchWindowDialog()' for: " + switchType + " Window Name:" + winExpValue + " *****");

		} catch (Exception ex) {

			return false;
		}
		return true;
	}

	/**
	 * @Description : Maximize window.
	 */
	public void maximizeWindow() {

		try {
			driver.manage().window().maximize();
		} catch (Exception e) {

		}

	}

	/**
	 * @Description : Gets the windows count.
	 * @return int
	 */
	public int getWindowsCount() {

		try {
			log.info("Return Window Count");
			return (driver.getWindowHandles().size());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

	}

	/**
	 * @Description :Checks if is data present.
	 * @param String: data
	 * @return boolean
	 */
	public boolean isDataPresent(String data) {
		try {
			if (data.isEmpty() || data.length() == 0 || data == null || data.equals("null")) {
				return false;
			} else {
				return true;
			}

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @Description : Gets the page source.
	 * @return String
	 */
	public String getPageSource() {
		try {

			return driver.getPageSource();

		} catch (Exception ex) {

			return null;
		}

	}

	/**
	 * @Description :Action click.
	 * @param Element: element
	 * @return boolean
	 */
	public boolean moveToElementAndClick(WebElement element) throws Exception {
		try {

			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();
		} catch (Exception ex) {
			log.error(ex);
			throw new RuntimeException(ex.fillInStackTrace());

		}
		return true;
	} 

	/**
	 * @Description : No ofwindows.
	 * @return int
	 */
	public int noOfwindows() {

		Set<String> listValues = null;

		listValues = driver.getWindowHandles();
		return listValues.size();

	}

	/**
	 * @Description : Cancel alert.
	 */
	public void cancelAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.dismiss();
		} // try
		catch (Exception e) {

		}

	}

	/**
	 * @Description :Split values.
	 * @param String :strValues , delimiter
	 * @return the string[]
	 */
	public String[] splitValues(String strValues, String delimiter) {
		String values[] = strValues.split(delimiter);

		String formattedArray[] = new String[values.length];

		for (int i = 0; i < values.length; i++) {
			formattedArray[i] = values[i].trim();
		}

		return formattedArray;

	}

	/**
	 * @Description :Click quick launch.
	 * @param int x , y
	 * @return boolean
	 */
	public boolean clickQuickLaunch(int x, int y) {

		try {
			log.info("Method 'clickQuickLaunch' starts here");
			// There is  a mouse event which take place in this method. Hence need of hard wait. WaitForPageLoad will not suffice the need as the page would be loaded.
			Thread.sleep(200);
			// while (System.currentTimeMillis() < System.currentTimeMillis() +
			// (2 * 1000)) {}

			Robot r = new Robot();
			r.mouseMove(x, y);
			// There is  a mouse event which take place in this method. Hence need of hard wait. WaitForPageLoad will not suffice the need as the page would be loaded.
			Thread.sleep(1000);
			// while (System.currentTimeMillis() < System.currentTimeMillis() +
			// (1 * 1000)) {}

			r.mousePress(InputEvent.BUTTON1_MASK);
			r.mouseRelease(InputEvent.BUTTON1_MASK);
			// There is  a mouse event which take place in this method. Hence need of hard wait. WaitForPageLoad will not suffice the need as the page would be loaded.
			Thread.sleep(2000);
			// while (System.currentTimeMillis() < System.currentTimeMillis() +
			// (2 * 1000)) {}

			log.info("Method 'clickQuickLaunch' ends here");

		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description : Close Word Document 
	 * @return boolean
	 */
	public boolean closeWordDocument() {

		try {
			Thread.sleep(15000);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ALT);
			r.keyPress(KeyEvent.VK_F4);
			r.keyRelease(KeyEvent.VK_ALT);
			r.keyRelease(KeyEvent.VK_F4);
			Thread.sleep(3000);

		} catch (Exception ex) {

			return false;
		}

		return true;
	}



	/**
	 * @Description :Gets the todays date.
	 * @return String
	 */
	public String getTodaysDate() {

		String todayAsString = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		return todayAsString;
	}

	/**
	 * @Description :Wait for alert.
	 * @return boolean
	 */
	public boolean WaitForAlert() {

		try {
			log.info("Method 'clickQuickLaunch' starts here");
			WebDriverWait wait = new WebDriverWait(driver, 50);
			wait.until(ExpectedConditions.alertIsPresent());
		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description : Wait for frameto switch by index.
	 * @param int : Index
	 * @return boolean
	 */
	public boolean WaitForFrametoSwitchByIndex(int Index) {

		try {
			log.info("Method 'clickQuickLaunch' starts here");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(Index));
		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description : Wait for frameto switch by name.
	 * @param String : frameName
	 * @return boolean
	 */
	public boolean WaitForFrametoSwitchByName(String frameName) {

		try {
			log.info("Method 'clickQuickLaunch' starts here");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description :Press enter.
	 * @return boolean
	 */
	public boolean pressEnter() {
		try {
			log.info("Method 'clickQuickLaunch' starts here");
			Actions act = new Actions(driver);
			act.keyDown(Keys.ENTER);
			act.keyUp(Keys.ENTER);
		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description :Checks if is image present.
	 * @param Element : imgElem
	 * @return boolean
	 */
	public boolean isImagePresent(WebElement imgElem) {
		try {
			log.info("Method 'isImagePresent' starts here");
			String src = imgElem.getAttribute("src");
			if (!src.equalsIgnoreCase("null") && src.length() > 1) {
				return true;
			} else {
				return false;
			}

		} catch (Exception ex) {

			return false;
		}

	}

	/**
	 * @Description : Gets the connection.
	 * @return Connection
	 */
	public Connection getConnection() {

		Connection connection = null;

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@ldap://oid:389/ngsot101,cn=OracleContext,dc=allstate,dc=com", "NGTESTI7USR", "NGTESTI7USR");

		} catch (ClassNotFoundException | SQLException e) {
			return null;

		}
		return connection;
	}

	/**
	 * @Description : Checks if is window closed after operation.
	 * @param int :expectedNoOfWindowsAfterCloser
	 * @throws InterruptedException
	 */
	public void IsWindowClosedAfterOperation(int expectedNoOfWindowsAfterCloser) throws InterruptedException {
		int b = 0;
		while (b <= 6) {
			int a = getWindowsCount();
			if (a == expectedNoOfWindowsAfterCloser) {
				break;
			}
			waitForPageLoad();

			b++;

		}
	}

	/**
	 * @Description : Checks if is element present.
	 * @param Element : elem
	 * @return boolean
	 */
	public boolean isElementPresent(WebElement elem) {
		try {

			elem.isDisplayed();

		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * @Description : Close all browsers.
	 * @param : String id
	 */
	public void closeAllBrowsers(String id) {
		try {

			Set<String> windowHandles = driver.getWindowHandles();

			for (String s : windowHandles) {
				if (!id.equalsIgnoreCase(s)) {
					driver.switchTo().window(s);
					driver.close();
				}

			}

			driver.switchTo().window(id);

		} catch (Exception ex) {

		}
	}

	/**
	 * @Description : Wait for page load.
	 * @return boolean
	 */
	/*public boolean waitForPageLoad() {
		int waitTime = new Double(20).intValue();

		ExpectedCondition<Boolean> pageLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};

		Wait<WebDriver> wait = new WebDriverWait(driver, waitTime);

		try {
			wait.until(pageLoad);
		} catch (Throwable pageLoadWaitError) {

			return false;

		}
		return true;
	}*/

	/**
	 * @Description : Close the Attachment Window 
	 */
	public void closeAttachmentWindow() {

		try {
			// thread needed to perform following action by robot
			Thread.sleep(15000);
			Robot r = new Robot();
			r.keyPress(KeyEvent.VK_ALT);
			r.keyPress(KeyEvent.VK_F4);
			r.keyRelease(KeyEvent.VK_ALT);
			r.keyRelease(KeyEvent.VK_F4);
			Thread.sleep(3000);

		} catch (Exception ex){ 
		} 
	}

	/**
	 * @Description : JS click.
	 * @param Element:element
	 * @return boolean
	 */
	public boolean JSScrollHorizontillay(WebElement element) {
		try {
			JavascriptExecutor jsexecutor = (JavascriptExecutor) driver;
			jsexecutor.executeScript("arguments[0].scrollIntoView(true);",element);
			jsexecutor.executeScript("scrollTo(3000,0);");
		} catch (Exception ex) {
			log.error(ex);
			return false;
		}
		return true;
	}


	public void closeCurrentBrowser(String id) {
		try {
			Set<String> windowHandles = driver.getWindowHandles();

			for (String s : windowHandles) {
				if (id.equalsIgnoreCase(s)) {
					driver.switchTo().window(s);
					driver.close();
				}

			}
			driver.switchTo().window(id);

		} catch (Exception ex) {

		}
	}

	/*
	 * @Description : Switch to window using specific window handle.
	 */
	public void switchToWindow(String parentWinID) {
		try {
			waitForPageLoad();
			TimeUnit.SECONDS.sleep(2);

			driver.switchTo().window(parentWinID);

			waitForPageLoad();
		} catch (Exception e) {

		}

	}

	/**
	 * @Description : get alert text.
	 */
	public String getAlertText() {
		String alertText = null;
		try {
			Alert currentAlert = driver.switchTo().alert();
			alertText= currentAlert.getText();

		} // try
		catch (Exception e) {

		}
		return alertText;

	}


	/**
	 * @Description : JS scroll into view.
	 * @param Element:element
	 * @return boolean
	 */

	public boolean scrollIntoViewClick(WebElement element) throws Exception {
		try {
			Actions actions = new Actions(driver);

			actions.moveToElement(element, 0, 0).click().build().perform();
		} catch (Exception ex) {
			log.error(ex);
			throw new RuntimeException(ex.fillInStackTrace());

		}
		return true;
	} 

	/**
	 * @Description :Select from dropdown by index.
	 * @param Element : element
	 * @param int : index
	 * 
	 */
	public void selectByIndexFromDropdown(WebElement element, int index) {
		log.debug("Begin of  DriverMethod.actionMouseOver()");
		try {

			Select sel = new Select(element);
			sel.selectByIndex(index);


		} catch (Exception ex) {
			log.error(ex.getStackTrace());

		}

	}

	/**
	 * @Description : Gets the current US timeZone date.
	 * @return String
	 */
	public String getCurrentTimeZoneDate() {
		try {

			Calendar c = Calendar.getInstance();
			c.setTimeZone(TimeZone.getTimeZone("US/Central"));
			Date date = c.getTime();
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String strDate = df.format(date);
			return strDate;

		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * @Description : compare Dates.
	 * @return String
	 */
	public static boolean compareDates(String expectedDate, String actualDate) {

		boolean status = false;
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date expected = df.parse(expectedDate);
			Date actual = df.parse(actualDate);

			Assert.assertEquals(expected,actual);
			status= true;

		} catch (Exception ex) {
			System.out.println(ex);
			return status;
		}

		return status;
	}

	/**
	 * @Description :send data.
	 * @param Element: element
	 * @return boolean
	 */
	public void setData(WebElement element,String data) throws Exception {
		try {

			element.sendKeys(data);

		} catch (Exception ex) {
			log.error(ex);
			throw new RuntimeException(ex.fillInStackTrace());

		}
	} 

	/**
	 * @Description :Press F12.
	 * @return boolean
	 */
	public boolean pressF12(WebElement element) {
		try {
			log.info("Method 'pressF12' starts here");
			Actions actions = new Actions(driver);

			actions.moveToElement(element).click().build().perform();
			actions.sendKeys(Keys.F12);
			actions.perform();			


		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description :Press Tab
	 * @return boolean
	 */
	public boolean pressTab() {
		try {
			log.info("Method 'pressTab' starts here");
			Actions actions = new Actions(driver);

			actions.sendKeys(Keys.TAB);
			actions.build().perform();			


		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description :Press Down Key
	 * @return boolean
	 */
	public boolean pressDownKey() {
		try {
			log.info("Method 'presDown' starts here");
			Actions actions = new Actions(driver);
			actions.sendKeys(Keys.DOWN);
			actions.build().perform();			
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	/**
	 * @Description : Scroll  the page down
	 * @return boolean
	 */
	public boolean scrollPageDown(int height) {
		try {
			log.info("Method 'scrollPageDown' starts here");
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			String arg = "window.scrollBy(0,"+height+")";

			jse.executeScript(arg, "");

		} catch (Exception ex) {

			return false;
		}

		return true;
	}

	/**
	 * @Description : getCurrentURL : get current URL
	 * @return : void
	 * @param : String :userId
	 */
	public String getCurrentURL() {
		String url ="";
		try {
			url = driver.getCurrentUrl();
			log.info("Current URL: " + url);
		} catch (Exception ex) {
			log.error(ex.getStackTrace());
		}
		return url;

	}


	/**
	 * @Description : navigateToURL : navigate to given URL
	 * @return : void
	 * @param : String :userId
	 */
	public void navigateToURL(String url) {
		try {
			driver.get(url);
			log.info("Navigated to URL: " + url);
		} catch (Exception ex) {
			log.error(ex.getStackTrace());
		}

	}

	/**
	 * @Description : Gets the random number of 7 digit.
	 * @param Element: element
	 * @return String
	 */
	public String getRandomNumber() {
		Random rnd = new Random(); 
		int n = 1000000 + rnd.nextInt(9000000);
		String newNumber = Integer.toString(n);
		return newNumber;

	}

}
