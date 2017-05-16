package com.page;

import com.page.base.BasePage;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public interface MakeMyTripPage extends BasePage{
	
	public String fromTextBox = "fromTextBox";
	public String toTextBox = "toTextBox";
	public String searchBtn = "searchBtn";
	
	public void searchTriptDetails(String recordId) throws Exception;

}
