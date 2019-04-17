import java.sql.Timestamp;

public class Task {
    private int task_id;
    private String title;
    private Timestamp due_date;
    private boolean is_done;

    public Task() {
        this.task_id = 0;
        this.title = "";
        this.due_date = new Timestamp(0);
        this.is_done = false;
    }

    public Task(int task_id, String title, Timestamp due_date, boolean is_done) {
        this.task_id = task_id;
        this.title = title;
        this.due_date = due_date;
        this.is_done = is_done;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getDue_date() {
        return due_date;
    }

    public void setDue_date(Timestamp due_date) {
        this.due_date = due_date;
    }

    public boolean isIs_done() {
        return is_done;
    }

    public void setIs_done(boolean is_done) {
        this.is_done = is_done;
    }
}