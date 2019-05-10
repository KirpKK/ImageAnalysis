package ru.kirpkk.image_processing;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.BitSet;

import static ru.kirpkk.image_processing.matrix.MatrixTransformation.horizontalReflection;

public class BMPReader {
    BufferedImage image;
    byte[] bitMapFileHeader;
    byte[] bitMapInfo;
    byte[] pixelData;
    long size;
    long pixelDataOffset;
    int version;
    int width;
    int height;
    int bpp;
    int compression;
    int clrUsed;
    int[] palette; //RGB color

    public BufferedImage read(URI uri) throws IOException, BMPReaderException {
        File file = new File(uri);
        try (FileInputStream is = new FileInputStream(file)) {
            bitMapFileHeader = readBitMapFileHeader(is);
            size = getSize();
            pixelDataOffset = getPixelDataOffset();

            bitMapInfo = readBitMapInfo(is);
            version = (int) getVersion();
            width = (int) getWidth();
            height = (int) getHeight();
            bpp = (int) getBpp();
            compression = (int) getCompression();
            clrUsed = (int) getClrUsed();

            pixelData = readPixelData(is);
            readImage();

        }
        return image;
    }

    private static byte[] readBitMapFileHeader(FileInputStream is) throws IOException, BMPReaderException {
        byte[] bitMapFileHeader = new byte[14];

        int numOfReadBytes = is.read(bitMapFileHeader, 0, 14);
        if (numOfReadBytes != 14) throw new BMPReaderException("Error while reading bitMapFileHeader");
        return bitMapFileHeader;
    }

    private byte[] readBitMapInfo(FileInputStream is) throws IOException, BMPReaderException {
        byte[] bitMapInfo = new byte[(int) pixelDataOffset - 14];
        int numOfReadBytes = is.read(bitMapInfo, 0, (int) pixelDataOffset - 14);
        if (numOfReadBytes != (int) pixelDataOffset - 14)
            throw new BMPReaderException("Error while reading bitMapFileHeader");
        return bitMapInfo;
    }

    private byte[] readPixelData(FileInputStream is) throws IOException, BMPReaderException {
        byte[] pixelData = new byte[(int) (size - pixelDataOffset)];

        int numOfReadBytes = is.read(pixelData, 0, (int) (size - pixelDataOffset));
        if (numOfReadBytes != (int) (size - pixelDataOffset))
            throw new BMPReaderException("Error while reading bitMapFileHeader");
        return pixelData;
    }

    private long getSize() {
        byte[] sizeBytes = getZero8ByteArray();
        sizeBytes[4] = bitMapFileHeader[5];
        sizeBytes[5] = bitMapFileHeader[4];
        sizeBytes[6] = bitMapFileHeader[3];
        sizeBytes[7] = bitMapFileHeader[2];
        return bytesToLong(sizeBytes);
    }

    private long getPixelDataOffset() {
        byte[] pixelDataOffsetBytes = getZero8ByteArray();
        pixelDataOffsetBytes[4] = bitMapFileHeader[13];
        pixelDataOffsetBytes[5] = bitMapFileHeader[12];
        pixelDataOffsetBytes[6] = bitMapFileHeader[11];
        pixelDataOffsetBytes[7] = bitMapFileHeader[10];
        return bytesToLong(pixelDataOffsetBytes);
    }

    private long getVersion() {
        byte[] versionBytes = getZero8ByteArray();
        versionBytes[4] = bitMapInfo[3];
        versionBytes[5] = bitMapInfo[2];
        versionBytes[6] = bitMapInfo[1];
        versionBytes[7] = bitMapInfo[0];
        return bytesToLong(versionBytes);
    }

    private long getWidth() throws BMPReaderException {
        //CORE
        byte[] widthBytes = getZero8ByteArray();
        if (version == 12) {
            widthBytes[6] = bitMapInfo[5];
            widthBytes[7] = bitMapInfo[4];
            return bytesToLong(widthBytes);
        } //version 3
        else if (version == 40) {
            widthBytes[4] = bitMapInfo[7];
            widthBytes[5] = bitMapInfo[6];
            widthBytes[6] = bitMapInfo[5];
            widthBytes[7] = bitMapInfo[4];
            return bytesToLong(widthBytes);
        } else throw new BMPReaderException("Version isn't supported");
    }

    private long getHeight() throws BMPReaderException {
        //CORE
        byte[] heightBytes = getZero8ByteArray();
        if (version == 12) {
            heightBytes[6] = bitMapInfo[7];
            heightBytes[7] = bitMapInfo[6];
            return bytesToLong(heightBytes);
        } //version 3
        else if (version == 40) {
            heightBytes[4] = bitMapInfo[11];
            heightBytes[5] = bitMapInfo[10];
            heightBytes[6] = bitMapInfo[9];
            heightBytes[7] = bitMapInfo[8];
            return bytesToLong(heightBytes);
        } else throw new BMPReaderException("Version isn't supported");
    }

