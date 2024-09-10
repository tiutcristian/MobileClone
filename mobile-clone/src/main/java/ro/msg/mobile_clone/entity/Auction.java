package ro.msg.mobile_clone.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "auctions")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "listing_id", nullable = false)
    @NotNull(message = "Listing is mandatory")
    private Listing listing;

    @NotNull(message = "Ending timestamp is mandatory")
    private Timestamp endingTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "winner_id")
    private User winner;

    @OneToMany(
            mappedBy = "auction",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    private Set<Bid> bids;

    private boolean active = true;
}
