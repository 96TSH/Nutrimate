package sg.edu.ntu.nutrimate.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

@Entity
public class CourseRegistration {

    @EmbeddedId
    CourseRegistrationKey id = new CourseRegistrationKey();

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate courseDate;


    public CourseRegistration() {
    }

    public CourseRegistration(Customer customer, Course course, LocalDate courseDate) {
        this.customer = customer;
        this.course = course;
        this.courseDate = courseDate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDate getCourseDate() {
        return this.courseDate;
    }

    public void setCourseDate(LocalDate courseDate) {
        this.courseDate = courseDate;
    }

}