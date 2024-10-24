package com.sirma.employees.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EmployeeDTO {

    private Integer empId;
    private Integer projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;
}
