package sti.project.template.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sti.project.template.base.dto.ApiResponse;
import sti.project.template.base.dto.ApiResponseFactory;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.dto.PageDTO;
import sti.project.template.base.dto.SearchCriteria;
import sti.project.template.base.entity.BaseEntity;
import sti.project.template.base.service.BaseService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import sti.project.template.base.entity.EntityStatus;

/**
 * Abstract base controller with common CRUD endpoints.
 * 
 * @param <T>   Entity type
 * @param <Res> Response DTO type
 * @param <Req> Request DTO type
 */
public abstract class BaseController<T extends BaseEntity, Res extends BaseResponseDTO, Req> {

    protected final BaseService<T, Res, Req> service;
    protected final ApiResponseFactory responseFactory;

    protected BaseController(BaseService<T, Res, Req> service, ApiResponseFactory responseFactory) {
        this.service = service;
        this.responseFactory = responseFactory;
    }

    @GetMapping
    @Operation(summary = "Search records", description = "Search and paginate records with dynamic field filters and ID list")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success")
    })
    public ApiResponse<PageDTO<Res>> search(
            @Parameter(description = "Filter by ID list (comma-separated)") @RequestParam(required = false) List<UUID> ids,
            @Parameter(description = "Filter by status", schema = @io.swagger.v3.oas.annotations.media.Schema(allowableValues = {
                    "ACTIVE", "INACTIVE" })) @RequestParam(required = false) EntityStatus status,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir,
            @Parameter(hidden = true) @RequestParam Map<String, String> allParams) {

        SearchCriteria criteria = new SearchCriteria();
        criteria.setIds(ids);
        criteria.setStatus(status);
        criteria.setPage(page);
        criteria.setSize(size);
        criteria.setSortBy(sortBy);
        criteria.setSortDir(sortDir);

        Map<String, String> fieldParams = new java.util.HashMap<>(allParams);
        fieldParams.remove("ids");
        fieldParams.remove("status");
        fieldParams.remove("page");
        fieldParams.remove("size");
        fieldParams.remove("sortBy");
        fieldParams.remove("sortDir");
        criteria.setFields(fieldParams);

        return responseFactory.success(service.search(criteria), "success.records_fetched");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID", description = "Get a single record by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Res> getById(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return responseFactory.success(service.getById(id), "success.fetched");
    }

    @PostMapping
    @Operation(summary = "Create record", description = "Create a new record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ApiResponse<Res> create(@Valid @RequestBody Req request) {
        return responseFactory.created(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update record", description = "Update an existing record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Res> update(@Parameter(description = "Record ID") @PathVariable UUID id,
            @Valid @RequestBody Req request) {
        return responseFactory.success(service.update(id, request), "success.updated");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete record", description = "Soft delete a record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Void> delete(@Parameter(description = "Record ID") @PathVariable UUID id) {
        service.delete(id);
        return responseFactory.success(null, "success.deleted");
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore record", description = "Restore a soft-deleted record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Res> restore(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return responseFactory.success(service.restore(id), "success.restored");
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle active status", description = "Toggle between active and inactive status")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Res> toggleActive(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return responseFactory.success(service.toggleActive(id), "success.updated");
    }
}