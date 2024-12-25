package com.brillio.application.dependency.discovery.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.brillio.application.dependency.discovery.service.GitService;

@RestController
@RequestMapping("/api/git")
public class GitController {

    @Autowired
    private GitService gitService;

    // Clone repository
    @PostMapping("/clone")
    public String cloneRepository(@RequestParam String repoUrl) {
        try {
            gitService.cloneRepository(repoUrl);
            return "Repository cloned successfully";
        } catch (Exception e) {
            return "Error cloning repository: " + e.getMessage();
        }
    }

    // Get all applications in the repository
    @GetMapping("/applications")
    public List<String> getApplications() {
        return gitService.getApplications();
    }

    // Get dependencies for a specific application
    @GetMapping("/dependencies")
    public String getDependencies(@RequestParam String appName) {
        try {
            return gitService.getDependencies(appName);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching dependencies", e);
        }
    }
}
