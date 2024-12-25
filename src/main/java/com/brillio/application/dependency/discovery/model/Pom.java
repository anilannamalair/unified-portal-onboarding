package com.brillio.application.dependency.discovery.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// This annotation tells Jackson to ignore unrecognized fields
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pom {

    @JsonProperty("dependencies")
    private List<Dependency> dependencies;

    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown fields in the Dependency class as well
    public static class Dependency { 
        @JsonProperty("artifactId")
        private String artifactId;

        public String getArtifactId() {
            return artifactId;
        }

        public void setArtifactId(String artifactId) {
            this.artifactId = artifactId;
        }
    }
}
