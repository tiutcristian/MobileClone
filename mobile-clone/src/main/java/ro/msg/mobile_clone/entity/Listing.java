package ro.msg.mobile_clone.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Listing {

    public enum Transmission {MANUAL, AUTOMATIC}
    public enum FuelType {PETROL, DIESEL, ELECTRIC, HYBRID}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;

    private double price;

    private String make;

    private String model;

    private String description;

    private Year year;

    private int mileage;

    private int engineSize;

    private int horsepower;

    @Enumerated(EnumType.STRING)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

}
