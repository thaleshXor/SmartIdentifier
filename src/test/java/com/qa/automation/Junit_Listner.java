package com.qa.automation;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class Junit_Listner {

	public static void listner() {
		// TODO Auto-generated method stub
		JUnitCore junit = new JUnitCore();
		Result result = junit.run(Junit_Login.class);
		System.out.println("Login JUnit Status: " + result.wasSuccessful());
		
	}

}
