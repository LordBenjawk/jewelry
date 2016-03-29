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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
        image.setName("default/full.png");
        return image;
    }

    /** TODO
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private BufferedImage getScaledImage(BufferedImage src, int w, int h){
        int finalw = w;
        int finalh = h;
        double factor = 1.0d;

        if(src.getWidth() > src.getHeight()){
            factor = ((double)src.getHeight()/(double)src.getWidth());
            finalh = (int)(finalw * factor);
        }else{
            factor = ((double)src.getWidth()/(double)src.getHeight());
            finalw = (int)(finalh * factor);
        }

        BufferedImage resizedImg = new BufferedImage(finalw, finalh, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(src, 0, 0, finalw, finalh, null);
        g2.dispose();
        return resizedImg;
    }

}
