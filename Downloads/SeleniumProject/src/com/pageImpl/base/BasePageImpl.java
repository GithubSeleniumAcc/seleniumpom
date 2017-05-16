package com.pageImpl.base;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.page.base.BasePage;
import com.utils.GenericMethods;
import com.utils.PageReuisits;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public abstract class BasePageImpl implements BasePage {
	protected WebDriver driver = null;
	protected GenericMethods genericMethods = null;
	protected String testCaseName = "";
	
	protected BasePageImpl(PageReuisits pageReuisits) {
		this.driver = pageReuisits.getDriver();
		this.genericMethods = new GenericMethods(driver , pageReuisits.getTestDataMap());
		this.testCaseName = pageReuisits.getTestCaseName();
	}
	
	protected String takeScreenShot() throws Exception{
		try {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("src/com/reporting/"+ testCaseName +".png").getAbsoluteFile());
			return "src/com/reporting/"+ testCaseName +".png";
			//return convertToJpg(scrFile);
		} catch (WebDriverException e) {
			throw e;
		}
	}
	
	
	public String convertToJpg(File path) {
		BufferedImage bufferedImage;

		try {

		  //read image file
		  bufferedImage = ImageIO.read(path);

		  // create a blank, RGB, same width and height, and a white background
		  BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
				bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		  newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

		  // write to jpeg file
		  ImageIO.write(newBufferedImage, "jpg", new File("src/com/reporting/"+ testCaseName +".jpg").getAbsoluteFile());

		  System.out.println("Done");

		} catch (IOException e) {

		  e.printStackTrace();

		}
		return "src/com/reporting/";
	}
}
