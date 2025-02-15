package com.security.apphoaqua.dto.response.banner;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class BannerAdminResponse {
    @NotNull
    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @NotNull
    @JsonProperty("url")
    private String url;

    @NotNull
    @JsonProperty("thumbnail")
    private String thumbnail;

    @NotNull
    @JsonProperty("activated")
    private boolean activated = true;

    @NotNull
    @JsonProperty("deleted")
    private boolean deleted = false;

}
