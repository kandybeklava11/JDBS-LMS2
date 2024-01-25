package java12.dao;

import java12.models.Employee;
import java12.models.Job;

import java.util.List;
import java.util.Map;

public interface EmployeeDao {
    void createEmployee();
    void addEmployee(Employee employee);
    void dropTable();
    void cleanTable();
    void updateEmployee(Long id,Employee employee);
    List<Employee> getAllEmployees();
    Employee findByEmail(String email);
    Map<Employee, Job> getEmployeeById(Long employeeId);
    List<Employee> getEmployeeByPosition(String position);
}
