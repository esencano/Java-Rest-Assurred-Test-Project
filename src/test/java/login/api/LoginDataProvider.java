package login.api;

import org.testng.annotations.DataProvider;

import com.google.gson.JsonObject;

public class LoginDataProvider {
	
	//Data can be added directly to db
	@DataProvider(name = "getloginData")
	private Object[][] getLoginData(){
		JsonObject admin = new JsonObject();
		admin.addProperty("username", "admin");
		admin.addProperty("password", "admin");

		JsonObject user = new JsonObject();
		user.addProperty("username", "user");
		user.addProperty("password", "user");

		Object userInfo[][] = new Object[][] {{admin,"ROLE_ADMIN"},{user,"ROLE_USER"}};
		return userInfo;
	}

	@DataProvider(name = "getInvalidUserData")
	private Object[][] getInvalidUserData(){
		JsonObject wrongUsernameandPassword = new JsonObject();
		wrongUsernameandPassword.addProperty("username", "test");
		wrongUsernameandPassword.addProperty("password", "test");

		JsonObject wrongUsername= new JsonObject();
		wrongUsername.addProperty("username", "test");
		wrongUsername.addProperty("password", "admin");

		JsonObject wrongPassword= new JsonObject();
		wrongPassword.addProperty("username", "admin");
		wrongPassword.addProperty("password", "test");

		JsonObject emptyUserNameandPassword = new JsonObject();
		emptyUserNameandPassword.addProperty("username", "");
		emptyUserNameandPassword.addProperty("password", "");


		JsonObject emptyPassword = new JsonObject();
		emptyPassword.addProperty("username", "admin");
		emptyPassword.addProperty("password", "");

		JsonObject emptyUsername = new JsonObject();
		emptyUsername.addProperty("username", "");
		emptyUsername.addProperty("password", "admin");

		JsonObject integerPasswrod = new JsonObject();
		integerPasswrod.addProperty("username", "admin");
		integerPasswrod.addProperty("password", 12345);


		Object userInfo[][] = new Object[][] {
			{wrongUsernameandPassword,401,"wrong username or password"},
			{wrongUsername,401,"wrong username or password"},
			{wrongPassword,401,"wrong username or password"},
			{emptyUserNameandPassword,401,"username and password requeired"},
			{emptyUsername,401,"username required"},
			{emptyPassword,401,"password requeired"},
			{integerPasswrod,400,"invalid type"},

		};
		return userInfo;
	}

}
