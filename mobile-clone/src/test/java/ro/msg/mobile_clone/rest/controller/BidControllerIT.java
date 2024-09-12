package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
public class BidControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/bids";

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(result -> result.getRequest().addHeader("x-api-key", "test123"))
                .build();
    }

    // create bid
        // uses create auction
            // uses create listing
                // uses create user (seller)
        // uses create user (bidder)
    private void createUser() {

    }

    @Test
    public void createBid() {

    }



    // get all bids paginated by auction

    // delete bid
}
