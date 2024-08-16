package ro.msg.mobile_clone.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {}
