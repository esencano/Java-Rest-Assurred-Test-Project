package user.api;

import static io.restassured.RestAssured.given;
import static logger.LogUtil.log;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import base.TestBase;
import base.TestDataCleaner;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import models.UserPOJO;

class UserBaseTest extends TestBase{

	final String extentedPath = "/user/";
	final String getAllExtentedPath = "/all";

	@BeforeClass(alwaysRun = true)
	void init() throws InterruptedException {

		UserPOJO admin = UserPOJO.getRandomInstance();

		log("Creating admin: "+admin.getUserName());
		if(!(given().spec(userReqSpec).body(admin).post().then().extract().statusCode() == 200)) {
			fail("initilazing test failed");
		}
		
		setRegisteredAdmin(admin);
	}


	@AfterClass(alwaysRun = true)
	void destroy() {
		log("Destroying Test Data");
		TestDataCleaner.deleteUserByUserName(getRegisteredAdmin().getUserName());
	}

	void verifyUserResponseBody(UserPOJO expectedUser, ResponseBody body) {
		JsonPath user = body.jsonPath();

		assertThat(user.get("id"), notNullValue());
		assertEquals(user.get("userName"), expectedUser.getUserName(), "Actual and expected user usernames don't match");
		assertEquals(user.get("email"), expectedUser.getEmail(), "Actual and expected user emails don't match");
		assertEquals(user.get("name"), expectedUser.getName(), "Actual and expected user names don't match");
		assertEquals(user.get("lastName"), expectedUser.getLastName(), "Actual and expected user lastnames don't match");
		assertEquals(user.get("active"), expectedUser.isActive(), "Actual and expected user activation status don't match");
		assertThat(user.get("password").toString(), not(containsString(expectedUser.getPassword())));
	}
	
	void verifyUserseEqaul(UserPOJO actualUser, UserPOJO expectedUser) {

		assertThat(actualUser.getId(), notNullValue());
		assertEquals(actualUser.getUserName(), expectedUser.getUserName(), "Actual and expected user usernames don't match");
		assertEquals(actualUser.getEmail(), expectedUser.getEmail(), "Actual and expected user emails don't match");
		assertEquals(actualUser.getName(), expectedUser.getName(), "Actual and expected user names don't match");
		assertEquals(actualUser.getLastName(), expectedUser.getLastName(), "Actual and expected user lastnames don't match");
		assertEquals(actualUser.isActive(), expectedUser.isActive(), "Actual and expected user activation status don't match");
		assertThat(actualUser.getPassword(), not(containsString(expectedUser.getPassword())));

	}


}





