package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
public class AuctionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/auctions";

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(result -> result.getRequest().addHeader("x-api-key", "test123"))
                .build();
    }

    private void createUserAndListing() throws Exception {
        // create user
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {\
                                "firstName": "Cristian",\
                                "lastName": "Tiut",\
                                "email": "tiutcristian@gmail.com",\
                                "phone": "0721644423"\
                                }""")
        );

        // create listing
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/listings/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {\
                            "userId": 1,\
                            "title": "Title",\
                            "price": 2000,\
                            "make": "Toyota",\
                            "model": "Auris",\
                            "description": "Some description here",\
                            "year": 2000,\
                            "mileage": 2000000,\
                            "engineSize": 2000,\
                            "horsepower": 100,\
                            "transmission": "MANUAL",\
                            "fuelType": "PETROL"\
                        }""")
        );
    }

    // create auction
    @Test
    public void testCreateAuction() throws Exception {
        createUserAndListing();

        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .plus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", "/api/v1/auctions/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.listingId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endingTimestamp")
                        .value(deadline + ".000+00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.winnerId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true));

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
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testCreateAuctionInvalidEndingTimestamp() throws Exception {
        createUserAndListing();

        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .minus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // get all auctions
    @Test
    public void testGetAllAuctions() throws Exception {
        createUserAndListing();

        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .plus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // get all auctions
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].listingId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].endingTimestamp")
                        .value(deadline + ".000+00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].winnerId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].active").value(true));
    }

    // get auction by id
    @Test
    public void testGetAuctionById() throws Exception {
        createUserAndListing();

        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .plus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // get auction by id
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.listingId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endingTimestamp")
                        .value(deadline + ".000+00:00"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.winnerId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true));
    }

    @Test
    public void testGetAuctionByIdInvalidId() throws Exception {
        // get auction by id
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // delete auction by id
    @Test
    public void testDeleteAuctionById() throws Exception {
        createUserAndListing();

        // create json object
        JSONObject jsonObject = new JSONObject();
        String deadline = Instant.now()
                .plus(3, java.time.temporal.ChronoUnit.DAYS)
                .toString()
                .substring(0, 19);
        jsonObject.put("listingId", 1);
        jsonObject.put("endingTimestamp", deadline);

        // create auction
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // delete auction by id
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteAuctionByIdInvalidId() throws Exception {
        // delete auction by id
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
