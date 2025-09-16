package br.com.vitor.cybercrimes.tracker.core.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PagedResponse<T> {

    private final long totalItems;
    private final List<T> items;

    public PagedResponse(long totalItems, List<T> items) {
        this.totalItems = totalItems;
        this.items = items == null ? List.of() : items;
    }
}
