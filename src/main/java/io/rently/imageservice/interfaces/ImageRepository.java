package io.rently.imageservice.interfaces;

import io.rently.imageservice.dtos.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface ImageRepository extends MongoRepository<Image, String> {

    @Query("{ '_id' : ?0 }")
    Optional<Image> queryById(String id);

}
