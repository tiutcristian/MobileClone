package ro.msg.mobile_clone.model.entity;

import jakarta.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.*;
import ro.msg.mobile_clone.model.utils.LowercaseConverter;

import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is mandatory")
    @Convert(converter = LowercaseConverter.class)
    private String email;

    @Pattern(regexp = "^\\+?[0-9]{10,14}$", message = "Invalid phone number")
    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private Set<Listing> listings;
}
