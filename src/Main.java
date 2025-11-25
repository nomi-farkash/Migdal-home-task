import java.util.*;

public class Main {

    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        String path = (args.length > 0) ? args[0] : "tasks.json";
        TaskRepository repo = new TaskRepository(path);
        TaskService service = new TaskService(repo);

        System.out.println("=== Todo List(JSON) ===");
        System.out.println("data file " + path);
        boolean running = true;
        //asking the user to choose an option till he will press 9 to stop
        while (running) {
            printMenu();
            int choice = readInt("choose ");
            try {
                switch (choice) {
                    case 1: doAdd(repo); break;
                    case 2: doGetById(repo); break;
                    case 3: doListAll(repo); break;
                    case 4: doUpdate(repo); break;
                    case 5: doDelete(repo); break;
                    case 6: doMarkDone(service); break;
                    case 7: doSearch(service); break;
                    case 8: doListSortedByStatus(service); break;
                    case 9: running = false; System.out.println("goodbye!"); break;
                    default: System.out.println("no correct choosing");
                }
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }
            System.out.println();
        }
    }

    //print the options
    private static void printMenu() {
        System.out.println("--------------");
        System.out.println("1) add task (Create)");
        System.out.println("2) get task by ID (Read)");
        System.out.println("3) get all the tasks (Read)");
        System.out.println("4) update task (Update)");
        System.out.println("5) delete task (Delete)");
        System.out.println("6) mark as -DONE (Service)");
        System.out.println("7) search by text (Service)");
        System.out.println("8) sorted list by status (Service)");
        System.out.println("9) enter");
    }
    //adding tasks
    private static void doAdd(TaskRepository repo) {
        int id = readInt("ID: ");
        String title = readLine("Title: ");
        String desc = readLine("Description: ");
        Status status = readStatus("Status [NEW/IN_PROGRESS/DONE]: ");
        repo.add(new Task(id, title, desc, status));
        System.out.println("added successfully");
    }
    //get task by id
    private static void doGetById(TaskRepository repo) {
        int id = readInt("get by ID: ");
        Task t = repo.getById(id);
        if (t == null) {
            System.out.println(" we didn't find a task with such id=" + id);
        } else {
            printTask(t);
        }
    }
    //get all tasks
    private static void doListAll(TaskRepository repo) {
        List<Task> all = repo.listAll();
        if (all.isEmpty()) {
            System.out.println("(no tasks)");
        } else {
            all.forEach(Main::printTask);
        }
    }
    //update task by id
    private static void doUpdate(TaskRepository repo) {
        int id = readInt("update id: ");
        Task current = repo.getById(id);
        if (current == null) {
            System.out.println("no found task id" + id);
            return;
        }
        System.out.println("current values");
        printTask(current);

        String title = readLineDefault("Title new: ", current.getTitle());
        String desc = readLineDefault("Description new: ", current.getDescription());
        Status status = readStatusDefault("Status new [NEW/IN_PROGRESS/DONE] (empty): ", current.getStatus());

        Task updated = new Task(current.getId(), title, desc, status);
        repo.update(updated);
        System.out.println("updated successfully");
    }
    //delete task by id
    private static void doDelete(TaskRepository repo) {
        int id = readInt("for deleting: ");
        repo.delete(id);
        System.out.println("deleted successfully");
    }
    // mark task to done
    private static void doMarkDone(TaskService service) {
        int id = readInt("ID to mark DONE: ");
        service.markAsDone(id);
        System.out.println("marked as DONE");
    }
    //search task
    private static void doSearch(TaskService service) {
        String q = readLine("text for search (title/description): ");
        List<Task> res = service.search(q);
        if (res.isEmpty()) System.out.println("(no results)");
        else res.forEach(Main::printTask);
    }
    //sort the tasks
    private static void doListSortedByStatus(TaskService service) {
        List<Task> sorted = service.listSortedByStatus();
        if (sorted.isEmpty()) System.out.println("(no tasks)");
        else sorted.forEach(Main::printTask);
    }
    //read user input
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("no correct number try again");
            }
        }
    }
    //read user input line
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }
    //read user input line or default
    private static String readLineDefault(String prompt, String defVal) {
        System.out.print(prompt);
        String s = in.nextLine();
        return s.isEmpty() ? defVal : s;
    }
    //changes if need to upper case the status that user enters
    private static Status readStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim().toUpperCase();
            try {
                return Status.valueOf(s);
            } catch (Exception e) {
                System.out.println("no correct- the options are: NEW, IN_PROGRESS, DONE.");
            }
        }
    }
    //changes if need to upper case the status that user enters or default
    private static Status readStatusDefault(String prompt, Status def) {
        System.out.print(prompt);
        String s = in.nextLine().trim();
        if (s.isEmpty()) return def;
        try {
            return Status.valueOf(s.toUpperCase());
        } catch (Exception e) {
            System.out.println("no correct value so the old one kept " + def);
            return def;
        }
    }
   //print task
    private static void printTask(Task t) {
        System.out.println(String.format("ID=%d | %-12s | %s",
                t.getId(), t.getStatus(), t.getTitle()));
        if (t.getDescription() != null && !t.getDescription().isEmpty()) {
            System.out.println("  desc: " + t.getDescription());
        }
    }
}