package home.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import org.testng.annotations.Test;

import base.TestDataCleaner;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import models.HomePOJO;
import models.UserPOJO;

@Test(groups= {"home-test","edit-home","regression"})
public class TestEditHome extends HomeBaseTest{
	@Test(groups= {"smoke"})
	public void TestEditValidWithAdminAuthentication() {
		HomePOJO home = createHome(HomePOJO.getRandomInstance());
		
		modifyHomeNameAndDescriptionRandomly(home);

		Response response =  given()
				.spec(homeReqSpec)
			 	.body(home, ObjectMapperType.JACKSON_2)
			 	.auth().oauth2(getToken())
			 .when()
			 	.put()
			 .then()
			  	.statusCode(200)
			 .extract().response();
			 
			 HomePOJO actualHome = response.body().as(HomePOJO.class);
			 TestDataCleaner.deleteHomeByHomeID(actualHome.getId());
			 verifyHomesEqual(actualHome, home);
			 
			 // also get request can be added and validate it for more strict validation
	}
	
	@Test
	public void TestEditHomeWithUserAuthentication() {
		HomePOJO home = createHome(HomePOJO.getRandomInstance());
	
		modifyHomeNameAndDescriptionRandomly(home);
		
		UserPOJO user = createAndAddRandomUserToRegisteredUser();
		String token = login(user.getUserName(), user.getPassword());
		

		given()
			.spec(homeReqSpec)
			.body(home, ObjectMapperType.JACKSON_2)
			.auth().oauth2(token)
		.when()
			.put()
		.then()
			.statusCode(403);
		
		 TestDataCleaner.deleteHomeByHomeID(home.getId());
		 TestDataCleaner.deleteUserByUserName(user.getUserName());
	}
	
	
	@Test
	public void TestEditHomeWithAuthenticationOfDifferentAdmin() {
		HomePOJO home = createHome(HomePOJO.getRandomInstance());
		
		modifyHomeNameAndDescriptionRandomly(home);
		
		UserPOJO newAdmin = createRandomAdmin();
		String token = login(newAdmin.getUserName(), newAdmin.getPassword());
		
		given()
			.spec(homeReqSpec)
			.body(home, ObjectMapperType.JACKSON_2)
			.auth().oauth2(token)
		.when()
			.put()
		.then()
			.statusCode(404); //can be 401 too
		
		 TestDataCleaner.deleteHomeByHomeID(home.getId());
		 TestDataCleaner.deleteUserByUserName(newAdmin.getUserName());

		
	}
	
	@Test
	public void TestEditValidHomeWithoutAuthentication() {
		HomePOJO home = createHome(HomePOJO.getRandomInstance());
		
		modifyHomeNameAndDescriptionRandomly(home);
		
		given()
			.spec(homeReqSpec)
			.body(home, ObjectMapperType.JACKSON_2)
		.when()
			.put()
		.then()
			.statusCode(401);
		
		TestDataCleaner.deleteHomeByHomeID(home.getId());
		
	}
	
	
	@Test
	public void TestEditHomeWithInvalidName() {
		HomePOJO home = createHome(HomePOJO.getRandomInstance());
		
		home.setName("");
		
		given()
			.spec(homeReqSpec)
			.auth().oauth2(getToken())
			.body(home, ObjectMapperType.JACKSON_2)
		.when()
			.put()
		.then()
			.statusCode(400)
			.body(containsString("Invalid home name"));
		
		TestDataCleaner.deleteHomeByHomeID(home.getId());
	
	}
}
