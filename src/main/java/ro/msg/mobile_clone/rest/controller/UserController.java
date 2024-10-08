package ro.msg.mobile_clone.rest.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.msg.mobile_clone.dto.UserDto;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFoundException;
import ro.msg.mobile_clone.exceptions.UniqueFieldsViolationException;
import ro.msg.mobile_clone.rest.mapper.UserMapper;
import ro.msg.mobile_clone.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(Pageable pageable) {

        Page<UserDto> resultPage = userService
                .getAllPaginated(pageable)
                .map(UserMapper.INSTANCE::mapUserToDto);

        log.info("Retrieved page {} of size {} with {} elements",
                resultPage.getNumber(), resultPage.getSize(), resultPage.getNumberOfElements());

        return ResponseEntity.ok(resultPage);
    }


    @PostMapping("/create")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto)
            throws UniqueFieldsViolationException {

        User newUser = UserMapper.INSTANCE.mapDtoToUser(userDto);
        log.debug("DTO mapped to entity: {}", newUser);

        User savedUser = userService.createUser(newUser);
        log.debug("Entity saved: {}", savedUser);

        UserDto savedUserDto = UserMapper.INSTANCE.mapUserToDto(savedUser);
        log.debug("Entity mapped back to DTO: {}", savedUserDto);

        log.info("Created user with id {}", savedUser.getId());

        return ResponseEntity
                .created(URI.create("/api/v1/users/" + savedUser.getId()))
                .body(savedUserDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id)
            throws EntityNotFoundException {

        User user = userService.getUserById(id);
        log.debug("User retrieved: {}", user);

        UserDto userDto = UserMapper.INSTANCE.mapUserToDto(user);
        log.debug("User mapped to DTO: {}", userDto);

        log.info("User with id {} retrieved successfully", id);

        return ResponseEntity.ok(userDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserDto userDto)
            throws EntityNotFoundException, UniqueFieldsViolationException {

        User user = UserMapper.INSTANCE.mapDtoToUser(userDto);
        log.debug("UserDTO mapped to User: {}", user);

        User updatedUser = userService.updateUser(id, user);
        log.debug("Updated user: {}", updatedUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(updatedUser.getId())
                .toUri();
        log.debug("Location: {}", location);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        log.debug("Headers: {}", headers);

        log.info("User with id {} updated successfully", id);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
            throws EntityNotFoundException {

        userService.deleteUser(id);
        log.info("User with id {} deleted successfully", id);

        return ResponseEntity.ok().build();
    }
}
