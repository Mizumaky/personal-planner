package old.generated;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tag", schema = "semestralka", catalog = "db19_mullemi5")
public class TagEntity {
    private int id;
    private String title;
    private int color;
    private CategoryEntity categoryByCategory;
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
    @Column(name = "title", nullable = true, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "color", nullable = false)
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id == tagEntity.id &&
                color == tagEntity.color &&
                Objects.equals(title, tagEntity.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, color);
    }

    @ManyToOne
    @JoinColumn(name = "category", referencedColumnName = "id", nullable = false)
    public CategoryEntity getCategoryByCategory() {
        return categoryByCategory;
    }

    public void setCategoryByCategory(CategoryEntity categoryByCategory) {
        this.categoryByCategory = categoryByCategory;
    }

    @OneToMany(mappedBy = "tagByTagId")
    public Collection<TaskTagEntity> getTaskTagsById() {
        return taskTagsById;
    }

    public void setTaskTagsById(Collection<TaskTagEntity> taskTagsById) {
        this.taskTagsById = taskTagsById;
    }
}
