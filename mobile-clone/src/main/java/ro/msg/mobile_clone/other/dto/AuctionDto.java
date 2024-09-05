package ro.msg.mobile_clone.other.dto;

import java.sql.Timestamp;

public record AuctionDto(
    Long id,
    Long listingId,
    Timestamp endingTimestamp,
    Long winnerId
) {}
