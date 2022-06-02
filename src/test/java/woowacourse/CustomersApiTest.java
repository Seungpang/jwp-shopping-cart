package woowacourse;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CustomersApiTest extends TestSupport {

    @Test
    void addCustomers_test() throws Exception {
        mockMvc.perform(
                post("/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readJson("/json/customers/customers-create.json"))
            )
            .andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("loginId").description("loginId"),
                        fieldWithPath("name").description("name"),
                        fieldWithPath("password").description("password")
                    ),
                    responseFields(
                        fieldWithPath("loginId").description("loginId"),
                        fieldWithPath("name").description("name")
                    )
                )
            );
    }

    @Test
    void getMe_test() throws Exception {
        mockMvc.perform(
                get("/customers/me")
                    .header("Authorization", "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName("Authorization").description("Authorization")
                    ),
                    responseFields(
                        fieldWithPath("loginId").description("loginId"),
                        fieldWithPath("name").description("name")
                    )
                )
            );
    }

    @Test
    void updateMe_test() throws Exception {
        mockMvc.perform(
                put("/customers/me")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readJson("/json/customers/customers-update.json"))
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName("Authorization").description("Authorization")
                    ),
                    requestFields(
                        fieldWithPath("loginId").description("loginId"),
                        fieldWithPath("name").description("name"),
                        fieldWithPath("password").description("password")
                    ),
                    responseFields(
                        fieldWithPath("loginId").description("loginId"),
                        fieldWithPath("name").description("name")
                    )
                )
            );

    }

    @Test
    void deleteMe_test() throws Exception {
        mockMvc.perform(
                delete("/customers/me")
                    .header("Authorization",
                        "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(readJson("/json/customers/customers-delete.json"))
            )
            .andExpect(status().isNoContent())
            .andDo(
                restDocs.document(
                    requestHeaders(
                        headerWithName("Authorization").description("Authorization")
                    ),
                    requestFields(
                        fieldWithPath("password").description("password")
                    )
                )
            );
    }
}
