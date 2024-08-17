package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.mapper.UserMapper;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final ListingService listingService;


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDto> userDtos = users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        return ResponseEntity.ok(userDtos);
    }


    @PostMapping("/create")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws Exception {
        User newUser = UserMapper.mapToUser(userDto);
        User savedUser = userService.createUser(newUser);
        UserDto savedUserDto = UserMapper.mapToUserDto(savedUser);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDto userDto = UserMapper.mapToUserDto(user);
        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws Exception {
        User user = UserMapper.mapToUser(userDto);
        userService.updateUser(id, user);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        listingService.deleteListingsByUserId(id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
