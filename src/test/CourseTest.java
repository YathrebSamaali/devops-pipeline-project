package com.example.gestionstationskii.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    @Test
    void testCourseAttributes() {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(3);
        course.setTypeCourse(TypeCourse.SNOWBOARD);
        course.setSupport(Support.SKIS);
        course.setPrice(150.0f);
        course.setTimeSlot(2);

        assertEquals(1L, course.getNumCourse());
        assertEquals(3, course.getLevel());
        assertEquals(TypeCourse.SNOWBOARD, course.getTypeCourse());
        assertEquals(Support.SKIS, course.getSupport());
        assertEquals(150.0f, course.getPrice());
        assertEquals(2, course.getTimeSlot());
    }

    @Test
    void testAllArgsConstructor() {
        Course course = new Course(
                1L,
                2,
                TypeCourse.SKI,
                Support.SNOW,
                120.0f,
                1,
                null
        );

        assertEquals(1L, course.getNumCourse());
        assertEquals(2, course.getLevel());
        assertEquals(TypeCourse.SKI, course.getTypeCourse());
        assertEquals(Support.SNOW, course.getSupport());
        assertEquals(120.0f, course.getPrice());
        assertEquals(1, course.getTimeSlot());
        assertNull(course.getRegistrations());
    }
}
