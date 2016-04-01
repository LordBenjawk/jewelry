package com.ea.jewelry.web.rest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileManagementUtil {
    private final static Logger log = LoggerFactory.getLogger(FileManagementUtil.class);
    private static final String IMG_FOLDER = "images";
    private static final String REPORTS_FOLDER = "reports";
    private static final String PURCHASE_ORDER_FOLDER = "purchaseOrders";

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

    public static String generateItemImageFolder(String applicationPath, Long itemId, Long imageId) {
        return applicationPath + IMG_FOLDER + File.separator +
            itemId + File.separator +
            imageId + File.separator;
    }

    public static String generatePurchaseOrderReportPath(String applicationPath, Long purchaseOrderId) {
        return applicationPath + REPORTS_FOLDER + File.separator +
            PURCHASE_ORDER_FOLDER + File.separator
            + purchaseOrderId + File.separator;
    }

    public static boolean deletePathIfExists(String path) {
        File pathFolder = new File(path);
        boolean result = Boolean.FALSE;
        if (pathFolder.exists()) {
            pathFolder.delete();
            result = Boolean.TRUE;
        }
        return result;
    }

    public static File createDirectoryAndFile(String filePath, String fileName) {
        String filePathToCreate = filePath + fileName;
        File pathToCreate = new File(filePath);
        File fileToCreate = new File(filePathToCreate);
        boolean isPathCreated = createDirectoryIfNotExists(pathToCreate);
        boolean isFileCreated = createFileInDirectoryIfNotExists(fileToCreate);

        if (!isPathCreated && !isFileCreated) {
            throw new RuntimeException();

        }

        return fileToCreate;
    }

    private static boolean createFileInDirectoryIfNotExists(File file) {
        boolean isValid = Boolean.TRUE;

        if ( !file.exists() ) {
            try {
                file.createNewFile();
                log.info("File created");
            } catch(Exception e) {
                isValid = Boolean.FALSE;
                log.debug("Error creating file: " + file.getAbsolutePath());
            }
        }

        return isValid;
    }

    private static boolean createDirectoryIfNotExists(File directory) {
        boolean isValid = Boolean.TRUE;

        if (!directory.exists()) {
            log.info("Creating directory");
            try {
                directory.mkdirs();;
                log.info("Directory created");
            }catch(Exception e) {
                isValid = Boolean.FALSE;
                log.debug("Error creating directory: " + directory.getAbsolutePath());
            }
        }

        return isValid;
    }

}
