package webDriverTestCases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import CommonClassReusables.AGlobalComponents;
import CommonClassReusables.BrowserSelection;
import CommonClassReusables.Utility;
import CommonFunctions.FB_Automation_CommonMethods;
import CommonFunctions.LoginPage;
import CommonFunctions.Self_Service_CommonMethods;


public class Self_Service extends BrowserSelection {

	String testName;
	
	@BeforeMethod
	public void commonPage(java.lang.reflect.Method method) throws Throwable
	{
		unhandledException = false;	
		testName = method.getName();
	//	AGlobalComponents.applicationURL = "http://hscpartner.alertenterprise.com/";
		AGlobalComponents.applicationURL = "http://aepdemo.alertenterprise.com";
		AGlobalComponents.takeScreenshotIfPass = true;
		driver.navigate().refresh();
	}
	
	public static void isConditionX(boolean condition) {
		if(condition==false){
			throw new SkipException("skipp");
		}
	}
			
/*
 * TC001 : AEAP-9 : Physical Access - Case1(Submission flow and validations)
*/
	
@Test(priority=1)
public void Self_Service_Automation_TC001() throws Throwable 
{
	
	logger =report.startTest("Self_Service_Automation_TC001","AEAP-9 : Physical Access - Case1(Submission flow and validations)");
	System.out.println("[INFO]--> Self_Service_Automation_TC001 - TestCase Execution Begins");
	
	String locationName = "Plaza, Financial Center";
	String accessName = "SC RBNSPL NONR STOCK RM";
	
	/** Login as Requester User **/
	boolean loginStatus = LoginPage.loginAEHSC("john.payne", "Alert1234");

	if(loginStatus){
		
		/** Create Access Request **/
		String requestNumber = Self_Service_CommonMethods.createAccessRequest("Physical Access",locationName,accessName,"Las Vegas","300 S 4th St");
		
		/** Launch New Private Browser **/
		Utility.switchToNewBrowserDriver();
		
		/** Login as Approver User **/
		LoginPage.loginAEHSC("carol.payne", "Alert1234");
		
		if(loginStatus){

			/** Approve Access Request **/
			Self_Service_CommonMethods.approveAccessRequest(accessName,requestNumber,"john.payne");
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** Validate Access Request Status **/
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, "Approved");
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		}
		
		/** Logout from Application **/
		LoginPage.logout();
		
	}else{
		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	}
	
}
	
	
/*
 * TC002 : AEAP-10 : Physical Access - Case2(Approval Flow - Role Rejection)
 */

@Test(priority=2)
public void Self_Service_Automation_TC002() throws Throwable 
{
	
	logger =report.startTest("Self_Service_Automation_TC002","AEAP-10 : Physical Access - Case2(Approval Flow - Role Rejection)");
	System.out.println("[INFO]--> Self_Service_Automation_TC001 - TestCase Execution Begins");
	
	String locationName = "Plaza, Financial Center";
	String accessName = "SC LEEXSS NONR GENERAL ACCESS";
	
	/** Login as Requester User **/
	boolean loginStatus = LoginPage.loginAEHSC("john.payne", "Alert1234");

	if(loginStatus){
		
		/** Create Access Request **/
		String requestNumber = Self_Service_CommonMethods.createAccessRequest("Physical Access",locationName,accessName,"Las Vegas","300 S 4th St");
		
		/** Launch New Private Browser **/
		Utility.switchToNewBrowserDriver();
		
		/** Login as Approver User **/
		LoginPage.loginAEHSC("carol.payne", "Alert1234");
		
		if(loginStatus){
			
			/** Reject Access Request **/
			Self_Service_CommonMethods.rejectAccessRequest(accessName,requestNumber,"john.payne");
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** Validate Access Request Status **/
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, "Rejected");
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		}
		
		/** Logout from Application **/
		LoginPage.logout();
		
	}else{
		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	}
	
}



/*
* TC003 : AEAP-11 : Physical Access - Case2(Approval Flow - Multiple Role approver with same owner)
*/

