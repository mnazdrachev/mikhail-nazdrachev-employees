package com.sirma.employees.service;

import com.opencsv.CSVReader;
import com.sirma.employees.dto.EmployeeAggregation;
import com.sirma.employees.dto.EmployeeDTO;
import com.sirma.employees.exception.CsvParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class CsvService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public EmployeeAggregation parseCsv(MultipartFile file) {
        Map<Integer, Set<EmployeeDTO>> employeesByEmpId = new HashMap<>();
        Map<Integer, Set<EmployeeDTO>> employeesByProjectId = new HashMap<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                int empId = Integer.parseInt(line[0].trim());
                int projectId = Integer.parseInt(line[1].trim());
                LocalDate dateFrom = LocalDate.parse(line[2].trim(), FORMATTER);
                LocalDate dateTo = line[3].trim().equals("NULL") ? LocalDate.now() : LocalDate.parse(line[3].trim(), FORMATTER);

                Set<EmployeeDTO> setByEmpId;
                if (employeesByEmpId.containsKey(empId)) {
                    setByEmpId = employeesByEmpId.get(empId);
                } else {
                    setByEmpId = new HashSet<>();
                }
                setByEmpId.add(new EmployeeDTO(empId, projectId, dateFrom, dateTo));
                employeesByEmpId.put(empId, setByEmpId);

                Set<EmployeeDTO> setByProjectId;
                if (employeesByProjectId.containsKey(projectId)) {
                    setByProjectId = employeesByProjectId.get(projectId);
                } else {
                    setByProjectId = new HashSet<>();
                }
                setByProjectId.add(new EmployeeDTO(empId, projectId, dateFrom, dateTo));
                employeesByProjectId.put(projectId, setByProjectId);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CsvParseException();
        }

        return new EmployeeAggregation(employeesByEmpId, employeesByProjectId);
    }
}
