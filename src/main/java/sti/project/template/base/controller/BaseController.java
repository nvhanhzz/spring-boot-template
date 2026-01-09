package sti.project.template.base.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sti.project.template.base.dto.ApiResponse;
import sti.project.template.base.dto.BaseResponseDTO;
import sti.project.template.base.dto.PageDTO;
import sti.project.template.base.entity.BaseEntity;
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

    protected BaseController(BaseService<T, Res, Req> service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Search records", description = "Search and paginate records with keyword filter")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success")
    })
    public ResponseEntity<ApiResponse<PageDTO<Res>>> search(
            @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "DESC") String sortDir) {
        return ResponseEntity.ok(ApiResponse.success(service.search(keyword, page, size, sortBy, sortDir)));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all records", description = "Get all active records without pagination")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success")
    })
    public ResponseEntity<ApiResponse<List<Res>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(service.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID", description = "Get a single record by ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ApiResponse<Res>> getById(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(service.getById(id)));
    }

    @PostMapping
    @Operation(summary = "Create record", description = "Create a new record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<ApiResponse<Res>> create(@Valid @RequestBody Req request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(service.create(request), "Created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update record", description = "Update an existing record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ApiResponse<Res>> update(@Parameter(description = "Record ID") @PathVariable UUID id,
            @Valid @RequestBody Req request) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, request), "Updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete record", description = "Soft delete a record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "No content"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<Void> delete(@Parameter(description = "Record ID") @PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    @Operation(summary = "Restore record", description = "Restore a soft-deleted record")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Success"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found")
    })
    public ResponseEntity<ApiResponse<Res>> restore(@Parameter(description = "Record ID") @PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(service.restore(id), "Restored successfully"));
    }
}
