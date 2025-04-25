package eu.gaiax.sd.rest;

import eu.gaiax.sd.service.SdService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
public class SelfDescriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SelfDescriptionController service;
    @MockBean
    private SdService sdService;

    @Test
    void convert() throws Exception {
        ResponseEntity rs = ResponseEntity.ok().build();
        when(service.convert(any(), any(), any())).thenReturn(rs);
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/sd-service/convert?descriptor_type=type")
                        .file(firstFile))
                .andExpect(status().isOk());
    }

    @Test
    void createServices() throws Exception {
        ResponseEntity rs = ResponseEntity.ok().build();
        when(service.createServices(any(), any(), any())).thenReturn(rs);
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/sd-service/services")
                        .file(firstFile))
                .andExpect(status().isOk());
    }

    @Test
    void createData() throws Exception {
        ResponseEntity rs = ResponseEntity.ok().build();
        when(service.createData(any(), any(), any())).thenReturn(rs);
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/sd-service/data")
                        .file(firstFile))
                .andExpect(status().isOk());
    }

    @Test
    void createNodes() throws Exception {
        ResponseEntity rs = ResponseEntity.ok().build();
        when(service.createNodes(any(), any(), any())).thenReturn(rs);
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        mockMvc.perform(
                MockMvcRequestBuilders
                        .multipart("/api/sd-service/nodes")
                        .file(firstFile))
                .andExpect(status().isOk());
    }

    @Test
    void updateServices() throws Exception {
        ResponseEntity rs = ResponseEntity.ok().build();
        when(service.updateServices(any(), any(), any(), any())).thenReturn(rs);
        MockMultipartFile firstFile = new MockMultipartFile("file", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/sd-service/services/12");
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        mockMvc.perform(builder
                        .file(firstFile))
                .andExpect(status().isOk());
    }
}
