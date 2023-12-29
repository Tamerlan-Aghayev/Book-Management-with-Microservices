package az.expressbank.book.util;

import az.expressbank.book.data.dto.CategoryDTO;
import az.expressbank.book.exception.DTOParsingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;
@Slf4j
public class BookConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);


    private static String getJsonAsString(Response response){
        String jsonData;
        try {
            var inputStream = response.body().asInputStream() ;
            jsonData = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return jsonData;

        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }
    public static List<CategoryDTO> getCategoryListFromResponse(Response response){
        String jsonData=getJsonAsString(response);
        logTraceResponse(jsonData);
        try {
            List <CategoryDTO>categoryDTOS = objectMapper.readValue(jsonData, new TypeReference<List<CategoryDTO>>() {
            });
            return categoryDTOS;
        }catch (Exception ex) {
            throw new DTOParsingException("Exception occurred when parsing JSON to DTO");
        }
    }
    public static CategoryDTO getCategoryFromResponse(Response response){
        String jsonData=getJsonAsString(response);
        logTraceResponse(jsonData);
        try {
            CategoryDTO categoryDTO = objectMapper.readValue(jsonData, CategoryDTO.class);
            return categoryDTO;
        }catch (Exception ex){
            throw new DTOParsingException("Exception occurred when parsing JSON to DTO");
        }

    }

    public static Long getIdOfCategory(Response response){
        String jsonData=getJsonAsString(response);
        logTraceResponse(jsonData);
        Long id = Long.parseLong(jsonData);
        return id;
    }
    private static void logTraceResponse(String jsonData) {
        log.trace("FeignClient Response: {}", jsonData);
    }

}
