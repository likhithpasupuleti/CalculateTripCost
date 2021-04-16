package com.calculatetripcost.pageclasses;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.calculatetripcost.baseclass.BaseClass;
import com.calculatetripcost.utils.ReadUserDataFromExcel;
import com.calculatetripcost.utils.RunScriptWriteExcel;
import com.calculatetripcost.utils.WriteDetailsOfHotelToExcel;

public class NairobiSearchResultsPage extends BaseClass
{
	//Constructor
	public NairobiSearchResultsPage(WebDriver driver,ExtentTest logger)
	{
		super(driver,logger);
	}
	
	@FindBy(xpath="//div[@class='_6DVWYAzK']")
	public WebElement holiday_Home_number;
	
	@FindBy(xpath="//div[@class='lRYY2wxe']")            //Check-in
	public WebElement click_calendar;                
	
	//Click on Check-in
	public void click_check_in()
	{
		try
		{
			logger.log(Status.INFO, "Check-in Calendar should be clicked");
			click_calendar.click();
			reportPass("Check-in Calendar is clicked");
			
			takeScreenshot("Check-in Calendar Displayed");
			RunScriptWriteExcel.write(9,6,"Pass"); 
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(9,6,"Fail"); 
		}
	}
	
	public Calendar calendar = Calendar.getInstance();   //Object Creation of Calendar
	
	@FindBy(xpath="//div[@class='_3ULdV0X_ ']")          //Commonly shared xpath for both Check-in and Check-out calendar dates
	public List<WebElement> click_date;
	
	//Choose date from Check-in calendar
	public void choose_checkin_date()
	{
		try
		{	
			calendar.add(Calendar.DAY_OF_YEAR, 1);        //Add 1 to the day of calendar to book tickets for tomorrow 
			Date tomorrow = calendar.getTime();    //16-04-2021 19:00:00 UTC
			
			DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");     //Get date in this format  //Apr 16 2021

			String tomorrowAsString = dateFormat.format(tomorrow);   //Date to String
			

			@SuppressWarnings({ "deprecation", "unused" })
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(30, TimeUnit.SECONDS) 			
					.pollingEvery(5, TimeUnit.SECONDS) 			
					.ignoring(NoSuchElementException.class);            
	
			logger.log(Status.INFO,"Check-in calendar should be displayed and Tommorow date should be selected");
			for(WebElement calendar_checkin:click_date)
			{
				if(calendar_checkin.getAttribute("aria-label").contains(tomorrowAsString))  
				{
					calendar_checkin.click();
					break;
				}
			}
			reportPass("Check-in calendar is displayed and Tomorrow Date is selected");
			
			takeScreenshot("Date Selected in Check-in Calendar");
			RunScriptWriteExcel.write(10,6,"Pass");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(10,6,"Fail");
		}
	}
	
	//Choose date from Check-out calendar
	public void choose_checkout_date()
	{
		try
		{	
			calendar.add(Calendar.DAY_OF_YEAR, 5);          //Add 5 to the day of calendar to book tickets for after 5 days from tomorrow
			Date fifth_day = calendar.getTime();
			
			DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");

			String fifth_day_As_String = dateFormat.format(fifth_day);
			
			@SuppressWarnings({ "deprecation", "unused" })
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(30, TimeUnit.SECONDS) 			
					.pollingEvery(5, TimeUnit.SECONDS) 			
					.ignoring(NoSuchElementException.class);
			
			logger.log(Status.INFO,"Check-out calendar should be displayed and from tomorrow fifth day should be selected");
			for(WebElement calendar_check_out:click_date)
			{
				if(calendar_check_out.getAttribute("aria-label").contains(fifth_day_As_String))
				{
					calendar_check_out.click();
					break;
				}
			}
			reportPass("Check-out calendar is displayed and fifth day from tomorrow is selected"); 
			
			takeScreenshot("Date selected in Check-out calendar");
			RunScriptWriteExcel.write(11,6,"Pass"); 
			RunScriptWriteExcel.write(12,6,"Pass");
		}
		catch(Exception e)
		{
			RunScriptWriteExcel.write(11,6,"Fail"); 
			RunScriptWriteExcel.write(12,6,"Fail");
			reportFail(e.getMessage());
		}
	}
	
	@FindBy(xpath="//div[@class='_2uJXqhFj']")
	public WebElement click_guests;
	
