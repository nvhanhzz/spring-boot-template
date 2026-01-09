package sti.project.template.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Generic paginated response DTO.
 * 
 * @param <T> The type of data in the page
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO<T> {
    private List<T> data;
    private long total;
    private int page;
    private int size;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;

    public static <T> PageDTO<T> of(List<T> data, long total, int page, int size) {
        int totalPages = (int) Math.ceil((double) total / size);
        return PageDTO.<T>builder()
                .data(data)
                .total(total)
                .page(page)
                .size(size)
                .totalPages(totalPages)
                .hasNext(page < totalPages - 1)
                .hasPrev(page > 0)
                .build();
    }
}
