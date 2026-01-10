package sti.project.template.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sti.project.template.base.dto.ApiResponse;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.dto.PageDTO;
import sti.project.template.base.entity.BaseEntity;
import sti.project.template.base.i18n.MessageHelper;
import sti.project.template.base.service.BaseService;

import java.util.List;
import java.util.UUID;

/**
 * Abstract base controller with common CRUD endpoints.
 * 
 * @param <T>   Entity type
 * @param <Res> Response DTO type
 * @param <Req> Request DTO type
 */
public abstract class BaseController<T extends BaseEntity, Res extends BaseResponseDTO, Req> {

    protected final BaseService<T, Res, Req> service;
    protected final MessageHelper messageHelper;

    protected BaseController(BaseService<T, Res, Req> service, MessageHelper messageHelper) {
        this.service = service;
        this.messageHelper = messageHelper;
    }

    @GetMapping
    @Operation(summary = "Search records", description = "Search and paginate records with keyword filter")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success")
    })
    public ApiResponse<PageDTO<Res>> search(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        return ApiResponse.success(service.search(keyword, page, size, sortBy, sortDir),
                messageHelper.getMessage("success.records_fetched"));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all records", description = "Get all active records without pagination")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success")
    })
    public ApiResponse<List<Res>> getAll() {
        return ApiResponse.success(service.getAll(), messageHelper.getMessage("success.records_fetched"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID", description = "Get a single record by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Res> getById(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return ApiResponse.success(service.getById(id), messageHelper.getMessage("success.fetched"));
    }

    @PostMapping
    @Operation(summary = "Create record", description = "Create a new record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ApiResponse<Res> create(@Valid @RequestBody Req request) {
        return ApiResponse.created(service.create(request), messageHelper.getMessage("success.created"));
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
        return ApiResponse.success(service.update(id, request), messageHelper.getMessage("success.updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete record", description = "Soft delete a record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Void> delete(@Parameter(description = "Record ID") @PathVariable UUID id) {
        service.delete(id);
        return ApiResponse.success(null, messageHelper.getMessage("success.deleted"));
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore record", description = "Restore a soft-deleted record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ApiResponse<Res> restore(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return ApiResponse.success(service.restore(id), messageHelper.getMessage("success.restored"));
    }
}
