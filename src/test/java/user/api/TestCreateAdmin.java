package user.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.Reporter;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

import base.TestDataCleaner;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import models.UserPOJO;

@Test(groups= {"user-test","create-admin","regression"})
public class TestCreateAdmin extends UserBaseTest{
	
	
	@Test(groups= {"smoke"})
	public void RegisterValidAdmin() {
      UserPOJO admin = UserPOJO.getRandomInstance();

	   Response response = 
	   given()
	   		.spec(userReqSpec)
			.body(admin,ObjectMapperType.GSON)
		.when()
			.post() 
		.then()
			.statusCode(200) // it should be 201
		.extract().response();
	
	   Reporter.log(response.body().asPrettyString()); // this line has been added just for example

	   TestDataCleaner.deleteUserByUserName(admin.getUserName());
	   verifyUserResponseBody(admin, response.body());
	   // also can be added get request and validate it for more strict validation
	  
	}

	@Test(dataProvider = "getInvalidUserData", dataProviderClass = CreateUserDataProvider.class)
	public void RegisterAdminWithInvalidData(JsonObject data, int status, String errorMessage) {

		given()
			.spec(userReqSpec)
			.body(data.toString())
		.when()
			.post() 
		.then()
			.statusCode(status)
			.body(containsString(errorMessage));
	}
}
