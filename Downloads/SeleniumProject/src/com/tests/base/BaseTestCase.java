package com.tests.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.reporting.Reporter;
import com.utils.GenericMethods;
import com.utils.PageReuisits;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public class BaseTestCase {
	protected String testCaseName="";

	protected WebDriver driver = null;
	protected PageReuisits pageReuisits = null;
	protected Reporter reporter = null;
	
	protected void  beforeTest() {
		reporter =  new Reporter(testCaseName);
	}

	@AfterMethod
	public void closeInstance() {
		GenericMethods.closeAllWindows();
	}
	
	//Todo : need to implement
	@DataProvider(name="dataProvidor")
	public Object[][] getData(){
		Object[][] data = new Object[3][2];
		return data;
	}
}