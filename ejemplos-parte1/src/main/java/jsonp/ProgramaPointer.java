package jsonp;

import java.io.FileReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonPointer;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;

public class ProgramaPointer {

	public static void main(String[] args) throws Exception {
		
		
		JsonReader reader = Json.createReader(new FileReader("json/colores.json"));
		JsonObject jsonStructure = reader.readObject();
		reader.close();
		
		JsonPointer jsonPointer = Json.createPointer("/colors/1/color");
		JsonString jsonString = (JsonString) jsonPointer.getValue(jsonStructure);
		System.out.println(jsonString.getString());
		
		JsonPointer jsonPointer2 = Json.createPointer("/colors/1/type");
		boolean found = jsonPointer2.containsValue(jsonStructure);
		System.out.println(found);
	}

}
