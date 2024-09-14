package ro.msg.mobile_clone.dto;

public record BidDto(
    Long id,
    Long auctionId,
    Long bidderId,
    double offer
) {}
