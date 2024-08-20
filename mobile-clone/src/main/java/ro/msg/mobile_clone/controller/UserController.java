package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.mapper.UserMapper;
import ro.msg.mobile_clone.service.ListingService;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;
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

        List<UserDto> userDTOs = users.stream()
                .map(UserMapper.INSTANCE::mapUserToDto)
                .toList();

        return ResponseEntity.ok(userDTOs);
    }


    @PostMapping("/create")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws Exception {

        User newUser = UserMapper.INSTANCE.mapDtoToUser(userDto);
        User savedUser = userService.createUser(newUser);
        UserDto savedUserDto = UserMapper.INSTANCE.mapUserToDto(savedUser);

        String currentPath = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString();

        String targetPath = currentPath.replace("/create", "/{id}");

        URI location = ServletUriComponentsBuilder
                .fromUriString(targetPath)
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUserDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);
        UserDto userDto = UserMapper.INSTANCE.mapUserToDto(user);

        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws Exception {

        User user = UserMapper.INSTANCE.mapDtoToUser(userDto);
        User updatedUser = userService.updateUser(id, user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(updatedUser.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        listingService.deleteListingsByUserId(id);
        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }
}