@Test(priority=3)
public void Self_Service_Automation_TC003() throws Throwable 
{

	logger =report.startTest("Self_Service_Automation_TC003","AEAP-11 : Physical Access - Case2(Approval Flow - Multiple Role approver with same owner)");
	System.out.println("[INFO]--> Self_Service_Automation_TC003 - TestCase Execution Begins");
	
	String locationName1 = "Plaza, Financial Center";
	String accessName1 = "SC LEEXSS NONR GENERAL ACCESS";
	
	String locationName2 = "Plaza, Financial Center";
	String accessName2 = "SC RIPPSU RLY GENERAL ACCESS SECURE";
	
	/** Login as Requester User **/
	boolean loginStatus = LoginPage.loginAEHSC("john.payne", "Alert1234");
	
	if(loginStatus){
		
		/** Create Multiple Access Request **/
		String requestNumber = Self_Service_CommonMethods.createAccessRequestMultiple("Physical Access",locationName1,accessName1,locationName2,accessName2);
		
		/** Launch New Private Browser **/
		Utility.switchToNewBrowserDriver();
		
		/** Login as Approver User **/
		LoginPage.loginAEHSC("carol.payne", "Alert1234");
		
		if(loginStatus){
			
			/** Approve Access Request **/
			Self_Service_CommonMethods.approveAccessRequestMultpile(accessName1,accessName2,requestNumber,"john.payne");
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** Validate Access Request Status **/
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, "Approved");
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		}
		
		/** Logout from Application **/
		LoginPage.logout();
		
	}else{
		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	}

}



/*
* TC004 : AEAP-12 : Physical Access - Case4(Approval Flow - Approve 1 Role and Reject 1 Role with same owner)
*/

@Test(priority=4)
public void Self_Service_Automation_TC004() throws Throwable 
{

	logger =report.startTest("Self_Service_Automation_TC004","AEAP-12 : Physical Access - Case4(Approval Flow - Approve 1 Role and Reject 1 Role with same owner)");
	System.out.println("[INFO]--> Self_Service_Automation_TC004 - TestCase Execution Begins");
	
	String locationName1 = "Plaza, Financial Center";
	String accessName1 = "SC LEEXSS NONR GENERAL ACCESS";
	
	String locationName2 = "Plaza, Financial Center";
	String accessName2 = "SC RIPPSU RLY GENERAL ACCESS SECURE";
	
	/** Login as Requester User **/
	boolean loginStatus = LoginPage.loginAEHSC("john.payne", "Alert1234");
	
	if(loginStatus){
		
		/** Create Multiple Access Request **/
		String requestNumber = Self_Service_CommonMethods.createAccessRequestMultiple("Physical Access",locationName1,accessName1,locationName2,accessName2);
		
		/** Launch New Private Browser **/
		Utility.switchToNewBrowserDriver();
		
		/** Login as Approver User **/
		LoginPage.loginAEHSC("carol.payne", "Alert1234");
		
		if(loginStatus){
			
			/** Approve Access Request **/
			Self_Service_CommonMethods.approveAndRejectAccessRequestMultpile(accessName1,accessName2,requestNumber,"john.payne");
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** Validate Access Request Status **/
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, "Approved");
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		}
		
		/** Logout from Application **/
		LoginPage.logout();
		
	}else{
		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	}

}



/*
* TC005 : AEAP-13 : Physical Access - Case5(Approval Flow - Approve/Reject Role with different owner)
*/

