    package org.example.backend_dip.controller;

    import org.springframework.core.io.Resource;
    import org.springframework.core.io.UrlResource;
    import org.springframework.http.MediaType;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import java.net.MalformedURLException;
    import java.nio.file.Path;
    import java.nio.file.Paths;

    @RestController
    @RequestMapping("/file")
    public class FileController {

        private final Path uploadsDir = Paths.get("uploads/audio");

        @GetMapping("/uploads/audio/{filename}")
        public ResponseEntity<Resource> getFile(@PathVariable String filename) throws MalformedURLException {
            Path file = uploadsDir.resolve(filename).normalize();
            Resource resource = new UrlResource(file.toUri());
            System.out.println(resource);
            if(resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("audio/mpeg"))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
