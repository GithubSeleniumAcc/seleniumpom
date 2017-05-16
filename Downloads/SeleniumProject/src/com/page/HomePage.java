package com.page;

import com.page.base.BasePage;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public interface HomePage extends BasePage {
	public String title = "title";
	public String signin = "signin";

	public void verifyTitle();

	public void clickSignIn();
}
