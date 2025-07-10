package org.example;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        Operation operation = new Operation();
        List<Employee> employeeList = operation.readFile("src/main/resources/employee.csv");
        operation.findManagersWithSalaryCondition(employeeList);
        operation.findManagerHavingMoreReportees(employeeList);
    }
}