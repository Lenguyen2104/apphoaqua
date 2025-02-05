package com.security.vinclub.dto.request.file;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileStorageUploadRequest {
    @NotNull(message = "File is not null")
    private MultipartFile file;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("file_directory")
    private String fileDirectory;
    @JsonProperty("doc_type_id")
    private String docTypeId;

    @NotBlank(message = "ID người dùng không được để trống")
    @JsonProperty("user_id")
    private String userId;

}