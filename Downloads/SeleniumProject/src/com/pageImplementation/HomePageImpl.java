package com.pageImplementation;

import static org.testng.Assert.assertEquals;

import com.page.HomePage;
import com.pageImpl.base.BasePageImpl;
import com.utils.PageReuisits;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public class HomePageImpl extends BasePageImpl implements HomePage {

	private static final String HOME_PAGE = "HomePage";

	public HomePageImpl(PageReuisits pageReuisits) {
		super(pageReuisits);
	}

	/**
	 * Verify the title
	 */
	
	public void verifyTitle() {
		String actualTitle = driver.getTitle();
		assertEquals(
				actualTitle,
				"Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more");
	}


	/**
	 * Click on Signing button
	 */
	
	public void clickSignIn() {
		genericMethods.clickElement(signin, HOME_PAGE);
	}

	/**
	 * returns Page Name
	 */
	
	public String getPageName() {
		return "HomePage";
	}

}