@Test(priority=5)
public void Self_Service_Automation_TC005() throws Throwable 
{

	logger =report.startTest("Self_Service_Automation_TC005","AEAP-13 : Physical Access - Case5(Approval Flow - Approve/Reject Role with different owner)");
	System.out.println("[INFO]--> Self_Service_Automation_TC005 - TestCase Execution Begins");
	
	String locationNames = "Plaza, Financial Center;Albany, Financial Center";
	String accessNames = "SC LEEXSS NONR GENERAL ACCESS;SC RIPPSU RLY GENERAL ACCESS SECURE;FL NRTHPN NONR CONF RM 4C6;NC CABRGR NONR LEGAL CONF RM ACCESS";
	
	/** Login as Requester User **/
	boolean loginStatus = LoginPage.loginAEHSC("john.payne", "Alert1234");
	
	if(loginStatus){
		
		/** Create Multiple Access Request **/
		String requestNumber = Self_Service_CommonMethods.createLocationAccessRequestMultiple("Physical Access",locationNames,accessNames);
		
		/** Launch New Private Browser **/
		Utility.switchToNewBrowserDriver();
		
		/** Login as Approver User **/
		LoginPage.loginAEHSC("carol.payne", "Alert1234");
		
		if(loginStatus){
			
			/** Approve Access Request **/
			Self_Service_CommonMethods.approveAccessRequestMultpile("SC LEEXSS NONR GENERAL ACCESS","SC RIPPSU RLY GENERAL ACCESS SECURE",requestNumber,"john.payne");
			
			/** Logout from Application **/
			LoginPage.logout();
			
			/** Login as Approver User **/
			LoginPage.loginAEHSC("john.payne", "Alert1234");
			
			/** Approve 1 and Reject 1 Access Request **/
			Self_Service_CommonMethods.approveAndRejectAccessRequestMultpile("FL NRTHPN NONR CONF RM 4C6","NC CABRGR NONR LEGAL CONF RM ACCESS",requestNumber,"john.payne");
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** Validate Access Request Status **/
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, "Approved");
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		}
		
		/** Logout from Application **/
		LoginPage.logout();
		
	}else{
		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	}

}

/*
* TC006 : AEAP-14 : Wellness check : Auto approval  )
*/

@Test(priority=6)
public void Self_Service_Automation_TC006() throws Throwable 
{

	logger =report.startTest("Self_Service_Automation_TC006","AEAP-14 : Wellness Check - Auto approval)");
	System.out.println("[INFO]--> Self_Service_Automation_TC006 - TestCase Execution Begins");
	
	String firstName = "Wellcheck";
	String lastName = "Bailey";
		
	AGlobalComponents.wellnessCheckCase=true;
	
	/** Login as admin User **/
	boolean loginStatus = LoginPage.loginAEHSC("admin", "Alert1234");
	
	if(loginStatus){
		
		for(int i=0;i<2;i++){
		
			/** check asset status in IDM before wellness check **/
			Self_Service_CommonMethods.checkStatus(firstName,lastName);
	
			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();
		
			/** Login as requester **/
			LoginPage.loginAEHSC("wellcheck.bailey", "Alert1234");
			
			if(loginStatus){
			
				/** submit wellness check request to activate the user**/
				Self_Service_CommonMethods.createWellnessCheckRequest();
			
				/** checkAssetStatusInMyRequestInbox**/
				Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName);
			
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
			}
			else{
				logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
			}
		
			/** Validate asset status in IDM after wellness check request approved**/
			Self_Service_CommonMethods.checkStatus(firstName,lastName);
			
		}
		
		/** Logout from Application **/
		LoginPage.logout();
		
	}else{
		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	}

}

/*
 * TC007 : 5.0 Use cases . Manager Login Scenarios : Modify Identity to update photo
 */

@Test(priority=7)
public void Self_Service_Automation_TC007() throws Throwable 
{
	
	logger =report.startTest("Self_Service_Automation_TC007","Manager Login Scenarios ");
	System.out.println("[INFO]--> Self_Service_Automation_TC007 - TestCase Execution Begins");
	AGlobalComponents.ManagerLogin = true;
	AGlobalComponents.updatePhoto = true;
	
	String firstName ="Testmlogin";
	String lastName ="Scenario";
		
	/** Login as admin User **/
	boolean loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	if(loginStatus){
		
		/** check asset and access and photo screenshot   in IDM **/
 		Self_Service_CommonMethods.checkStatus(firstName,lastName);
	
 		/** Launch New Private Browser **/
 		Utility.switchToNewBrowserDriver();
		
 		/* Login as Manager */
 		loginStatus = LoginPage.loginAEHSC("carol.payne", "Alert1234");

 		if(loginStatus){
 			logger.log(LogStatus.PASS, "Login Successful");
				
 			/** Modify Identity **/
 	     	Self_Service_CommonMethods.modifyIdentity(firstName);
		
 			/** checkStatusInMyRequestInbox**/
 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName);
	
 			/** Switch to Default Browser **/
 			Utility.switchToDefaultBrowserDriver();
 		}
 		else{
 			logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
 		}

 		/** Validate  status in IDM after  request approved**/
 		Self_Service_CommonMethods.checkStatus(firstName,lastName);
 		
 		/** Reverting the updations **/
 		Self_Service_CommonMethods.revertIdentityChanges(firstName,lastName);
	
 		/** Logout from Application **/
 		LoginPage.logout();
	
 	}
 	else {
 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
 	}	
}

