package com.uninter.checktask.api;

import com.uninter.checktask.model.Task;
import com.uninter.checktask.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    private final TaskService service;

    public TaskRestController(TaskService service) {
        this.service = service;
    }

    // Listar todas as tarefas
    @GetMapping
    public List<Task> getAllTasks() {
        return service.findAll();
    }

    // Buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> taskOpt = service.findById(id);
        return taskOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar nova tarefa
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = service.save(task);
        return ResponseEntity.ok(savedTask);
    }

    // Atualizar tarefa existente
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        Optional<Task> existingTask = service.findById(id);
        if (existingTask.isPresent()) {
            updatedTask.setId(id);
            Task savedTask = service.save(updatedTask);
            return ResponseEntity.ok(savedTask);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}