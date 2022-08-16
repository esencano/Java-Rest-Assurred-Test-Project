package home.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.annotations.Test;

import base.TestDataCleaner;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import models.HomePOJO;
import models.UserPOJO;

@Test(groups= {"home-test","create-home","regression"})
public class TestCreateHome extends HomeBaseTest{
	@Test(groups= {"smoke"})
	public void TestCreateValidHomeWithAdminAuthentication() {
		HomePOJO home = HomePOJO.getRandomInstance();
		
		Response response =  given()
			.spec(homeReqSpec)
		 	.body(home, ObjectMapperType.JACKSON_2)
		 	.auth().oauth2(getToken())
		 .when()
		 	.post()
		 .then()
		  	.statusCode(200) // should be 201
		 .extract().response();
		 
		 HomePOJO actualHome = response.body().as(HomePOJO.class);
		 TestDataCleaner.deleteHomeByHomeID(actualHome.getId());
		 verifyHomesEqual(actualHome, home);
		 
		 // also get request can be added and validate it for more strict validation
	}
	
	@Test
	public void TestCreateValidHomeWithUserAuthentication() {
		HomePOJO home = HomePOJO.getRandomInstance();
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		String token = login(user.getUserName(), user.getPassword());
		
		given()
			  .spec(homeReqSpec)
			 .body(home, ObjectMapperType.JACKSON_2)
			 .auth().oauth2(token)
		.when()
			 .post()
		.then()
			.statusCode(403);
			 
		TestDataCleaner.deleteUserByUserName(user.getUserName());		 
	}
	
	@Test
	public void TestCreateValidHomeWithoutAuthentication() {
		HomePOJO home = HomePOJO.getRandomInstance();
		
		given()
			.spec(homeReqSpec)
			.body(home, ObjectMapperType.JACKSON_2)
		.when()
			.post()
		.then()
			.statusCode(401); 
	}
	
	@Test
	public void TestCreateHomeWithInvalidName() {
		HomePOJO home = new HomePOJO();
		
		home.setName("");
		
		given()
			.spec(homeReqSpec)
			.auth().oauth2(getToken())
			.body(home, ObjectMapperType.JACKSON_2)
		.when()
			.post()
		.then()
			.statusCode(400)
			.body(containsString("Invalid home name"));	 
	}
	
}
