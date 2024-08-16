package ro.msg.mobile_clone.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Year;

@Entity
@Table(name = "listings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    public enum Transmission {MANUAL, AUTOMATIC}
    public enum FuelType {PETROL, DIESEL, ELECTRIC, HYBRID}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "engine_size")
    private int engineSize;

    private int horsepower;

    private Transmission transmission;

    @Column(name = "fuel_type")
    private FuelType fuelType;

}
