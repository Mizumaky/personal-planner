package old.generated;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "task", schema = "semestralka", catalog = "db19_mullemi5")
public class TaskEntity {
    private int id;
    private String title;
    private String description;
    private boolean isDone;
    private Collection<TaskTagEntity> taskTagsById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 1024)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "is_done", nullable = false)
    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }

    @OneToMany(mappedBy = "taskByTaskId")
    public Collection<TaskTagEntity> getTaskTagsById() {
        return taskTagsById;
    }

    public void setTaskTagsById(Collection<TaskTagEntity> taskTagsById) {
        this.taskTagsById = taskTagsById;
    }
}
