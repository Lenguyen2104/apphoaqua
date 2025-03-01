package com.security.apphoaqua.dto.request.banner;

import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBannerRequest {
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;
}

