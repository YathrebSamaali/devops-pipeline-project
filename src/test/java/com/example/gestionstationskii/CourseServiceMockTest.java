package com.example.gestionstationskii;

import com.example.gestionstationskii.entities.Course;
import com.example.gestionstationskii.entities.Support;
import com.example.gestionstationskii.entities.TypeCourse;
import com.example.gestionstationskii.repositories.ICourseRepository;
import com.example.gestionstationskii.services.CourseServicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseServiceMockTest {

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private CourseServicesImpl courseService;

    private Course course;

    @BeforeEach
    void setup() {
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);
        course.setSupport(Support.SKI);
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // ✅ corrigé
        course.setPrice(100f);
        course.setTimeSlot(1);
    }

    @Test
    @Order(1)
    void testAddCourse() {
        log.info("🔹 Test mock: addCourse()");
        when(courseRepository.save(course)).thenReturn(course);

        Course saved = courseService.addCourse(course);

        Assertions.assertNotNull(saved);
        Assertions.assertEquals(course.getPrice(), saved.getPrice());
        verify(courseRepository, times(1)).save(course);
        log.info("✅ Course ajouté (mock) : {}", saved);
    }

    @Test
    @Order(2)
    void testGetCourse() {
        log.info("🔹 Test mock: getCourse()");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course found = courseService.retrieveCourse(1L);

        Assertions.assertNotNull(found);
        Assertions.assertEquals(1L, found.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
        log.info("✅ Course récupéré (mock) : {}", found);
    }

    @Test
    @Order(3)
    void testUpdateCourse() {
        log.info("🔹 Test mock: updateCourse()");
        course.setPrice(180f);
        when(courseRepository.save(course)).thenReturn(course);

        Course updated = courseService.updateCourse(course);

        Assertions.assertEquals(180f, updated.getPrice());
        verify(courseRepository, times(1)).save(course);
        log.info("✅ Course mis à jour (mock) : {}", updated);
    }

    @Test
    @Order(4)
    void testDeleteCourse() {
        log.info("🔹 Test mock: deleteCourse()");
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
        log.info("✅ Course supprimé (mock) avec succès.");
    }
}
