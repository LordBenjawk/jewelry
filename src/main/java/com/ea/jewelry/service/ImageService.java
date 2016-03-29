package com.ea.jewelry.service;

import com.ea.jewelry.domain.Image;
import com.ea.jewelry.domain.Item;
import com.ea.jewelry.repository.ImageRepository;
import com.ea.jewelry.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Set;

@Service
@Transactional
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ImageRepository imageRepository;

    public Image createImage() {
        Image image = new Image();
        image.setName("");
        image = imageRepository.save(image);
        return image;
    }

    public Item setImageToItem(Image image, Long itemId) {
        Set itemImages;
        Item item = itemRepository.findOne(itemId);
        image.setName(itemId + "/" + image.getId() + "/full.png");
        image.setItem(item);
        itemImages = item.getImages();
        itemImages.add(image);
        item.setImages(itemImages);
        imageRepository.save(image);
        item = itemRepository.save(item);
        return item;
    }

    @Transactional
    public Item removeImageFromItem(Item item) {
        Set<Image> images = imageRepository.findAllByItem(item);
        images.forEach(
            image -> {
                imageRepository.delete(image.getId());
            }
        );
        item.setImages(null);
        return item;
    }

}
