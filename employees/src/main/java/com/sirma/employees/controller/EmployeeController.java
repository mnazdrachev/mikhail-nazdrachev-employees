package com.sirma.employees.controller;

import com.sirma.employees.dto.EmployeeResultDTO;
import com.sirma.employees.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping(value = "/upload")
    public EmployeeResultDTO uploadFile(@RequestBody MultipartFile file) {
        return employeeService.generateReport(file);
    }
}
