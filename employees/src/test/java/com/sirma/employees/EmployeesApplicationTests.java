package com.sirma.employees;

import com.sirma.employees.dto.EmployeeResultDTO;
import com.sirma.employees.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EmployeesApplicationTests {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void contextLoads() {
    }

    @Test
    void testFileUploadFromResources() throws IOException, URISyntaxException {

        Path filePath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test.csv")).toURI());
        byte[] content = Files.readAllBytes(filePath);

        MultipartFile mockFile = new MockMultipartFile("file", "test.csv", "text/csv", content);

        EmployeeResultDTO result = employeeService.generateReport(mockFile);

        assertEquals(143, result.getEmpId());
        assertEquals(145, result.getCollegueId());
        assertEquals(730, result.getDays());
    }

}
