package base;

import static io.restassured.RestAssured.given;
import static logger.LogUtil.log;

import java.io.PrintStream;


import com.google.gson.JsonObject;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;
import logger.LogUtil;
import models.UserPOJO;

public class TestBase {
	
	protected final String BASE_URI = "https://micro19v2.herokuapp.com";
	protected final String LOGIN_BASE_PATH = "/giris";
	protected final String USER_BASE_PATH = "/user";
	protected final String HOME_BASE_PATH = "/home";

	private UserPOJO registeredAdmin;
	private String token;
	
	protected final String testAdminUsername = "admin";
	protected final String testAdminPassword = "admin";
	protected final String testUserUserName = "user";
	protected final String testUserPassword = "user";
	
	protected RequestSpecification loginReqSpec;
	protected RequestSpecification userReqSpec;
	protected RequestSpecification homeReqSpec;

	
	public TestBase() {
		PrintStream outputStream = new PrintStream(LogUtil.getPrintStream());
		// Below command can be used to print logs log4j2 in case of a fail
		RestAssured.config = RestAssured.config().logConfig(new LogConfig().defaultStream(outputStream).enableLoggingOfRequestAndResponseIfValidationFails().enablePrettyPrinting(true));
	      
		// response and requests logs are mixed beetwen test classes if filter added globally
		//RestAssured.filters( new AllureRestAssured()); // in this way request and response will not be written logs/app.log file automaticly
		RestAssured.baseURI = BASE_URI;
		
		loginReqSpec = new RequestSpecBuilder().addFilter(new AllureRestAssured()).setContentType(ContentType.JSON).setBasePath(LOGIN_BASE_PATH).build();
		userReqSpec =  new RequestSpecBuilder().addFilter(new AllureRestAssured()).setContentType(ContentType.JSON).setBasePath(USER_BASE_PATH).build();
		homeReqSpec =  new RequestSpecBuilder().addFilter(new AllureRestAssured()).setContentType(ContentType.JSON).setBasePath(HOME_BASE_PATH).build();	
	}

	// This function will be used in other tests that's why it has been added in main test base
	protected String login(String userName, String password) {
		log("User login: "+userName);
		
		JsonObject user = new JsonObject();
		user.addProperty("userName", userName);
		user.addProperty("password", password);

		String token = given()
				.spec(loginReqSpec)
				.body(user.toString())
				.when().post().then().extract().header("Authorization");
		return token;
	}
	
	
	protected UserPOJO getRegisteredAdmin() {
		return registeredAdmin;
	}
	
	protected void setRegisteredAdmin(UserPOJO registeredUser) {
		this.registeredAdmin = registeredUser;
		loginRegisteredAdmin();
	}
	

	protected void loginRegisteredAdmin() {
		token = login(registeredAdmin.getUserName(), registeredAdmin.getPassword());
	}
	

	protected String getToken() {
		return token;
	}
	
	
	protected UserPOJO createAndAddRandomUserToRegisteredUser() {
		UserPOJO user = UserPOJO.getRandomInstance();
		int id = given().spec(userReqSpec).auth().oauth2(getToken()).body(user, ObjectMapperType.JACKSON_2).when().post("user/"+getRegisteredAdmin().getUserName()).then().extract().path("id");
		// Dont extract all body because password returns encrypted
		user.setId(id);
		return user;
	}
	
	protected UserPOJO createRandomAdmin() {
		UserPOJO user = UserPOJO.getRandomInstance();
		int id = given().spec(userReqSpec).body(user, ObjectMapperType.JACKSON_2).when().post().then().extract().path("id");
		// Dont extract all body because password returns encrypted
		user.setId(id);
		return user;
	}
	
}
