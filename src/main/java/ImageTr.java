import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Random;
import java.util.Scanner;

public class ImageTr {




    /**
     * read a pgm file
     *
     * @param fileName
     * @return
     */
    public static int[][] readImage(String fileName) {
        try {
            String filePath = fileName;
            FileInputStream fileInputStream = new FileInputStream(filePath);
            Scanner scan = new Scanner(fileInputStream);
            // Discard the magic number
            scan.nextLine();
            // Discard the comment line
            scan.nextLine();
            // Read pic width, height and max value
            int picWidth = scan.nextInt();
            int picHeight = scan.nextInt();
            int maxvalue = scan.nextInt();
            // Read the image data
            int[][] data2D = new int[picHeight][picWidth];
            for (int row = 0; row < picHeight; row++) {
                for (int col = 0; col < picWidth; col++) {
                    data2D[row][col] = scan.nextInt();
                    //System.out.print(data2D[row][col] + " ");
                }
                //System.out.println();
            }
            return data2D;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * write a pgm file
     *
     * @param fileName
     * @param tab
     * @param width
     * @param height
     */
    public static void writeImage(String fileName, int[][] tab, int width, int height) {
        try {
            //specify the name of the output..
            FileWriter fstream = new FileWriter(fileName);
            //we create a new BufferedWriter
            BufferedWriter out = new BufferedWriter(fstream);
            //we add the header, 128 128 is the width-height and 63 is the max value-1 of ur data
            out.write("P2\n# Comment here :) \n" + width + " " + height + "\n255\n");
            //2 loops to read the 2d array
            for (int i = 0; i < tab.length; i++) {


                for (int j = 0; j < tab[i].length; j++)
                    //we write in the output the value in the position ij of the array
                    out.write(tab[i][j] + " ");
                out.write("\n");
            }
            //we close the bufferedwritter
            out.close();
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }

    /**
     * calculate the average of a matrix
     *
     * @param d
     * @return
     */
    public static double average(int[][] d) {
        long sum = 0;
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                sum += d[i][j];
            }
        }
        double moy = sum / (d.length * d[0].length);
        return moy;
    }

    /**
     * calculate the standard deviation of a matrix
     *
     * @param d
     * @return
     */
    public static double standardDeviation(int[][] d) {
        double sd = 0;
        double moy = average(d);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                sd += (d[i][j] - moy) * (d[i][j] - moy);
            }
        }
        sd = Math.sqrt(sd / (d.length * d[0].length));
        return sd;

    }

    /**
     * calcuate the histogram of a matrix
     *
     * @param tab
     * @return
     */
    public static int[] histogramGrid(int[][] tab) {
        int[] tabPixel = new int[256];
        for (int j = 0; j <= 255; j++) {
            tabPixel[j] = 0;
        }
        for (int j = 0; j < tab.length; j++) {
            for (int i = 0; i < tab[j].length; i++) {
                tabPixel[tab[j][i]]++;
            }
        }
        for (int j = 0; j <= 255; j++) {
            System.out.print(tabPixel[j] + " | ");
        }
        return tabPixel;
    }

    /**
     * calculate the cumulated Histogramof a matrix
     *
     * @param tab
     * @param width
     * @param height
     * @return
     */
    public static double[] histogramCumulatif(int[][] tab, int width, int height) {
        int[] tabPixel = new int[256];
        for (int j = 0; j <= 255; j++) {
            tabPixel[j] = 0;
        }
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                tabPixel[tab[j][i]]++;
            }
        }
        double[] tabCumul = new double[256];
        tabCumul[0] = tabPixel[0];
        for (int j = 1; j < tabPixel.length; j++) {
            tabCumul[j] = tabCumul[j - 1] + tabPixel[j];
        }
        for (int j = 0; j <= 255; j++) {
            tabCumul[j] = tabCumul[j] / (height * width);
            System.out.print(tabCumul[j] + " | ");
        }
        return tabCumul;
    }

    /**
     * calculate the equalized histogram of a matrix
     *
     * @param tabcumul
     * @param h
     * @param total
     */
    public static void histogrammeEgalise(double[] tabcumul, double[] h, int total) {
        double[] a = new double[tabcumul.length];
        for (int i = 0; i < tabcumul.length; i++) {
            a[i] = (tabcumul.length - 1) * tabcumul[i];
        }
        System.out.println("a :");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " | ");
        }
        int hp = total / tabcumul.length;
        System.out.println("hp: " + hp);
        int[] n1 = new int[tabcumul.length];

        for (int i = 0; i < n1.length; i++) {
            n1[i] = (int) a[i];
        }
        System.out.println("n1 :");
        for (int i = 0; i < n1.length; i++) {
            System.out.print(n1[i] + " | ");
        }
        System.out.println("\nheg :");
        int[] heg = new int[tabcumul.length];
        for (int i = 0; i < tabcumul.length; i++) {
            int val = 0;
            for (int j = 0; j < n1.length; j++) {
                if (n1[j] == i)
                    val += h[j];
            }
            heg[i] = val;
        }
        for (int j = 0; j < tabcumul.length; j++) {
            System.out.print(heg[j] + " | ");
        }
    }

    /**
     * apply linear transition
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param h
     * @param d
     */
    public static void transLineare(int x1, int y1, int x2, int y2, int[] h, int[][] d) {
        double a1 = y1 / x1;
        double a2 = (double) (y2 - y1) / (x2 - x1);
        double b2 = y2 - a2 * x2;
        double a3 = (double) (255 - y2) / (255 - x2);
        double b3 = 255 - a3 * 255;

        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                if (d[i][j] <= x1) {
                    d[i][j] = (int) (a1 * d[i][j]);
                } else if (d[i][j] <= x2) {
                    d[i][j] = (int) (a2 * d[i][j] + b2);
                } else {
                    d[i][j] = (int) (a3 * d[i][j] + b3);
                }
            }
        }
        writeImage("output.pgm", d, 320, 240);

    }

    /**
     * apply random distortion to an image
     *
     * @param fileName
     * @param tab
     * @param width
     * @param height
     */

    public static void bruit(String fileName, int[][] tab, int width, int height) {
        try {
            //specify the name of the output..
            FileWriter fstream = new FileWriter(fileName);
            //we create a new BufferedWriter
            BufferedWriter out = new BufferedWriter(fstream);
            Random rand = new Random(); //instance of random class
            int upperbound = 21;
            //generate random values from 0-21
            int int_random;
            //we add the header, 128 128 is the width-height and 63 is the max value-1 of ur data
            out.write("P2\n# Comment here :) \n" + width + " " + height + "\n255\n");
            //2 loops to read the 2d array
            for (int i = 0; i < tab.length; i++) {


                for (int j = 0; j < tab[i].length; j++) {

                    //generate random number between 0 and 21
                    int_random = rand.nextInt(upperbound);
                    // if number == 0 replace current pixel with 0
                    if (int_random == 0) {
                        out.write(0 + " ");
                        //if number == 20 replace current pixel with 255
                    } else if (int_random == 20) {
                        out.write(255 + " ");
                    } else {
                        out.write(tab[i][j] + " ");
                    }
                }
                out.write("\n");
            }
            //we close the bufferedwritter
            out.close();
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }
//apply average filtre on a photo

    public static void filtreAvg(String output, int[][] tab, int maskSize) {

        try {
            //specify the name of the output..
            FileWriter fstream = new FileWriter(output);
            //we create a new BufferedWriter
            BufferedWriter out = new BufferedWriter(fstream);
            //we add the header, 128 128 is the width-height and 63 is the max value-1 of ur data
            out.write("P2\n# Comment here :) \n" + tab[0].length + " " + tab.length + "\n255\n");
            //2 loops to read the 2d array
            int somme = 0;
            for (int i = 0; i < tab.length; i++) {


                for (int j = 0; j < tab[i].length; j++) {
                    //we write in the output the value in the position ij of the array
                    somme = 0;


                    for (int a = i - (maskSize / 2); a <= i + (maskSize / 2); a++) {
                        for (int b = j - (maskSize / 2); b <= j + (maskSize / 2); b++) {
                            if (a < 0 || a >= tab.length || b < 0 || b >= tab[i].length) {
                                /** Some portion of the mask is outside the image. */
                                continue;
                            } else {
                                somme += tab[a][b];

                            }

                        }
                    }

                    out.write(somme / (maskSize * maskSize) + " ");
                }

                out.write("\n");
            }
            //we close the bufferedwritter
            out.close();
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }



    public static void median(String output, int[][] tab, int maskSize) {
        if (maskSize * maskSize > tab.length || maskSize * maskSize > tab[0].length)
            throw new RuntimeException("Mask Size Is Bigger Than Image");
        else if (maskSize / 2 == 0) throw new RuntimeException("Masksize must be impair");
        else {
            int[] buff;
            try {
                //specify the name of the output..
                FileWriter fstream = new FileWriter(output);
                //we create a new BufferedWriter
                BufferedWriter out = new BufferedWriter(fstream);
                //we add the header, 128 128 is the width-height and 63 is the max value-1 of ur data
                out.write("P2\n# Comment here :) \n" + tab[0].length + " " + tab.length + "\n255\n");
                //2 loops to read the 2d array


                for (int i = 0; i < tab.length; i++) {
                    for (int j = 0; j < tab[i].length; j++) {
                        buff = new int[maskSize * maskSize];
                        int count = 0;

                        for (int a = i - (maskSize / 2); a <= i + (maskSize / 2); a++) {
                            for (int b = j - (maskSize / 2); b <= j + (maskSize / 2); b++) {

                                if (a < 0 || a >= tab.length || b < 0 || b >= tab[i].length) {
                                    /** Some portion of the mask is outside the image. */
                                    continue;
                                } else {
                                    buff[count] = tab[a][b];
                                    count++;
                                }

                            }
                        }


                        /** sort buff array */
                        java.util.Arrays.sort(buff);

                        /** save median value in outputPixels array */
                        out.write(buff[count / 2] + " ");
                    }

                }

                //we close the bufferedwritter
                out.close();

            } catch (Exception e) {
                System.err.println("Error : " + e.getMessage());
            }
        }
    }

    public static void randomFiltre(String output, int[][] tab, int[][]filtre) {

        try {
            //specify the name of the output..
            FileWriter fstream = new FileWriter(output);
            //we create a new BufferedWriter
            BufferedWriter out = new BufferedWriter(fstream);
            //we add the header, 128 128 is the width-height and 63 is the max value-1 of ur data
            out.write("P2\n# Comment here :) \n" + tab[0].length + " " + tab.length + "\n255\n");
            //2 loops to read the 2d array
            int somme = 0;
            for (int i = 0; i < tab.length; i++) {


                for (int j = 0; j < tab[i].length; j++) {
                    //we write in the output the value in the position ij of the array
                    somme = 0;

                    for (int a = -1;a<=1;a++){
                        for (int b=-1;b<=1;b++){
                        if (i-a <=0 ||i+a >= tab.length || j-b <=0 || j >= tab[i].length) {
                            /** Some portion of the mask is outside the image. */
                            continue;
                        }
                        else {
                           somme=tab[]

                        }
                        }
                    }
                    System.out.println(somme+"||");
                    out.write(somme+ " ");
                }

                out.write("\n");
            }
            //we close the bufferedwritter
            out.close();
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
        }
    }


    public static void main(String args[]) {
        int[][] d = readImage("chat.pgm");
        System.out.println(average(d));
        System.out.println(standardDeviation(d));



        // statistics
        int[] hist = histogramGrid(d);
        System.out.println();
        double[] tabcumul = histogramCumulatif(d, d.length, d[0].length);
        double[] h = histogramCumulatif(d, d.length, d[0].length);

        int[] n1 = new int[tabcumul.length];
        for (int i = 0; i < n1.length; i++) {
            n1[i] = (int) tabcumul[i];
        }
        histogrammeEgalise(tabcumul, h, 144);
        transLineare(50, 100, 150, 175, hist, d);

        //distortion

        bruit("bruit.pgm", d, 320, 240);
        int[][] matbruit = readImage("bruit.pgm");

        //avg filtre

        filtreAvg("filtreavg.pgm", matbruit, 5);

        // median

        median("filtremed.pgm", matbruit, 5);

        // chosen filtre

        int [][] filtre = new int[3][3];
        filtre[0][0] = 0;
        filtre[0][1] = -1;
        filtre[0][2] = 0;
        filtre[1][0] = -1;
        filtre[1][1] = 5;
        filtre[1][2] = -1;
        filtre[2][0] = 0;
        filtre[2][1] = -1;
        filtre[2][2] = 0;
        randomFiltre("filtre.pgm",matbruit,filtre);
    }
}

