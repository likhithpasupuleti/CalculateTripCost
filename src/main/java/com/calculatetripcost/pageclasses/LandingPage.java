package com.calculatetripcost.pageclasses;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.calculatetripcost.baseclass.BaseClass;
import com.calculatetripcost.utils.ReadUserDataFromExcel;
import com.calculatetripcost.utils.RunScriptWriteExcel;

public class LandingPage extends BaseClass
{
	//Constructor
	public LandingPage(WebDriver driver,ExtentTest logger)
	{
		super(driver,logger);
	}
	
	@FindBy(xpath="//span[@class='_3088r1hR']")
	public List<WebElement> click_HolidayHomes_from_menu;
	
	//Click on Holiday Homes
	public void click_HolidayHomes()
	{
		try
		{
			takeScreenshot("Trip Advisor Home Page");
			logger.log(Status.INFO, "Clicking the Holiday Homes Button in Landing Page");
			for(WebElement click_HolidayHomes:click_HolidayHomes_from_menu)
			{
				if(click_HolidayHomes.getText().contains("Holiday"))
				{
					click_HolidayHomes.click();   
					break;
				}
			}
			reportPass("Holiday Homes Button Successfully Clicked in Landing Page");
			RunScriptWriteExcel.write(6,6,"Pass");        //For writing test case Passed in Run Script WorkSheet
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(6,6,"Fail");            //For writing test case Failed in Run Script WorkSheet
		}
	}
	
	@FindBy(xpath="(//input[@class='_3qLQ-U8m'])[3]")
	public WebElement enter_destination;
	
	//Sending Keys Nairobi as destination in Location Input box
	public void enterDestination()
	{
		try
		{
			takeScreenshot("Location Box");
			
			logger.log(Status.INFO,"Destination should entered in location input textbox");
			String destination=ReadUserDataFromExcel.ReadDestination();   //Reads Nairobi Destination from Holiday_Homes_User_Details.xlsx
			enter_destination.sendKeys(destination);
			
			takeScreenshot("Destination Entered");
			reportPass("Destination is Entered in location input text box");
			
			RunScriptWriteExcel.write(7,6,"Pass");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			
			RunScriptWriteExcel.write(7,6,"Fail");
		}
	}
	
	@FindBy(xpath="//div[@class='_27pk-lCQ']")
	WebElement Parent;
	//Choose Nairobi from auto_suggestions
	public NairobiSearchResultsPage choose_Nairobi()
	{
		try
		{
			List<WebElement> Search_Suggestions=new ArrayList<WebElement>(); 
			Search_Suggestions = Parent.findElements(By.tagName("a"));
			int temp=0;
			
			takeScreenshot("Auto Suggestions Displayed");
			logger.log(Status.INFO,"Search Suggestions are displayed,Choose destination from the auto suggestion");
			
			//Here we check if the suggestions shown matches our desired suggestion
			for(int i = 0 ; i < Search_Suggestions.size(); i++) 
			{
				String link = Search_Suggestions.get(i).getAttribute("href");
				//Now we are checking if the search suggestions href contains the href of the page we want
				if(link.equalsIgnoreCase("/VacationRentals-g294207-Reviews-Nairobi-Vacation_Rentals.html")) 
				{
					temp = i;
					break;
				}
			}
			//Navigating to the href that we got from Search Suggestions
			driver.get(Search_Suggestions.get(temp).getAttribute("href"));
			reportPass("Destination from the auto suggestion is clicked");
			
			takeScreenshot("Holiday Homes Results Page");
			logger.log(Status.INFO, "Page Navigated to Nairobi Results Search Page");
			RunScriptWriteExcel.write(8,6,"Pass"); 
	
			NairobiSearchResultsPage nsrp=new NairobiSearchResultsPage(driver,logger);
			PageFactory.initElements(driver,nsrp);
			return nsrp;
		}
		catch(Exception e) 
		{
			System.out.println("Provided Destination Search-Suggestions Not available");
			driver.get("https://www.tripadvisor.in/VacationRentals-g294207-Reviews-Nairobi-Vacation_Rentals.html");  //If in auto-suggestion it is not displayed,then get this url to Open Nairobi Results Page
			reportFail(e.getMessage());
			
			RunScriptWriteExcel.write(8,6,"Fail");
		}
		return null;
	}
}
