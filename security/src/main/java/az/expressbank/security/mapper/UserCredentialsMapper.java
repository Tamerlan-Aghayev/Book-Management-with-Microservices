package az.expressbank.security.mapper;

import az.expressbank.security.data.dto.UserCredentialsDTO;
import az.expressbank.security.data.entity.UserCredentials;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserCredentialsMapper {
    UserCredentials dtoToEntity(UserCredentialsDTO userCredentialsDTO);
    UserCredentialsDTO dtoToEntity(UserCredentials userCredentials);

}
