package webDriverTestCases;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import CommonClassReusables.MsSql;

import com.relevantcodes.extentreports.LogStatus;

import CommonClassReusables.AGlobalComponents;
import CommonClassReusables.BrowserSelection;
import CommonClassReusables.Utility;
import CommonFunctions.FB_Automation_CommonMethods;
import CommonFunctions.LoginPage;
import CommonFunctions.Self_Service_CommonMethods;


public class Golden_Suite  extends BrowserSelection {

	String testName;
	
	@BeforeMethod
	public void commonPage(java.lang.reflect.Method method) throws Throwable
	{
		unhandledException = false;	
		testName = method.getName();
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
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC001");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		
		/** Login as Requester User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));
	
		if(loginStatus){
			
			/** Create Access Request **/
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createAccessRequest((String) testData.get("request_type"),(String) testData.get("location_name_1"),(String) testData.get("access_name_1"),"Las Vegas","300 S 4th St");
				Utility.updateDataInDatasource("Self_Service_Automation_TC001", "request_number", requestNumber);
			}
			
			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();
			
			/** Login as Approver User **/
			LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
			
			if(loginStatus){
	
				/** Approve Access Request **/
				Self_Service_CommonMethods.approveAccessRequest((String) testData.get("access_name_1"),requestNumber,(String) testData.get("workflow_stage"));
				
				/** Logout from Application **/
				LoginPage.logout();
				
				/** Login as Admin User **/
				LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
				
				/** Validate Provisioning Monitor Status **/
				Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,(String) testData.get("provisioning_code"),(String) testData.get("user_id"));
				
