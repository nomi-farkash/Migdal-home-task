Java Todo List – JSON-Based Storage (No External Libraries)
A simple and lightweight Todo List application written in pure Java, using a plain JSON file as the storage backend.
The project demonstrates file-based data persistence, manual JSON parsing, and a minimal command-line interface — all without any third-party libraries.

Project Structure
.
├── Task.java                # Data model representing a task
├── JsonTaskRepository.java  # Handles CRUD operations directly on the JSON file
├── TaskService.java         # Additional business logic (search, sort, mark done)
├── Main.java                # CLI interface for interacting with the system
└── tasks.json               # Data file (auto-generated on first run)
Features
Full CRUD support

Create tasks

Read tasks (by ID or list all)

Update tasks

Delete tasks

Direct JSON File Storage

Every action reads/writes directly to the JSON file

Business Logic (via TaskService)

Mark task as DONE

Search in title or description (case-insensitive)

Sort all tasks by status (NEW → IN_PROGRESS → DONE)

Simple CLI (Command Line Interface)

Interactive menu

Easy to test and extend

100% Pure Java

Uses only java.io and java.util

No Gson, Jackson, or any external libraries

Running the Project
1. Compile all source files
javac *.java
2. Run the CLI
java Main
3. (Optional) Specify a custom JSON file
java Main data/mytasks.json
If the file doesn’t exist, it will be created automatically.

CLI Menu Example
=== Todo CLI (JSON) ===
1) Add Task (Create)
2) Get Task by ID (Read)
3) List All Tasks (Read)
4) Update Task (Update)
5) Delete Task (Delete)
6) Mark as DONE
7) Search Tasks
8) List Tasks Sorted by Status
9) Exit
Example tasks.json File
[
  {
    "id": 1,
    "title": "Finish project",
    "description": "Review code and submit",
    "status": "DONE"
  },
  {
    "id": 2,
    "title": "Prepare meeting",
    "description": "Sync with team",
    "status": "IN_PROGRESS"
  }
]
Technologies Used
Java SE

java.io for file handling

java.util for collections and utilities

No frameworks. No dependencies. No build tools required.

Possible Future Improvements
File locking for safe multi-process access

Additional sorting options (by name, by description, etc.)

Task priority levels

Export/Import functionality
