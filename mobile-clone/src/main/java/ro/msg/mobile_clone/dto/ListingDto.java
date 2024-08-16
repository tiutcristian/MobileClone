package ro.msg.mobile_clone.dto;

import ro.msg.mobile_clone.entity.Listing;
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
    Listing.Transmission transmission,
    Listing.FuelType fuelType
) {}