				/** Validate IDM User Status **/
				Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),(String) testData.get("idm_validation_status"));
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
				
				/** Validate Access Request Status **/
				Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"));
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
	 * TC007 : AEAP-17 : Self Service - New Badge
	 */

	@Test(priority=7)
	public void Self_Service_Automation_TC007() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC007","AEAP-17 : Self Service - New Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC007 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC007");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		String badgeName = (String) testData.get("badge_name");
		
		/** Login as Requester User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/** Create Access Request **/
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createNewBadgeRequest((String) testData.get("request_type"),(String) testData.get("request_for"),(String) testData.get("first_name"),(String) testData.get("card_status"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC007", "request_number", requestNumber);
			}
			
			/** Logout from Application **/
			LoginPage.logout();
			
			/** Login as Admin User **/
			LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
			
			/** Create New Badge **/
			if(badgeName==null||badgeName.equals(""))
			{
				badgeName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system")); //CCURE 9000,AMAG,etc.
				Utility.updateDataInDatasource("Self_Service_Automation_TC007", "badge_name", badgeName);
			}
			
			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();
			
			/** Login as Stage 1 Approver User **/
			LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
			
			if(loginStatus){
				
				/** Approve Badge Request Stage 1 **/
				Self_Service_CommonMethods.approveBadgeRequest(1,requestNumber,(String) testData.get("card_status"),badgeName,(String) testData.get("full_name"));
				
				/** Logout from Application **/
				LoginPage.logout();
				
				/** Login as Stage 2 Approver User **/
				LoginPage.loginAEHSC((String) testData.get("badge_admin_username"), (String) testData.get("badge_admin_password"));
				
				/** Approve Badge Request Stage 2 **/
				Self_Service_CommonMethods.approveBadgeRequest(2,requestNumber,(String) testData.get("card_status"),badgeName,(String) testData.get("full_name"));
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
				
				/** Validate Self Approval Request Status **/
				Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("card_status"), (String) testData.get("full_name"));
				
				/** Validate Provisioning Monitor Status **/
				AGlobalComponents.systemName=(String) testData.get("system_name");
				Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,(String) testData.get("provisioning_code"),(String) testData.get("user_id"));
				AGlobalComponents.systemName="";
				
				/** Validate IDM User Status **/
				Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),(String) testData.get("card_status"));
				
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
	 * TC008 : AEAP-18 : Self Service - ACTIVATE Badge
	 */

	@Test(priority=8)
	public void Self_Service_Automation_TC008() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC008","AEAP-18 : Self Service - ACTIVATE Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC008 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC008");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		
		/* Login as Requester User */
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/* Create Activate Badge Request */
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createActivateDeactivateBadgeRequest((String) testData.get("request_type"),(String) testData.get("request_for"),(String) testData.get("first_name"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC008", "request_number", requestNumber);
			}
			
			/* Validate Self Approval Request Status */
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"), (String) testData.get("full_name"));
			
			/* Logout from Application */
			LoginPage.logout();
			
			/* Login as Admin User */
			LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
			
			/* Validate Provisioning Monitor Status */
			AGlobalComponents.systemName=(String) testData.get("system_name");
			Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,(String) testData.get("provisioning_code"),(String) testData.get("user_id"));
			AGlobalComponents.systemName="";
			
			/* Validate IDM User Status */
			Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),(String) testData.get("idm_validation_status"));		
			
			/* Logout from Application */
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}


	/*
	 * TC009 : AEAP-19 : Self Service - DEACTIVATE Badge
	*/

	@Test(priority=9)
	public void Self_Service_Automation_TC009() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC009","AEAP-19 : Self Service - DEACTIVATE Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC009 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC009");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		
		/* Login as Requester User */
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/* Create Deactivate Badge Request */
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createActivateDeactivateBadgeRequest((String) testData.get("request_type"),(String) testData.get("request_for"),(String) testData.get("first_name"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC009", "request_number", requestNumber);
			}
			
			/* Validate Self Approval Request Status */
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"), (String) testData.get("full_name"));
			
			/* Logout from Application */
			LoginPage.logout();
			
			/* Login as Admin User */
			LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
			
			/* Validate Provisioning Monitor Status */
			AGlobalComponents.systemName=(String) testData.get("system_name");
			Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,(String) testData.get("provisioning_code"),(String) testData.get("user_id"));
			AGlobalComponents.systemName="";
			
			/* Validate IDM User Status */
			Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),(String) testData.get("idm_validation_status"));
			
			/* Logout from Application */
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}
	/*
	 * TC010 : Self Service - IT Access
	*/
		
	@Test(priority=10)
	public void Self_Service_Automation_TC010() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC010","Self Service - IT Access");
		System.out.println("[INFO]--> Self_Service_Automation_TC010 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC010");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		
		/** Login as Requester User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/** Create Access Request **/
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createAccessRequestOthers((String) testData.get("request_type"),(String) testData.get("system_name"),(String) testData.get("access_name_1"),(String) testData.get("first_name"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC010", "request_number", requestNumber);
			}		
			
			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();
			
			/** Login as Approver User **/
			LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
			
			if(loginStatus){

				/** Approve Access Request **/
				Self_Service_CommonMethods.approveAccessRequest((String) testData.get("access_name_1"),requestNumber,(String) testData.get("workflow_stage"));
				
				/** Logout from Application **/
				LoginPage.logout();
				
				/** Login as Approver User **/
				LoginPage.loginAEHSC((String) testData.get("area_admin_username"), (String) testData.get("area_admin_password"));
				
				/** Approve Access Request **/
				Self_Service_CommonMethods.approveAccessRequest((String) testData.get("access_name_1"),requestNumber,(String) testData.get("request_status"));
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
				
				/** Validate Access Request Status **/
				Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"));
				
				/** Logout from Application **/
				LoginPage.logout();
				
				/** Login as Admin User **/
				LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
				
				/** Validate IDM User Status **/
				Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("access_name_1"),(String) testData.get("idm_validation_status"));
				
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
	 * TC011 : Self Service - Application Access
	*/
		
	@Test(priority=11)
	public void Self_Service_Automation_TC011() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC011","Self Service - Application Access");
		System.out.println("[INFO]--> Self_Service_Automation_TC011 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC011");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		
		/** Login as Requester User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/** Create Access Request **/
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createAccessRequestOthers((String) testData.get("request_type"),(String) testData.get("system_name"),(String) testData.get("access_name_1"),(String) testData.get("first_name"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC011", "request_number", requestNumber);
			}	
			
			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();
			
			/** Login as Approver User **/
			LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
			
			if(loginStatus){

				/** Approve Access Request **/
				Self_Service_CommonMethods.approveAccessRequest((String) testData.get("access_name_1"),requestNumber,(String) testData.get("workflow_stage"));
				
				/** Logout from Application **/
				LoginPage.logout();
				
				/** Login as Approver User **/
				LoginPage.loginAEHSC((String) testData.get("area_admin_username"), (String) testData.get("area_admin_password"));
				
				/** Approve Access Request **/
				Self_Service_CommonMethods.approveAccessRequest((String) testData.get("access_name_1"),requestNumber,(String) testData.get("request_status"));
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
				
				/** Validate Access Request Status **/
				Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"));
				
				/** Logout from Application **/
				LoginPage.logout();
				
				/** Login as Admin User **/
				LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
				
				/** Validate IDM User Status **/
				Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("access_name_1"),(String) testData.get("idm_validation_status"));
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
	 * TC012 : Self Service - Report Lost or Stolen Badge
	*/

	@Test(priority=12)
	public void Self_Service_Automation_TC012() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC012","Self Service - Report Lost or Stolen Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC012 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC012");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		
		/** Login as Requester User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/** Create Deactivate Badge Request **/
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createLostOrStolenBadgeRequest((String) testData.get("request_type"),(String) testData.get("request_for"),(String) testData.get("first_name"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC012", "request_number", requestNumber);
			}	
			
			/** Validate Self Approval Request Status **/
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"), (String) testData.get("full_name"));
			
			/** Validate Provisioning Monitor Status **/
			Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,(String) testData.get("provisioning_code"),(String) testData.get("user_id"));
			
			/** Validate IDM User Status **/
			Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),(String) testData.get("idm_validation_status"));	
			
			/** Logout from Application **/
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}


	/*
	 * TC013 : Self Service - Request Replacement Badge DAMAGED
	*/

	@Test(priority=13)
	public void Self_Service_Automation_TC013() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC013","Self Service - Request Replacement Badge DAMAGED");
		System.out.println("[INFO]--> Self_Service_Automation_TC013 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC013");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
		String badgeName = (String) testData.get("badge_name");
		
		/* Login as Requester User */
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("end_user_username"), (String) testData.get("end_user_password"));

		if(loginStatus){
			
			/* Create Replace Badge Request */
			if(requestNumber==null||requestNumber.equals(""))
			{
				requestNumber = Self_Service_CommonMethods.createReplaceBadgeRequest((String) testData.get("request_type"),(String) testData.get("request_for"),(String) testData.get("first_name"),(String) testData.get("idm_validation_status"));
				Utility.updateDataInDatasource("Self_Service_Automation_TC013", "request_number", requestNumber);
			}	
			
			/* Logout from Application */
			LoginPage.logout();
			
			/* Login as Admin User */
			LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
			
			/* Create New Badge */
			if(badgeName==null||badgeName.equals(""))
			{
				badgeName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system")); //CCURE 9000,AMAG,etc.
				Utility.updateDataInDatasource("Self_Service_Automation_TC013", "badge_name", badgeName);
			}
			
			/* Launch New Private Browser */
			Utility.switchToNewBrowserDriver();
			
			/* Login as Manager User */
			LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
			
			/* Approve Replace Badge Request Stage 1 */
			Self_Service_CommonMethods.approveReplaceBadgeRequest(1,requestNumber,(String) testData.get("idm_validation_status"),badgeName,(String) testData.get("full_name"));
			
			/* Logout from Application */
			LoginPage.logout();
			
			/* Login as Stage 2 Approver User */
			LoginPage.loginAEHSC((String) testData.get("badge_admin_username"), (String) testData.get("badge_admin_password"));
			
			/* Approve Replace Badge Request Stage 2 */
			Self_Service_CommonMethods.approveReplaceBadgeRequest(2,requestNumber,(String) testData.get("idm_validation_status"),badgeName,(String) testData.get("full_name"));
			
			/* Switch to Default Browser */
			Utility.switchToDefaultBrowserDriver();
			
			/* Validate Self Approval Request Status */
			Self_Service_CommonMethods.validateAccessRequestStatus(requestNumber, (String) testData.get("request_status"), (String) testData.get("full_name"));
			
			/* Validate Provisioning Monitor Status */
			AGlobalComponents.systemName=(String) testData.get("system_name");
			Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,(String) testData.get("provisioning_code"),(String) testData.get("user_id"));
			AGlobalComponents.systemName="";
			Self_Service_CommonMethods.provisioningMonitorValidation(requestNumber,"CREATE_USER_SUCCESS",(String) testData.get("user_id"));
			
			/* Validate IDM User Status */
			Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),(String) testData.get("idm_validation_status"));
			Self_Service_CommonMethods.idmUserValidation((String) testData.get("user_id"),(String) testData.get("idm_validation_tab"),(String) testData.get("idm_validation_key"),"ASSIGNED");
			
			/* Logout from Application */
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}


	/*
	 * TC014 : Self Service - Return Temporary Badge
	*/

	@Test(priority=14)
	public void Self_Service_Automation_TC014() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC014","Self Service - Return Temporary Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC014 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC014");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String badgeName = (String) testData.get("badge_name");
		
		/** Login as Requester User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

		if(loginStatus){
			
			/** Create New Badge **/
			if(badgeName==null||badgeName.equals(""))
			{
				badgeName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system")); //CCURE 9000,AMAG,etc.
				Utility.updateDataInDatasource("Self_Service_Automation_TC014", "badge_name", badgeName);
			}	
			
			/** Assign New Badge **/
			Self_Service_CommonMethods.assignBadgeIDM((String) testData.get("user_id"), badgeName, (String) testData.get("badge_type"));
			
			/** Create Return Badge Request **/
			Self_Service_CommonMethods.returnTempBadge((String) testData.get("user_id"));
			
			/** Logout from Application **/
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}

	/*
	 * TC017 : 5.0 Use cases . Manager Login Scenarios :Employment Type Conversion
	 */

	@Test(priority=17)
	public void Self_Service_Automation_TC017() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC017","Manager Login Scenarios : Employment Type Conversion ");
		System.out.println("[INFO]--> Self_Service_Automation_TC017 - TestCase Execution Begins");
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC017");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String firstName ="Test"+Utility.getRandomString(4);
		String lastName =Utility.getRandomString(4);
		String requestNumber = "";
		String scriptName = (String) testData.get("script_name");
		AGlobalComponents.userId=firstName+"."+lastName;
		AGlobalComponents.contractorToPermanentEmployeeConversion=true;
		String accessToBeAdded = (String) testData.get("access_name_1");
			
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));	
		if(loginStatus){
			
			/**create new asset **/
			AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
			
			/**create identity **/
			FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "first_name", firstName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "last_name", lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "full_name", firstName+" "+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "user_id", firstName+"."+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "asset_code", AGlobalComponents.assetCode);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "first_name", firstName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "last_name", lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "full_name", firstName+" "+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "user_id", firstName+"."+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "asset_code", AGlobalComponents.assetCode);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "first_name", firstName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "last_name", lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "full_name", firstName+" "+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "user_id", firstName+"."+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "asset_code", AGlobalComponents.assetCode);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "first_name", firstName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "last_name", lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "full_name", firstName+" "+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "user_id", firstName+"."+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "asset_code", AGlobalComponents.assetCode);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", firstName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", firstName+" "+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", firstName+"."+lastName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", AGlobalComponents.assetCode);
					
			/** check accesses assigned to Contractor in IDM **/
	 		Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,"",accessToBeAdded,scriptName);
		
	 		/** Launch New Private Browser **/
	 		Utility.switchToNewBrowserDriver();
			
	 		/* Login as Manager */
	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 		if(loginStatus){
	 			logger.log(LogStatus.PASS, "Login Successful");
					
	 			/** Employee Type Conversion**/
	 			requestNumber = Self_Service_CommonMethods.employeeConversion(firstName);
	 				
	 			/** checkStatusInMyRequestInbox**/
	 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,"",accessToBeAdded,requestNumber,scriptName);
	 			
	 			/** Switch to Default Browser **/
	 			Utility.switchToDefaultBrowserDriver();
	 		}
	 		else{
	 			logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
	 		}

	 		/** Validate  status in IDM after  request approved**/
	 		Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",accessToBeAdded,scriptName);
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		
	 	}
	 	else {
	 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	 	}	
	}




	/*
	 * TC018 : 5.0 Use cases . Manager Login Scenarios : Modify Identity : photo
	 */

	@Test(priority=18)
	public void Self_Service_Automation_TC018_1() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC018_1","Manager Login Scenarios  : Modify Identity");
		System.out.println("[INFO]--> Self_Service_Automation_TC018_1 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC018_1");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
			
		String firstName =(String) testData.get("first_name");
		String lastName =(String) testData.get("last_name");
		String scriptName =(String) testData.get("script_name");
		AGlobalComponents.userId = (String) testData.get("user_id");
		String parameterToBeModified=(String) testData.get("parameter_tobemodified");
		
				
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));	
		if(loginStatus){
			
			if((firstName==null||firstName.equals(""))&&(lastName==null||lastName.equals(""))){
				firstName ="Test"+Utility.getRandomString(4);
				lastName =Utility.getRandomString(4);
				AGlobalComponents.userId=firstName+"."+lastName;
				
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
					
				/**create identity **/
			
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
				
				Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "first_name", firstName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "last_name", lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "full_name", firstName+" "+lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "user_id", firstName+"."+lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "asset_code", AGlobalComponents.assetCode);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "first_name", firstName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "last_name", lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "full_name", firstName+" "+lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "user_id", firstName+"."+lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "asset_code", AGlobalComponents.assetCode);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "asset_code", AGlobalComponents.assetCode);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", AGlobalComponents.assetCode);
			}
			
			/** checking the user details in IDM  before modification**/
	 		Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,parameterToBeModified,"",scriptName);
		
	 		/** Launch New Private Browser **/
	 		Utility.switchToNewBrowserDriver();
			
	 		/* Login as Manager */
	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 		if(loginStatus){
	 			logger.log(LogStatus.PASS, "Login Successful");
					
	 			/** Modify Identity **/
	 			requestNumber =Self_Service_CommonMethods.modifyIdentity(firstName,parameterToBeModified,(String) testData.get("request_type"));
	 				 			
	 			/** checkStatusInMyRequestInbox**/
	 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,parameterToBeModified,"",requestNumber,scriptName);
		
	 			/** Switch to Default Browser **/
	 			Utility.switchToDefaultBrowserDriver();
	 		}
	 		else{
	 			logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
	 		}

	 		/** Validate  status in IDM after  request approved**/
	 		Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",parameterToBeModified,scriptName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "asset_code", "");
	 		
	 		if(Utility.compareStringValues(parameterToBeModified, "photo")){
	 		
	 			/** checking the user details in IDM  before modification**/
	 			Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,parameterToBeModified,"",scriptName);
	 		
	 			/** Launch New Private Browser **/
	 			Utility.switchToNewBrowserDriver();
	 		
	 			/* Login as Manager */
	 			loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 			if(loginStatus){
	 				logger.log(LogStatus.PASS, "Login Successful");
					
	 				/** Modify Identity **/
	 				requestNumber =Self_Service_CommonMethods.modifyIdentity(firstName,parameterToBeModified,(String) testData.get("request_type"));
			
	 				/** checkStatusInMyRequestInbox**/
	 				Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,parameterToBeModified,"",requestNumber,scriptName);
		
	 				/** Switch to Default Browser **/
	 				Utility.switchToDefaultBrowserDriver();
	 			}
	 			else{
	 				logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
	 			}

	 			/** Validate  status in IDM after  request approved**/
	 			Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,parameterToBeModified,"",scriptName);
	 			Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "first_name", "");
				Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "last_name", "");
				Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "full_name", "");
				Utility.updateDataInDatasource("Self_Service_Automation_TC018_1", "user_id", "");
	 		}
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		
	 	}
	 	else {
	 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	 	}	
	}

	/*
	 * TC018 : 5.0 Use cases . Manager Login Scenarios : Modify Identity :lastName
	 */

	@Test(priority=18)
	public void Self_Service_Automation_TC018_2() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC018_2","Manager Login Scenarios  : Modify Identity");
		System.out.println("[INFO]--> Self_Service_Automation_TC018_2 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC018_2");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
			
		String firstName =(String) testData.get("first_name");
		String lastName =(String) testData.get("last_name");
		String scriptName =(String) testData.get("script_name");
		AGlobalComponents.userId = (String) testData.get("user_id");
		String parameterToBeModified=(String) testData.get("parameter_tobemodified");
		
				
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));	
		if(loginStatus){
			
			if((firstName==null||firstName.equals(""))&&(lastName==null||lastName.equals(""))){
				firstName ="Test"+Utility.getRandomString(4);
				lastName =Utility.getRandomString(4);
				AGlobalComponents.userId=firstName+"."+lastName;
				
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
					
				/**create identity **/
			
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "first_name", firstName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "last_name", lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "full_name", firstName+" "+lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "user_id", firstName+"."+lastName);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "asset_code", AGlobalComponents.assetCode);
		 		Utility.updateDataInDatasource("Self_Service_Automation_TC023", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "asset_code", AGlobalComponents.assetCode);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", AGlobalComponents.assetCode);
			}
			
			/** checking the user details in IDM  before modification**/
	 		Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,parameterToBeModified,"",scriptName);
		
	 		/** Launch New Private Browser **/
	 		Utility.switchToNewBrowserDriver();
			
	 		/* Login as Manager */
	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 		if(loginStatus){
	 			logger.log(LogStatus.PASS, "Login Successful");
					
	 			/** Modify Identity **/
	 			requestNumber =Self_Service_CommonMethods.modifyIdentity(firstName,parameterToBeModified,(String) testData.get("request_type"));
	 				 			
	 			/** checkStatusInMyRequestInbox**/
	 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,parameterToBeModified,"",requestNumber,scriptName);
		
	 			/** Switch to Default Browser **/
	 			Utility.switchToDefaultBrowserDriver();
	 		}
	 		else{
	 			logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
	 		}

	 		/** Validate  status in IDM after  request approved**/
	 		Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",parameterToBeModified,scriptName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_2", "asset_code", "");
	 		
	 		
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		
	 	}
	 	else {
	 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	 	}	
	}

	/*
	 * TC018 : 5.0 Use cases . Manager Login Scenarios : Modify Identity  : phoneNo
	 */

	@Test(priority=18)
	public void Self_Service_Automation_TC018_3() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC018_3","Manager Login Scenarios  : Modify Identity , modifying phone number");
		System.out.println("[INFO]--> Self_Service_Automation_TC018_3 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC018_3");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String requestNumber = (String) testData.get("request_number");
			
		String firstName =(String) testData.get("first_name");
		String lastName =(String) testData.get("last_name");
		String scriptName =(String) testData.get("script_name");
		AGlobalComponents.userId = (String) testData.get("user_id");
		String parameterToBeModified=(String) testData.get("parameter_tobemodified");
		
				
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));	
		if(loginStatus){
			
			if((firstName==null||firstName.equals(""))&&(lastName==null||lastName.equals(""))){
				firstName ="Test"+Utility.getRandomString(4);
				lastName =Utility.getRandomString(4);
				AGlobalComponents.userId=firstName+"."+lastName;
				
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
					
				/**create identity **/
			
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
			
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC023", "asset_code", AGlobalComponents.assetCode);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", AGlobalComponents.assetCode);
			}
			
			/** checking the user details in IDM  before modification**/
	 		Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,parameterToBeModified,"",scriptName);
		
	 		/** Launch New Private Browser **/
	 		Utility.switchToNewBrowserDriver();
			
	 		/* Login as Manager */
	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 		if(loginStatus){
	 			logger.log(LogStatus.PASS, "Login Successful");
					
	 			/** Modify Identity **/
	 			requestNumber =Self_Service_CommonMethods.modifyIdentity(firstName,parameterToBeModified,(String) testData.get("request_type"));
	 				 			
	 			/** checkStatusInMyRequestInbox**/
	 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,parameterToBeModified,"",requestNumber,scriptName);
		
	 			/** Switch to Default Browser **/
	 			Utility.switchToDefaultBrowserDriver();
	 		}
	 		else{
	 			logger.log(LogStatus.FAIL, "Unable to Login as end user. Plz Check Application");
	 		}

	 		/** Validate  status in IDM after  request approved**/
	 		Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",parameterToBeModified,scriptName);
	 		Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC018_3", "asset_code", "");
	 		
	 		
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		
	 	}
	 	else {
	 		logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
	 	}	
	}

	/*
	 * TC023 :  Physical Access - Request Location access for Others
	*/
		
	@Test(priority=23)
	public void Self_Service_Automation_TC023() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC023"," Physical Access - Request Location access for Others");
		System.out.println("[INFO]--> Self_Service_Automation_TC023 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC023");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String locationName = (String) testData.get("location_name_1");
		String accessName = (String) testData.get("access_name_1");
			
		String firstName =(String) testData.get("first_name");
		String lastName =(String) testData.get("last_name");
		String scriptName =(String) testData.get("script_name");
		AGlobalComponents.userId = firstName+"."+lastName;
		String requestNumber="";
			
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));	
		if(loginStatus){
			
			if((firstName==null||firstName.equals(""))&&(lastName==null||lastName.equals(""))){
				firstName ="Test"+Utility.getRandomString(4);
				lastName ="Reqlocation";
				
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
				
				/**create identity **/
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", AGlobalComponents.assetCode);
			}
				
			/** check accesses assigned to the user in IDM **/
	 		Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,"","",scriptName);
		
	 		/** Launch New Private Browser **/
	 		Utility.switchToNewBrowserDriver();
		
	 		/** Login as manager **/
	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 		if(loginStatus){
			
	 			/** Request new location and add access Request **/
	 			requestNumber=Self_Service_CommonMethods.requestNewLocation(locationName,accessName,"Pittsburgh","430 Market St",firstName,lastName);
	 			Utility.updateDataInDatasource("Self_Service_Automation_TC023", "request_number", requestNumber);
	 				
	 			/** logout from the application **/
	 	 		LoginPage.logout();
	 			
	 			/** Login as approver **/
	 	 		loginStatus = LoginPage.loginAEHSC("anna.mordeno", "Alert1234");
			
	 	 		if(loginStatus){

	 	 			/** Approve Access Request by area admin**/
	 	 			Self_Service_CommonMethods.approveRequest("areaAdmin",requestNumber,accessName);
	 	 			
	 		 	 	/** Validate Access Request Status **/
	 	 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,"","",requestNumber,scriptName);
	 	 		}else{
	 	 			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
	 	 	
	 	 		}
	 	 	}else{
	 			logger.log(LogStatus.FAIL, "Unable to Login as Manager. Plz Check Application");
	 	 	}
	 		
	 		/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
				
			/** checking status of access assigned in IDM **/
			Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",accessName,scriptName);	
			Utility.updateDataInDatasource("Self_Service_Automation_TC023", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC023", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC023", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC023", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC023", "asset_code", "");
			
			/** Logout from Application **/
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}


	/*
	 * TC024 :  emergency Termination
	*/
		
	@Test(priority=24)
	public void Self_Service_Automation_TC024() throws Throwable 
	{


		logger =report.startTest("Self_Service_Automation_TC024"," Emergency Termination");
		System.out.println("[INFO]--> Self_Service_Automation_TC024 - TestCase Execution Begins");

		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC024");

		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String firstName =(String) testData.get("first_name");
		String lastName =(String) testData.get("last_name");
		String scriptName =(String) testData.get("script_name");
		AGlobalComponents.userId=(String) testData.get("user_id");
		AGlobalComponents.assetCode=(String) testData.get("asset_code");
		String requestNumber="";
		
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));	
		if(loginStatus){
		
			/**create identity **/
			if((firstName==null||firstName.equals(""))&&(lastName==null||lastName.equals(""))){
				firstName ="Test"+Utility.getRandomString(4); lastName ="EmergencyTermination";
				AGlobalComponents.userId=firstName+"."+lastName;
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
			
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", firstName+"."+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", AGlobalComponents.assetCode);
			
			}
			
		
		/** check accesses assigned to the user in IDM **/
			Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,"","",scriptName);

			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();

			/** Login as manager **/
			loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

			if(loginStatus){
		
				/** Request for emergency termination **/
				requestNumber=Self_Service_CommonMethods.emergencyTermination(firstName,lastName);
			
			 	/** Validate Access Request Status **/
		 		Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName, lastName,"","",requestNumber,scriptName);
		 		
		 	}else{
				logger.log(LogStatus.FAIL, "Unable to Login as Manager. Plz Check Application");
		 	}
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** checking status of access assigned in IDM **/
			Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"","",scriptName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC024", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC024", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC024", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC024", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC024", "asset_code", "");
		
			/** Logout from Application **/
			LoginPage.logout();
		
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}

	}

	/*
	* TC025 :  Position Access
	*/

	@Test(priority=25)
	public void Self_Service_Automation_TC025() throws Throwable 
	{

		logger =report.startTest("Self_Service_Automation_TC025"," Position Access for Others");
		System.out.println("[INFO]--> Self_Service_Automation_TC025 - TestCase Execution Begins");

		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC025");

		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String position = (String) testData.get("position");
		String accessName = (String) testData.get("access_name_1");
		String scriptName = (String) testData.get("script_name");

		String firstName =Utility.getRandomString(6);
		String lastName = Utility.getRandomString(4);

		AGlobalComponents.userId = firstName+"."+lastName;
		String requestNumber="";
		
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

		if(loginStatus){
		
			/**create new asset **/
			AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
				
			/**create identity **/
			FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC026", "first_name", firstName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC026", "last_name", lastName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC026", "full_name", firstName+" "+lastName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC026", "user_id", AGlobalComponents.userId);
			Utility.updateDataInDatasource("Self_Service_Automation_TC026", "asset_code", AGlobalComponents.assetCode);
			Utility.updateDataInDatasource("Self_Service_Automation_TC026", "badge_name", AGlobalComponents.assetName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "first_name", firstName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "last_name", lastName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "full_name", firstName+" "+lastName);
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "user_id", AGlobalComponents.userId);
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "asset_code", AGlobalComponents.assetCode);
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "badge_name", AGlobalComponents.assetName);
			
		
			
			/** check accesses assigned to the user in IDM **/
			Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,"",accessName,scriptName);

			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();

			/** Login as manager **/
			loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

			if(loginStatus){
		
				/** Request new position and add access Request **/
				requestNumber=Self_Service_CommonMethods.positionAccess(position,accessName,firstName,lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC025", "request_number", requestNumber);
					
				/** logout from the application **/
		 		LoginPage.logout();
				
				/** Login as approver **/
		 		loginStatus = LoginPage.loginAEHSC((String) testData.get("access_owner_username"), (String) testData.get("access_owner_password"));
		
		 		if(loginStatus){

		 			/** Approve Access Request by area admin**/
		 			Self_Service_CommonMethods.approveRequest("access_owner",requestNumber,accessName);
		 			
			 	 	/** Validate Access Request Status **/
		 			Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,"","",requestNumber,scriptName);
		 			
		 		}else{
		 			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		 	
		 		}
		 	}else{
				logger.log(LogStatus.FAIL, "Unable to Login as Manager. Plz Check Application");
		 	}
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** checking status of access assigned in IDM **/
			Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",accessName,scriptName);	
		
			/** Logout from Application **/
			LoginPage.logout();
		
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}

	}

	/*
	* TC026 :  Application  Access - Others
	*/


	@Test(priority=26)
	public void Self_Service_Automation_TC026() throws Throwable 
	{

		logger =report.startTest("Self_Service_Automation_TC026"," Application Access for Others");
		System.out.println("[INFO]--> Self_Service_Automation_TC026 - TestCase Execution Begins");

		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC026");

		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String application_name = (String) testData.get("application_name");
		String accessName = (String) testData.get("access_name_1");
		String scriptName = (String) testData.get("script_name");
		String firstName=(String) testData.get("first_name");
		String lastName=(String) testData.get("last_name");

		AGlobalComponents.userId = (String) testData.get("user_id");
		AGlobalComponents.assetCode = (String) testData.get("asset_code");
		AGlobalComponents.assetName = (String) testData.get("badge_name");
		String requestNumber="";
		
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
		if(loginStatus){
		
			if(firstName==null||firstName.equals("")){
			
				firstName =Utility.getRandomString(6);
				lastName =Utility.getRandomString(4);
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
						
				/**create identity **/
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC026", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC026", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC026", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC026", "user_id", AGlobalComponents.userId);
				Utility.updateDataInDatasource("Self_Service_Automation_TC026", "asset_code", AGlobalComponents.assetCode);
				Utility.updateDataInDatasource("Self_Service_Automation_TC026", "badge_name", AGlobalComponents.assetName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC027", "first_name", firstName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC027", "last_name", lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC027", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("Self_Service_Automation_TC027", "user_id", AGlobalComponents.userId);
				Utility.updateDataInDatasource("Self_Service_Automation_TC027", "asset_code", AGlobalComponents.assetCode);
				Utility.updateDataInDatasource("Self_Service_Automation_TC027", "badge_name", AGlobalComponents.assetName);
			}
			
			/** check accesses assigned to the user in IDM **/
			Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,"",accessName,scriptName);

			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();

			/** Login as manager **/
			loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

			if(loginStatus){
		
				/** Request new location and add access Request **/
				requestNumber=Self_Service_CommonMethods.applicationAccess(application_name,accessName,firstName,lastName);
				Utility.updateDataInDatasource(scriptName, "request_number", requestNumber);
					
				/** logout from the application **/
		 		LoginPage.logout();
				
				/** Login as approver **/
		 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
		
		 		if(loginStatus){

		 			/** Approve Access Request by manager**/
		 			Self_Service_CommonMethods.approveRequest("manager",requestNumber,accessName);
		 			
		 			/** logout from the application **/
		 			LoginPage.logout();
		 			
		 			/** Login as approver **/
		 	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("area_admin_username"), (String) testData.get("area_admin_password"));
				
		 	 		if(loginStatus){

		 	 			/** Approve Access Request by areaAdmin**/
		 	 			Self_Service_CommonMethods.approveRequest("areaAdmin",requestNumber,accessName);
		 			
		 	 			/** Validate Access Request Status **/
		 				Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,"","",requestNumber,scriptName);
		 	 		}else{
		 	 			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		 	 		}
		 	 		
		 			
		 		}else{
		 			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
		 	
		 		}
		 	}else{
				logger.log(LogStatus.FAIL, "Unable to Login as Manager. Plz Check Application");
		 	}
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			/** checking status of access assigned in IDM **/
			Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",accessName,scriptName);	
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "asset_code", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "badge_name", "");
		
			/** Logout from Application **/
			LoginPage.logout();
		
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}

	}


	/*
	 * TC027 :  IT  Access - Others
	*/
		

	@Test(priority=27)
	public void Self_Service_Automation_TC027() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC027"," IT Access for Others");
		System.out.println("[INFO]--> Self_Service_Automation_TC027 - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("Self_Service_Automation_TC027");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String IT_system = (String) testData.get("it_system");
		String accessName = (String) testData.get("access_name_1");
		String scriptName = (String) testData.get("script_name");
		String firstName=(String) testData.get("first_name");
		String lastName=(String) testData.get("last_name");
		
		AGlobalComponents.userId = (String) testData.get("user_id");
		AGlobalComponents.assetCode = (String) testData.get("asset_code");
		AGlobalComponents.assetName = (String) testData.get("badge_name");
		String requestNumber="";
			
		/** Login as admin User **/
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
		if(loginStatus){
			
			if(firstName==null||firstName.equals("")){
				
				firstName =Utility.getRandomString(6);
				lastName =Utility.getRandomString(4);
				/**create new asset **/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
							
				/**create identity **/
				FB_Automation_CommonMethods.createIdentity(firstName,lastName,scriptName);
				
			}
				
			/** check accesses assigned to the user in IDM **/
	 		Self_Service_CommonMethods.checkStatusBeforeRequestSubmission(AGlobalComponents.userId,"",accessName,scriptName);

	 		/** Launch New Private Browser **/
	 		Utility.switchToNewBrowserDriver();
		
	 		/** Login as manager **/
			loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

	 		if(loginStatus){
			
	 			/** Request new IT Access **/
	 			requestNumber=Self_Service_CommonMethods.itAccess(IT_system,accessName,firstName,lastName);
	 			Utility.updateDataInDatasource(scriptName, "request_number", requestNumber);
	 				
	 			/** logout from the application **/
	 	 		LoginPage.logout();
	 			
	 			/** Login as approver **/
	 	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));
			
	 	 		if(loginStatus){

	 	 			/** Approve Access Request by manager**/
	 	 			Self_Service_CommonMethods.approveRequest("manager",requestNumber,accessName);
	 	 			
	 	 			/** logout from the application **/
	 	 			LoginPage.logout();
	 	 			
	 	 			/** Login as approver **/
	 	 	 		loginStatus = LoginPage.loginAEHSC((String) testData.get("area_admin_username"), (String) testData.get("area_admin_password"));
	 			
	 	 	 		if(loginStatus){

	 	 	 			/** Approve Access Request by areaAdmin**/
	 	 	 			Self_Service_CommonMethods.approveRequest("areaAdmin",requestNumber,accessName);
	 	 			
	 	 	 			/** Validate Access Request Status **/
	 	 				Self_Service_CommonMethods.checkRequestInMyRequestInbox(firstName,lastName,"","",requestNumber,scriptName);
	 	 	 		}else{
	 	 	 			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
	 	 	 		}
	 	 	 		
	 	 			
	 	 		}else{
	 	 			logger.log(LogStatus.FAIL, "Unable to Login as Approver. Plz Check Application");
	 	 	
	 	 		}
	 	 	}else{
	 			logger.log(LogStatus.FAIL, "Unable to Login as Manager. Plz Check Application");
	 	 	}
	 		
	 		/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
				
			/** checking status of access assigned in IDM **/
			Self_Service_CommonMethods.checkStatusAfterRequestApproval(firstName,"",accessName,scriptName);	
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "first_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "last_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "full_name", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "user_id", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "asset_code", "");
			Utility.updateDataInDatasource("Self_Service_Automation_TC027", "badge_name", "");
			
			/** Logout from Application **/
			LoginPage.logout();
			
		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
		
	}

	/*
	 * TC028 : 5.0 Use cases . Badge Admin Login Scenarios : Activate Badge
	 */

	@Test(priority=28)
	public void Self_Service_Automation_TC028() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC0028","Badge Admin login scenarios - Activate Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC028 - TestCase Execution Begins");
		String firstName ="Amberd";
		String lastName ="Root";
			
		/** Login as badge admin User **/

	 	logger.log(LogStatus.PASS, "Login Successful");
	 	boolean loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");
	 	if (loginStatus) {
	 		Self_Service_CommonMethods.activatedeactivateBadge(firstName,lastName,"activate");
	 	 }
	 		
	 	Utility.switchToNewBrowserDriver();
	 	/** Login as admin User **/
	 		loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	 		if(loginStatus){
	 			Self_Service_CommonMethods.checkProvisioningLogs("UNLOCK_USER_SUCCESS","CCURE9000");
	 			
	 		}
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		}
		

	/*
	 * TC029 : 5.0 Use cases . Badge Admin Login Scenarios : Deactivate Badge
	 */

	@Test(priority=29)
	public void Self_Service_Automation_TC029() throws Throwable 
	{
		
		logger =report.startTest("Self_Service_Automation_TC029","Badge Admin login scenarios - Deactivate Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC029 - TestCase Execution Begins");
		String firstName ="Amberd";
		String lastName ="Root";
			
		/** Login as badge admin User **/

	 	logger.log(LogStatus.PASS, "Login Successful");
	 	boolean loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");
	 	if (loginStatus) {
	 		Self_Service_CommonMethods.activatedeactivateBadge(firstName,lastName,"deactivate");
	 	 }
	 		
	 	Utility.switchToNewBrowserDriver();
	 	/** Login as admin User **/
	 		loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	 		if(loginStatus){
	 			Self_Service_CommonMethods.checkProvisioningLogs("LOCK_USER_SUCCESS","CCURE9000");
	 			
	 		}
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		}
		

	/*
	 * TC030 : 5.0 Use cases . Badge Admin Login Scenarios : Request Replacement Badge
	 */

	@Test(priority=30)
	public void Self_Service_Automation_TC030() throws Throwable 
		{
			String firstName ="Mike";
			String lastName ="Rodi";
			logger =report.startTest("Self_Service_Automation_TC030","Badge Admin login scenarios - Request Replacement badge");
			System.out.println("[INFO]--> Self_Service_Automation_TC030 - TestCase Execution Begins");
			boolean loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");
			if(loginStatus){
	 			logger.log(LogStatus.PASS, "Login Successful");
	 			Self_Service_CommonMethods.requestReplacementBadge(firstName, lastName);
	 			}
			
			Utility.switchToNewBrowserDriver();
		 	/** Login as admin User **/
		 		loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
		 		if(loginStatus){
		 			Self_Service_CommonMethods.checkProvisioningLogs("LOCK_USER_SUCCESS","CCURE9000");
		 			Self_Service_CommonMethods.checkProvisioningLogs("CREATE_USER_SUCCESS","AMAG");
		 			
		 		}
		 		
		 		/** Logout from Application **/
		 		LoginPage.logout();
		}

	/*
	 * TC031 : 5.0 Use cases . Badge Admin Login Scenarios : Request new badge
	 */

	@Test(priority=31)
	public void Self_Service_Automation_TC031() throws Throwable 
		{
			
		logger =report.startTest("Self_Service_Automation_TC031","Badge Admin login scenarios - Request New Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC031- TestCase Execution Begins");
		String firstName ="Mike";
		String lastName ="Rodi";
			
		/** Login as badge admin User **/

	 	logger.log(LogStatus.PASS, "Login Successful");
	 	boolean loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");
	 	if (loginStatus) {
	 		Self_Service_CommonMethods.requestNewBadge(firstName,lastName);
	 	 	}
	 		
	 	Utility.switchToNewBrowserDriver();
	 	/** Login as admin User **/
	 		loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
	 		if(loginStatus){
	 			Self_Service_CommonMethods.checkProvisioningLogs("CREATE_USER_SUCCESS","CCURE9000");
	 			
	 		}
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
		}

	/*
	 * TC032 : 5.0 Use cases . Badge Admin Login Scenarios : Reset Pin
	 */

	@Test(priority=32)
	public void Self_Service_Automation_TC032() throws Throwable 
		{
			
		logger =report.startTest("Self_Service_Automation_TC032","Badge Admin login scenarios - Reset Pin");
		System.out.println("[INFO]--> Self_Service_Automation_TC032 - TestCase Execution Begins");
		String firstName ="Scott";
		String lastName ="Carter";
			
		/** Login as badge admin User **/

	 	logger.log(LogStatus.PASS, "Login Successful");
	 	boolean loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");
	 	if (loginStatus) {
	 		Self_Service_CommonMethods.resetBadgePin(firstName, lastName);
	 		
	 		/** Logout from Application **/
	 		LoginPage.logout();
	 	 	}
		}

	/*
	 * TC033 : Self Service - Return Temporary Badge
	*/

	@Test(priority=33)
	public void Self_Service_Automation_TC033() throws Throwable 
	{

		logger =report.startTest("Self_Service_Automation_TC033","Self Service - Return Temporary Badge");
		System.out.println("[INFO]--> Self_Service_Automation_TC033 - TestCase Execution Begins");
		
		String firstName ="Ujjwal";
		String lastName ="Mishra";

		/** Login as badge admin **/
		boolean loginStatus = LoginPage.loginAEHSC("badge.admin", "Alert1234");

		if(loginStatus){

			/** Create Return Badge Request **/
			Self_Service_CommonMethods.returnTemporaryBadge(firstName, lastName);

			Utility.switchToNewBrowserDriver();
		 	/** Login as admin User **/
		 		loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");	
		 		if(loginStatus){
		 			Self_Service_CommonMethods.checkProvisioningLogs("DELETE_USER_SUCCESS","LENEL");
		 			
		 		}
		 		/** Logout from Application **/
		 		LoginPage.logout();

		}else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}

	}

	/*
	 * TC034 : Self Service - Access Review Identity Expiring in X days
	*/

	@Test(priority=34)
	public void Self_Service_Automation_TC034() throws Throwable 
	{
		logger =report.startTest("Self_Service_Automation_TC034","Self Service - Access Review Identity Expiring in X days");
		System.out.println("[INFO]--> Self_Service_Automation_TC034 - TestCase Execution Begins");
		
		/** Login as  admin **/
		boolean loginStatus = LoginPage.loginAEHSC("admin", "Alert@783");
		
		if(loginStatus){
			ArrayList<String> arr = Self_Service_CommonMethods.checkIdentityExpiring("CCTV_ROOM", "abc@gmail.com", "New York", "employee", "SYS-000002","QA Manager");
			String reqNo =  arr.remove(arr.size() - 1);
			System.out.println("the array is "+arr);
			System.out.println("the req no is "+reqNo);
			
			/** Launch New Private Browser **/
			Utility.switchToNewBrowserDriver();
			
			/** Login as Manager User **/
			LoginPage.loginAEHSC("anna.mordeno", "Alert1234");
			HashMap<String, String> map = Self_Service_CommonMethods.checkIdentityExpiringRequestInManagerInbox(arr, reqNo,"ADMIN USER","CCTV_ROOM");
			
			/** Switch to Default Browser **/
			Utility.switchToDefaultBrowserDriver();
			
			for(int i=0;i<arr.size();i++)
			{	
			Self_Service_CommonMethods.checkUserStatusInIDM(arr.get(i),map,"CCTV_ROOM");
			}
			Self_Service_CommonMethods.checkRequestStatusInMyRequest(reqNo);
			/** Logout from Application **/
	 		LoginPage.logout();
		}
		else{
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}
	}
	
	@Test(priority=35)
	public void FB_Automation_TC011() throws Throwable 
	{
		logger =report.startTest("FB_Automation_TC011","Employee Onboarding from HR DB Connector");
		System.out.println("[INFO]--> FB_Automation_TC011 - TestCase Execution Begins");

		String firstName=Utility.getRandomString(5);
		String lastName="Onboard";
		String userId=Utility.UniqueNumber(6);
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("FB_Automation_TC011");
		
		AGlobalComponents.applicationURL = (String) testData.get("application_url");

		if(FB_Automation_CommonMethods.createUserInHRDb(firstName,lastName,userId)) {
			boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

			if(loginStatus){
				logger.log(LogStatus.PASS, "Login Successful");
			
				/* Create Recon Job */
				FB_Automation_CommonMethods.setUpReconJob((String) testData.get("recon_entity"),(String) testData.get("recon_connector"),(String) testData.get("recon_prefeedrule"),(String) testData.get("recon_scheduletype"),(boolean) testData.get("recon_createrequest"),(boolean) testData.get("recon_fetchentity"));
						
				/**creating asset for the user**/
			 	AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
				Utility.pause(10);	
				/** Launch New Private Browser **/
				Utility.switchToNewBrowserDriver();
				
				/* Login as Manager */
				loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

				if(loginStatus){
					logger.log(LogStatus.PASS, "Login Successful");
					
					/** checkStatusInMyRequestInbox**/
					Self_Service_CommonMethods.checkRequestInManagerInbox((String) testData.get("request_type"),firstName,lastName);
			 		
					/** approve request  by manager**/
					Self_Service_CommonMethods.approveRequestInInbox((String) testData.get("workflow_stage"));
					
					/* Logout from application */
					LoginPage.logout();
			 		
					/* Login as Badge Admin */
					loginStatus = LoginPage.loginAEHSC((String) testData.get("badge_admin_username"), (String) testData.get("badge_admin_password"));

					if(loginStatus){
						logger.log(LogStatus.PASS, "Login Successful");
			 	 			
						/** approve request By badge admin **/
						Self_Service_CommonMethods.approveRequestInInbox((String) testData.get("workflow_stage2"));
						
						Self_Service_CommonMethods.checkRequestInCompletedInbox((String) testData.get("request_type"),firstName,lastName,"");	
					}
				}
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
			 		
				/** Validate  created User in IDM after  request approved**/
				Self_Service_CommonMethods.checkStatusAfterRequestApproval("","","",(String) testData.get("script_name"));
			 	
				Utility.updateDataInDatasource("FB_Automation_TC011", "first_name", firstName);
				Utility.updateDataInDatasource("FB_Automation_TC011", "last_name", lastName);
				Utility.updateDataInDatasource("FB_Automation_TC011", "user_id", userId);
				Utility.updateDataInDatasource("FB_Automation_TC011", "full_name", firstName+" "+lastName);
				Utility.updateDataInDatasource("FB_Automation_TC011", "badge_name", AGlobalComponents.assetName);
				
				/** Logout from Application **/
				LoginPage.logout();		
			}
			else {
				logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
			}
		} 
	}
	
	@Test(priority=36)
	public void FB_Automation_TC012() throws Throwable 
	{

		logger =report.startTest("FB_Automation_TC012","Change Job Title from HR DB Connector");
		System.out.println("[INFO]--> FB_Automation_TC012 - TestCase Execution Begins");
	
		HashMap<String, Comparable> testData1 = Utility.getDataFromDatasource("FB_Automation_TC011");
		String userId = (String) testData1.get("user_id");
		String firstName=(String) testData1.get("first_name");
		String lastName=(String) testData1.get("last_name");
		
		if(userId==null||userId.equals(""))
		{
			logger.log(LogStatus.INFO, "UserId doesn't exists in Db,Executing EmployeeOnboarding");
			FB_Automation_TC011();
		}
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("FB_Automation_TC012");
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String jobTitle = (String) testData.get("job_title");
		
		if(FB_Automation_CommonMethods.changeEmpJobTitleThroughHRDB(userId,jobTitle)) {
			boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

			if(loginStatus){
				logger.log(LogStatus.PASS, "Login Successful");
				
				/* Create Recon Job */
				FB_Automation_CommonMethods.setUpReconJob((String) testData.get("recon_entity"),(String) testData.get("recon_connector"),(String) testData.get("recon_prefeedrule"),(String) testData.get("recon_scheduletype"),(boolean) testData.get("recon_createrequest"),(boolean) testData.get("recon_fetchentity"));
							
				/** Launch New Private Browser **/
				Utility.switchToNewBrowserDriver();
				
				/* Login as Manager */
				loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

				if(loginStatus){
					logger.log(LogStatus.PASS, "Login Successful");
					
					/** checkStatusInMyRequestInbox**/
					Self_Service_CommonMethods.checkRequestInManagerInbox((String) testData.get("request_type"),firstName,lastName);
			 		
					/** approve request  by manager**/
					Self_Service_CommonMethods.approveRequestInInbox((String) testData.get("workflow_stage"));
					
					Self_Service_CommonMethods.checkRequestInCompletedInbox((String) testData.get("request_type"),"","","");
			 			
				}
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
			 		
				/** Validate  Changed Job Title in IDM after  request approved**/
		 		Self_Service_CommonMethods.checkStatusAfterRequestApproval("","",jobTitle,(String) testData.get("script_name"));
	
				/** Logout from Application **/
				LoginPage.logout();		
			}
			else {
				logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
			}	
		}
	}
	
	@Test(priority=37)
	public void FB_Automation_TC013() throws Throwable 
	{

		logger =report.startTest("FB_Automation_TC013","Employee Type Conversion From Permanent To Temporary from HR DB Connector");
		System.out.println("[INFO]--> FB_Automation_TC013 - TestCase Execution Begins");

		HashMap<String, Comparable> testData1 = Utility.getDataFromDatasource("FB_Automation_TC011");
		String userId = (String) testData1.get("user_id");
		
		if(userId==null||userId.equals(""))
		{
			logger.log(LogStatus.INFO, "UserId doesn't exists in Db,Executing EmpOnboarding");
			FB_Automation_TC011();
		}
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("FB_Automation_TC013");
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String employeeType =  (String) testData.get("employee_type");
		String accessName =  (String) testData.get("access_name_1");
		
		if(FB_Automation_CommonMethods.empTypeConversionThroughHRDB(userId,employeeType)) {
			boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

			if(loginStatus){
				logger.log(LogStatus.PASS, "Login Successful");
				
				/* Create Recon Job */
				FB_Automation_CommonMethods.setUpReconJob((String) testData.get("recon_entity"),(String) testData.get("recon_connector"),(String) testData.get("recon_prefeedrule"),(String) testData.get("recon_scheduletype"),(boolean) testData.get("recon_createrequest"),(boolean) testData.get("recon_fetchentity"));
							
				/* check request in admin */
				Self_Service_CommonMethods.checkRequestInCompletedInbox((String) testData.get("request_type"),"","",accessName);
			 			
				/** Validate  Changed Job Title in IDM after  request approved**/
		 		Self_Service_CommonMethods.checkStatusAfterRequestApproval("","","",(String) testData.get("script_name"));
		 		
		 		/** Logout from Application **/
				LoginPage.logout();	
			}
			else {
				logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
			}	
		}
	}
	
	@Test(priority=38)
	public void FB_Automation_TC014() throws Throwable 
	{

		logger =report.startTest("FB_Automation_TC014","Employee Type Conversion From Temporary To Permanent from HR DB Connector");
		System.out.println("[INFO]--> FB_Automation_TC014 - TestCase Execution Begins");

		HashMap<String, Comparable> testData1 = Utility.getDataFromDatasource("FB_Automation_TC011");
		String userId = (String) testData1.get("user_id");
		
		if(userId==null||userId.equals(""))
		{
			logger.log(LogStatus.INFO, "UserId doesn't exists in Db,Executing EmpOnboarding");
			FB_Automation_TC011();
		}
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("FB_Automation_TC014");
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		String employeeType =  (String) testData.get("employee_type");
		String accessName =  (String) testData.get("access_name_1");
		
		if(FB_Automation_CommonMethods.empTypeConversionThroughHRDB(userId,employeeType)) {
			boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

			if(loginStatus){
				logger.log(LogStatus.PASS, "Login Successful");
				
				/* Create Recon Job */
				FB_Automation_CommonMethods.setUpReconJob((String) testData.get("recon_entity"),(String) testData.get("recon_connector"),(String) testData.get("recon_prefeedrule"),(String) testData.get("recon_scheduletype"),(boolean) testData.get("recon_createrequest"),(boolean) testData.get("recon_fetchentity"));
							
				/* check request in admin */
				Self_Service_CommonMethods.checkRequestInCompletedInbox((String) testData.get("request_type"),"","",accessName);
			 			
				/** Validate  Changed Job Title in IDM after  request approved**/
		 		Self_Service_CommonMethods.checkStatusAfterRequestApproval("","","",(String) testData.get("script_name"));
		 		
		 		/** Logout from Application **/
				LoginPage.logout();	
			}
			else {
				logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
			}	
		}
	}
	
	@Test(priority=39)
	public void FB_Automation_TC015() throws Throwable 
	{

		logger =report.startTest("FB_Automation_TC015","Employee Offboarding from HR DB Connector");
		System.out.println("[INFO]--> FB_Automation_TC015 - TestCase Execution Begins");

		HashMap<String, Comparable> testData1 = Utility.getDataFromDatasource("FB_Automation_TC011");
		String userId = (String) testData1.get("user_id");
		String firstName=(String) testData1.get("first_name");
		String lastName=(String) testData1.get("last_name");
		AGlobalComponents.assetName=(String) testData1.get("badge_name");
		
		if(userId==null||userId.equals(""))
		{
			logger.log(LogStatus.INFO, "UserId doesn't exists in Db,Executing Change Of JobTitle");
			FB_Automation_TC012();
		}
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("FB_Automation_TC015");
		AGlobalComponents.applicationURL = (String) testData.get("application_url");
		
		if(FB_Automation_CommonMethods.empTerminateThroughHRDB(userId)) {
			boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));

			if(loginStatus){
				logger.log(LogStatus.PASS, "Login Successful");
				
				/* Create Recon Job */
				FB_Automation_CommonMethods.setUpReconJob((String) testData.get("recon_entity"),(String) testData.get("recon_connector"),(String) testData.get("recon_prefeedrule"),(String) testData.get("recon_scheduletype"),(boolean) testData.get("recon_createrequest"),(boolean) testData.get("recon_fetchentity"));
							
				/** Launch New Private Browser **/
				Utility.switchToNewBrowserDriver();
				
				/* Login as Manager */
				loginStatus = LoginPage.loginAEHSC((String) testData.get("manager_username"), (String) testData.get("manager_password"));

				if(loginStatus){
					logger.log(LogStatus.PASS, "Login Successful");
					
					/** checkStatusInMyRequestInbox**/
					Self_Service_CommonMethods.checkRequestInManagerInbox((String) testData.get("request_type"),firstName,lastName);
			 		
					/** approve request  by manager**/
					Self_Service_CommonMethods.approveRequestInInbox((String) testData.get("workflow_stage"));
					
					Self_Service_CommonMethods.checkRequestInCompletedInbox((String) testData.get("request_type"),"","","");
			 		
				}
				
				/** Switch to Default Browser **/
				Utility.switchToDefaultBrowserDriver();
			 		
				/** Validate  Changed Job Title in IDM after  request approved**/
		 		Self_Service_CommonMethods.checkStatusAfterRequestApproval("","","",(String) testData.get("script_name"));
		 		
		 		/** Logout from Application **/
				LoginPage.logout();	
			}
			else {
				logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
			}	
		}
	}
	

	@Test(priority=40)
	public void FB_Automation_TC016() throws Throwable 	{
		logger =report.startTest("FB_Automation_TC016","Area Admin Cases(Add Identities,Remove Identities)");
		System.out.println("[INFO]--> Area Admin Cases - TestCase Execution Begins");
		
		HashMap<String, Comparable> testData = Utility.getDataFromDatasource("FB_Automation_TC016");
		AGlobalComponents.applicationURL = (String) testData.get("application_url"); 
		String accessName = (String) testData.get("access_name_1");
		
		ArrayList<String> firstNames=new ArrayList<String>();
		ArrayList<String> lastNames= new ArrayList<String>();
		
		/* Login as Manager */
		boolean loginStatus = LoginPage.loginAEHSC((String) testData.get("admin_username"), (String) testData.get("admin_password"));
		
		if(loginStatus){
			logger.log(LogStatus.PASS, "Login Successful");
			for(int i=0;i<1;i++) {
				firstNames.add("Test"+Utility.getRandomString(5));
				lastNames.add("AreaAdmin");
				
				/**creating asset for the user**/
				AGlobalComponents.assetName = Self_Service_CommonMethods.createNewAsset((String) testData.get("badge_type"), (String) testData.get("badge_subtype"), (String) testData.get("badge_system"));
				
				/* Create Identities */
				FB_Automation_CommonMethods.createIdentity(firstNames.get(i), lastNames.get(i),"FB_Automation_TC016");
			}
			
			/* Launch New Private Browser */
			Utility.switchToNewBrowserDriver();
			
			/* Login as Area Admin */
			loginStatus = LoginPage.loginAEHSC((String) testData.get("area_admin_username"), (String) testData.get("area_admin_password"));	
			
			if(loginStatus){
				logger.log(LogStatus.PASS, "Login Successful");
		
				if (FB_Automation_CommonMethods.addIdentities(firstNames, lastNames, accessName)) 
				{
					/* Launch New Private Browser */
					Utility.switchToNewBrowserDriver();
		
					/* Login as Manager */
					loginStatus = LoginPage.loginAEHSC((String) testData.get("area_admin_username"), (String) testData.get("area_admin_password"));	

					if(loginStatus){
						logger.log(LogStatus.PASS, "Login Successful");
			
						FB_Automation_CommonMethods.removeIdentities(firstNames, lastNames, accessName);
					}
				}
				else
					logger.log(LogStatus.FAIL, "Unable to add Identity");
			}	
			LoginPage.logout();		
		}
		else {
			logger.log(LogStatus.FAIL, "Unable to Login----> Plz Check Application");
		}			
	}
	

}


