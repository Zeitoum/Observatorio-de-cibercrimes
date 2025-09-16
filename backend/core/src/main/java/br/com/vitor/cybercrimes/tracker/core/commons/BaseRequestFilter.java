package br.com.vitor.cybercrimes.tracker.core.commons;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public abstract class BaseRequestFilter {

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    private int pageSize;

    @Min(value = 1, message = "Page number must be at least 1")
    private int pageNumber;

    private String orderBy;

    private String search;
}
