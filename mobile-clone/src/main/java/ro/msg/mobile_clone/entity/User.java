package ro.msg.mobile_clone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import ro.msg.mobile_clone.converters.LowercaseConverter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @Size(
            min = 2,
            max = 50,
            message = "First name must be between 2 and 50 characters long"
    )
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Size(
            min = 2,
            max = 50,
            message = "Last name must be between 2 and 50 characters long"
    )
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    @Convert(converter = LowercaseConverter.class)
    private String email;

    @Pattern(
            regexp = "^\\+?[0-9]{10,14}$",
            message = "Invalid phone number"
    )
    @NotBlank(message = "Phone is mandatory")
    @Column(unique = true)
    private String phone;
}
