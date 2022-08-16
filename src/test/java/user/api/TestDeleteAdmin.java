package user.api;
import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;


@Test(groups= {"user-test","delete-admin","regression"})
public class TestDeleteAdmin extends UserBaseTest{
	
	// This test case should be run after negative test cases that's why its priority set to 100
	@Test(groups= {"smoke"}, priority = 100)
	public void DeleteAdminWithAuthentication() {
		given()
			.spec(userReqSpec)
			.header("Authorization", getToken()) // auth method can be used but here just wanted to show another way of authentication
		.when()
			.delete("/"+getRegisteredAdmin().getUserName())
		.then().statusCode(200); //it should be 204
		
		given().auth().oauth2(getToken()).when().get().then().statusCode(404);
	}
		
	@Test(groups= {"smoke"})
	public void DeleteAdminWithoutAuthentication() {
		given().spec(userReqSpec).delete("/"+getRegisteredAdmin().getUserName()).then().statusCode(401);
	}
	
	@Test
	public void DeleteAdminWithAuthenticationInvalidUsername() {

		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
		.when()
			.delete("/"+"admin")
		.then().statusCode(403);
	}
	
	@Test
	public void DeleteAdminWithAuthenticationNotExistingUsername() {

		given()
			.spec(userReqSpec)
			.auth().oauth2(getToken())
		.when()
			.delete("/"+"abc")
		.then().statusCode(403);
	}
	
	
}
