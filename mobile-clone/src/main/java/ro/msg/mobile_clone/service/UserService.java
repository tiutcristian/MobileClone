package ro.msg.mobile_clone.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.action.internal.EntityActionVetoException;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ro.msg.mobile_clone.entity.User;
import ro.msg.mobile_clone.exceptions.EntityNotFound;
import ro.msg.mobile_clone.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User createUser(User u) {
        return userRepository.save(u);
    }


    public User getUserById(Long id)
            throws EntityNotFound {

        return userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(User.class, id));
    }


    public Page<User> getUsersPaginated(int pageNumber, int size) {
        return userRepository.findAll(PageRequest.of(pageNumber, size));
    }


    public User updateUser(Long id, @NotNull User userDto)
            throws EntityNotFound {

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(User.class, id));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());

        return userRepository.save(user);
    }


    public void deleteUser(Long id)
            throws EntityNotFound {

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFound(User.class, id));

        userRepository.delete(user);
    }
}