    private long getBpp() throws BMPReaderException {
        //CORE
        byte[] bppBytes = getZero8ByteArray();
        if (version == 12) {
            bppBytes[6] = bitMapInfo[11];
            bppBytes[7] = bitMapInfo[10];
            return bytesToLong(bppBytes);
        } //version 3
        else if (version == 40) {
            bppBytes[6] = bitMapInfo[15];
            bppBytes[7] = bitMapInfo[14];
            return bytesToLong(bppBytes);
        } else throw new BMPReaderException("Version isn't supported");
    }

    private long getCompression() throws BMPReaderException {
        //CORE
        byte[] compressionBytes = getZero8ByteArray();
        if (version == 12) {
            return 0;
        } //version 3
        else if (version == 40) {
            compressionBytes[4] = bitMapInfo[19];
            compressionBytes[5] = bitMapInfo[18];
            compressionBytes[6] = bitMapInfo[17];
            compressionBytes[7] = bitMapInfo[16];
            return bytesToLong(compressionBytes);
        } else throw new BMPReaderException("Version isn't supported");
    }

    private long getClrUsed() throws BMPReaderException {
        //CORE
        byte[] clrUsedBytes = getZero8ByteArray();
        if (version == 12) {
            return (long) Math.pow(2, bpp);
        } //version 3
        else if (version == 40) {
            clrUsedBytes[4] = bitMapInfo[35];
            clrUsedBytes[5] = bitMapInfo[34];
            clrUsedBytes[6] = bitMapInfo[33];
            clrUsedBytes[7] = bitMapInfo[32];
            return bytesToLong(clrUsedBytes);
        } else throw new BMPReaderException("Version isn't supported");
    }

    private int[] getPalette() throws BMPReaderException {
        //CORE
        if (version == 12) {
            int[] palette = new int[(int) Math.pow(2, bpp)];
            for (int i = 0; i < Math.pow(2, bpp); i++) {
                byte[] redBytes = getZero8ByteArray();
                byte[] greenBytes = getZero8ByteArray();
                byte[] blueBytes = getZero8ByteArray();
                blueBytes[7] = bitMapInfo[12 + 3 * i];
                greenBytes[7] = bitMapInfo[12 + 3 * i + 1];
                redBytes[7] = bitMapInfo[12 + 3 * i + 2];
                palette[i] = new Color((int) bytesToLong(redBytes), (int) bytesToLong(greenBytes), (int) bytesToLong(blueBytes)).getRGB();
            }
            return palette;
        } //version 3
        else if (version == 40) {
            int[] palette = new int[(int) Math.pow(2, bpp)];
            for (int i = 0; i < Math.pow(2, bpp); i++) {
                byte[] redBytes = getZero8ByteArray();
                byte[] greenBytes = getZero8ByteArray();
                byte[] blueBytes = getZero8ByteArray();
                blueBytes[7] = bitMapInfo[40 + 4 * i ];
                greenBytes[7] = bitMapInfo[40 + 4 * i + 1];
                redBytes[7] = bitMapInfo[40 + 4 * i + 2];
                palette[i] = new Color((int) bytesToLong(redBytes), (int) bytesToLong(greenBytes), (int) bytesToLong(blueBytes)).getRGB();
            }
            return palette;
        } else throw new BMPReaderException("Version isn't supported");
    }

    private void readImage() throws BMPReaderException {
        image = new BufferedImage(width, height, 1);
        // two-dimensional array
        if (bpp <= 8 && compression == 0) {
            palette = getPalette();
            BitSet pixelBits = BitSet.valueOf(pixelData);

            int rowLength = (int) Math.ceil((double) (width * bpp) / 32) * 4; // in bytes
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < rowLength; i++) {
                    BitSet bits = pixelBits.get((j * rowLength + i) * 8, (j * rowLength + i + 1) * 8);
                    setColors(bits, i, j);
                }
            }
            if (height > 0) {
                image = horizontalReflection(image);
            }
        } // RLE8 / RLE4
        else if ((bpp == 8 && compression == 1) || (bpp == 4 && compression == 2)) {
            throw new BMPReaderException("RLE isn't supported yet");
        } else throw new BMPReaderException("Version isn't supported");
    }

    private void setColors(BitSet bits, int i, int j) {
        for (byte k = 0; k < 8 / bpp; k++) {
            if (i * (8 / bpp) + k < width) {
                image.setRGB(i * (8 / bpp) + k, j, palette[getColorIndex(bits, k)]);
            }
        }
    }

    private int getColorIndex(BitSet bits, byte k) {
        byte[] bytes = getZero8ByteArray();
        bytes[7] = (bits.get(8 - (k + 1) * bpp, 8 - k * bpp).toByteArray().length == 0) ?
                0 : bits.get(8 - (k + 1) * bpp, 8 - k * bpp).toByteArray()[0];
        return (int) bytesToLong(bytes);
    }

    static class BMPReaderException extends Exception {
        BMPReaderException(String message) {
            super(message);
        }
    }

    private static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    private static byte[] getZero8ByteArray() {
        byte[] byteArray = new byte[8];
        for (byte i = 0; i < 8; i++) {
            byteArray[i] = 0;
        }
        return byteArray;
    }
}