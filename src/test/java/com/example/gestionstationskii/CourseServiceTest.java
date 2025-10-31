package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.services.ICourseServices;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseServiceTest {

    @Autowired
    private ICourseServices courseService;

    private static Course savedCourse;

    @Test
    @Order(1)
    public void testAddCourse() {
        log.info("🔹 Test: addCourse()");

        Course course = new Course();
        course.setLevel(3);
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // ✅ corrigé
        course.setSupport(Support.SKI);
        course.setPrice(120.5f);
        course.setTimeSlot(2);

        savedCourse = courseService.addCourse(course);

        Assertions.assertNotNull(savedCourse, "Le cours sauvegardé ne doit pas être null");
        Assertions.assertNotNull(savedCourse.getNumCourse(), "L'ID du cours doit être généré");
        Assertions.assertEquals(TypeCourse.INDIVIDUAL, savedCourse.getTypeCourse()); // ✅ corrigé
        log.info("✅ Course ajouté avec succès : {}", savedCourse);
    }

    @Test
    @Order(2)
    public void testGetCourse() {
        log.info("🔹 Test: getCourse()");
        Course course = courseService.retrieveCourse(savedCourse.getNumCourse());
        Assertions.assertNotNull(course, "Le cours récupéré ne doit pas être null");
        Assertions.assertEquals(savedCourse.getNumCourse(), course.getNumCourse());
        log.info("✅ Course récupéré avec succès : {}", course);
    }

    @Test
    @Order(3)
    public void testUpdateCourse() {
        log.info("🔹 Test: updateCourse()");
        savedCourse.setPrice(150.0f);
        Course updated = courseService.updateCourse(savedCourse);
        Assertions.assertEquals(150.0f, updated.getPrice());
        log.info("✅ Course mis à jour avec succès : {}", updated);
    }

    @Test
    @Order(4)
    public void testDeleteCourse() {
        log.info("🔹 Test: deleteCourse()");
        courseService.deleteCourse(savedCourse.getNumCourse());

        Course deleted = courseService.retrieveCourse(savedCourse.getNumCourse());
        Assertions.assertNull(deleted, "Le cours devrait être supprimé");
        log.info("✅ Course supprimé avec succès.");
    }
}
