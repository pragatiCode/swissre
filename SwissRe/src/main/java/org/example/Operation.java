package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Operation {
    public List<Employee> readFile(String filePath) {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) { // Ensure the row has at least 5 columns
                    System.err.println("Skipping invalid row: " + line);
                    continue;
                }
                int id = Integer.parseInt(parts[0]);
                String firstName = parts[1];
                String lastName = parts[2];
                double salary = Double.parseDouble(parts[3]);
                Integer managerId = parts[4].isEmpty() ? null : Integer.parseInt(parts[4]);
                employees.add(new Employee(id, firstName, lastName, salary, managerId));
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return employees;
    }
    public Map<Integer, List<Employee>> groupByManager(List<Employee> employees) {
        Map<Integer, List<Employee>> managerToReportees = new HashMap<>();
        for (Employee employee : employees) {
            Integer managerId = employee.getManagerId();
            if (managerId != null) {
                managerToReportees.putIfAbsent(managerId, new ArrayList<>());
                managerToReportees.get(managerId).add(employee);
            }
        }
        return managerToReportees;
    }
    public void findManagersWithSalaryCondition(List<Employee> employees) {
        Map<Integer, List<Employee>> managerToReportees = groupByManager(employees);

        for (Map.Entry<Integer, List<Employee>> entry : managerToReportees.entrySet()) {
            Integer managerId = entry.getKey();
            List<Employee> reportees = entry.getValue();

            // Calculate the average salary of the reportees
            double totalSalary = 0;
            for (Employee reportee : reportees) {
                totalSalary += reportee.getSalary();
            }
            double averageSalary = totalSalary / reportees.size();

            // Find the manager
            Employee manager = employees.stream()
                    .filter(emp -> emp.getId() == managerId)
                    .findFirst()
                    .orElse(null);

            if (manager != null) {
                double managerSalary = manager.getSalary();
                // Check if the manager's salary is at least 20% more but no more than 50% more
                if (managerSalary >= 1.2 * averageSalary && managerSalary <= 1.5 * averageSalary) {
                    System.out.println("Manager ID: " + managerId + " meets the condition.");
                    System.out.println("Manager: " + manager);
                    System.out.println("Average Salary of Reportees: " + averageSalary);
                    System.out.println("Difference Salary of Reportees & Manager: " + (managerSalary-averageSalary));
                }
            }
        }
    }

    public void findManagerHavingMoreReportees(List<Employee> employees) {
        Map<Integer, List<Employee>> managerToReportees = groupByManager(employees);
        for (Map.Entry<Integer, List<Employee>> entry : managerToReportees.entrySet()) {
            Integer managerId = entry.getKey();
            List<Employee> reportees = entry.getValue();

            if (reportees.size() > 4) {
                // Find the manager
                Employee manager = employees.stream()
                        .filter(emp -> emp.getId() == managerId)
                        .findFirst()
                        .orElse(null);

                if (manager != null) {
                    System.out.println("Manager ID: " + managerId + " has more than 4 reportees.");
                    System.out.println("Manager: " + manager);
                    System.out.println("Reportees: " + reportees);
                }
            }
        }
    }
}