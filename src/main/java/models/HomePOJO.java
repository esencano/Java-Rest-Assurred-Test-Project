package models;


import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HomePOJO {
	private int id;
	private String name;
	private String description;
	
	public HomePOJO(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public static HomePOJO getRandomInstance() {
		String randomString = RandomStringUtils.randomAlphabetic(6);

		String homeName = "test_home_name"+randomString;
		String homeDescription = "test_home_descripton"+randomString;


		HomePOJO home = new HomePOJO();
		home.setName(homeName);
		home.setDescription(homeDescription);
		
		return home;
	}
}
