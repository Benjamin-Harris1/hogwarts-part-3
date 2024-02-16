package studentadmin.controllers;

import org.springframework.web.bind.annotation.RestController;
import studentadmin.repositories.CourseRepository;

@RestController
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
}
