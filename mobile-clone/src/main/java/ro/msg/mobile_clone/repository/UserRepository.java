package ro.msg.mobile_clone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.mobile_clone.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPhone(String phone);
}
