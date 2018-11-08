package com.hongtu.slice.util;

import com.google.common.base.Throwables;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.awt.image.BufferedImage;

//import com.hongtu.slice.poi.ComDocIO;
import com.hongtu.slice.cplus.ComDocIO;

public class MDSFile {
    private Logger LOGGER = LoggerFactory.getLogger(MDSFile.class);
    private String xml_path;
    private int width;
    private int height;
    private int cellWidth;
    private int cellHeight;
    private int maxLevel;
    private int minLevel;
    private int layerCount;
    private ComDocIO io;
    BufferedImage minLevelImage;
    public Map<Integer, LayerProperty> layerPropertyMap = new HashMap<Integer, LayerProperty>();

    public MDSFile(Object context) {
        Properties configuration = (Properties) context;
        xml_path = configuration.getProperty("xml.path");
    }

    public void init(String fileName) {
        io = new ComDocIO(fileName);
        parseXml();
        acquireMinLevelImage();
    }


    public int acquireMaxLevel(int in_height, int in_width) {
        int heightLevel = (int) Math.ceil(Math.log(in_height) / Math.log(2));
        int widthLevel = (int) Math.ceil(Math.log(in_width) / Math.log(2));
        return heightLevel > widthLevel ? heightLevel : widthLevel;
    }

    private void parseXml() {
        if (io == null)
            return;
        byte[] rawXmlContent = io.readFromPath(xml_path);
        String xmlContent = "";
        for (int i = 0; i < rawXmlContent.length; i += 2) {
            xmlContent += (char) rawXmlContent[i];
        }
        SAXBuilder sb = new SAXBuilder();
        try {
            Document doc = sb.build(new StringReader(xmlContent));
            Element root = doc.getRootElement();
            Element imageMatrix = root.getChild("ImageMatrix");
            width = Integer.valueOf(imageMatrix.getChild("Width").getAttribute("value").getValue());
            height = Integer.valueOf(imageMatrix.getChild("Height").getAttribute("value").getValue());
            cellWidth = Integer.valueOf(imageMatrix.getChild("CellWidth").getAttribute("value").getValue());
            cellHeight = Integer.valueOf(imageMatrix.getChild("CellHeight").getAttribute("value").getValue());
            layerCount = Integer.valueOf(imageMatrix.getChild("LayerCount").getAttribute("value").getValue()) - 1;
            maxLevel = acquireMaxLevel(height, width);
            minLevel = maxLevel - layerCount + 1;
            for (int i = maxLevel; i >= minLevel; i--) {
                LayerProperty layerProperty = new LayerProperty();
                Element layer = imageMatrix.getChild("Layer" + (maxLevel - i));
                int rowCount = Integer.valueOf(layer.getChild("Rows").getAttribute("value").getValue());
                int colCount = Integer.valueOf(layer.getChild("Cols").getAttribute("value").getValue());
                layerProperty.setRowCount(rowCount);
                layerProperty.setColCount(colCount);
                layerProperty.setFolderName(layer.getChild("Scale").getAttribute("value").getValue());
                layerProperty.setScaleVal(Float.valueOf(layer.getChild("Scale").getAttribute("value").getValue()));
                int currentWidth = (int) (width / Math.pow(2.0, maxLevel - i));
                int currentHeight = (int) (height / Math.pow(2.0, maxLevel - i));
                int lastImageWidth = currentWidth % cellWidth;
                int lastImageHeight = currentHeight % cellHeight;
                layerProperty.setCurrentWidth(currentWidth);
                layerProperty.setCurrentHeight(currentHeight);
                layerProperty.setLastImageWidth(lastImageWidth);
                layerProperty.setLastImageHeight(lastImageHeight);
                layerProperty.setWidth(colCount * cellWidth);
                layerProperty.setHeight(rowCount * cellHeight);
                layerPropertyMap.put(i, layerProperty);
            }
            LayerProperty maxLevelLayerProperty = layerPropertyMap.get(maxLevel);
            for (int i = minLevel - 1; i >= 1; i--) {
                LayerProperty layerProperty = new LayerProperty();
                int currentWidth = (int) (maxLevelLayerProperty.getWidth() / Math.pow(2, maxLevel - i));
                int currentHeight = (int) (maxLevelLayerProperty.getHeight() / Math.pow(2, maxLevel - i));
                int colCount = (int) Math.ceil(currentWidth * 1.0 / cellWidth);
                int rowCount = (int) Math.ceil(currentHeight * 1.0 / cellHeight);
                int lastImageWidth = currentWidth % cellWidth;
                int lastImageHeight = currentHeight % cellHeight;
                float scaleValue = (float) (1 / Math.pow(2, maxLevel - i));
                layerProperty.setRowCount(rowCount);
                layerProperty.setColCount(colCount);
                layerProperty.setCurrentWidth(currentWidth);
                layerProperty.setCurrentHeight(currentHeight);
                layerProperty.setLastImageWidth(lastImageWidth);
                layerProperty.setLastImageHeight(lastImageHeight);
                layerProperty.setWidth(colCount * cellWidth);
                layerProperty.setHeight(rowCount * cellHeight);
                layerProperty.setScaleVal(scaleValue);
                layerPropertyMap.put(i, layerProperty);
            }

        } catch (Exception ex) {
            LOGGER.error("parseXml faild :{}", Throwables.getStackTraceAsString(ex));

        }
    }

