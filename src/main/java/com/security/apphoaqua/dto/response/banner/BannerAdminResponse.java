package com.security.apphoaqua.dto.response.banner;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BannerAdminResponse {
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
    @JsonProperty("spot")
    private String spot;

    @NotNull
    @JsonProperty("activated")
    private boolean activated = true;

    @NotNull
    @JsonProperty("deleted")
    private boolean deleted = false;

}
