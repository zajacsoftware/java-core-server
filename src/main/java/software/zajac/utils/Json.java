package software.zajac.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Json {
    private static ObjectMapper mapper = defaultObjectMapper();
    public  static  ObjectMapper defaultObjectMapper(){
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String jsonSrc) throws IOException {
        return mapper.readTree(jsonSrc);
    }

    public static <A> A fromJson(JsonNode node, Class<A> aClass) throws JsonProcessingException {
        return mapper.treeToValue(node, aClass);
    }

    public static JsonNode toJson(Object obj) throws IOException {
        return mapper.valueToTree(obj);
    }

    public String stringify(Object obj, boolean prettify) throws JsonProcessingException {
        return generateJson(obj, prettify);
    }

    private static String generateJson(Object obj, boolean prettify) throws JsonProcessingException {
        ObjectWriter writer = mapper.writer();
        if(prettify){
            writer = writer.with(SerializationFeature.INDENT_OUTPUT);
        }
        return writer.writeValueAsString(obj);
    }
}
