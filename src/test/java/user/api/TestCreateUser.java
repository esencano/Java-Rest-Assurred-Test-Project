package user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import base.TestDataCleaner;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import models.UserPOJO;

@Test(groups= {"user-test","create-user","regression"})
public class TestCreateUser extends UserBaseTest{
	
	@Test(groups= {"smoke"})
	public void TestCreateValidUserWithAuthentication() {
		UserPOJO user = UserPOJO.getRandomInstance();
		
		Response response =  given()
			.spec(userReqSpec)
		 	.body(user, ObjectMapperType.GSON)
		 	.auth().oauth2(getToken())
		 .when()
		 	.post(extentedPath+getRegisteredAdmin().getUserName())
		 .then()
		  	.statusCode(200) // should be 201
		 .extract().response();
		 
		 TestDataCleaner.deleteUserByUserName(user.getUserName());
		 verifyUserResponseBody(user, response.body());
		 
		 // also can be added get request and validate it for more strict validation
	}
	
	@Test
	public void TestCreateValidUserWithoutAuthentication() {
		UserPOJO user = UserPOJO.getRandomInstance();
		given()
			.spec(userReqSpec)
			.body(user, ObjectMapperType.GSON)
		.when()
			.post(extentedPath+getRegisteredAdmin().getUserName())
		.then()
			.statusCode(401);
	}
	
	@Test
	public void TestCreateValidUserWithAuthenticationOfDifferentUser() {
		UserPOJO user = UserPOJO.getRandomInstance();
		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
			.body(user, ObjectMapperType.GSON)	
		.when()
			.post(extentedPath+"admin")
		.then()
			.statusCode(403);
	}
	
	@Test
	public void TestCreateValidUserWithoutAutherization() {
		
		/****CREATE TEST DATA****/
		// Data can be added with db or with api, i prefered to choose api here becasue user password need to be encrypted while adding to db
		// 1- Create a user and add it to registeredAdmin
		UserPOJO user = UserPOJO.getRandomInstance();
		given().spec(userReqSpec).auth().oauth2(getToken()).body(user, ObjectMapperType.GSON).when().post(extentedPath+getRegisteredAdmin().getUserName());
		
		// 2- Login and get token of created user to use it to add new user
		String tokenOfcreatedUser = login(user.getUserName(), user.getPassword());
		
		// 3- Create instance of a UserPOJO and try to add it to created user
		UserPOJO user2 = UserPOJO.getRandomInstance();
		
		given()
			.spec(userReqSpec)
			.auth().oauth2(tokenOfcreatedUser)
			.body(user2, ObjectMapperType.GSON)	
		.when()
			.post(extentedPath+user.getUserName())
		.then()
			.statusCode(403);
	}
	

	@Test(dataProvider = "getInvalidUserData", dataProviderClass = CreateUserDataProvider.class)
	public void CreateUserWithInvalidData(JsonObject data, int status, String errorMessage) {
		given()
			.spec(userReqSpec)
			.body(data.toString())
			.auth().oauth2(getToken())
		.when()
			.post(extentedPath+getRegisteredAdmin().getUserName()) 
		.then()
			.statusCode(status)
			.body(containsString(errorMessage));
	}
}
