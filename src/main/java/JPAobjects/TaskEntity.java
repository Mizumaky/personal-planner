package JPAobjects;

import javax.persistence.*;
import java.util.*;

@Entity
@Table (name = "task", schema = "public")
@NamedQueries({
        @NamedQuery(name = "TaskEntity.findAll", query = "select t from TaskEntity t"),
//        @NamedQuery(name = "TaskEntity.findById", query = "select t from TaskEntity t where t.id = :id"),
//        @NamedQuery(name = "TaskEntity.findByTitle", query = "select t from TaskEntity t where t.title = :title"),
//        @NamedQuery(name = "TaskEntity.findByIsDone", query = "select t from TaskEntity t where t.is_done = :is_done")
})
public class TaskEntity {
    @Id
    @SequenceGenerator(name="seq", initialValue=7, allocationSize=100)
    @GeneratedValue  (strategy = GenerationType.SEQUENCE, generator = "seq") //do i need this if i have serial at sql creation??
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_done", nullable = false)
    private Boolean is_done;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(
            name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    )
    private Set<TagEntity> tags = new HashSet<>();

    public TaskEntity() {}

    public TaskEntity(String title, String description, boolean is_done, Set<TagEntity> tags) {
        this.title = title;
        this.description = description;
        this.is_done = is_done;
        this.tags = tags;
    }

    public int getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIs_done() { return is_done; }
    public void setIs_done(Boolean is_done) { this.is_done = is_done; }

    public Set<TagEntity> getTags() { return tags; }
    public void setTags(Set<TagEntity> tags) { this.tags = tags; }

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", is_done=" + is_done +
                ", tags=" + tags +
                '}' +
                '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskEntity that = (TaskEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
