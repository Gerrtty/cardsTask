import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        String[] nums = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] suit = {"d", "s", "h", "c"};

        ArrayList<Figure> list = new ArrayList<>();
        ArrayList<Figure> suits = new ArrayList<>();

        for (String s : nums) {
            list.add(new Figure(s));
        }

        for (String s : suit) {
            suits.add(new Figure(s));
        }

        File folder = new File("/home/yuliya/Desktop/java_test_task/imgs/");

        if (args.length > 0) {
            folder = new File(args[0]);
        }

        File[] folderEntries = folder.listFiles();

        for (File picture : folderEntries) {
//            System.out.println(picture.getName());
            BufferedImage img = ImageIO.read(picture);

            BufferedImage img1 = img.getSubimage(137, img.getHeight() - img.getHeight() / 2, 360, 90);

            String outS = "";

            for (int i = 0; i < 5; i++) {
                BufferedImage img2 = img1.getSubimage(i * (img1.getWidth() / 5), 0, (img1.getWidth() / 5), img1.getHeight());

                BufferedImage info = img2.getSubimage(5, 0, img2.getWidth() / 2, img2.getHeight() / 2 + 10);
                BufferedImage n = img2.getSubimage(0, 32, img2.getWidth(), img2.getHeight() - 32);
                File fileN = new File("n.png");
                ImageIO.write(n, "png", fileN);

                BufferedImage num = info.getSubimage(4, 5, info.getWidth() - 4, 30);
                File fileNum = new File("num.png");

                ImageIO.write(num, "png", fileNum);
                Figure figure = new Figure("n");

                Figure nF = new Figure("num");

                HashMap<String, Integer> mapM = new HashMap<>();
                HashMap<String, Integer> mapN = new HashMap<>();

                for (int j = 0; j < nums.length; j++) {
                    mapN.put(nums[j], nF.getDiff(list.get(j)));
                }

                for (int j = 0; j < suit.length; j++) {
                    mapM.put(suit[j], figure.getDiff(suits.get(j)));
                }

                outS += min(mapN, "num") + min(mapM, "suit");
            }

            System.out.println(outS);
        }

    }

    private static String min(HashMap<String, Integer> map, String name) {
        String minS = "";
        int min = Integer.MAX_VALUE;
        for(Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() < min) {
                min = entry.getValue();
                minS = entry.getKey();
            }
        }
        return min < 1000 && name.equals("suit") ? minS : min < 500 && name.equals("num") ? minS : "";
    }

}

class Figure {

    private int[][] input;
    private int x, y;

    protected Figure(String name) throws IOException {
        BufferedImage image = ImageIO.read(new File(name +  ".png"));
        input = new int[image.getHeight()][image.getWidth()];
        x = image.getHeight(); y = image.getWidth();

        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                Color color = new Color(image.getRGB(j, i));
                if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255) input[i][j] = 0;

                else input[i][j] = 1;
            }
        }
    }

    protected int getDiff(Figure figure2) {
        int count = 0;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) count += this.input[i][j] - figure2.input[i][j] == 0 ? 0 : 1;
        }
        return count;
    }
}