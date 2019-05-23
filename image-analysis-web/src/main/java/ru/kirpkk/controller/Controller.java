package ru.kirpkk.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kirpkk.image_processing.*;
import ru.kirpkk.image_processing.matrix.MatrixTransformation;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

@RestController
class Controller {
    static String BASE_DIR = "D:\\Учебная литература\\Анализ изображений\\";

    @RequestMapping(value = "/app.js", method = RequestMethod.GET)
    public String scriptApp() throws IOException {
        try (InputStream is = Controller.class.getResourceAsStream("/app.js")) {
            return IOUtils.toString(is);
        }
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String renderHome() throws IOException {
        try (InputStream is = Controller.class.getResourceAsStream("/home.html")) {
            return IOUtils.toString(is);
        }
    }

    @RequestMapping(value = "/open", method = RequestMethod.GET)
    public String openImage(HttpServletResponse response) throws IOException {
        return response.toString();
    }

    @RequestMapping(value = "/image/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void openImageAsByteArray(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/transpose/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void transpose(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = MatrixTransformation.transpose(image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/verticalReflection/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void verticalReflection(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = MatrixTransformation.verticalReflection(image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/horizontalReflection/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void horizontalReflection(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = MatrixTransformation.horizontalReflection(image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/blend/{path}/{ext}/{path2}/{ext2}/{path3}/{ext3}", method = RequestMethod.GET)
    @ResponseBody
    public void blend(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                      @PathVariable String path2, @PathVariable String ext2,
                      @PathVariable String path3, @PathVariable String ext3) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage second = openImage(path2, ext2);
            BufferedImage alpha = openImage(path3, ext3);
            BufferedImage result = ImageProcessing.blend(image, second, alpha);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/grayscale/{type}/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void grayscale(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                          @PathVariable String type) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = null;
            switch (type) {
                case "fast":
                    result = ImageProcessing.fast(image);
                    break;
                case "desaturate":
                    result = ImageProcessing.desat(image);
                    break;
                case "human":
                    result = ImageProcessing.forHuman(image);
                    break;
                case "max":
                    result = ImageProcessing.gradMax(image);
                    break;
                case "min":
                    result = ImageProcessing.gradMin(image);
                    break;
            }
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/equalize/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void equalize(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = ImageProcessing.equalization(image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/histogram/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void getHistogram(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = ImageProcessing.getHistogram(image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/statistics/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public String getStatistics(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try (InputStream is = Controller.class.getResourceAsStream("/statistics.html")) {
            BufferedImage image = openImage(path, ext);
            Statistics statistics = new Statistics(image);
            return String.format(IOUtils.toString(is),
                    statistics.getrMean(),
                    statistics.getgMean(),
                    statistics.getbMean(),
                    statistics.getrVariance(),
                    statistics.getgVariance(),
                    statistics.getbVariance(),
                    statistics.getrKurtosis(),
                    statistics.getgKurtosis(),
                    statistics.getbKurtosis(),
                    statistics.getrSkewness(),
                    statistics.getgSkewness(),
                    statistics.getbSkewness(),
                    statistics.getrUniformity(),
                    statistics.getgUniformity(),
                    statistics.getbUniformity(),
                    statistics.getrEntropy(),
                    statistics.getgEntropy(),
                    statistics.getbEntropy());
        }
    }

    @RequestMapping(value = "/cooccurrence/{path}/{ext}/{dr}/{dc}", method = RequestMethod.GET)
    @ResponseBody
    public void getCooccurrenceMatrix(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                                      @PathVariable int dr, @PathVariable int dc) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = ImageProcessing.getCooccurerenceMatrixPlot(image, dr, dc);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/cooccurrenceUniformity/{path}/{ext}/{dr}/{dc}", method = RequestMethod.GET)
    @ResponseBody
    public double getCooccurrenceMatrixUniformity(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                                                  @PathVariable int dr, @PathVariable int dc) throws IOException {
        double result = 0;
        try {
            BufferedImage image = openImage(path, ext);
            result = ImageProcessing.getCooccurerenceMatrixUniformity(image, dr, dc);
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
        return result;
    }

    @RequestMapping(value = "/rotated/{path}/{ext}/{angle}", method = RequestMethod.GET)
    @ResponseBody
    public void getRotatedImage(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                                @PathVariable double angle) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = ImageRotating.getRotatedImage(angle, image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        }
    }

    @RequestMapping(value = "/quantize/{path}/{ext}/{newBpp}", method = RequestMethod.GET)
    @ResponseBody
    public void getQuantizedImage(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                                  @PathVariable byte newBpp) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = ImageQuantization.getQuantizedImage(newBpp, image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/binarize/{path}/{ext}/{threshold}", method = RequestMethod.GET)
    @ResponseBody
    public void getBinarizedImage(HttpServletResponse response, @PathVariable String path, @PathVariable String ext,
                                  @PathVariable int threshold) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            BufferedImage result = ImageProcessing.binary(image, threshold);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/markObjects/{path}/{ext}/{threshold}", method = RequestMethod.GET)
    @ResponseBody
    public void getImageWithObjects(HttpServletResponse response, @PathVariable String path,
                                    @PathVariable String ext, @PathVariable int threshold) throws IOException {
        try {
            BufferedImage image = ImageProcessing.binary(openImage(path, ext), threshold);
            BufferedImage result = ImageLabeling.markObjects(image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/Canny/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void getCannyEdges(HttpServletResponse response, @PathVariable String path,
                              @PathVariable String ext) throws IOException {
        try {
            CannyEdgeDetecting canny = new CannyEdgeDetecting(1, 100, 140);
            BufferedImage result = canny.processCanny(openImage(path, ext));
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/convolution/{width}/{height}", method = RequestMethod.GET)
    @ResponseBody
    public String convolution(@PathVariable byte width, @PathVariable byte height) throws IOException {
        final String input = "<input id=\"%s\"/>";
        final String get = " + document.getElementById(\"%s\").value + ','";
        try (InputStream is = Controller.class.getResourceAsStream("/convolution.html")) {
            String conv = IOUtils.toString(is);
            StringBuilder inputFields = new StringBuilder();
            StringBuilder getFields = new StringBuilder();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    inputFields.append(String.format(input, i + "." + j));
                    getFields.append(String.format(get, i + "." + j));
                }
                inputFields.append("<br/>\n");
            }
            conv = String.format(conv, inputFields.toString(), getFields.toString());
            return conv;
        }
    }

    @RequestMapping(value = "/convolution/{width}/{height}/{path}/{ext}/{mainRow}/{mainColumn}/{aperture}", method = RequestMethod.GET)
    @ResponseBody
    public void convolution(HttpServletResponse response,
                            @PathVariable int width, @PathVariable int height,
                            @PathVariable String path, @PathVariable String ext,
                            @PathVariable String aperture,
                            @PathVariable int mainRow, @PathVariable int mainColumn) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            Float[][] f = new Float[height][width];
            String[] apertureElements = aperture.split(",");

            int denominator = Integer.valueOf(apertureElements[width * height]);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    f[i][j] = Float.valueOf(apertureElements[i * width + j]) / denominator;
                }
            }

            BufferedImage result = ImageFiltering.convolution(image, f, height, width, mainRow, mainColumn);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/rankFiltering/{width}/{height}", method = RequestMethod.GET)
    @ResponseBody
    public String rankFiltering(@PathVariable byte width, @PathVariable byte height) throws IOException {
        final String input = "<input id=\"%s\"/>";
        final String get = " + document.getElementById(\"%s\").value + ','";
        try (InputStream is = Controller.class.getResourceAsStream("/rankFiltering.html")) {
            String rankFiltering = IOUtils.toString(is);
            StringBuilder inputFields = new StringBuilder();
            StringBuilder getFields = new StringBuilder();
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    inputFields.append(String.format(input, i + "." + j));
                    getFields.append(String.format(get, i + "." + j));
                }
                inputFields.append("<br/>\n");
            }
            rankFiltering = String.format(rankFiltering, inputFields.toString(), getFields.toString());
            return rankFiltering;
        }
    }

    @RequestMapping(value = "/rankFiltering/{width}/{height}/{path}/{ext}/{mainRow}/{mainColumn}/{rank}/{aperture}", method = RequestMethod.GET)
    @ResponseBody
    public void rankFiltering(HttpServletResponse response,
                              @PathVariable int width, @PathVariable int height,
                              @PathVariable String path, @PathVariable String ext,
                              @PathVariable int rank, @PathVariable String aperture,
                              @PathVariable int mainRow, @PathVariable int mainColumn) throws IOException {
        try {
            BufferedImage image = openImage(path, ext);
            int[][] f = new int[height][width];
            String[] apertureElements = aperture.split(",");

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    f[i][j] = Integer.valueOf(apertureElements[i * width + j]);
                }
            }

            BufferedImage result = ImageFiltering.rankFilter(image, f, height, width, mainRow, mainColumn, rank);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(result, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/readBMP/{path}/{ext}", method = RequestMethod.GET)
    @ResponseBody
    public void readBMP(HttpServletResponse response, @PathVariable String path, @PathVariable String ext) throws IOException {
        try {
            BMPReader bmpReader = new BMPReader();
            BufferedImage image = bmpReader.read(Paths.get(BASE_DIR, path + "." + ext).toUri());
//            BufferedImage result = ImageQuantization.getQuantizedImage(newBpp, image);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            response.sendError(400, "Incorrect image");
        } catch (Exception e) {
            response.sendError(400, e.getMessage());
        }
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ResponseBody
    public void getImageAsByteArray(HttpServletResponse response) throws IOException {
        BufferedImage image = new BufferedImage(256, 256, TYPE_INT_RGB);
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                image.setRGB(i, j, new Color(80, 160, 50).getRGB());
            }
        }
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        ImageIO.write(image, "jpg", response.getOutputStream());
    }

    public static BufferedImage openImage(String path, String ext) throws IOException {
        if (path == null || ext == null || path.isEmpty() || ext.isEmpty())
            throw new IllegalArgumentException("Incorrect image");
        String fullPath = BASE_DIR + path + "." + ext;
        File file = new File(fullPath);
        return ImageIO.read(file);
    }

}