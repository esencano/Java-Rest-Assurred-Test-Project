package user.api;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.response.Response;

@Test(groups= {"user-test","get-admin","regression"})
public class TestGetAdmin extends UserBaseTest{
	@Test(groups= {"smoke"})
	public void GetAdminWithoutAuthentication() {		
		given()
			.spec(userReqSpec)
		.get()
		.then()
			.statusCode(401);
	}

	@Test
	public void GetAdminWithAuthentication() {	
		System.out.println(getToken());
		Response response = 
		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
		.when()
			.get()
		.then()
			.statusCode(200)
		.extract().response();
		
		verifyUserResponseBody(getRegisteredAdmin(),response.body());
	}

}
