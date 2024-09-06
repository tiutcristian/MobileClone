package ro.msg.mobile_clone.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bids")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "auction_id", nullable = false)
    @NotNull(message = "Auction is mandatory")
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "bidder_id", nullable = false)
    @NotNull(message = "Bidder is mandatory")
    private User bidder;

    @NotNull(message = "Offer is mandatory")
    private double offer;
}
