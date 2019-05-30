package JPAobjects;

import javax.persistence.*;
import java.util.Collection;

/**
 * JPA Entity holding all
 */
@Entity
@Table(name = "category", schema = "public")
@NamedQueries({
        @NamedQuery(name = "CategoryEntity.findAll", query = "select c from CategoryEntity c where c.parent = null")
})
public class CategoryEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "parent")
    private CategoryEntity parent;

    //derived field from parent db column
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    private Collection<CategoryEntity> subcategories;

    @OneToMany (mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    private Collection<TagEntity> tags;

    public CategoryEntity() {}
    public CategoryEntity(String title, CategoryEntity parent, Collection<CategoryEntity> subcategories, Collection<TagEntity> tags) {
        this.title = title;
        this.parent = parent;
        this.subcategories = subcategories;
        this.tags = tags;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public CategoryEntity getParent() {
        return parent;
    }
    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public Collection<CategoryEntity> getSubcategories() {
        return subcategories;
    }
    public void setSubcategories(Collection<CategoryEntity> subcategories) {
        this.subcategories = subcategories;
    }

    public Collection<TagEntity> getTags() {
        return tags;
    }
    public void setTags(Collection<TagEntity> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return title;
//        return "CategoryEntity{" +
//                "id=" + id +
//                ", title='" + title + '\'' +
//                ", parent=" + parent +
//                ", subcategories=" + subcategories +
//                ", tags=" + tags +
//                '}' +
//                '\n';
    }
}
