package ro.msg.mobile_clone.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto mapUserToDto(User post);
    User mapDtoToUser(UserDto dto);
}
