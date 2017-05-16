package com.pageImplementation;

import java.io.FileNotFoundException;

import com.page.LoginPage;
import com.pageImpl.base.BasePageImpl;
import com.utils.PageReuisits;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public class LoginPageImpl extends BasePageImpl implements LoginPage  {
	private static final String LOGIN_PAGE = "LoginPage";
	public LoginPageImpl(PageReuisits pageReuisits) {
		super(pageReuisits);
	}

	public String getPageName() {
		return LOGIN_PAGE;
	}

	/**
	 * Method to accept username , password and click on signinbutton 
	 * @param recordId
	 */
	
	public void loginToApplication(String recordId) throws FileNotFoundException {
		try {
			log.debug("Begin Login To Appliation");
			genericMethods.setValueInTextBox(userName, getPageName(), recordId);
			genericMethods.setValueInTextBox(password, getPageName(),recordId);
			genericMethods.clickElement(signInSubmit,  getPageName());
			log.debug("end Login To Appliation");
		} catch (Exception e) {
			log.error("Exception in loginToApplication");
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally{
		}
	}

}
