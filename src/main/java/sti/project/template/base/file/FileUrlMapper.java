package sti.project.template.base.file;

import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUrlMapper {

    private final FileStorageProperties properties;

    @Named("toFullUrl")
    public String toFullUrl(String relativePath) {
        return properties.buildFullUrl(relativePath);
    }
}
