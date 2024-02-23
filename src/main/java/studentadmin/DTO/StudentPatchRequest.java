package studentadmin.DTO;

public class StudentPatchRequest {
    private Boolean graduated;
    private Integer graduationYear;
    private Boolean prefect;
    private Integer schoolYear;
    private boolean graduationYearUpdated = false;

    public Boolean getGraduated() {
        return graduated;
    }

    public void setGraduated(Boolean graduated) {
        this.graduated = graduated;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Boolean getPrefect() {
        return prefect;
    }

    public void setPrefect(Boolean prefect) {
        this.prefect = prefect;
    }

    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    public boolean isGraduationYearUpdated() {
        return graduationYearUpdated;
    }

    public void setGraduationYearUpdated(boolean graduationYearUpdated) {
        this.graduationYearUpdated = graduationYearUpdated;
    }
}
