package com.cts.edutrack.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "learning_content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @NotNull(message = "Module ID is required")
    private Long moduleId;

    @NotBlank(message = "Content type is required")
    @Pattern(regexp = "Video|PDF|Slide|Lab", message = "ContentType must be Video, PDF, Slide, or Lab")
    private String contentType;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Content URI is required")
    private String contentUri;

    @Positive(message = "Duration must be a positive value")
    private Double duration;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "Draft|Published", message = "Status must be either Draft or Published")
    private String status;

    public Long getContentId() { return contentId; }
    public void setContentId(Long contentId) { this.contentId = contentId; }
    public Long getModuleId() { return moduleId; }
    public void setModuleId(Long moduleId) { this.moduleId = moduleId; }
    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContentUri() { return contentUri; }
    public void setContentUri(String contentUri) { this.contentUri = contentUri; }
    public Double getDuration() { return duration; }
    public void setDuration(Double duration) { this.duration = duration; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}