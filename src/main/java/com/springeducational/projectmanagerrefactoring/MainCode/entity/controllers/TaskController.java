package com.ngu.restclienttemplate.MainCode.entity.controllers;


import com.ngu.restclienttemplate.MainCode.entity.*;
import com.ngu.restclienttemplate.MainCode.entity.repository.TaskRepository;
import com.ngu.restclienttemplate.auth.UserService;
import com.ngu.restclienttemplate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
public class TaskController1 {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(Model model) {
        Long userId = userService.getUserId();
        User user = userService.userFindById(userId);
        List<Task> tasks = taskRepository.findAllByusersIdentity(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new Task());
        model.addAttribute("newSubtask", new Subtask());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(Task task) {
        if (taskRepository.existsByprojectName(task.getProjectName())) {
            return "WrongLoginData";
        } else {
            Long userId = userService.getUserId();
            User user = userService.userFindById(userId);
            task.setUsersIdentity(List.of(user));

            taskRepository.save(task);


            return "redirect:/";
        }
    }


    @PostMapping("/addSubtask/{projectId}")
    public String addSubtask(@PathVariable Long projectId, Subtask subtask) {
        Optional<Task> taskOptional = taskRepository.findById(projectId);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            subtask.setProject(task);
            task.getSubtasks().add(subtask);
            taskRepository.save(task);
        }
        return "redirect:/project/{projectId}";
    }
}
