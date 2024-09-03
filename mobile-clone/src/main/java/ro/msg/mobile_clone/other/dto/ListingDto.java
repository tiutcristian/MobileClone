package ro.msg.mobile_clone.other.dto;

import ro.msg.mobile_clone.model.entity.FuelType;
import ro.msg.mobile_clone.model.entity.Transmission;

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
