import java.util.*;

import static java.util.stream.Collectors.toList;

public class TaskService {
    private TaskRepository taskRepo;
    public TaskService(TaskRepository taskRepo){
        this.taskRepo = Objects.requireNonNull(taskRepo,"repository is required");
    }
    public void markAsDone(int id){
        Task task = taskRepo.getById(id);
        if (task == null)
            throw new NoSuchElementException("Task id "+id+" not found");
        if (task.getStatus()!=Status.DONE){
            task.setStatus(Status.DONE);
            taskRepo.update(task);
        }

    }
    public List<Task> search(String text) {
        String query = (text == null) ? "" : text.trim().toLowerCase();
        if (query.isEmpty())
            return taskRepo.listAll();

        List<Task> allTasks = taskRepo.listAll();
        return allTasks.stream()
                .filter(t -> containsIgnoreCase(t.getTitle(), query) ||
                        containsIgnoreCase(t.getDescription(), query))
                .collect(toList());
    }
    public List<Task> listSortedByStatus() {
        List<Task> allTasks = new ArrayList<>(taskRepo.listAll());
        allTasks.sort(Comparator
                .comparingInt((Task t) -> t.getStatus().ordinal())
                .thenComparingInt(Task::getId));
        return allTasks;
    }
    private static boolean containsIgnoreCase(String source, String needleLower) {
        if (source == null) return false;
        return source.toLowerCase().contains(needleLower);
    }

}
