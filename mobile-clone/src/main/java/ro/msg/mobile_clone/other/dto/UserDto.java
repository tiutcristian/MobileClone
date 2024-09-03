package ro.msg.mobile_clone.other.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {}
