package login.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;


@Test(groups= {"login-test","regression"})
public class LoginTest extends LoginBaseTest {


	@Test(description = "this test case verify that api return a token and user role on headers" , priority = 1, dataProvider = "getloginData", dataProviderClass = LoginDataProvider.class ,groups= {"smoke"})
	public void loginSuccesfull(JsonObject data, String role) {
		given()
			.spec(loginReqSpec)
			.body(data.toString())
		.when()
			.post() 
		.then()
			.statusCode(200)
			.header("Authorization", startsWith("Bearer "))
			.header("Role", equalTo(role))
			.body(Matchers.blankOrNullString());

	}

	@Test(description = "this test case verify that api returns error in invalid user credentials" , priority = 0, dataProvider = "getInvalidUserData", dataProviderClass = LoginDataProvider.class)	
	public void loginUnsuccesfull(JsonObject data, int status, String errorMessage) {
		given()
			.spec(loginReqSpec)
			.body(data.toString())
		.when()
			.post()
		.then()
			.statusCode(status)
			.header("Authorization", not(startsWith("Bearer ")))
			.header("Role",nullValue())
			.body(containsString(errorMessage));
	}
	
	//a test case can be added to login again with already loggedin user credentials
	//logout proccess can be added but this application does not support logout
}
