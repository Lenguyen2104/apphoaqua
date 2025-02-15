package com.security.apphoaqua.dto.response.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class ShareholderLevelResponse {
    @NotNull
    @JsonProperty("shareholder_level_4")
    private boolean shareholderLevel4 = false;
    @NotNull
    @JsonProperty("shareholder_level_3")
    private boolean shareholderLevel3 = false;
    @NotNull
    @JsonProperty("shareholder_level_2")
    private boolean shareholderLevel2 = false;
    @NotNull
    @JsonProperty("shareholder_level_1")
    private boolean shareholderLevel1 = true;
}
