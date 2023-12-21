package sg.edu.ntu.nutrimate.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CourseRegistrationKey implements Serializable {

    @Column(name = "customer_id")
    private int id;

    @Column(name = "course_id")
    private int courseId;

    public CourseRegistrationKey() {
    }

    public CourseRegistrationKey(int id, int courseId) {
        this.id = id;
        this.courseId = courseId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return this.courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCourseId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof CourseRegistrationKey))
            return false;
        CourseRegistrationKey that = (CourseRegistrationKey) obj;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getCourseId(), that.getCourseId());
    }
}
