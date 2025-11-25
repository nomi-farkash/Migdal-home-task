import java.util.Objects;
public class Task {
    private int id;
    private String title;
    private String description;
    private Status status;

    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //this is convert the text to json format
    public String toJson() {
        return String.format(
                "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\",\"status\":\"%s\"}",
                id, escape(title), escape(description), status);
    }

    //this is convert the json to text format
    public static Task fromJson(String json) {
        json = json.trim()
                .replace("{", "")
                .replace("}", "");
        String[] parts = json.split(",");
        int id = 0;
        String title = "", description = "";
        Status status = Status.NEW;

        for (String part : parts) {
            String[] kv = part.split(":", 2);
            if (kv.length < 2) continue;
            String key = kv[0].replace("\"", "").trim();
            String value = kv[1].replace("\"", "").trim();
            switch (key) {
                case "id": id = Integer.parseInt(value); break;
                case "title": title = value; break;
                case "description": description = value; break;
                case "status": status = Status.valueOf(value); break;
            }
        }
        return new Task(id, title, description, status);
    }
    @Override
    public String toString() {
        return String.format("Task{id=%d, title='%s', status=%s}", id, title, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    //changes quotation marks
    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }

}
