package com.hashrate.util;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Component
public class ImageOptimizer {
    
    @Value("${app.image.max-width:1920}")
    private int maxWidth;
    
    @Value("${app.image.max-height:1080}")
    private int maxHeight;
    
    @Value("${app.image.quality:0.85f}")
    private float quality;
    
    @Value("${app.image.thumbnail-width:300}")
    private int thumbnailWidth;
    
    @Value("${app.image.thumbnail-height:300}")
    private int thumbnailHeight;
    
    public byte[] optimizeImage(MultipartFile file) throws IOException {
        return optimizeImage(file.getInputStream(), file.getContentType());
    }
    
    public byte[] optimizeImage(InputStream inputStream, String contentType) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        
        if (originalImage == null) {
            throw new IOException("Failed to read image");
        }
        
        // Resize if necessary
        BufferedImage resizedImage = resizeImage(originalImage, maxWidth, maxHeight);
        
        // Compress
        return compressImage(resizedImage, contentType, quality);
    }
    
    public byte[] createThumbnail(MultipartFile file) throws IOException {
        return createThumbnail(file.getInputStream(), file.getContentType());
    }
    
    public byte[] createThumbnail(InputStream inputStream, String contentType) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        
        if (originalImage == null) {
            throw new IOException("Failed to read image");
        }
        
        // Create thumbnail
        BufferedImage thumbnail = Scalr.resize(originalImage, 
                                              Scalr.Method.QUALITY,
                                              Scalr.Mode.AUTOMATIC,
                                              thumbnailWidth,
                                              thumbnailHeight);
        
        // Compress
        return compressImage(thumbnail, contentType, quality);
    }
    
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // Check if resize is needed
        if (originalWidth <= targetWidth && originalHeight <= targetHeight) {
            return originalImage;
        }
        
        // Calculate new dimensions maintaining aspect ratio
        double widthRatio = (double) targetWidth / originalWidth;
        double heightRatio = (double) targetHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);
        
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);
        
        return Scalr.resize(originalImage, 
                           Scalr.Method.QUALITY,
                           Scalr.Mode.AUTOMATIC,
                           newWidth,
                           newHeight);
    }
    
    private byte[] compressImage(BufferedImage image, String contentType, float quality) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        // Get appropriate writer
        String formatName = getFormatName(contentType);
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
        
        if (!writers.hasNext()) {
            throw new IOException("No writer found for format: " + formatName);
        }
        
        ImageWriter writer = writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
        writer.setOutput(ios);
        
        ImageWriteParam param = writer.getDefaultWriteParam();
        
        // Set compression quality
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }
        
        // Write image
        writer.write(null, new IIOImage(image, null, null), param);
        
        // Cleanup
        ios.close();
        writer.dispose();
        
        return outputStream.toByteArray();
    }
    
    private String getFormatName(String contentType) {
        if (contentType == null) {
            return "jpg";
        }
        
        return switch (contentType.toLowerCase()) {
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
    }
    
    public ImageInfo getImageInfo(byte[] imageData) throws IOException {
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
        if (image == null) {
            throw new IOException("Failed to read image");
        }
        
        return new ImageInfo(image.getWidth(), image.getHeight(), imageData.length);
    }
    
    public record ImageInfo(int width, int height, int size) {}
}