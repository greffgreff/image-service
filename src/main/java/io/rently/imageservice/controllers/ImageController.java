package io.rently.imageservice.controllers;

import io.rently.imageservice.dtos.Image;
import io.rently.imageservice.dtos.ResponseContent;
import io.rently.imageservice.interfaces.ImageRepository;
import io.rently.imageservice.utils.Broadcaster;
import io.rently.imageservice.utils.Images;
import io.rently.imageservice.exceptions.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImageRepository repository;
    @Value("${server.baseurl}")
    private String baseUrl;

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] handleGetImage(@PathVariable String id) {
        Broadcaster.info("Fetching image by id: " + id);
        Optional<Image> data = repository.queryById(id);
        if (data.isPresent()) {
            byte[] originalImage = Base64.getDecoder().decode(data.get().dataUrl);
            return Images.addRentlyWatermark(originalImage);
        }
        throw Errors.NO_IMAGE;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public String handlePostImage(@PathVariable String id, @RequestBody String data) {
        Broadcaster.info("Adding image by id: " + id);
        checkData(data);
        repository.save(new Image(id, data));
        return baseUrl + "api/v1/images/" + id;
    }

    @PutMapping("/{id}")
    public String handlePutImage(@PathVariable String id, @RequestBody String data) {
        Broadcaster.info("Updating image by id: " + id);
        checkData(data);
        repository.save(new Image(id, data));
        return baseUrl + "api/v1/images/" + id;
    }

    @DeleteMapping("/{id}")
    public void handleDeleteImage(@PathVariable String id) {
        Broadcaster.info("Deleting image by id: " + id);
        Optional<Image> data = repository.queryById(id);
        if (data.isEmpty()) throw Errors.NO_IMAGE;
        repository.deleteById(id);
    }

    private void checkData(String data) {
        try {
            Base64.getDecoder().decode(data);
        } catch (Exception ignore) {
            throw Errors.INVALID_DATA;
        }
    }
}
