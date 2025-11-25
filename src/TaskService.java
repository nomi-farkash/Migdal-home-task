import java.util.*;

import static java.util.stream.Collectors.toList;

public class TaskService {
    private TaskRepository taskRepo;
    public TaskService(TaskRepository taskRepo){
        this.taskRepo = Objects.requireNonNull(taskRepo,"repository is required");
    }
    //only if the status is not done it changes it to done
    public void markAsDone(int id){
        Task task = taskRepo.getById(id);
        if (task == null)
            throw new NoSuchElementException("Task id "+id+" not found");
        if (task.getStatus()!=Status.DONE){
            task.setStatus(Status.DONE);
            taskRepo.update(task);
        }

    }
    //search by prompt (title or description)
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
    //sort the tasks list by status
    public List<Task> listSortedByStatus() {
        List<Task> allTasks = new ArrayList<>(taskRepo.listAll());
        allTasks.sort(Comparator
                .comparingInt((Task t) -> t.getStatus().ordinal())
                .thenComparingInt(Task::getId));
        return allTasks;
    }
    //changes the letters to lower case(for easy search)
    private static boolean containsIgnoreCase(String source, String needleLower) {
        if (source == null) return false;
        return source.toLowerCase().contains(needleLower);
    }

}
