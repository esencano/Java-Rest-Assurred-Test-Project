package base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class ConvertHelper {
	public static <T> List<T> jsonArrayToList(String json, Class<T> elementClass) throws IOException {
	    ObjectMapper objectMapper = new ObjectMapper();
	    CollectionType listType = 
	      objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, elementClass);
	    
	    return objectMapper.readValue(json, listType);
	}
}
