package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotBlank(message = "Name can't blank")
    String name;
    @NotBlank(message = "apiPath can't blank")
    String apiPath;
    @NotBlank(message = "method can't blank")
    String method;
    @NotBlank(message = "module can't blank")
    String module;

    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    List<Role> roles;

    public Permission(@NotBlank(message = "Name can't blank") String name,
            @NotBlank(message = "apiPath can't blank") String apiPath,
            @NotBlank(message = "method can't blank") String method,
            @NotBlank(message = "module can't blank") String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedAt = Instant.now();
    }
}
