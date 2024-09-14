package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc(addFilters = false)
public class ListingControllerIT {

    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/listings";

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(result -> result.getRequest().addHeader("x-api-key", "test123"))
                .build();
    }

    @BeforeEach
    public void setUp() throws Exception {
        // create a user
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
    }

    @Test
    public void testCreateListing() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
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
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1));
    }

    @Test
    public void testCreateListingWithInvalidUserId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""

                                {\
                            "userId": 2,\
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
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testCreateListingWithInvalidTransmission() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
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
                            "fuelType": "PETROL"\
                        }""")
                )
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void testCreateListingWithInvalidPrice() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
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
                        "fuelType": "PETROL"\
                        }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetAllListings() throws Exception {
        for (int i = 0; i < 6; i++) {
            testCreateListing();
        }
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL)
                        .param("page", "0")
                        .param("size", "5")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].id").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[5]").doesNotExist());
    }

    @Test
    public void testGetListingById() throws Exception {
        testCreateListing();
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void testGetListingByIdWithInvalidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateListing() throws Exception {
        testCreateListing();
        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/1")
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
                            "fuelType": "PETROL"\
                        }""")
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateListingWithInvalidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/2")
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
                            "fuelType": "PETROL"\
                            }""")
        )
        .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteListing() throws Exception {
        testCreateListing();
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteListingWithInvalidId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testSearchListings() throws Exception {
        testCreateListing();
        testCreateListing();
        testCreateListing();
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/search")
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
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(1));
    }
}
