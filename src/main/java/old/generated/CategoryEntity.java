package old.generated;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "category", schema = "semestralka", catalog = "db19_mullemi5")
public class CategoryEntity {
    private int id;
    private String title;
    private CategoryEntity categoryByParent;
    private Collection<CategoryEntity> categoriesById;
    private Collection<TagEntity> tagsById;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return id == that.id &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @ManyToOne
    @JoinColumn(name = "parent", referencedColumnName = "id")
    public CategoryEntity getCategoryByParent() {
        return categoryByParent;
    }

    public void setCategoryByParent(CategoryEntity categoryByParent) {
        this.categoryByParent = categoryByParent;
    }

    @OneToMany(mappedBy = "categoryByParent")
    public Collection<CategoryEntity> getCategoriesById() {
        return categoriesById;
    }

    public void setCategoriesById(Collection<CategoryEntity> categoriesById) {
        this.categoriesById = categoriesById;
    }

    @OneToMany(mappedBy = "categoryByCategory")
    public Collection<TagEntity> getTagsById() {
        return tagsById;
    }

    public void setTagsById(Collection<TagEntity> tagsById) {
        this.tagsById = tagsById;
    }
}
