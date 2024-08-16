package ro.msg.mobile_clone.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.msg.mobile_clone.dto.UserDto;
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
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PostMapping("/create")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) throws Exception {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        listingService.deleteListingsByUserId(id);
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
