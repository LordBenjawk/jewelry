package com.ea.jewelry.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileManagementUtil {
    private final static Logger log = LoggerFactory.getLogger(FileManagementUtil.class);
    private static final String IMG_FOLDER = "images";

    public static boolean transferMultiPartToImages(final MultipartFile file,
                                                    final String itemDirectoryPath,
                                                    final String imageName) {
        boolean result = Boolean.TRUE;
        File itemDirectory = new File(itemDirectoryPath);
        boolean itemDirectoryExists = createDirectoryIfNotExists(itemDirectory);

        if (itemDirectoryExists) {
            File imageFile = new File(itemDirectoryPath + imageName + ".png");

            try {
                file.transferTo(imageFile);
                log.info("Image transferred into web application");
            } catch (Exception e) {
                result = Boolean.FALSE;
                log.debug("Error transferring image to web application");
            }
        }

        return result;
    }

    public static String generateImageFolderWithItemPath(String applicationPath, Long itemId, Long imageId) {
        return applicationPath + IMG_FOLDER + File.separator +
            itemId + File.separator +
            imageId + File.separator;
    }

    public static boolean deletePath(String path) {
        File pathFolder = new File(path);
        boolean result = Boolean.FALSE;
        if (pathFolder.exists()) {
            pathFolder.delete();
            result = Boolean.TRUE;
        }
        return result;
    }

    private static boolean createDirectoryIfNotExists(File file) {
        boolean result = Boolean.TRUE;

        if (!file.exists()) {
            log.info("Creating directory");
            try {
                file.mkdirs();
                log.info("Directory created");
            }catch(Exception e){
                result = Boolean.FALSE;
                log.debug("Error creating directory");
            }
        }

        return result;
    }

}
