package com.security.apphoaqua.dto.response.videoslider;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class VideoSliderAdminResponse {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("file_id")
    private String fileId;

    @NotNull
    @JsonProperty("thumbnail")
    private String thumbnail;

    @NotNull
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

}
