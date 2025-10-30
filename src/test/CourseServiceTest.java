package com.example.gestionstationskii.services;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCourses() {
        Course c1 = new Course(1L, 1, TypeCourse.SKI, Support.SNOW, 100.0f, 1, null);
        Course c2 = new Course(2L, 2, TypeCourse.SNOWBOARD, Support.SNOW, 120.0f, 2, null);
        when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Course> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testAddCourse() {
        Course c = new Course(1L, 1, TypeCourse.SKI, Support.SNOW, 100.0f, 1, null);
        when(courseRepository.save(c)).thenReturn(c);

        Course result = courseService.addCourse(c);

        assertEquals(c, result);
        verify(courseRepository, times(1)).save(c);
    }
}
