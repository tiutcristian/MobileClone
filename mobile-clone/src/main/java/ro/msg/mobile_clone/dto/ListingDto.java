package ro.msg.mobile_clone.dto;

import ro.msg.mobile_clone.entity.FuelType;
import ro.msg.mobile_clone.entity.Transmission;

import java.time.Year;

public record ListingDto (
    Long id,
    Long userId,
    String title,
    double price,
    String make,
    String model,
    String description,
    Year year,
    int mileage,
    int engineSize,
    int horsepower,
    Transmission transmission,
    FuelType fuelType
) {}
