package user.api;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

import com.google.gson.JsonObject;

public class CreateUserDataProvider {
	@DataProvider(name = "getInvalidUserData")
	private Object[][] getInvalidUserData(){
		String randomString = RandomStringUtils.randomAlphabetic(6);


		String username = "test_user"+randomString;
		String email = "test_user"+randomString+"@ercansencanoglu.com";
		String password = "test_user";
		String name = "test_user"+randomString;
		String lastName = "test_user"+randomString;
		
		JsonObject userWithoutUsername = new JsonObject();
		userWithoutUsername.addProperty("email", email);
		userWithoutUsername.addProperty("password", password);
		userWithoutUsername.addProperty("name", name);
		userWithoutUsername.addProperty("lastName", lastName);
		
		JsonObject userWithoutPassword = new JsonObject();
		userWithoutPassword.addProperty("userName", username);
		userWithoutPassword.addProperty("email", email);
		userWithoutPassword.addProperty("name", name);
		userWithoutPassword.addProperty("lastName", lastName);
		
		JsonObject userWithoutEmail = new JsonObject();
		userWithoutEmail.addProperty("userName", username);
		userWithoutEmail.addProperty("password", password);
		userWithoutEmail.addProperty("name", name);
		userWithoutEmail.addProperty("lastName", lastName);
		
		JsonObject userWithoutName  = new JsonObject();
		userWithoutName.addProperty("userName", username);
		userWithoutName.addProperty("email", email);
		userWithoutName.addProperty("password", password);
		userWithoutName.addProperty("lastName", lastName);
		
		JsonObject userWithoutLastname  = new JsonObject();
		userWithoutLastname.addProperty("userName", username);
		userWithoutLastname.addProperty("email", email);
		userWithoutLastname.addProperty("password", password);
		userWithoutLastname.addProperty("name", name);
		
		JsonObject userWithInvalidEdmail  = new JsonObject();
		userWithInvalidEdmail.addProperty("userName", username);
		userWithInvalidEdmail.addProperty("email", "test_user@");
		userWithInvalidEdmail.addProperty("password", password);
		userWithInvalidEdmail.addProperty("name", name);
		userWithInvalidEdmail.addProperty("lastName", lastName);
		
		JsonObject userWithInvalidUsername  = new JsonObject();
		userWithInvalidUsername.addProperty("userName", RandomStringUtils.random(6, '!','@','#','$','%','^','&'));
		userWithInvalidUsername.addProperty("email", email);
		userWithInvalidUsername.addProperty("password", password);
		userWithInvalidUsername.addProperty("name", name);
		userWithInvalidUsername.addProperty("lastName", lastName);
		
		JsonObject userWithInvalidPassword  = new JsonObject();
		userWithInvalidPassword.addProperty("userName", username);
		userWithInvalidPassword.addProperty("email", email);
		userWithInvalidPassword.addProperty("password", "123");
		userWithInvalidPassword.addProperty("name", name);
		userWithInvalidPassword.addProperty("lastName", lastName);
		
		JsonObject userWithExistingUsername  = new JsonObject();
		userWithExistingUsername.addProperty("userName", "admin");
		userWithExistingUsername.addProperty("email", email);
		userWithExistingUsername.addProperty("password", "123");
		userWithExistingUsername.addProperty("name", name);
		userWithExistingUsername.addProperty("lastName", lastName);
		

		
		Object userInfo[][] = new Object[][] {
			{userWithoutUsername,400,"username required"},
			{userWithoutPassword,400,"password required"},
			{userWithoutEmail,400, "email required"},
			{userWithoutName,400, "name required"},
			{userWithoutLastname,400, "lastname required"},
			{userWithInvalidEdmail,400,"email is invalid"},
			{userWithInvalidUsername,400,"username is invalid"},
			{userWithInvalidPassword,400,"password is invalid"},
			{userWithExistingUsername,400,"username already exist"},

		};
		
		return userInfo;
	}
}
