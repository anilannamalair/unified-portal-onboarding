package com.brillio.application.dependency.discovery.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class GitService {

    private static final String CLONE_DIR = "C:/Users/Anil.Kumar4/repo";  // Temporary directory to clone repos

    // Clone the repository
    public void cloneRepository(String repoUrl) throws Exception {
        // Ensure directory exists and check if it's already cloned
        File cloneDir = new File(CLONE_DIR);

        // Check if the repository is already cloned
        if (cloneDir.exists() && isGitRepository(cloneDir)) {
            System.out.println("Repository already cloned at: " + cloneDir.getAbsolutePath());
            return;  // Skip cloning as the repository is already present
        }

        // Clone the repo into the directory
        System.out.println("Cloning repository from: " + repoUrl);
        Git.cloneRepository()
            .setURI(repoUrl)
            .setDirectory(cloneDir)
            .call();

        if (cloneDir.exists() && cloneDir.isDirectory()) {
            System.out.println("Repository successfully cloned.");
        } else {
            System.out.println("Failed to clone repository.");
        }
    }

    // Check if directory contains a .git folder to determine if it is a Git repository
    private boolean isGitRepository(File directory) {
        File gitDir = new File(directory, ".git");
        return gitDir.exists() && gitDir.isDirectory();
    }

    // Traverse the repo directory to find all applications
    public List<String> getApplications() {
        List<String> applications = new ArrayList<>();
        File repoDir = new File(CLONE_DIR);

        if (!repoDir.exists() || !repoDir.isDirectory()) {
            System.out.println("Repository directory not found or is not a valid directory.");
            return applications;  // Return empty list if repo isn't found
        }

        for (File file : repoDir.listFiles()) {
            if (file.isDirectory() && containsPom(file)) {
                applications.add(file.getName());  // Application found
            }
        }

        if (applications.isEmpty()) {
            System.out.println("No applications found in the repository.");
        } else {
            System.out.println("Applications found: " + applications);
        }

        return applications;
    }

    // Check if directory contains pom.xml
    private boolean containsPom(File dir) {
        File pomFile = new File(dir, "pom.xml");
        return pomFile.exists();
    }

    public String getDependencies(String appName) throws IOException {
        List<String> dependencies = new ArrayList<>();
        File appDir = new File(CLONE_DIR, appName);
        File pomFile = new File(appDir, "pom.xml");

        if (pomFile.exists()) {
            // Read the content of the pom.xml file as a string
            String xmlContent = new String(Files.readAllBytes(pomFile.toPath()), StandardCharsets.UTF_8);

            try {
                // Initialize XmlMapper and ObjectMapper
                XmlMapper xmlMapper = new XmlMapper();
                ObjectMapper jsonMapper = new ObjectMapper();

                // Convert XML content into a JsonNode
                JsonNode jsonNode = xmlMapper.readTree(xmlContent);

                // Optionally: You could filter or process the JSON if needed, 
                // for example, extract dependencies and add them to a list

                // Convert JsonNode to a JSON string
                String jsonString = jsonMapper.writeValueAsString(jsonNode);
                return jsonString;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
