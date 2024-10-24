package com.sirma.employees.dto;

import java.util.Map;
import java.util.Set;

public record EmployeeAggregation(Map<Integer, Set<EmployeeDTO>> employeesByEmpId,
                                  Map<Integer, Set<EmployeeDTO>> employeesByProjectId) {
}
