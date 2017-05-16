package com.reporting;
/**
 * 
 * @author rajendra.beelagi
 *
 */
public class Result {
	
	private String executionTime;
	private String result;
	private String resultText;
	private String exception;
	private String methodName;
	private String className;
	private String imagePath;
	
	public Result(String result , String executionTime , String resultText) {
		this.result = result;
		this.executionTime = executionTime;
		this.resultText = resultText;
	}
	
	public Result(String className , String methodName ,String executionTime, String result) {
		this.className = className;
		this.methodName = methodName;
		this.executionTime = executionTime;
		this.result = result;
	}
	
	public Result(String className , String methodName ,String executionTime, String result , String exception ,  String imagePath) {
		this( className, methodName , executionTime,  result ); 
		this.exception = exception;
		this.resultText = imagePath;
	}
	
	public String getException() {
		return exception;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getClassName() {
		return className;
	}

	public String getImagePath() {
		return imagePath;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return this.result;
	}
	
	public void setResultText(String resultText) {
		this.resultText = resultText;
	}
	
	public String getResultText() {
		return this.resultText;
	}
	
	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}
}