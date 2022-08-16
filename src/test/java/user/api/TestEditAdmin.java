package user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import io.restassured.response.Response;
import models.UserPOJO;

@Test(groups= {"user-test","edit-admin","regression"})
public class TestEditAdmin extends UserBaseTest {
	//Normally it must not be possible to update username
	@Test(groups= {"smoke"}, priority = -1)
	public void EditValidAdminWithValidAuthentication() {	    
		UserPOJO admin = UserPOJO.getRandomInstance();
		Response response = 
		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
			.body(admin)		
		.when()
			.put() 
		.then()
			.statusCode(200)
		.extract().response();
		
		verifyUserResponseBody(admin, response.body());
		// also can be added get request and validate it for more strict validation

		setRegisteredAdmin(admin);
	}

	@Test
	public void EditValidAdminWithoutdAuthentication() {
		UserPOJO user = UserPOJO.getRandomInstance();

		given()
			.spec(userReqSpec)
			.body(user)
		.when()
			.put() 
		.then()
			.statusCode(401);
	}


	@Test(dataProvider = "getInvalidUserData", dataProviderClass = CreateUserDataProvider.class)
	public void EditAdminWithInvalidData(JsonObject data, int status, String errorMessage) {
		given()
			.spec(userReqSpec)
			.body(data.toString())
			.auth().oauth2(getToken())
		.when()
			.put() 
		.then()
			.statusCode(status)
			.body(containsString(errorMessage));
	}	
}
