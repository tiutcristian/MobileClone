package ro.msg.mobile_clone.dto;

import lombok.Builder;
import lombok.Data;
import ro.msg.mobile_clone.entity.Listing;

import java.time.Year;

@Data
@Builder
public class ListingDto {
    private Long id;
    private Long userId;
    private String title;
    private double price;
    private String make;
    private String model;
    private String description;
    private Year year;
    private int mileage;
    private int engineSize;
    private int horsepower;
    private Listing.Transmission transmission;
    private Listing.FuelType fuelType;
}
