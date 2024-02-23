package studentadmin.DTO.TeacherDTO;

import studentadmin.models.EmpType;

import java.time.LocalDate;

public class TeacherPatchDTO {
    private Boolean headOfHouse;
    private LocalDate employmentEnd;
    private EmpType employment;
    private boolean employmentEndUpdated = false;

    public Boolean getHeadOfHouse() {
        return headOfHouse;
    }

    public void setHeadOfHouse(Boolean headOfHouse) {
        this.headOfHouse = headOfHouse;
    }

    public LocalDate getEmploymentEnd() {
        return employmentEnd;
    }

    public void setEmploymentEnd(LocalDate employmentEnd) {
        this.employmentEnd = employmentEnd;
    }

    public EmpType getEmployment() {
        return employment;
    }

    public void setEmployment(EmpType employment) {
        this.employment = employment;
    }

    public boolean isEmploymentEndUpdated() {
        return employmentEndUpdated;
    }

    public void setEmploymentEndUpdated(boolean employmentEndUpdated) {
        this.employmentEndUpdated = employmentEndUpdated;
    }
}
