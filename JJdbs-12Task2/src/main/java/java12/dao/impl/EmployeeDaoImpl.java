package java12.dao.impl;

import java12.config.JdbcConfig;
import java12.dao.EmployeeDao;
import java12.models.Employee;
import java12.models.Job;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.postgresql.hostchooser.HostRequirement.primary;

public class EmployeeDaoImpl implements EmployeeDao {
 private final Connection connection= JdbcConfig.getConnection();
    @Override
    public void createEmployee() {
        String sql = """
            create table Employee(
            id serial primary key,
            first_name varchar,
            last_name varchar,
            age int,
            email varchar unique,
            job_id int references Job(id)
            ) ;
            """;
        Statement statement;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully created!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addEmployee(Employee employee) {
        PreparedStatement preparedStatement;
     String sql= """
             insert into Employee(first_name,last_name,age,email,job_id)
             values(?,?,?,?,?) ;
             """;

        try {
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,employee.getFirstName());
            preparedStatement.setString(2,employee.getLastName());
            preparedStatement.setInt(3,employee.getAge());
            preparedStatement.setString(4,employee.getEmail());
            preparedStatement.setLong(5,employee.getJob_id());
            int check=preparedStatement.executeUpdate();

            if(check>0){
                System.out.println("Successfully added!");
            }else {
                System.out.println("Failed to add!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropTable() {
    String sql= """
            drop table Employee ;
            """;
        try {
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully dropped!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanTable() {
    String sql= """
            delete  from Employee ;
            """;
        try {
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully cleaned!");
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEmployee(Long id, Employee employee) {
        PreparedStatement preparedStatement;
        String sql = """
            update Employee 
            set first_name = ?,
            last_name = ?,
            age = ?,
            email = ?,
            job_id = ?
            where id = ? ;
            """;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setInt(3, employee.getAge());
            preparedStatement.setString(4, employee.getEmail());
            preparedStatement.setLong(5, employee.getJob_id());
            preparedStatement.setLong(6, id);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Successfully updated!");
            } else {
                System.out.println("Failed to update  with ID " + id );
            }

            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT * FROM Employee ;";

        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("Id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJob_id(resultSet.getLong("job_id"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }



    @Override
    public Employee findByEmail(String email) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Employee employee = new Employee();
        try {
            preparedStatement = connection.prepareStatement("select * from Employee where email = ? ;");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee.setId(resultSet.getLong("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJob_id(resultSet.getLong("job_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return employee;
    }

    @Override
    public Map<Employee, Job> getEmployeeById(Long employeeId) {
        Map<Employee, Job> map = new LinkedHashMap<>();

        String sql = "SELECT e.id AS e_id, e.first_name, e.last_name, e.age, e.email," +
                "       j.id AS j_id, j.position, j.profession, j.experience, j.description " +
                "FROM Employee e " +
                "INNER JOIN Job j ON e.job_id = j.id " +
                "WHERE e.id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setId(resultSet.getLong("e_id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setAge(resultSet.getInt("age"));
                employee.setEmail(resultSet.getString("email"));
                employee.setJob_id(resultSet.getLong("j_id"));

                Job job = new Job();
                job.setId(resultSet.getLong("j_id"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setExperience(resultSet.getInt("experience"));
                job.setDescription(resultSet.getString("description"));

                map.put(employee, job);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    @Override
    public List<Employee> getEmployeeByPosition(String position) {

        List<Employee> employees = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String sql=" select e.id, e.first_name, e.last_name, e.age, e.email, e.job_id" +
                "            from Employee e" +
                "            inner join Job j on e.job_id = j.id" +
                "            where j.position = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, position);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                employees.add(new Employee(
                        resultSet.getLong("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getInt("age"),
                        resultSet.getString("email"),
                        resultSet.getLong("job_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return employees;
    }
}
