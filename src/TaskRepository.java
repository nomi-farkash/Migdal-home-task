import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class TaskRepository {
    private final File file;
    public TaskRepository(String path) {
        if (!path.toLowerCase().endsWith(".json")) path += ".json";
        this.file = new File(path);
        initialized();
    }
    public synchronized void add(Task task) {
        List<Task> allTasks = readAll();
        if (allTasks.stream().anyMatch(t -> t.getId() == task.getId()))
            throw new IllegalArgumentException("Task with id " + task.getId() + " already exists");
        allTasks.add(task);
        writeToTempFile(allTasks);
    }
    public synchronized Task getById(int id) {
        for (Task t : readAll()) if (t.getId() == id) return t;
        return null;
    }

    public synchronized List<Task> listAll() {
        return readAll();
    }

    public synchronized void update(Task task) {
        List<Task> allTasks = readAll();
        boolean found = false;
        for (int i = 0; i < allTasks.size(); i++) {
            if (allTasks.get(i).getId() == task.getId()) {
                allTasks.set(i, task);
                found = true;
                break;
            }
        }
        if (!found) throw new NoSuchElementException("Task id " + task.getId() + " not found can't update");
        writeToTempFile(allTasks);
    }
    public synchronized void delete(int id) {
        List<Task> all = readAll();
        boolean removed = all.removeIf(t -> t.getId() == id);
        if (!removed) throw new NoSuchElementException("Task id " + id + " not found can't delete");
        writeToTempFile(all);
    }
    private void initialized() {
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent != null) parent.mkdirs();
                try (Writer w = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                    w.write("[]");
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<Task> readAll() {
        try {
            String json = readString(file);
            return parseArray(json);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void writeToTempFile(List<Task> tasks) {
        File tmp = new File(file.getParentFile(), file.getName() + ".tmp");
        try (Writer w = new OutputStreamWriter(new FileOutputStream(tmp), StandardCharsets.UTF_8)) {
            w.write("[");
            for (int i = 0; i < tasks.size(); i++) {
                if (i > 0) w.write(",");
                w.write(tasks.get(i).toJson());
            }
            w.write("]");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        if (!tmp.renameTo(file)) {
            try { copyFile(tmp, file); }
            catch (IOException e)
            { throw new UncheckedIOException(e); }
            tmp.delete();
        }
    }

    private static String readString(File f) throws IOException {
        try (InputStream in = new FileInputStream(f)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
            return out.toString(String.valueOf(StandardCharsets.UTF_8)).trim();
        }
    }

    private static List<Task> parseArray(String jsonArray) {
        List<Task> out = new ArrayList<>();
        String s = jsonArray == null ? "" : jsonArray.trim();
        if (s.isEmpty() || s.equals("[]")) return out;
        if (s.charAt(0) == '[') s = s.substring(1);
        if (s.charAt(s.length()-1) == ']') s = s.substring(0, s.length()-1);

        List<String> objects = new ArrayList<>();
        int depth = 0; boolean inString = false;
        StringBuilder cur = new StringBuilder();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            cur.append(c);
            if (c == '"' && (i == 0 || s.charAt(i-1) != '\\')) inString = !inString;
            if (!inString) {
                if (c == '{') depth++;
                else if (c == '}') depth--;
                if (depth == 0 && c == '}') {
                    objects.add(cur.toString().trim());
                    cur.setLength(0);
                    if (i + 1 < s.length() && s.charAt(i+1) == ',') i++;
                }
            }
        }
        for (String obj : objects) out.add(Task.fromJson(obj));
        return out;
    }

    private static void copyFile(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) out.write(buf, 0, n);
        }
    }
}
