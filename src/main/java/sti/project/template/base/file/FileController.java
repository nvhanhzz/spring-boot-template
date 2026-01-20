package sti.project.template.base.file;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sti.project.template.base.constant.ApiVersion;
import sti.project.template.base.dto.ApiResponse;
import sti.project.template.base.dto.ApiResponseFactory;

import java.util.List;

@RestController
@RequestMapping(ApiVersion.V1 + "/files")
@Tag(name = "Files", description = "File upload APIs")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    FileService fileService;
    ApiResponseFactory responseFactory;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasPermission(null, 'file.upload')")
    @Operation(summary = "Upload one or more files")
    public ApiResponse<FileUploadResponse> upload(
            @Parameter(description = "Files to upload", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestParam("files") List<MultipartFile> files) {
        return responseFactory.success(fileService.upload(files), "success.created");
    }

    @PostMapping("/delete")
    @PreAuthorize("hasPermission(null, 'file.delete')")
    @Operation(summary = "Delete one or more files by URLs")
    public ApiResponse<Void> delete(@RequestBody List<String> fileUrls) {
        fileService.deleteFiles(fileUrls);
        return responseFactory.success(null, "success.deleted");
    }
}