	//click guests_dropdown
	public void click_guests_dropdown()
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_2uJXqhFj']")));
			
			logger.log(Status.INFO, "Guests filter should be clicked to select number of guests");
			click_guests.click();
			reportPass("Guests filter is clicked to select number of guests");
			takeScreenshot("Guests filter Displayed");
			RunScriptWriteExcel.write(13,6,"Pass");
		}
		catch(Exception e)
		{
			RunScriptWriteExcel.write(13,6,"Fail");
			reportFail(e.getMessage());
		}
	}
	
	@FindBy(xpath="//div[@class='_3Vh-bNyy']/div/div/div[1]")   //Guests list containing Bedrooms,Guests,Bathrooms
	public List<WebElement> fieldNameInGuestsFilter;
	
	//Choose Number of guests as 4+
	public void choose_number_of_guests() throws Exception
	{
		try
		{
			int i=0;
			for(WebElement guestCount:fieldNameInGuestsFilter)
			{
				i++;
				if(guestCount.getText().contains("Guest"))
				{
					break;
				}
			}
			
			WebElement element=driver.findElement(By.xpath("(//div[@class='_3Vh-bNyy']/div/div/div/input)["+i+"]"));   //WebElement of Number of guests intially displaying
			
			String value=element.getAttribute("value");    //Fetching the attribute value 1+ ,2+
			value=value.replace("+","");                   //It will display as 1 , 2 etc. So it replaces + with blank
			int num=Integer.parseInt(value);			   //Converting from String to Integer
			
			int num_guests=ReadUserDataFromExcel.ReadNumberOfGuest() ;      //FETCH value 4 from Holiday_Homes_User_Details.xlsx
			logger.log(Status.INFO,"4 guests should be selected in Guests filter");
			//If it is displaying guests as less than 4+, than it will click Plus
			if(num<num_guests)    // (2<4)
			{
				while(num<num_guests)
				{
					driver.findElement(By.xpath("(//div[@class='_3Vh-bNyy']/div/div/div/span[2]/span)["+i+"]")).click();  //Plus Click
					if(num==num_guests-1)  //4==4
					{
						break;
					}
					num++;
				}
			}
			else    
			{
				//If it is displaying guests as greater than 4+, than it will click Minus
				while(num>num_guests)
				{
					driver.findElement(By.xpath("(//div[@class='_1h6PBHw9']/span[1])["+i+"]")).click();     //Minus Click 
					if(num==num_guests+1)
					{
						break;
					}
					num--; 
				}
			}
			reportPass("4 guests is selected in Guests filter");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
		}
	}
	
	@FindBy(xpath="(//button[@class='ui_button primary fullwidth'])[2]")  //Apply Button Xpath
	public WebElement applyButton;
	
	//Click Apply Button
	public void click_apply_button()
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@class='ui_button primary fullwidth'])[2]")));
			
			logger.log(Status.INFO, "Apply button should be clicked");
			applyButton.click();
			waitFor(3);
			logger.log(Status.PASS,"Apply button is clicked");
			
			RunScriptWriteExcel.write(14,6,"Pass"); 
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(14,6,"Fail"); 
		}
	}
	
	@FindBy(xpath="//div[@class='_1wuPwxoN']//div[@class='_1NO-LVmX']")
	public WebElement clickSortBy;
	
	//Click on Sort By
	public void click_Sort_By()
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='_1wuPwxoN']//div[@class='_1NO-LVmX']")));
			
			logger.log(Status.INFO, "Sort By should be clicked to select Traveller Rating option");
			clickSortBy.click();
			reportPass("Sort By is clicked");
			
			takeScreenshot("Sort By Displayed");
			RunScriptWriteExcel.write(15,6,"Pass"); 
		}
		catch(Exception e)
		{
			RunScriptWriteExcel.write(15,6,"Fail"); 
			reportFail(e.getMessage());
		}
	}
	
	@FindBy(xpath="//div[@class='CfqcQ1jD option _1_x2Cz-c']/span")
	public List<WebElement> select_From_Sort_By;
	
	//Select Traveller Rating from Sort By Filter
	public void chooseSortBy()
	{
		try
		{
			logger.log(Status.INFO, "In Sort By Traveller Rating should be selected");
			for(WebElement l:select_From_Sort_By)
			{
				if(l.getText().contains("Traveller Rating"))
				{
					l.click();    //Click Traveller Rating
					break;
				}
			}
			reportPass("In Sort By Traveller Rating is selected");
			
			RunScriptWriteExcel.write(16,6,"Pass"); 
			takeScreenshot("Traveller Rating Selected");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(16,6,"Fail"); 
		}
	}
	
	@FindBy(xpath="(//span[@class='_3ncH7U-p'])[2]")
	public WebElement amenitiesMoreOptions;
	
	//Click More options in Amenities to display the options
	public void clickAmenitiesMoreOptions()
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(driver,30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[@class='_3ncH7U-p'])[2]")));
			
			//Scroll Down upto More Options
			logger.log(Status.INFO, "Page should be scrolled down to click More Options");
			JavascriptExecutor jse=(JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,500);");
			
			amenitiesMoreOptions.click();
			reportPass("Page is scrolled down and More Options is clicked");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
		}
	}
	
	@FindBy(xpath="//div[@class='_2_0ZaZMd']")
	public List<WebElement> elevatorLiftFilter;
		
	//Check Elevator-Lift Access in Amenities
	public void checkElevatorLiftAccess()
	{
		try
		{
			logger.log(Status.INFO,"Elevator/Lift Access filter should be checked");
			for(WebElement f:elevatorLiftFilter)
			{
				if(f.getText().contains("Elevator"))
				{
					f.click();
					break;
				}
			}
			reportPass("Elevator Access filter is checked");
			takeScreenshot("Elevator Access Checked");
			JavascriptExecutor jse=(JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,-1000);");        //Scrollup
			takeScreenshot("Results Displayed According to Filters");
			RunScriptWriteExcel.write(17,6,"Pass"); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
			RunScriptWriteExcel.write(17,6,"Fail"); 
		}
	}
		
	
	//@FindBy(xpath="//div[@class='_3uQcWelb']")
	//public WebElement ParentHolidayHomes;
	
	@FindBy(xpath="//h2[contains(@class='_2K0y-IXo', '')]")
	public List<WebElement> TitleWebElementlist;
		
	//This function is used to fetch the Hotel Names
	public String[] getHotelNames() 
	{
		try
		{
			//Creating a String array to store the Hotel Names of the First 3 Holiday Homes Search Results
			String[] HolidayHomesNames = new String[3];
			//To print to console
			System.out.println("Holiday Homes Names");
			System.out.println("*******************************************************");
			for(int i = 0 ; i < 3; i++)
			{
				//Storing hotel names inside our array
				HolidayHomesNames[i] = TitleWebElementlist.get(i).getText();   
				System.out.println(TitleWebElementlist.get(i).getText());  //To display Console
			}
			System.out.println(" ");
			reportPass("Holiday Homes Names is displayed and stored in excel");
			return HolidayHomesNames;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	@FindBy(xpath=" //div[@class='_33TIi_t4']")    
	public List<WebElement> PerNightChargesWebElementList;
	
	//This function is to fetch the Hotel Charges per night
	public String[] getChargesPerNight() 
	{
		try
		{
			//Creating a String array to store the Charges Per Night of the First 3 Search Results
			String[] ChargePerNight = new String[3];
			//To print to console
			System.out.println("Charges per Night");
			System.out.println("*******************************************************");
			//Creating a String array to store the Charges per Night of the First 3 Search Results
			for(int i = 0 ; i < 3; i++) 
			{
				//Storing hotel names inside our array
				ChargePerNight[i] =PerNightChargesWebElementList.get(i).getText();
				System.out.println(PerNightChargesWebElementList.get(i).getText());	//To Print on console	
			}
			reportPass("Charges Per Night is displayed and stored in excel");
			System.out.println(" ");
			return ChargePerNight;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@FindBy(xpath="//div[@class='_3f9mHAKH']")    
	public List<WebElement> TotalChargesWebElementList;
	
	//This function is used to fetch the Total Hotel Charges
	public String[] getHotelTotalCharges() 
	{
		try
		{	
			//Creating a String array to store the total charges of the First 3 Search Results
			String[] TotalCharges = new String[3];
			//To print to console
			System.out.println("Total charges");
			System.out.println("*******************************************************");
			for(int i = 0 ; i < 3; i++) 
			{
				//Storing hotel names inside our array
				TotalCharges[i] =TotalChargesWebElementList.get(i).getText();
				System.out.println(TotalChargesWebElementList.get(i).getText());		
			}
			System.out.println(" ");
			reportPass("Total Charges is displayed and stored in excel");
			return TotalCharges;
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
		}
		return null;
	}
	
	@FindBy(xpath="//h2[contains(@class='_2K0y-IXo', '')]/a")
	public List<WebElement> hreflinks;

	//This function is to get the Hotel Hrefs
	public void clickThreeHolidayHomesLinks() 
	{
		try
		{
			//Creating a String array to store the Href Links of the First 3 Search Results
			String[] Holiday_Homes_Hrefs = new String[3];
			//Store the 3 href links in Hotel_Hrefs
			for(int i = 0; i < 3; i++) 
			{
				Holiday_Homes_Hrefs[i]=hreflinks.get(i).getAttribute("href"); 
			}
			//Iterating Hotel_Hrefs array and clicking the each link
			for(int i = 0 ; i < Holiday_Homes_Hrefs.length; i++) 
			{
				logger.log(Status.INFO,(i+1)+ " Holiday Home should be clicked");
				driver.get(Holiday_Homes_Hrefs[i]);//click each holiday home link  
				//Scroll down for each hotel
				RunScriptWriteExcel.write(18,6,"Pass");    //Results in Pass for Clicking on Holiday Home
				JavascriptExecutor jse=(JavascriptExecutor)driver;
				reportPass((i+1)+ " Holiday Home is clicked");
				jse.executeScript("window.scrollBy(0,200);"); 							//To scroll down to see the price
				takeScreenshot("HolidayHome "+(i+1));									//takeScreenshot of each HolidayHome
				logger.log(Status.INFO,"Page should navigate back to Nairobi Results Page");
				driver.navigate().back();       //navigate back
				reportPass("Page is navigated back to Nairobi Results Page");
				//ScrollDown
				jse.executeScript("window.scrollBy(0,300);");     //To scroll down the cruise page to see the results
				RunScriptWriteExcel.write(19,6,"Pass");		//Pass Navigating Back
			}	 
			logger.log(Status.INFO,"Page should be scrolled up to click Cruises Link Text");
			
			JavascriptExecutor jse=(JavascriptExecutor)driver;
			jse.executeScript("window.scrollBy(0,-600)", " ");    //Scroll Up
			logger.log(Status.PASS, "Page scrolled up to click Cruises Link Text");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(18,6,"Fail");     //Fail Clicking on Holiday Home
			RunScriptWriteExcel.write(19,6,"Fail"); 	//Fail Navigating Back
		}
	}
	
	@FindBy(xpath="//span[@class='_3dv66Lr7']")
	public List<WebElement> headerMenu;
	
	//Click Cruises Link Text
	public CruisesPage clickCruises()
	{
		try
		{
			logger.log(Status.INFO, "Cruises Link Text Should be clicked");
			for(WebElement m:headerMenu)
			{
				if(m.getText().contains("Cruises"))
				{
					m.click();
					break;
				}
			}
			reportPass("Cruises Link Text is clicked");
			RunScriptWriteExcel.write(20,6,"Pass");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
			RunScriptWriteExcel.write(20,6,"Fail");
		}
		CruisesPage cp=new CruisesPage(driver,logger);
		PageFactory.initElements(driver,cp);
		return cp;
	}
	
	//Writes the Output of Hotel Details to the Excel file Hotel_Details.xlsx
	public void WriteHotelDetailsToExcel()
	{
		try
		{
			logger.log(Status.INFO, "Holiday Homes, Charges Per Night, Total Charges should be displayed in console and Stored in Excel file Hotel_Details.xlsx in Excel_Output_Test_Result folder");
			WriteDetailsOfHotelToExcel write=new WriteDetailsOfHotelToExcel();       //WriteDetailsOfHotelToExcel class declaration
			write.WriteToExcel(getHotelNames(),getChargesPerNight(),getHotelTotalCharges());
			reportPass("Holiday Homes, Charges Per Night, Total Charges is displayed in console and Stored in Excel file Hotel_Details.xlsx in Excel_Output_Test_Result folder");
		}
		catch(Exception e)
		{
			reportFail(e.getMessage());
		}
	}
}
