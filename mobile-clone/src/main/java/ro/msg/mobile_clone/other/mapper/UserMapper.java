package ro.msg.mobile_clone.other.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ro.msg.mobile_clone.other.dto.UserDto;
import ro.msg.mobile_clone.model.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto mapUserToDto(User post);
    User mapDtoToUser(UserDto dto);
}
