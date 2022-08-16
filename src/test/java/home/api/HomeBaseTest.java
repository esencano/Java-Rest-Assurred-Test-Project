package home.api;

import static io.restassured.RestAssured.given;
import static logger.LogUtil.log;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import base.TestBase;
import base.TestDataCleaner;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBody;
import models.HomePOJO;
import models.UserPOJO;

class HomeBaseTest extends TestBase{

	
	@BeforeClass(alwaysRun = true)
	void init() {
		UserPOJO admin = UserPOJO.getRandomInstance();

		log("Creating admin");
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
	
	HomePOJO createHome(HomePOJO home) {
		log("Creating home: "+home.getName());
		return given().spec(homeReqSpec).auth().oauth2(getToken()).body(home).when().post().then().extract().as(HomePOJO.class);
	}
	
	void modifyHomeNameAndDescriptionRandomly(HomePOJO home) {
		String randomString = RandomStringUtils.randomAlphabetic(6);
		home.setName("test_home_name"+randomString);
		home.setDescription("test_home_descritpion"+randomString);
	}

	void verifyHomeEqualsToResponseBody(HomePOJO expectedHome, ResponseBody body) {
		JsonPath home = body.jsonPath();

		assertThat(home.get("id"), notNullValue());
		assertEquals(home.get("userName"), expectedHome.getName(), "Actual and expected home names don't match");
		assertEquals(home.get("email"), expectedHome.getDescription(), "Actual and expected home descriptions don't match");
	}
	
	void verifyHomesEqual(HomePOJO actualHome, HomePOJO expectedHome) {

		assertThat(actualHome.getId(), notNullValue());
		assertEquals(actualHome.getName(), expectedHome.getName(), "Actual and expected home names don't match");
		assertEquals(actualHome.getDescription(), expectedHome.getDescription(), "Actual and expected home descriptions don't match");
	}
	
	


}





