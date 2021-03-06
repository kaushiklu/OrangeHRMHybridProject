package testCases;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import factory.BrowserFactory;
import factory.DataProviderFactory;
import pages.AdminDashboard;
import pages.HomePageAdmin;
import pages.SaveSystemUserPage;
import pages.ViewSystemUsersPage;
import utility.LoginUtil;
import utility.ScreenshotUtility;

public class VerifySearchandDelete {

	WebDriver driver; 
	@BeforeMethod
	public void setup()
	{
		driver = BrowserFactory.getBrowser("Chrome");
		driver.get(DataProviderFactory.getConfig().getApplicationURL());
	}
	@Test
	public void testSignin() throws Exception
	{
		
		LoginUtil loginpage = PageFactory.initElements(driver,LoginUtil.class);
		System.out.println(driver.getTitle());
		Assert.assertEquals("OrangeHRM", driver.getTitle());
		ScreenshotUtility.saveScreenshot(driver, "LoginPage");
		
        loginpage.signIn(DataProviderFactory.getExcel().getData("UserIDpwd", 0, 0),DataProviderFactory.getExcel().getData("UserIDpwd", 0, 1));

        
        HomePageAdmin adminHomePage = PageFactory.initElements(driver, HomePageAdmin.class);
        
        Assert.assertEquals("Welcome Admin",adminHomePage.verifyWelcomeMessageforAdmin());
        Assert.assertEquals("Admin", adminHomePage.verifyAdminTab());
        ScreenshotUtility.saveScreenshot(driver, "AdminHomepage");
        
        AdminDashboard adminDashboard = PageFactory.initElements(loginpage.returnDriver(),AdminDashboard.class);
		adminDashboard.moveToAdminTab();
        
        ViewSystemUsersPage vsup = PageFactory.initElements(driver, ViewSystemUsersPage.class);
	    vsup.clickonAddButton();
	    
	    SaveSystemUserPage ssup = PageFactory.initElements(driver,SaveSystemUserPage.class);
	    String createdUser = ssup.setUserIDandPassword();
	    ssup.clickonSaveButton();
	    
	    Assert.assertEquals(ssup.getURL(), "https://enterprise-demo.orangehrmlive.com/admin/viewSystemUsers");
	    
	    Assert.assertEquals(vsup.waitforSuccessfullyAddedMessage(),"Successfully Saved"+
"\nClose");
	    
	    vsup.searchUser(createdUser);
	    vsup.selectFoundUserCheckbox(createdUser);
	    vsup.deleteUser();
	    
	    Assert.assertEquals(vsup.waitforSuccessfullyDeletedMessage(),"Successfully Deleted"+
	    		"\nClose");
	    
 	
	}
	
	@AfterMethod
	public void tearDown()
	{
		//BrowserFactory.closeBrowser(driver);
	}
}
