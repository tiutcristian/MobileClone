package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
public class AuctionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/auctions";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (id, first_name, last_name, email, phone) " +
            "VALUES (1, 'Cristian', 'Tiut', 'tiutcristian@gmail.com', '0721644423')";
    private static final String INSERT_LISTING_QUERY = "INSERT INTO listings (id, user_id, title, price, make, model, description, manufacture_year, mileage, engine_size, horsepower, transmission, fuel_type) " +
            "VALUES (1, 1, 'Title', 2000, 'Toyota', 'Auris', 'Some description here', '2000', 2000000, 2000, 100, 'MANUAL', 'PETROL')";
    private static final String INSERT_AUCTION_QUERY = "INSERT INTO auctions (id, listing_id, ending_timestamp, winner_id, active) " +
            "VALUES (1, 1, '2021-10-10 12:00:00', NULL, true)";

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = webAppContextSetup(context)
                .alwaysDo(result -> result.getRequest().addHeader("x-api-key", "test123"))
                .build();
    }

    // create auction
    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateAuction() throws Exception {
        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .plus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/api/v1/auctions/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.listingId").value(1))
                .andExpect(jsonPath("$.endingTimestamp")
                        .value(deadline + ".000+00:00"))
                .andExpect(jsonPath("$.winnerId").isEmpty())
                .andExpect(jsonPath("$.active").value(true));

    }

    @Test
    public void testCreateAuctionInvalidListingId() throws Exception {
        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .plus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateAuctionInvalidEndingTimestamp() throws Exception {
        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .minus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(status().isBadRequest());
    }

    // get all auctions
    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY, INSERT_AUCTION_QUERY}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllAuctions() throws Exception {
        // get all auctions
        this.mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].listingId").value(1))
                .andExpect(jsonPath("$.content[0].endingTimestamp")
                        .value("2021-10-10T09:00:00.000+00:00"))
                .andExpect(jsonPath("$.content[0].winnerId").isEmpty())
                .andExpect(jsonPath("$.content[0].active").value(true));
    }

    // get auction by id
    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY, INSERT_AUCTION_QUERY}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAuctionById() throws Exception {
        // get auction by id
        this.mockMvc.perform(get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.listingId").value(1))
                .andExpect(jsonPath("$.endingTimestamp")
                        .value("2021-10-10T09:00:00.000+00:00"))
                .andExpect(jsonPath("$.winnerId").isEmpty())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    public void testGetAuctionByIdInvalidId() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY, INSERT_AUCTION_QUERY}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteAuctionById() throws Exception {
        // delete auction by id
        this.mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAuctionByIdInvalidId() throws Exception {
        // delete auction by id
        this.mockMvc.perform(delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
}
