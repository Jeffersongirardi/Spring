package com.uninter.checktask.controller;

import com.uninter.checktask.model.Task;
import com.uninter.checktask.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // Listar todas as tarefas (Página inicial)
    @GetMapping
    public String listTasks(Model model) {
        model.addAttribute("tasks", service.findAll());
        return "tasks/list";
    }

    // Formulário para nova tarefa
    @GetMapping("/new")
    public String newTaskForm(Model model) {
        try {
            Task task = new Task();
            task.setCompleted(false);
            model.addAttribute("task", task);
            return "tasks/form";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao carregar o formulário: " + e.getMessage());
            return "tasks/list"; // Fallback para a lista de tarefas
        }
    }

    // Salvar tarefa (nova ou editada)
    @PostMapping
    public String saveTask(@ModelAttribute Task task, @RequestParam String dueDateTime, @RequestParam String responsible, Model model) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            task.setDueDate(LocalDateTime.parse(dueDateTime, formatter));
            task.setResponsible(responsible);
            service.save(task);
            return "redirect:/tasks";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erro ao salvar a tarefa: " + e.getMessage());
            return "tasks/form";
        }
    }

    // Formulário para editar tarefa
    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable Long id, Model model) {
        Optional<Task> taskOpt = service.findById(id);
        if (taskOpt.isPresent()) {
            model.addAttribute("task", taskOpt.get());
            return "tasks/form";
        } else {
            model.addAttribute("error", "Tarefa não encontrada com ID: " + id);
            return "redirect:/tasks";
        }
    }

    // Visualizar tarefa específica por ID
    @GetMapping("/view/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        Optional<Task> taskOpt = service.findById(id);
        if (taskOpt.isPresent()) {
            model.addAttribute("task", taskOpt.get());
            return "tasks/view";
        } else {
            model.addAttribute("error", "Tarefa não encontrada com ID: " + id);
            return "redirect:/tasks";
        }
    }

    // Excluir tarefa
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/tasks";
    }
}