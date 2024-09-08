//package ro.msg.mobile_clone.rest.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.actuate.autoconfigure.wavefront.WavefrontProperties;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
////@RunWith(SpringRunner.class)
//@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//        classes = WavefrontProperties.Application.class)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-integrationtest.properties")
//public class UserControllerIT {
//
//    @Autowired
//    private MockMvc mvc;
//
////    @Autowired
////    private UserService userService;
//
////    @Test
//    public void givenEmployees_whenGetEmployees_thenStatus200()
//            throws Exception {
//
//        mvc.perform(get("/api/v1/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .param("page", "0")
//                        .param("size", "5"))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.totalPages").value(2))
//                .andExpect(jsonPath("$.totalElements").value(6))
//                .andExpect(jsonPath("$.size").value(5))
//                .andExpect(jsonPath("$.content[0].id").value(9))
//                .andExpect(jsonPath("$.content[0].firstName").value("Marius"));
//    }
//
//}
