package com.utils;

import java.util.Map;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author rajendra.beelagi
 *
 */
public class PageReuisits {
	private WebDriver driver;
	private String  testCaseName;
	Map<String, String> tesDataValuesMap;
	
	public String getTestCaseName() {
		return testCaseName;
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	public Map<String, String> getTestDataMap() {
		return tesDataValuesMap;
	}

	public PageReuisits(WebDriver driver, Map<String, String> tesDataValuesMap , String testCaseName) {
		this.driver = driver;
		this.tesDataValuesMap = tesDataValuesMap;
		this.testCaseName = testCaseName;
	}

}
