package com.pageImplementation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.page.MakeMyTripPage;
import com.pageImpl.base.BasePageImpl;
import com.reporting.Reporter;
import com.utils.PageReuisits;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public class MakeMyTripPageImpl extends BasePageImpl implements MakeMyTripPage {

	public MakeMyTripPageImpl(PageReuisits pageReuisits) {
		super(pageReuisits);
	}

	@Override
	public String getPageName() {
		return "MakeMyTripPage";
	}

	@Override
	public void searchTriptDetails(String recordId) throws Exception{
		Long startTime = System.currentTimeMillis();
		try {
			genericMethods.setValueInTextBox(fromTextBox, getPageName(), recordId);
			genericMethods.setValueInTextBox(toTextBox, getPageName(), recordId);
			genericMethods.clickElement(searchBtn, getPageName());
			Reporter.reportMethodDetails(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), String.valueOf(System.currentTimeMillis()-startTime), "Pass");
			
		} catch (Exception e) {
			Writer result = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(result);
		    e.printStackTrace(printWriter);
		    result.toString();
			Reporter.reportErrorDetails(this.getClass().getSimpleName(), Thread.currentThread().getStackTrace()[1].getMethodName(), String.valueOf(System.currentTimeMillis()-startTime), "Fail", takeScreenShot() , result.toString())   ;
			throw e;
		}
	}

}
