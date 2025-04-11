package labs.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    private final AtomicLong counter = new AtomicLong();

    // CREATE: Создать новую задачу
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        long id = counter.incrementAndGet();
        task.setId(id);
        tasks.put(id, task);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    // READ: Получить все задачи
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(new ArrayList<>(tasks.values()));
    }

    // READ: Получить задачу по ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = tasks.get(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // UPDATE: Обновить задачу по ID
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Task existingTask = tasks.get(id);
        if (existingTask != null) {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setCompleted(updatedTask.isCompleted());
            tasks.put(id, existingTask);
            return ResponseEntity.ok(existingTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Удалить задачу по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