    private void acquireMinLevelImage() {
        LayerProperty minLevelLayerProperty = layerPropertyMap.get(minLevel);
        try {
            minLevelImage = new BufferedImage(minLevelLayerProperty.getWidth(), minLevelLayerProperty.getHeight(), BufferedImage.TYPE_INT_BGR);
            Graphics2D graphics = minLevelImage.createGraphics();
            for (int i = 0; i < minLevelLayerProperty.getRowCount(); i++)
                for (int j = 0; j < minLevelLayerProperty.getColCount(); j++) {
                    byte[] rawData = io.readFromPath(getPathByLevelAndPos(minLevel, j, i));
                    ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData));
                    BufferedImage img = ImageIO.read(iis);
                    graphics.drawImage(img, null, j * cellWidth, i * cellHeight);
                }
        } catch (Exception ex) {
            LOGGER.error("acquireMinLevelImage faild :{}", Throwables.getStackTraceAsString(ex));
        }
    }

    public String getPathByLevelAndPos(int level, int x, int y) {
        String folderName = layerPropertyMap.get(level).getFolderName();
        return String.format("/DSI0/%s/%04d_%04d", folderName, y, x);
    }

    public byte[] getTileData(int level, int x, int y) {
        byte[] data = null;
        if (level > maxLevel || level <= 1) {
            return null;
        }
        try {
            LayerProperty layerProperty = layerPropertyMap.get(level);
            if (level >= minLevel) {
                String path = getPathByLevelAndPos(level, x, y);
                data = io.readFromPath(path);
                if (x == layerProperty.getColCount() - 1 && y < layerProperty.getRowCount() - 1) {
                    ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
                    BufferedImage img = ImageIO.read(iis);
                    BufferedImage cropImage = img.getSubimage(0, 0, layerProperty.getLastImageWidth(), cellHeight);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(cropImage, "jpg", byteArrayOutputStream);
                    data = byteArrayOutputStream.toByteArray();
                }
                if (x < layerProperty.getColCount() - 1 && y == layerProperty.getRowCount() - 1) {
                    ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
                    BufferedImage img = ImageIO.read(iis);
                    BufferedImage cropImage = img.getSubimage(0, 0, cellWidth, layerProperty.getLastImageHeight());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(cropImage, "jpg", byteArrayOutputStream);
                    data = byteArrayOutputStream.toByteArray();
                }
                if (x == layerProperty.getColCount() - 1 && y == layerProperty.getRowCount() - 1) {
                    ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
                    BufferedImage img = ImageIO.read(iis);
                    BufferedImage cropImage = img.getSubimage(0, 0, layerProperty.getLastImageWidth(), layerProperty.getLastImageHeight());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ImageIO.write(cropImage, "jpg", byteArrayOutputStream);
                    data = byteArrayOutputStream.toByteArray();
                }
            } else {
                BufferedImage resizeImage = zoomImage(minLevelImage, 1 / Math.pow(2, minLevel - level));
                BufferedImage resultImage;
                if (x < layerProperty.getColCount() - 1 && y < layerProperty.getRowCount() - 1) {
                    resultImage = resizeImage.getSubimage(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
                } else if (x == layerProperty.getColCount() - 1 && y < layerProperty.getRowCount() - 1) {
                    resultImage = resizeImage.getSubimage(x * cellWidth, y * cellHeight, layerProperty.getLastImageWidth(), cellHeight);
                } else if (x < layerProperty.getColCount() - 1 && y == layerProperty.getRowCount() - 1) {
                    resultImage = resizeImage.getSubimage(x * cellWidth, y * cellHeight, cellWidth, layerProperty.getLastImageHeight());
                } else {
                    resultImage = resizeImage.getSubimage(x * cellWidth, y * cellHeight, layerProperty.getLastImageWidth(), layerProperty.getLastImageHeight());
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(resultImage, "jpg", byteArrayOutputStream);
                data = byteArrayOutputStream.toByteArray();
            }
        } catch (Exception ex) {
            LOGGER.error("getTileData faild :{}", Throwables.getStackTraceAsString(ex));
        }
        return data;
    }

    public static BufferedImage zoomImage(BufferedImage im, double resizeTimes) {
        int width = im.getWidth();
        int height = im.getHeight();
        int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
        int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);
        // /* 新生成结果图片 */
        BufferedImage result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
        result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);
        return result;
    }

    public byte[] getThumbnail(int width) {
        byte[] data = null;
        LayerProperty minLevelLayerProperty=layerPropertyMap.get(minLevel);
        BufferedImage clipImage=minLevelImage.getSubimage(0,0,minLevelLayerProperty.getCurrentWidth(),minLevelLayerProperty.getCurrentHeight());
        double resizeTimes = (1.0 * width) / minLevelLayerProperty.getCurrentWidth();
        BufferedImage bufferedImage = zoomImage(clipImage, resizeTimes);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            data = byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex){
            LOGGER.error("getThumbnail faild :{}", Throwables.getStackTraceAsString(ex));
        }
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getLayerCount() {
        return layerCount;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getMinLevel() {
        return minLevel;
    }

}
