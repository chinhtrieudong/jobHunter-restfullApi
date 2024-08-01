package vn.hoidanit.jobhunter.domain.response.file;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResUploadFile {
    private String fileName;
    private Instant uploadedAt;
}
