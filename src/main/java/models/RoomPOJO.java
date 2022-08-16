package models;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomPOJO {
	private int id;
	private String name;
	private String description;

	public RoomPOJO(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public static RoomPOJO getRandomInstance() {
		String randomString = RandomStringUtils.randomAlphabetic(6);

		String roomName = "test_room_name"+randomString;
		String roomDescription = "test_room_descripton"+randomString;


		RoomPOJO room = new RoomPOJO();
		room.setName(roomName);
		room.setDescription(roomDescription);
		
		return room;
	}
}