/*
 * TC008 : 5.0 Use cases . Manager Login Scenarios :Employment Type Conversion
 */

@Test(priority=8)
public void Self_Service_Automation_TC008() throws Throwable 
{
	
	logger =report.startTest("Self_Service_Automation_TC008","Manager Login Scenarios ");
	System.out.println("[INFO]--> Self_Service_Automation_TC008 - TestCase Execution Begins");
	AGlobalComponents.ManagerLogin = true;
	AGlobalComponents.contractorToPermanentEmployeeConversion = true;
	
	String firstName ="Test"+Utility.getRandomString(4);
	String lastName ="EmpConversion";
		
	/** Login as admin User **/
	boolean loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	if(loginStatus){
		
	
		/**create identity **/
		FB_Automation_CommonMethods.createIdentity(firstName,lastName);
		
		/** check accesses assigned to Contractor in IDM **/
 		Self_Service_CommonMethods.checkStatus(firstName,lastName);
	
 		/** Launch New Private Browser **/
 		Utility.switchToNewBrowserDriver();
		
 		/* Login as Manager */
 		loginStatus = LoginPage.loginAEHSC("carol.payne", "Alert1234");

 		if(loginStatus){
 			logger.log(LogStatus.PASS, "Login Successful");
				
 			/** Employee Type Conversion**/
			Self_Service_CommonMethods.employeeConversion(firstName);
		
 			/** checkStatusInMyRequestInbox**/
 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName);
 			
 			/* Logout from application */
 	 		LoginPage.logout();
 		
 			/* Login as Badge Admin */
 	 		loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");

 	 		if(loginStatus){
 	 			logger.log(LogStatus.PASS, "Login Successful");
 	 			
 	 			/** approve request By badge admin **/
 	 			Self_Service_CommonMethods.approveRequest("badgeAdmin");
 	 		}
 			/** Switch to Default Browser **/
 			Utility.switchToDefaultBrowserDriver();
 		}
 		else{
 			logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
 		}

 		/** Validate  status in IDM after  request approved**/
 		Self_Service_CommonMethods.checkStatus(firstName,lastName);
 		
 		/** Logout from Application **/
 		LoginPage.logout();
	
 	}
 	else {
 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
 	}	
}


/*
 * TC010 : 5.0 Use cases . Manager Login Scenarios :Temp Worker Modification . Modifying last name of the temp worker.
 */

