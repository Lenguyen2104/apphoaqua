package com.security.apphoaqua.dto.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchRequest {

    @JsonProperty("search_text")
    private String searchText;

    @NotBlank(message = "Page number is not blank")
    @JsonProperty("page_number")
    private String pageNumber;

    @NotBlank(message = "Page size is not blank")
    @JsonProperty("page_size")
    private String pageSize;

    @JsonProperty("sort_by")
    private String sortBy;

    @JsonProperty("sort_direction")
    private String sortDirection;
}
