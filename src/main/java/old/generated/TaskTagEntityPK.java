package old.generated;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class TaskTagEntityPK implements Serializable {
    private int taskId;
    private int tagId;

    @Column(name = "task_id", nullable = false)
    @Id
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Column(name = "tag_id", nullable = false)
    @Id
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
        TaskTagEntityPK that = (TaskTagEntityPK) o;
        return taskId == that.taskId &&
                tagId == that.tagId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, tagId);
    }
}
