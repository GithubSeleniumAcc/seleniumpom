package com.page.base;

import org.apache.log4j.Logger;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public interface BasePage {
	static Logger log = Logger.getLogger(BasePage.class.getName());
	public String getPageName();
}
