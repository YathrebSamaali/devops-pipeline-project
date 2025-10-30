package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // CREATE
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    // READ
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // UPDATE
    public Course updateCourse(Long id, Course updatedCourse) {
        return courseRepository.findById(id)
                .map(existing -> {
                    existing.setLevel(updatedCourse.getLevel());
                    existing.setPrice(updatedCourse.getPrice());
                    existing.setSupport(updatedCourse.getSupport());
                    existing.setTypeCourse(updatedCourse.getTypeCourse());
                    existing.setTimeSlot(updatedCourse.getTimeSlot());
                    return courseRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    // DELETE
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    // READ ALL (bonus)
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
