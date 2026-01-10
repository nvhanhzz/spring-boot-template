package sti.project.template.base.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import sti.project.template.base.entity.EntityStatus;

/**
 * Search criteria DTO for dynamic field search.
 */
@Data
public class SearchCriteria {
    private Map<String, String> fields;
    private List<UUID> ids;
    private EntityStatus status;
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDir = "DESC";
}