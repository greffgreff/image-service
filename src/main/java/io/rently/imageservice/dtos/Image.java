package io.rently.searchservice.dtos;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("images")
public class Image {
    @Id
    public String id;
    public String dataUrl;

    public Image(String id, String data) {
        this.id = id;
        this.dataUrl = data;
    }

    public Image() { }
}