package JPAobjects;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * JPA Entity holding tag data
 */
@Entity
@Table(name = "tag", schema = "public")
public class TagEntity {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "color", nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private CategoryEntity category;

    @ManyToMany(mappedBy = "tags")
    private Set<TaskEntity> tasks;

    public TagEntity() {}
    public TagEntity(String title, String color, CategoryEntity category) {
        this.title = title;
        this.color = color;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public CategoryEntity getCategory() {
        return category;
    }
    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public Set<TaskEntity> getTasks() {
        return tasks;
    }
    public void setTasks(Set<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return title;
//        return "TagEntity{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", color=" + color +
//                //", tasks=" + tasks +
//                //", category=" + category +
//                '}' +
//                '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id == tagEntity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
