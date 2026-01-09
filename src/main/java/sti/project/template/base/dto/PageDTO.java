package sti.project.template.base.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageDTO<T> {
    List<T> data;
    Long totalElements;

    public static <T> PageDTO<T> of(List<T> data, long totalElements) {
        return PageDTO.<T>builder()
                .data(data)
                .totalElements(totalElements)
                .build();
    }
}
