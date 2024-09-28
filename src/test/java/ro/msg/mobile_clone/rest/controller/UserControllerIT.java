package ro.msg.mobile_clone.rest.controller;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final String BASE_URL = "/api/v1/users";
    private static final String INSERT_USER_QUERY1 = "INSERT INTO users (first_name, last_name, email, phone)" +
            " VALUES ('Cristian', 'Tiut', 'tiutcristian@gmail.com', '0721644423')";
    private static final String INSERT_USER_QUERY2 = "INSERT INTO users (first_name, last_name, email, phone)" +
            " VALUES ('Alice', 'Smith', 'alice.smith@example.com', '0731567890')";
    private static final String INSERT_USER_QUERY3 = "INSERT INTO users (first_name, last_name, email, phone)" +
            " VALUES ('John', 'Doe', 'john.doe@example.com', '0729876543')";

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

        this.mockMvc.perform(post(BASE_URL + "/create")
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
        this.mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "Cristian",
                                "lastName": "Tiut",
                                "email": "tiutcristian@gmail.com",
                                "phone": "0721644423"
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
        this.mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "Alice",
                                "lastName": "Smith",
                                "phone": "0731567890"
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Sql(statements = INSERT_USER_QUERY1, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateUserRepeatedPhone() throws Exception {
        mockMvc.perform(post(BASE_URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "john@doe.com",
                                "phone": "0721644423"
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY1, INSERT_USER_QUERY2, INSERT_USER_QUERY3},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllUsers() throws Exception {
        this.mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value("Cristian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].firstName").value("Alice"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2]").doesNotExist());
    }
// =================== aici am ajuns
    @Test
    @Sql(statements = {INSERT_USER_QUERY2, INSERT_USER_QUERY3, INSERT_USER_QUERY1},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUserById() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0721644423"));
        // using phone as a check because it is unique
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY1, INSERT_USER_QUERY2},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateUser() throws Exception {
        // updating the user with id 2
        MvcResult result = this.mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "alice.smith@example.com",
                                "phone": "0731567890"
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andReturn();

        // Getting the location of the updated user
        String location = result.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);

        // checking the updated user
        this.mockMvc.perform(get(location))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("alice.smith@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("0731567890"));
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY1, INSERT_USER_QUERY2},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateUserMissingEmail() throws Exception {
        // updating the user with id 2
        this.mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "firstName": "John",
                                "lastName": "Doe",
                                "phone": "0731567890"
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY1, INSERT_USER_QUERY2},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateUserRepeatedPhone() throws Exception {
        // updating the user with id 2
        this.mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": 2,
                                "firstName": "John",
                                "lastName": "Doe",
                                "email": "alice.smith@example.com",
                                "phone": "0721644423"
                                }""")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY1, INSERT_USER_QUERY2},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteUser() throws Exception {
        // deleting the user with id 2
        this.mockMvc.perform(delete(BASE_URL + "/2"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // checking if the user was deleted
        this.mockMvc.perform(get(BASE_URL + "/2"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Sql(statements = {INSERT_USER_QUERY1, INSERT_USER_QUERY2},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteUserNotFound() throws Exception {
        // trying to delete a non-existent user
        this.mockMvc.perform(delete(BASE_URL + "/3"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}