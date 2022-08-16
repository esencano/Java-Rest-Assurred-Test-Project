package user.api;

import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.util.List;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import base.ConvertHelper;
import base.TestDataCleaner;
import io.restassured.response.Response;
import models.UserPOJO;

@Test(groups= {"user-test","get-user","regression"})
public class TestGetUser extends UserBaseTest{
	
	
	@Test(groups= {"smoke"})
	public void GetUserWithUsernAuthentication() {	
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		String token = login(user.getUserName(),user.getPassword());
		Response response = 
				given()
					.spec(userReqSpec)
					.auth().oauth2(token)
				.when()
					.get()
				.then()
					.statusCode(200)			
				.extract().response();

		verifyUserResponseBody(user,response.body());
		TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	
	@Test(groups= {"smoke"})
	public void GetUserWithAdminAuthentication() {	
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		
		Response response = 
				given()
					.spec(userReqSpec)
					.auth().oauth2(getToken())
				.when()
					.get(extentedPath+user.getUserName())
				.then()
					.statusCode(200)
				.extract().response();

		verifyUserResponseBody(user,response.body());
		TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	
	@Test
	public void GetUserWithAnotherAdminAuthentication() {	
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		String token = login(testAdminUsername, testAdminPassword);

		given()
			.spec(userReqSpec)
			.auth().oauth2(token)
		.when()
			.get(extentedPath+user.getUserName())
		.then()
			.statusCode(401); //404 possible


		 TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	
	@Test
	public void GetUserWithAnotherUserAuthentication() {	
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		UserPOJO user2 = createAndAddRandomUserToRegisteredUser();
		String token = login(user2.getUserName(),user2.getPassword());
		given()
			.spec(userReqSpec)
			.auth().oauth2(token)
		.when()
			.get(extentedPath+user.getUserName())
		.then()
			.statusCode(401); //403-404 possible

		 TestDataCleaner.deleteUserByUserName(user.getUserName());
		 TestDataCleaner.deleteUserByUserName(user2.getUserName());
	}
	
	@Test
	public void GetUserWithoutAuthentication() {
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		given()
			.spec(userReqSpec)
		.get(extentedPath+user.getUserName())
		.then()
			.statusCode(401);
	}
	
	@Test
	public void GetNotExistingUser() {	
		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())	
		.get(extentedPath+"NotExistingUser")
		.then()
			.statusCode(404); //401 possible
	}
	
	
	@Test(groups= {"smoke"})
	public void GetAllUsersWithAdminAuthentication() throws IOException {	
		UserPOJO user1 = createAndAddRandomUserToRegisteredUser();
		UserPOJO user2 = createAndAddRandomUserToRegisteredUser();
				

		Response response = 
				given()
					.spec(userReqSpec)
					.auth().oauth2(getToken())
				.when()
					.get(getAllExtentedPath)
				.then()
					.statusCode(200)
					.body("",Matchers.hasSize(2))
				.extract().response();
		

		List<UserPOJO> allUsers = ConvertHelper.jsonArrayToList(response.body().asString(), UserPOJO.class);
		
		verifyUserseEqaul(allUsers.get(0), user1);
		verifyUserseEqaul(allUsers.get(1), user2);
		
		TestDataCleaner.deleteUserByUserName(user1.getUserName());
		TestDataCleaner.deleteUserByUserName(user2.getUserName());
	}

	@Test
	public void GetAllUsesrWithUsernAuthentication() throws IOException {	
		UserPOJO user1 = createAndAddRandomUserToRegisteredUser();
		UserPOJO user2 = createAndAddRandomUserToRegisteredUser();
		String token = login(user1.getUserName(), user1.getPassword());

		given()
			.spec(userReqSpec)
			.auth().oauth2(token)
		.when()
			.get(getAllExtentedPath)
		.then()
			.statusCode(403);
	
		 TestDataCleaner.deleteUserByUserName(user1.getUserName());
		 TestDataCleaner.deleteUserByUserName(user2.getUserName());
	}

	@Test
	public void GetAllUsesrWithoutnAuthentication() throws IOException {	
		UserPOJO user1 = createAndAddRandomUserToRegisteredUser();
		UserPOJO user2 = createAndAddRandomUserToRegisteredUser();

		given()
			.spec(userReqSpec)
		.get(getAllExtentedPath)
		.then()
			.statusCode(401);

		 TestDataCleaner.deleteUserByUserName(user1.getUserName());
		 TestDataCleaner.deleteUserByUserName(user2.getUserName());
	}
	
}
