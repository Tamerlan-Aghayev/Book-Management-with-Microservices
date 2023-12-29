package az.express.gateway.util;

import az.express.gateway.exception.ErrorResponse;
import az.express.gateway.exception.TokenValidationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ErrorResponseConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);


    public static String getErrorMessage(Response response){
        String jsonData;
        try {
            var inputStream = response.body().asInputStream();
            jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            ErrorResponse errorResponse = objectMapper.readValue(jsonData, ErrorResponse.class);

            throw new TokenValidationException(errorResponse.getMessage());
        }catch (IOException exception){
            throw new RuntimeException(exception.getMessage());
        }
    }

}
