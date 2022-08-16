package user.api;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import base.TestDataCleaner;
import models.UserPOJO;

@Test(groups= {"user-test","delete-user","regression"})
public class TestDeleteUser extends UserBaseTest{
	
	@Test(groups = {"smoke"})
	public void DeleteUserWithAdminAuthentication() {
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		
		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
		.when()
			.delete("/"+user.getUserName())
		.then()
			.statusCode(200); // should be 204
	
		given().auth().oauth2(getToken()).when().get().then().statusCode(404);
	}
	
	@Test(groups = {"smoke"})
	public void DeleteUserWithUserAuthentication() {
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		String token = login(user.getUserName(), user.getPassword());
		
		given()
			.spec(userReqSpec)
			.auth().oauth2(token)
		.when()
			.delete("/"+user.getUserName())
		.then()
			.statusCode(200); // should be 204
	
		given().auth().oauth2(getToken()).when().get().then().statusCode(404);
	}
	
	
	@Test
	public void DeleteUserWithoutAuthentication() {
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		given()
			.spec(userReqSpec)
		.when()
			.delete("/"+user.getUserName())
		.then()
			.statusCode(401); // might be 404
		
		 TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	
	@Test
	public void DeleteUserWithAnotherAdminAuthentication() {
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		String token = login(testAdminUsername, testAdminPassword);
		given()
			.spec(userReqSpec)
			.auth().oauth2(token)
		.when()
			.delete("/"+user.getUserName())
		.then()
			.statusCode(401); // might be 404
	
		 TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	
	@Test
	public void DeleteUserWithNotExistingPath() {
		UserPOJO user = createAndAddRandomUserToRegisteredUser();

		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
		.when()
			.delete("/"+"notExistingUserNameQWR")
		.then()
			.statusCode(404); // might be 404
	
		 TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	

}
