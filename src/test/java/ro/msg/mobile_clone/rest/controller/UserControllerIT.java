package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/users";

    @Autowired
    private WebApplicationContext context;

    @PostConstruct
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(result -> result.getRequest().addHeader("x-api-key", "test123"))
                .build();
    }

    private void createUser(String firstName, String lastName, String email, String phone)
            throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "\"firstName\": \"" + firstName + "\"," +
                        "\"lastName\": \"" + lastName + "\"," +
                        "\"email\": \"" + email + "\"," +
                        "\"phone\": \"" + phone + "\"" +
                        "}")
        );
    }

    @Test
    public void testCreateUser() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {\
                                "firstName": "Cristian",\
                                "lastName": "Tiut",\
                                "email": "tiutcristian@gmail.com",\
                                "phone": "0721644423"\
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Cristian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Tiut"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("tiutcristian@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0721644423"));
    }

    @Test
    public void testCreateUserMissingEmail() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {\
                                "firstName": "Alice",\
                                "lastName": "Smith",\
                                "phone": "0731567890"\
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateUserRepeatedPhone() throws Exception {
        createUser("Cristian", "Tiut", "cv@sth.com", "0721644423");
        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {\
                                "firstName": "John",\
                                "lastName": "Doe",\
                                "email": "john@doe.com",\
                                "phone": "0721644423"\
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testGetAllUsers() throws Exception {

        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");
        createUser("John", "Doe", "john.doe@example.com", "0729876543");
        createUser("Emily", "Johnson", "emily.johnson@example.com", "0741234567");
        createUser("Michael", "Brown", "michael.brown@example.com", "0750987654");
        createUser("Sophia", "Davis", "sophia.davis@example.com", "0763456789");

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get(BASE_URL)
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Cristian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Alice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[3].firstName").value("Emily"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[4].firstName").value("Michael"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[5]").doesNotExist());
    }

    @Test
    public void testGetUserById() throws Exception {
        // creating some users
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");
        createUser("John", "Doe", "john.doe@example.com", "0729876543");

        // creating the user to be retrieved
        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        String location = BASE_URL + "/3"; // id of the user to be retrieved is 3

        // creating some more users
        createUser("Emily", "Johnson", "emily.johnson@example.com", "0741234567");
        createUser("Michael", "Brown", "michael.brown@example.com", "0750987654");
        createUser("Sophia", "Davis", "sophia.davis@example.com", "0763456789");

        this.mockMvc.perform(MockMvcRequestBuilders.get(location))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0721644423"));
        // using phone as a check because it is unique
    }

    @Test
    public void testUpdateUser() throws Exception {
        // creating some users
        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");

        // updating the user with id 2
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {\
                                "firstName": "John",\
                                "lastName": "Doe",\
                                "email": "alice.smith@example.com",\
                                "phone": "0731567890"\
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andReturn();

        // Getting the location of the updated user
        String location = result.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);

        // checking the updated user
        this.mockMvc.perform(MockMvcRequestBuilders.get(location))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("alice.smith@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0731567890"));
    }

    @Test
    public void testUpdateUserMissingEmail() throws Exception {
        // creating some users
        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");

        // updating the user with id 2
        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {\
                                "firstName": "John",\
                                "lastName": "Doe",\
                                "phone": "0731567890"\
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUpdateUserRepeatedPhone() throws Exception {
        // creating some users
        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");

        // updating the user with id 2
        this.mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {\
                                "id": 2,\
                                "firstName": "John",\
                                "lastName": "Doe",\
                                "email": "alice.smith@example.com",\
                                "phone": "0721644423"\
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDeleteUser() throws Exception {
        // creating some users
        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");

        // deleting the user with id 2
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // checking if the user was deleted
        this.mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        // creating some users
        createUser("Cristian", "Tiut", "tiutcristian@gmail.com", "0721644423");
        createUser("Alice", "Smith", "alice.smith@example.com", "0731567890");

        // trying to delete a non-existent user
        this.mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}