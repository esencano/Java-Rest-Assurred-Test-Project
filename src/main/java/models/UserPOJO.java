package models;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPOJO{
	
	private int id;
	
	private String userName;

	private String password;

	private String email;

	private String name;

	private String lastName;

	private boolean active;

	public UserPOJO() {
		this.active = true;
	}
	
	public UserPOJO(String userName, String password, String email, String name, String lastName) {
		super();
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.name = name;
		this.lastName = lastName;
	}

	public static UserPOJO getRandomInstance() {
		String randomString = RandomStringUtils.randomAlphabetic(6);

		String userName = "test_user"+randomString;
		String password = "test_user";
		String email = "test_user"+randomString+"@ercansencanoglu.com";
		String name = "test_user"+randomString;
		String lastName = "test_user"+randomString;


		UserPOJO user = new UserPOJO();
		user.setUserName(userName);
		user.setPassword(password);
		user.setEmail(email);
		user.setName(name);
		user.setLastName(lastName);
		
		return user;
	}
}
