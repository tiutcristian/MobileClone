package ro.msg.mobile_clone.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import ro.msg.mobile_clone.enums.FuelType;
import ro.msg.mobile_clone.enums.Transmission;

import java.time.Year;

@Entity
@Table(name = "listings")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is mandatory")
    private User user;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull(message = "Price is mandatory")
    @Min(value = 1, message = "Price must be positive")
    private double price;

    @NotBlank(message = "Make is mandatory")
    private String make;

    @NotBlank(message = "Model is mandatory")
    private String model;

    private String description;

    @NotNull(message = "Year is mandatory")
    @PastOrPresent(message = "Year must be in the past")
    private Year year;

    @NotNull(message = "Mileage is mandatory")
    @Min(value = 0, message = "Mileage must be positive")
    private int mileage;

    @NotNull(message = "Engine size is mandatory")
    @Min(value = 1, message = "Engine size must be positive")
    private int engineSize;

    @NotNull(message = "Horsepower is mandatory")
    @Min(value = 1, message = "Horsepower must be positive")
    private int horsepower;

    @NotNull(message = "Transmission is mandatory")
    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @NotNull(message = "Fuel type is mandatory")
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

}
