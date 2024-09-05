package ro.msg.mobile_clone.other.dto;

public record BidDto(
    Long id,
    Long auctionId,
    Long bidderId,
    double offer
) {}
