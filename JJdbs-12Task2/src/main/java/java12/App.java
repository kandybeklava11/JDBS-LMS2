package java12;

import java12.models.Employee;
import java12.models.Job;
import java12.services.EmployeeService;
import java12.services.JobService;
import java12.services.impl.EmployeeServiceImpl;
import java12.services.impl.JobServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        EmployeeService employeeDAO = new EmployeeServiceImpl();
        JobService jobDAO = new JobServiceImpl();

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Choose an option:");
            System.out.println("1. Create Employee table");
            System.out.println("2. Add Employee");
            System.out.println("3. Drop Employee table");
            System.out.println("4. Clean Employee table");
            System.out.println("5. Update Employee");
            System.out.println("6. Get all Employees");
            System.out.println("7. Find Employee by email");
            System.out.println("8. Get Employee and Job by Employee ID");
            System.out.println("9. Get Employees by Position");
            System.out.println("10. Create Job table");
            System.out.println("11. Add Job");
            System.out.println("12. Get Job by ID");
            System.out.println("13. Sort Jobs by Experience");
            System.out.println("14. Get Job by Employee ID");
            System.out.println("15. Delete Description Column");
            System.out.println("0. Exit");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    employeeDAO.createEmployee();
                    break;
                case 2:
                    System.out.println("Enter employee details:");
                    System.out.print("First Name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Last Name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Job ID: ");
                    long jobId = scanner.nextLong();
                    scanner.nextLine();

                    Employee employee = new Employee(firstName, lastName, age, email, jobId);
                    employeeDAO.addEmployee(employee);
                    break;
                case 3:
                    employeeDAO.dropTable();
                    break;
                case 4:
                    employeeDAO.cleanTable();
                    break;
                case 5:
                    System.out.print("Enter employee ID to update: ");
                    long id = scanner.nextLong();
                    scanner.nextLine();

                    System.out.println("Enter updated employee details:");
                    System.out.print("First Name: ");
                    firstName = scanner.nextLine();
                    System.out.print("Last Name: ");
                    lastName = scanner.nextLine();
                    System.out.print("Age: ");
                    age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Email: ");
                    email = scanner.nextLine();
                    System.out.print("Job ID: ");
                    jobId = scanner.nextLong();
                    scanner.nextLine();

                    employee = new Employee(firstName, lastName, age, email, jobId);
                    employeeDAO.updateEmployee(id, employee);
                    break;
                case 6:
                    List<Employee> employees = employeeDAO.getAllEmployees();
                    for (Employee emp : employees) {
                        System.out.println(emp);
                    }
                    break;
                case 7:
                    System.out.print("Enter email to search: ");
                    email = scanner.nextLine();
                    Employee foundEmployee = employeeDAO.findByEmail(email);
                    System.out.println(foundEmployee);
                    break;
                case 8:
                    System.out.print("Enter Employee ID: ");
                    long employeeId = scanner.nextLong();
                    scanner.nextLine();

                    Map<Employee, Job> employeeJobMap = employeeDAO.getEmployeeById(employeeId);
                    for (Map.Entry<Employee, Job> entry : employeeJobMap.entrySet()) {
                        System.out.println("Employee: " + entry.getKey());
                        System.out.println("Job: " + entry.getValue());
                    }
                    break;
                case 9:
                    System.out.print("Enter Position: ");
                    String position = scanner.nextLine();

                    List<Employee> employeesByPosition = employeeDAO.getEmployeeByPosition(position);
                    for (Employee emp : employeesByPosition) {
                        System.out.println(emp);
                    }
                    break;
                case 10:
                    jobDAO.createJobTable();
                    break;
                case 11:
                    System.out.println("Enter job details:");
                    System.out.print("Position: ");
                    String jobPosition = scanner.nextLine();
                    System.out.print("Profession: ");
                    String profession = scanner.nextLine();
                    System.out.print("Description: ");
                    String description = scanner.nextLine();
                    System.out.print("Experience: ");
                    int experience = scanner.nextInt();
                    scanner.nextLine();

                    Job job = new Job(jobPosition, profession, description, experience);
                    jobDAO.addJob(job);
                    break;
                case 12:
                    System.out.print("Enter JobID: ");
                    long jobbId = scanner.nextLong();
                    scanner.nextLine();

                    Job foundJob = jobDAO.getJobById(jobbId);
                    System.out.println(foundJob);
                    break;
                case 13:
                    System.out.println("Enter asc or desc");
                    String choice =scanner.nextLine();
                    jobDAO.sortByExperience(choice);

                    break;
                case 14:
                    System.out.print("Enter Employee ID: ");
                    long empId = scanner.nextLong();
                    scanner.nextLine();

                    Job jobByEmployeeId = jobDAO.getJobByEmployeeId(empId);
                    System.out.println(jobByEmployeeId);
                    break;
                case 15:
                    jobDAO.deleteDescriptionColumn();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
