import java.awt.*;                         // For Graphics2D and RenderingHints
import java.awt.image.*;       // For handling image data in memory
import java.io.File;                       // For file and directory operations
import java.io.IOException;                // For handling IO exceptions
import javax.imageio.ImageIO;              // For reading and writing image files

public class files {
    public static void main(String[] args) {
        String img="C:\\Users\\meira\\Downloads\\cds.png";
        convert_to_black_white(img);
        // Define the path to the directory containing the images
        String path = "C:\\Users\\meira\\OneDrive\\Pictures\\test"; // Change this to your desired path
        
        // Get all image filenames from the directory and resize each one
        //for (String file : get_files(path)) {
         //   resize_image(path + "\\" + file); // Process each file
        //}
    }

    // Method to retrieve all file names in a given directory
    public static String[] get_files(String path) {
        File f = new File(path); // Create a File object for the given path

        // Check if the path is a directory (not a single file)
        if (!f.isFile()) {
            File list[] = f.listFiles(); // Get all files in the directory

            if (list != null) {
                String files[] = new String[list.length];

                // Store the names of all files in the directory
                for (int i = 0; i < list.length; i++) {
                    files[i] = list[i].getName();
                }
                return files; // Return the array of file names
            }
        }
        return new String[0]; // Return empty array if path is not a directory or error occurs
    }

    // Method to resize an image to half its original dimensions
    public static void resize_image(String path) {
        // Check if the file has a .png or .jpg extension
        if (path.endsWith(".png") || path.endsWith(".jpg")) {
            try {
                // Read the image file into memory
                BufferedImage img = ImageIO.read(new File(path));

                if (img == null) {
                    System.out.println("Failed to read image: " + path);
                    return;
                }

                // Calculate new dimensions (half the original size)
                int width = img.getWidth() / 2;
                int height = img.getHeight() / 2;

                // Create a new image with the new dimensions
                BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

                // Create a Graphics2D object to draw the resized image
                Graphics2D g = resizedImage.createGraphics();

                // Set rendering hints for better image quality
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                // Draw the scaled image
                g.drawImage(img, 0, 0, width, height, null);
                g.dispose(); // Release system resources

                // Get the image format (extension)
                String format = path.substring(path.lastIndexOf(".") + 1);

                // Overwrite the original image file with the resized image
                ImageIO.write(resizedImage, format, new File(path));

                System.out.println("Resized: " + path);

            } catch (IOException e) {
                System.out.println("Error resizing image: " + path + " " + e.getMessage());
            }
        }
    }
    public static void convert_to_black_white(String path) {
        if (path.endsWith(".png") || path.endsWith(".jpg")) {
            try {
                BufferedImage img = ImageIO.read(new File(path));
                if (img == null) {
                    System.out.println("Failed to read image: " + path);
                    return;
                }
    
                int width = img.getWidth();
                int height = img.getHeight();
                // Create new image and set the grayscale pixels
                for(int i=0;i<height;i++){
                    for(int j=0; j<width; j++) {
                    
                       Color c = new Color(img.getRGB(j, i));
                       int red = (int)(c.getRed() * 0.299);
                       int green = (int)(c.getGreen() * 0.587);
                       int blue = (int)(c.getBlue() *0.114);
                       Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
                       img.setRGB(j,i,newColor.getRGB());
                    }
                 }
                String format = path.substring(path.lastIndexOf(".") + 1);
                ImageIO.write(img, format, new File(path));
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }    
}
