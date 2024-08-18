package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;
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
    private ModelMapper mapper;


    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        List<UserDto> userDTOs = users.stream()
                .map(user -> mapper.map(user, UserDto.class))
                .toList();

        return ResponseEntity.ok(userDTOs);
    }


    @PostMapping("/create")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws Exception {

        User newUser = mapper.map(userDto, User.class);
        User savedUser = userService.createUser(newUser);
        UserDto savedUserDto = mapper.map(savedUser, UserDto.class);

        String currentPath = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString();

        String targetPath = currentPath.replace("/create", "/{id}");

        URI location = ServletUriComponentsBuilder
                .fromUriString(targetPath)
                .buildAndExpand(savedUserDto.id())
                .toUri();

        return ResponseEntity.created(location).body(savedUserDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        User user = userService.getUserById(id);
        UserDto userDto = mapper.map(user, UserDto.class);

        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws Exception {

        User user = mapper.map(userDto, User.class);
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
