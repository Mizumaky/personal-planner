import JPAobjects.CategoryEntity;
import JPAobjects.TagEntity;


/**
 * A wrapper / proxy class for a tag tree view, storing either a tag or a category.
 */
public class TreeEntityProxy {
    enum Type {
        CATEGORY, TAG
    }

    private Type type;
    private CategoryEntity category = null;
    private TagEntity tag = null;

//    private ObservableList<CategoryEntity> subcategories = null;
//    private ObservableList<TagEntity> tags = null;

    public TreeEntityProxy(CategoryEntity category, TagEntity tag) {
        if (category != null && tag == null) {
            this.type = Type.CATEGORY;
            this.category = category;
        } else if (category == null && tag != null) {
            this.type = Type.TAG;
            this.tag = tag;
        } else {
            throw new RuntimeException("Invalid creation of tree entity proxy object");
        }
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public TagEntity getTag() {
        return tag;
    }

    public void setTag(TagEntity tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return type == Type.CATEGORY ? category.toString() : tag.toString();
    }
}
