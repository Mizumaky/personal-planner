package old.generated;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "task_tag", schema = "semestralka", catalog = "db19_mullemi5")
@IdClass(TaskTagEntityPK.class)
public class TaskTagEntity {
    private int taskId;
    private int tagId;
    private TaskEntity taskByTaskId;
    private TagEntity tagByTagId;

    @Id
    @Column(name = "task_id", nullable = false)
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Id
    @Column(name = "tag_id", nullable = false)
    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskTagEntity that = (TaskTagEntity) o;
        return taskId == that.taskId &&
                tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, tagId);
    }

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
    public TaskEntity getTaskByTaskId() {
        return taskByTaskId;
    }

    public void setTaskByTaskId(TaskEntity taskByTaskId) {
        this.taskByTaskId = taskByTaskId;
    }

    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    public TagEntity getTagByTagId() {
        return tagByTagId;
    }

    public void setTagByTagId(TagEntity tagByTagId) {
        this.tagByTagId = tagByTagId;
    }
}
