package com.sirma.employees.service;

import com.sirma.employees.dto.EmployeeAggregation;
import com.sirma.employees.dto.EmployeeDTO;
import com.sirma.employees.dto.EmployeeResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final CsvService csvService;

    public EmployeeResultDTO generateReport(MultipartFile file) {
        EmployeeAggregation generatedAggregation = csvService.parseCsv(file);

        Map<Integer, Set<EmployeeDTO>> setByEmpId = generatedAggregation.employeesByEmpId();
        Map<Integer, Set<EmployeeDTO>> setByProjectId = generatedAggregation.employeesByProjectId();

        EmployeeResultDTO result = new EmployeeResultDTO();
        result.setDays(0L);

        for (Integer empId : setByEmpId.keySet()) {
            Set<EmployeeDTO> employeeDTOS = setByEmpId.get(empId);
            Map<String, Long> resultMap = new HashMap<>();

            employeeDTOS.forEach(employee -> {
                Set<EmployeeDTO> collegueSet = setByProjectId.get(employee.getProjectId());
                if (collegueSet != null) {
                    collegueSet.stream().filter(collegue -> !collegue.getEmpId().equals(empId)).forEach(collegue -> {
                        Long days = calculateOverlapDays(employee, collegue);
                        String key = empId < collegue.getEmpId()
                                ? empId + "+" + collegue.getEmpId() + "+" + collegue.getProjectId()
                                : collegue.getEmpId() + "+" + empId + "+" + collegue.getProjectId();
                        if (resultMap.containsKey(key)) {
                            Long currentDays = resultMap.get(key) + days;
                            resultMap.put(key, currentDays);
                        } else {
                            resultMap.put(key, days);
                        }
                        Long totalDays = resultMap.get(key);
                        if (result.getDays() <= totalDays) {
                            result.setDays(totalDays);
                            String[] employees = key.split("\\+");
                            result.setEmpId(Integer.parseInt(employees[0]));
                            result.setCollegueId(Integer.parseInt(employees[1]));
                        }
                    });
                }
            });

        }
        return result;
    }

    private Long calculateOverlapDays(EmployeeDTO employee, EmployeeDTO collegue) {
        LocalDate startDate1 = employee.getDateFrom();
        LocalDate endDate1 = employee.getDateTo();

        LocalDate startDate2 = collegue.getDateFrom();
        LocalDate endDate2 = collegue.getDateTo();

        LocalDate overlapStart = startDate1.isAfter(startDate2) ? startDate1 : startDate2;
        LocalDate overlapEnd = endDate1.isBefore(endDate2) ? endDate1 : endDate2;

        if (!overlapStart.isAfter(overlapEnd)) {
            return ChronoUnit.DAYS.between(overlapStart, overlapEnd) + 1;
        } else {
            return 0L;
        }
    }
}
