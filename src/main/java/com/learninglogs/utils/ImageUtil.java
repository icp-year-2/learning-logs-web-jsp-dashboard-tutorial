package com.learninglogs.utils;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Utility class for handling image file uploads.
 *
 * <p>Provides methods to upload image files to an external
 * folder and delete previously uploaded images.
 * Supported formats: JPG, JPEG, PNG.</p>
 *
 * <p><strong>Note:</strong> Files are stored in
 * {@code ~/learning-logs-uploads/} (outside the project).
 * They persist across {@code mvn clean} rebuilds.</p>
 */
public class ImageUtil {

    /**
     * Saves an uploaded image file to the external uploads folder.
     *
     * @param imagePart the file part from the multipart form submission
     * @return the filename (e.g., "2025-04-10_cat.jpg"),
     *         or {@code null} if the file is invalid or the upload fails
     */
    public static String uploadImage(Part imagePart) {
        String fileName = imagePart.getSubmittedFileName();
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return null;
        }
        String extension = fileName.substring(dotIndex).toLowerCase();
        if (!extension.equals(".jpg") && !extension.equals(".jpeg") && !extension.equals(".png")) {
            return null;
        }

        String uniqueName = LocalDateTime.now().toString().replace(":", "-") + "_" + fileName;

        String uploadPath = System.getProperty("user.home") + File.separator + "learning-logs-uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            imagePart.write(uploadPath + File.separator + uniqueName);
            return uniqueName;
        } catch (IOException e) {
            System.out.println("Error uploading image: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a previously uploaded image file from the external folder.
     *
     * <p>Safely skips deletion if the path is null, empty, or points
     * to the default fallback image (static/images/book.png).</p>
     *
     * @param imagePath the filename of the image to delete
     */
    public static void deleteImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }
        if (imagePath.equals("static/images/book.png")) {
            return;
        }
        String uploadPath = System.getProperty("user.home") + File.separator + "learning-logs-uploads";
        File file = new File(uploadPath + File.separator + imagePath);
        try {
            if (!file.getCanonicalPath().startsWith(new File(uploadPath).getCanonicalPath())) {
                return;
            }
        } catch (IOException e) {
            return;
        }
        if (file.exists()) {
            file.delete();
        }
    }
}
