package studentadmin.DTO.CourseDTO;

import java.util.List;

public class AddStudentsToCourseDTO {
    private List<StudentIdentifier> students;

    public List<StudentIdentifier> getStudents() {
        return students;
    }

    public void setStudents(List<StudentIdentifier> students) {
        this.students = students;
    }

    public static class StudentIdentifier {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
