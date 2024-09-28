package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
public class ListingControllerIT {

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/listings";
    private static final String INSERT_USER_QUERY = "INSERT INTO users (id, first_name, last_name, email, phone) " +
            "VALUES (1, 'Cristian', 'Tiut', 'tiutcristian@gmail.com', '0721644423')";
    private static final String INSERT_LISTING_QUERY1 = "INSERT INTO listings (id, user_id, title, price, make, model, description, manufacture_year, mileage, engine_size, horsepower, transmission, fuel_type) " +
            "VALUES (1, 1, 'Title', 2000, 'Toyota', 'Auris', 'Some description here', 2000, 2000000, 2000, 100, 'MANUAL', 'PETROL')";
    private static final String INSERT_LISTING_QUERY2 = "INSERT INTO listings (id, user_id, title, price, make, model, description, manufacture_year, mileage, engine_size, horsepower, transmission, fuel_type) " +
            "VALUES (2, 1, 'Title', 2000, 'Toyota', 'Auris', 'Some description here', 2000, 2000000, 2000, 100, 'MANUAL', 'PETROL')";
    private static final String INSERT_LISTING_QUERY3 = "INSERT INTO listings (id, user_id, title, price, make, model, description, manufacture_year, mileage, engine_size, horsepower, transmission, fuel_type) " +
            "VALUES (3, 1, 'Title', 2000, 'Toyota', 'Auris', 'Some description here', 2000, 2000000, 2000, 100, 'MANUAL', 'PETROL')";

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = webAppContextSetup(context)
                .alwaysDo(result -> result.getRequest().addHeader("x-api-key", "test123"))
                .build();
    }

    @Test
    @Sql(statements = INSERT_USER_QUERY, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateListing() throws Exception {
        this.mockMvc.perform(post(BASE_URL + "/create")
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
                            "fuelType": "PETROL"
                        }""")
                )
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    public void testCreateListingWithInvalidUserId() throws Exception {
        this.mockMvc.perform(post(BASE_URL + "/create")
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
                            "fuelType": "PETROL"
                        }""")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = INSERT_USER_QUERY, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateListingWithInvalidTransmission() throws Exception {
        this.mockMvc.perform(post(BASE_URL + "/create")
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
                            "transmission": "INVALID",\
                            "fuelType": "PETROL"
                        }""")
                )
                .andExpect(status().isInternalServerError());
    }

    @Test
    @Sql(statements = INSERT_USER_QUERY, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateListingWithInvalidPrice() throws Exception {
        this.mockMvc.perform(post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {\
                        "userId": 1,\
                        "title": "Title",\
                        "price": -2000,\
                        "make": "Toyota",\
                        "model": "Auris",\
                        "description": "Some description here",\
                        "year": 2000,\
                        "mileage": 2000000,\
                        "engineSize": 2000,\
                        "horsepower": 100,\
                        "transmission": "MANUAL",\
                        "fuelType": "PETROL"
                        }""")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY1, INSERT_LISTING_QUERY2, INSERT_LISTING_QUERY3},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllListings() throws Exception {
        this.mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "2")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2]").doesNotExist())
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY1}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetListingById() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetListingByIdWithInvalidId() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY1}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateListing() throws Exception {
        this.mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {\
                            "id": 1,\
                            "userId": 1,\
                            "title": "Title",\
                            "price": 2000,\
                            "make": "Toyota",\
                            "model": "Auris",\
                            "description": "Some other description here",\
                            "year": 2000,\
                            "mileage": 2000000,\
                            "engineSize": 2000,\
                            "horsepower": 100,\
                            "transmission": "MANUAL",\
                            "fuelType": "PETROL"
                        }""")
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateListingWithInvalidId() throws Exception {
        this.mockMvc.perform(put(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {\
                            "id": 2,\
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
                            "fuelType": "PETROL"
                            }""")
        )
        .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY1}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteListing() throws Exception {
        this.mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteListingWithInvalidId() throws Exception {
        this.mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY, INSERT_LISTING_QUERY1, INSERT_LISTING_QUERY2, INSERT_LISTING_QUERY3},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testSearchListings() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/search")
                .param("make", "Toyota")
                .param("model", "Auris")
                .param("mileage", "2000000")
                .param("engineSize", "2000")
                .param("horsepower", "100")
                .param("transmission", "MANUAL")
                .param("fuelType", "PETROL")
                .param("page", "0")
                .param("size", "5")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].id").value(1));
    }
}
