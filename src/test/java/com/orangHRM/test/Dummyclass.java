package com.orangHRM.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;

public class Dummyclass extends BaseClass{

	@Test
	public void dummytest()
	{
		String title= getDriver().getTitle();
		assert title.equals("OrangeHRM"):"Title Test Failed";
		
		System.out.println("test Passed");
	}
}
