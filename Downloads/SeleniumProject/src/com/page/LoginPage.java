package com.page;

import java.io.FileNotFoundException;

import com.page.base.BasePage;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public interface LoginPage extends BasePage {
	
	public  String userName = "userName";
	public  String password = "password";
	public String signInSubmit="signInSubmit";
	
	public void loginToApplication(String recordId) throws FileNotFoundException;
}
