package deadlinefighters.analyticsframework.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class ObjectMapperInstance {
    private static ObjectMapper objectMapper = null;

    /**
     * Get the instance of ObjectMapper with defined defaults
     * @return object mapper instance
     */
    public static ObjectMapper getInstance() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.registerModule(new JavaTimeModule());
        }
        return objectMapper;
    }
}
