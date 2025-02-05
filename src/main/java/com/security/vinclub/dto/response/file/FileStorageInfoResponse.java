package com.security.vinclub.dto.response.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileStorageInfoResponse {

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("raw_file_name")
    private String rawFileName;

    private String description;

    @JsonProperty("file_directory")
    private String fileDirectory;

    @JsonProperty("file_extension")
    private String fileExtension;

    private String type;
}
