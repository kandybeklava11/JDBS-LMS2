package java12.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString

public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private Long job_id;

    public Employee(Long id, String firstName, String lastName, int age, String email, Long job_id) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.job_id = job_id;
    }

    public Employee(String firstName, String lastName, int age, String email, Long job_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.job_id = job_id;
    }
}
