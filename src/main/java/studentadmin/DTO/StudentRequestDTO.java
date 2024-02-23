package studentadmin.DTO;

import java.time.LocalDate;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonSetter;

public class StudentRequestDTO {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String house; // House name som streng
    private boolean prefect;
    private int enrollmentYear;
    private Integer graduationYear;
    private boolean graduated;
    private int schoolYear;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


      @JsonSetter("name")
    public void setName(String fullName) {
        // split("\\s+"") opdeler streng ved 1 eller flere mellemrum
        String[] parts = fullName.trim().split("\\s+");
        this.firstName = parts[0];
        this.lastName = parts[parts.length - 1];
        if (parts.length > 2) {
            this.middleName = String.join(" ", Arrays.copyOfRange(parts, 1, parts.length - 1));
        } else {
            this.middleName = "";
        }
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public boolean isPrefect() {
        return prefect;
    }

    public void setPrefect(boolean prefect) {
        this.prefect = prefect;
    }

    public int getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(int enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public boolean isGraduated() {
        return graduated;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    public int getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(int schoolYear) {
        this.schoolYear = schoolYear;
    }
}
