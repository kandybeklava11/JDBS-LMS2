package java12.dao.impl;

import java12.config.JdbcConfig;
import java12.dao.JobDao;
import java12.models.Job;

import java.io.ObjectInputFilter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

public class JobDaoImpl implements JobDao {
    private final Connection connection=JdbcConfig.getConnection();
    @Override
    public void createJobTable() {
        String sql="create table Job(" +
                "id serial primary key," +
                "position varchar," +
                "profession varchar," +
                "description varchar," +
                "experience int);" ;
        try {
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully created!");
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void addJob(Job job) {
        String sql = "insert into Job(position, profession, description, experience) values (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, job.getPosition());
            statement.setString(2, job.getProfession());
            statement.setString(3, job.getDescription());
            statement.setInt(4, job.getExperience());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully added!");
            } else {
                System.out.println("No rows were affected.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding job: " + e.getMessage());
        }
    }


    @Override
    public Job getJobById(Long jobId) {
        Job job = null;
        String sql = "SELECT * FROM Job WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, jobId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    job = new Job();
                    job.setId(resultSet.getLong("id"));
                    job.setPosition(resultSet.getString("position"));
                    job.setProfession(resultSet.getString("profession"));
                    job.setDescription(resultSet.getString("description"));
                    job.setExperience(resultSet.getInt("experience"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return job;
    }

    @Override
    public List<Job> sortByExperience(String ascOrDesc) {
        String sortOrder = ascOrDesc.equalsIgnoreCase("asc") ? "asc" : "desc";
        String sql = "select description, position, profession, experience from Job order by experience " + sortOrder;
        List<Job> jobs = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Job job = new Job();
                job.setDescription(resultSet.getString("description"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setExperience(resultSet.getInt("experience"));
                jobs.add(job);
            }

            System.out.println("Successfully sorted!");
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return jobs;
    }


    @Override
    public Job getJobByEmployeeId(Long employeeId) {
        Job job = new Job();
        String sql = "select j.position, j.profession, j.description, j.experience " +
                "from Employee e " +
                "inner join Job j on e.job_id = j.id " +
                "where e.id = ? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                job.setExperience(resultSet.getInt("experience"));
                job.setPosition(resultSet.getString("position"));
                job.setProfession(resultSet.getString("profession"));
                job.setDescription(resultSet.getString("description"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return job;
    }

    @Override
    public void deleteDescriptionColumn() {
     String sql=" alter table Job " +
             "drop column description ;";
        try {
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            System.out.println("Successfully deleted!");
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