@Test(priority=10)
public void Self_Service_Automation_TC010() throws Throwable 
{
	
	logger =report.startTest("Self_Service_Automation_TC010","Manager Login Scenarios ");
	System.out.println("[INFO]--> Self_Service_Automation_TC010 - TestCase Execution Begins");
	AGlobalComponents.ManagerLogin = true;
	AGlobalComponents.tempWorkerModification=true;
	String firstName ="Temp" + Utility.getRandomString(4),lastName ="Onboard" , modifiedLastName = "Modify";
		
			
	/** Login as admin **/
	boolean loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	if(loginStatus){
		
	
		/** Launch New Private Browser **/
 		Utility.switchToNewBrowserDriver();
		
 		/* Login as Manager */
 		loginStatus = LoginPage.loginAEHSC("anna.mordeno", "Alert1234");

 		if(loginStatus){
 			logger.log(LogStatus.PASS, "Login Successful");
		
			/** temp worker onboarding**/
 			Self_Service_CommonMethods.temporaryWorkerOnboarding(firstName,lastName);
		
 			/** approve request  by manager**/
 			Self_Service_CommonMethods.approveRequest("manager");
 			
 			/* Logout from application */
 	 		LoginPage.logout();
 		
 			/* Login as Badge Admin */
 	 		loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");

 	 		if(loginStatus){
 	 			logger.log(LogStatus.PASS, "Login Successful");
 	 			
 	 			/** approve request By badge admin **/
 	 			Self_Service_CommonMethods.approveRequest("badgeAdmin");
 	 			
 	 		}
 		}
	
 		/** Switch to Default Browser **/
 		Utility.switchToDefaultBrowserDriver();
 		

 		/** checking current last name of temp worker in IDM **/
 		Self_Service_CommonMethods.checkStatus(firstName,lastName);
 		
 		/** Launch New Private Browser **/
 		Utility.switchToNewBrowserDriver();
 		
 		/* Login as Manager */
 		loginStatus = LoginPage.loginAEHSC("anna.mordeno", "Alert1234");

 		if(loginStatus){
 			logger.log(LogStatus.PASS, "Login Successful");
 		
 			/** Modifying the last name of the temp worker**/
 			Self_Service_CommonMethods.tempWorkerModification(firstName,lastName,modifiedLastName);
 			
 			/** checkStatusInMyRequestInbox**/
 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,modifiedLastName);
 			
 		}
 		/** Switch to Default Browser **/
 		Utility.switchToDefaultBrowserDriver();
 		

 		/** checking modified last name of temp worker in IDM **/
 		Self_Service_CommonMethods.checkStatus(firstName,modifiedLastName);
 		
 		
 		/** Logout from Application **/
 		LoginPage.logout();
	
 	}

 	else {
 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
 	}	
}

/*
 * TC009 : 5.0 Use cases . Manager Login Scenarios :Temp Worker Onboarding
 */

@Test(priority=9)
public void Self_Service_Automation_TC009() throws Throwable 
{
	
	logger =report.startTest("Self_Service_Automation_TC009","Manager Login Scenarios ");
	System.out.println("[INFO]--> Self_Service_Automation_TC009 - TestCase Execution Begins");
	AGlobalComponents.ManagerLogin = true;
	AGlobalComponents.tempWorkerOnboarding=true;
	String firstName ="Temp" + Utility.getRandomString(4),lastName ="Onboard";
		
			
	/** Login as admin **/
	boolean loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	if(loginStatus){
		
		/**creating asset for the user**/
		AGlobalComponents.badgeName = Self_Service_CommonMethods.createNewAsset("Permanent Badge", "SRSeries_10And12Digit", "AMAG");
		
			
		/** Launch New Private Browser **/
 		Utility.switchToNewBrowserDriver();
		
 		/* Login as Manager */
 		loginStatus = LoginPage.loginAEHSC("anna.mordeno", "Alert1234");

 		if(loginStatus){
 			logger.log(LogStatus.PASS, "Login Successful");
		
			/** temp worker onboarding**/
 			Self_Service_CommonMethods.temporaryWorkerOnboarding(firstName,lastName);
		
 			/** checkStatusInMyRequestInbox**/
 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName);
 		
 			/** approve request  by manager**/
 			Self_Service_CommonMethods.approveRequest("manager");
 			
 			/* Logout from application */
 	 		LoginPage.logout();
 		
 			/* Login as Badge Admin */
 	 		loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");

 	 		if(loginStatus){
 	 			logger.log(LogStatus.PASS, "Login Successful");
 	 			
 	 			/** approve request By badge admin **/
 	 			Self_Service_CommonMethods.approveRequest("badgeAdmin");
 	 			
 	 		}
 		}
	
 		/** Switch to Default Browser **/
 		Utility.switchToDefaultBrowserDriver();
 		

 		/** Validate  created temp worker in IDM after  request approved**/
 		Self_Service_CommonMethods.checkStatus(firstName,lastName);
 		
 		/** Validate  created temp worker in database**/
 //		Self_Service_CommonMethods.checkStatusInDB(firstName,lastName);
 		
 		
 		/** Logout from Application **/
 		LoginPage.logout();
	
 	}

 	else {
 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
 	}	
}

}