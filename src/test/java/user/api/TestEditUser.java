package user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import base.TestDataCleaner;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import models.UserPOJO;

@Test(groups= {"user-test","edit-user","regression"})
public class TestEditUser extends UserBaseTest{
	//Test data can be added with db or with api, i prefered to choose api here becasue user password need to be encrypted while adding to db

	
	@Test(description = "This test case verifies that sub user infos can be updated by its admin", groups= {"smoke"})
	public void TestEditValidUserWithAdminAuthentication() {

		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
	
		
		// 2- Create an instance of UserPojo then modify its id and email becasue those fields can't be modified
		UserPOJO user2 = UserPOJO.getRandomInstance();
		user2.setId(user.getId());
		user2.setEmail(user.getEmail());// email can't updated
		
		Response response =  given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
		 	.body(user2, ObjectMapperType.GSON)
		 .when()
		 	.put(extentedPath+getRegisteredAdmin().getUserName())
		 .then()
		  	.statusCode(200)
		 .extract().response();
		 
		 TestDataCleaner.deleteUserByUserName(user.getUserName()); // This step is not needed but it is added to be sure to delete data in case of any problem
		 TestDataCleaner.deleteUserByUserName(user2.getUserName());
		 verifyUserResponseBody(user2, response.body());
		 
		 // also can be added get request and validate it for more strict validation
	}
	
	@Test(description = "This test case verifies that sub user infos can be updated by itself", groups= {"smoke"})
	public void TestEditValidUserWithUserAuthentication() {

		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
	
		
		// 2- Create an instance of UserPojo then modify its id and email becasue those fields can't be modified
		UserPOJO user2 = UserPOJO.getRandomInstance();
		user2.setId(user.getId());
		user2.setEmail(user.getEmail());// email can't updated
		
		// 3- Login registered user and get credentials
		String token = login(user.getName(), user.getPassword());
		
		Response response =  given()
			.spec(userReqSpec)
			.auth().oauth2(token)
		 	.body(user2, ObjectMapperType.GSON)	
		 .when()
		 	.put(extentedPath+getRegisteredAdmin().getUserName())
		 .then()
		  	.statusCode(200)
		 .extract().response();
		 
		 TestDataCleaner.deleteUserByUserName(user.getUserName()); // This step is not needed but it is added to be sure to delete data in case of any problem
		 TestDataCleaner.deleteUserByUserName(user2.getUserName());
		 verifyUserResponseBody(user2, response.body());
		 
		 // also can be added get request and validate it for more strict validation
	}
	
	@Test(description = "This test case verifies that sub user infos can't be updated without authentication", groups= {"smoke"})
	public void TestEditValidUserWithoutAuthentication() {

		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		user.setName("testuser");
		
		  given()
		  	.spec(userReqSpec)
		 	.body(user, ObjectMapperType.GSON)
		 .when()
		 	.put(extentedPath+getRegisteredAdmin().getUserName())
		 .then()
		  	.statusCode(401);
		 
		  TestDataCleaner.deleteUserByUserName(user.getUserName());  
	}
	
	@Test(description = "This test case verifies that infos of a sub user of another admin can't be updated")
	public void TestEditValidUserWithAuthenticationOfDifferentUser() {

		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		user.setName("testuser");
		
		String token = login(testAdminUsername,testAdminPassword);
		
		  given()
		  	.spec(userReqSpec)
			.auth().oauth2(token)
		 	.body(user, ObjectMapperType.GSON)
		 .when()
		 	.put(extentedPath+getRegisteredAdmin().getUserName())
		 .then()
		  	.statusCode(401); // might be 404

		  TestDataCleaner.deleteUserByUserName(user.getUserName());  
	}
	

	@Test(description = "This test case verifies that infos of a sub user of another admin can't be updated")
	public void TestEditValidUserWithAuthenticationButDifferentUserPath() {

		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		user.setName("testuser");
		
		  given()
		  	.spec(userReqSpec)
			.auth().oauth2(getToken())
		 	.body(user, ObjectMapperType.GSON)
		 .when()
		 	.put(extentedPath+"admin")
		 .then()
		  	.statusCode(401); // might be 404
		 
		  TestDataCleaner.deleteUserByUserName(user.getUserName());  
	}
	
	@Test(description = "This test case verifies that this api can't be used to updated a user")
	public void TestEditValidUserWithAuthenticationButNotExistingtUserPath() {

		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		user.setName("testuser");
		
		  given()
		  	.spec(userReqSpec)
			.auth().oauth2(getToken())
		 	.body(user, ObjectMapperType.GSON)
		 .when()
		 	.put(extentedPath+"notExistingUserPathABC")
		 .then()
		  	.statusCode(401); // might be 404
		 
		  TestDataCleaner.deleteUserByUserName(user.getUserName());  
	}

	@Test(description = "This test case verifies that user can not be updated with invalid values", dataProvider = "getInvalidUserData", dataProviderClass = CreateUserDataProvider.class)
	public void EditUserWithInvalidData(JsonObject data, int status, String errorMessage) {
		/****CREATE TEST DATA****/
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
	
		data.addProperty("id", user.getId());
		
		given().
			spec(userReqSpec)
			.auth().oauth2(getToken())
		 	.body(user, ObjectMapperType.GSON)
		 .when()
		 	.put(extentedPath+getRegisteredAdmin().getUserName())
		 .then()
		  	.statusCode(status)
			.body(containsString(errorMessage));
		 
		TestDataCleaner.deleteUserByUserName(user.getUserName()); 

	}

}
