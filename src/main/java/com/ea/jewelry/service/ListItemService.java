package com.ea.jewelry.service;

import com.ea.jewelry.domain.Image;
import com.ea.jewelry.domain.Item;
import com.ea.jewelry.domain.ItemInformation;
import com.ea.jewelry.repository.ImageRepository;
import com.ea.jewelry.repository.ItemInformationRepository;
import com.ea.jewelry.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Service class for ListItem module.
 */
@Service
@Transactional
public class ListItemService {
    private final Logger log = LoggerFactory.getLogger(ListItemService.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ItemInformationRepository itemInformationRepository;

    @Inject
    private ImageRepository imageRepository;

    @Transactional(readOnly = true)
    public List<ItemInformation> getItemInformationWithItemAndImages(List<ItemInformation> itemInformationList) {
        itemInformationList.forEach(itemInformation -> {
            Set<Item> items = itemRepository.findAllByItemInformation(itemInformation);
            items.forEach(item -> {
                Set<Image> images = imageRepository.findAllByItem(item);
                if (images.isEmpty()) {
                    images.add(ListItemService.getEmptyImage());
                }
                item.setImages(images);
                item.getImages().forEach(image ->image.setItem(null));
                item.setItemInformation(null);
            });
            itemInformation.setItems(items);
        });
        return itemInformationList;
    }

    @Transactional(readOnly = true)
    public ItemInformation getItemInformationWithItemAndImages(ItemInformation itemInformation){
        List<ItemInformation> itemInformationList = new ArrayList<>();
        itemInformationList.add(itemInformation);
        itemInformationList = this.getItemInformationWithItemAndImages(itemInformationList);
        if (itemInformationList.isEmpty()) {
            return null;
        }
        return itemInformationList.get(0);
    }

    private static Image getEmptyImage() {
        Image image = new Image();
        image.setId(0L);
        image.setName("assets/images/hipster.png");
        return image;
    }

}
