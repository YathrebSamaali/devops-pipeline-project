package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course(1L, 2, TypeCourse.SKI, Support.SNOW, 120.0f, 3, null);
    }

    // 🔹 1. CREATE
    @Test
    void testAddCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseService.addCourse(course);

        assertNotNull(result);
        assertEquals(TypeCourse.SKI, result.getTypeCourse());
        verify(courseRepository, times(1)).save(course);
    }

    // 🔹 2. READ
    @Test
    void testGetCourseById() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Optional<Course> result = courseService.getCourseById(1L);

        assertTrue(result.isPresent());
        assertEquals(120.0f, result.get().getPrice());
        verify(courseRepository, times(1)).findById(1L);
    }

    // 🔹 3. UPDATE
    @Test
    void testUpdateCourse() {
        Course updatedCourse = new Course(1L, 3, TypeCourse.SNOWBOARD, Support.SKIS, 200.0f, 5, null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(updatedCourse);

        Course result = courseService.updateCourse(1L, updatedCourse);

        assertEquals(3, result.getLevel());
        assertEquals(TypeCourse.SNOWBOARD, result.getTypeCourse());
        assertEquals(200.0f, result.getPrice());
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    // 🔹 4. DELETE
    @Test
    void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }
}
