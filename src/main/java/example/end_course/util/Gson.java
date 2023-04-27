package example.end_course.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class Gson {
    public static com.google.gson.Gson gson() {
        return new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonDeserializer<>() {
            @Override
            public Object deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
            }
        }).create();
    }
}
