package com.reporting;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.*;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public class Reporter {
	private static String testCaseName = "";
	private static List<Result> details;
	private static List<Result> passMethodDetails;
	private static List<Result> failMethodDeails;
	private static final String resultPlaceholder = "<!-- INSERT_RESULTS -->";
	private static final String methodresultUtils= "../reporting/methodexecutiondetails.html";
	static String reportpath = "src/com/reporting/";
	private static final String templatePath = reportpath+"report_template.html";
	
	private static final String methodTemplatePath = reportpath+"methodexecutiondetails.html";
	static String envDetails = "Browser: Chrome , OS: Windows";

	public Reporter(String testCase) {
		testCaseName = testCase;
		details = new ArrayList<Result>();
		passMethodDetails = new ArrayList<Result>();
		failMethodDeails = new ArrayList<Result>();
	}

	public synchronized  static void report(String status, String executionTime) {
		Result r = new Result(status,executionTime , envDetails );
		details.add(r);
	}

	public synchronized  static void reportMethodDetails(String className,String methodName,String executionTime, String result) {
		Result r = new Result(className, methodName, executionTime, result);
		passMethodDetails.add(r);
	}

	public synchronized  static void reportErrorDetails(String className,String methodName,String executionTime, String result, String imageLocation, String stackTraceElements) {
		Result r = new Result(className, methodName, executionTime, result, stackTraceElements, imageLocation ); 
		failMethodDeails.add(r);
	}

	public synchronized  static void showResults() {
		for (int i = 0;i < details.size();i++) {
			System.out.println("Result " + Integer.toString(i) + ": " + details.get(i).getResult());
		}
	}

	public static void writeResults() {
		try {
			String reportIn = new String(Files.readAllBytes(Paths.get(templatePath).toAbsolutePath()));
			reportIn = reportIn.replaceFirst("<!-- TEST_NAME -->","AmazonLogin");
			for (int i = 0; i < details.size();i++) {
				String result= "";
				if ("Fail".equals(details.get(i).getResult())) {
					result = "</td><td bgcolor="+"#FF0000"+">" + details.get(i).getResult() + "</td>";
				} else {
					result = "</td><td bgcolor="+"green"+">" + details.get(i).getResult() + "</td>";
				}
				reportIn = reportIn.replaceFirst(resultPlaceholder,"<tr><td>" + details.get(i).getExecutionTime() + "</td>"+result+"<td>" + details.get(i).getResultText() + "</td></tr>" + resultPlaceholder);
			}

			String currentDate = new SimpleDateFormat("yyyy-MM-dd HHmmssSSS").format(new Date());
			String reportPath = reportpath+"\\" +testCaseName+ currentDate+ ".html";
			String methodsReportPath = reportpath + testCaseName+"Method"+currentDate+ ".html";
			String methodsReport = testCaseName +"Method"+currentDate+ ".html";
			
			reportIn = reportIn.replaceFirst(methodresultUtils, "../reporting/"+methodsReport);

			Files.write(Paths.get(reportPath).toAbsolutePath(),reportIn.getBytes(),StandardOpenOption.CREATE);
			
			String reportInMethod = new String(Files.readAllBytes(Paths.get(methodTemplatePath).toAbsolutePath()));
			reportInMethod = reportInMethod.replaceFirst("<!-- TEST_NAME -->","AmazonLogin");
			
			String result= "";
			
			if (failMethodDeails.size()>0) {
				passMethodDetails.addAll(failMethodDeails);
				String errorMsg =replaceSpecialCharectors(failMethodDeails.get(0).getException().toString());
				
				reportInMethod = reportInMethod.replaceFirst("<!-- ERROR_LOGS -->",errorMsg);
				reportInMethod = reportInMethod.replaceFirst("error.jpg",testCaseName+".png");
			} else {
				reportInMethod = reportInMethod.replaceFirst("<!-- ERROR_LOGS -->","");
				reportInMethod = reportInMethod.replaceFirst("error.jpg","");
			}
			
			for (int i = 0; i < passMethodDetails.size();i++) {
				if ("Fail".equals(passMethodDetails.get(i).getResult())) {
					result = "<td bgcolor="+"#FF0000"+">" + passMethodDetails.get(i).getResult() + "</td>";
				} else {
					result = "<td bgcolor="+"green"+">" + passMethodDetails.get(i).getResult() + "</td>";
				}
				reportInMethod = reportInMethod.replaceFirst(resultPlaceholder,
						"<tr>"
						+ "<td>" + passMethodDetails.get(i).getClassName() + "</td>"
						+ "<td>"+passMethodDetails.get(i).getMethodName()+"</td>"
					    + "<td>" + passMethodDetails.get(i).getExecutionTime() +"</td>"
					    + result +"</tr>" + resultPlaceholder);
			}
			
			
			Files.write(Paths.get(methodsReportPath).toAbsolutePath(),reportInMethod.getBytes(),StandardOpenOption.CREATE);
			
		} catch (Exception e) {
			System.out.println("Error when writing report file:\n" + e.toString());
		}
	}
	
	/*
	 * 
	 */
	private  synchronized  static String replaceSpecialCharectors(String input) {
       // Pattern pt = Pattern.compile("[^a-zA-Z0-9_-]");
		 Pattern pt = Pattern.compile("[^a-zA-Z0-9.,_-]");
        
        Matcher match= pt.matcher(input);
        while(match.find()){
            String s= match.group();
            input=input.replaceAll("\\"+s, " ");
        }
		return input;
	}
}