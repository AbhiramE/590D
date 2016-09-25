package HW;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Valar Dohaeris 9/20/16.
 */

public class ReservoirSampling {

    public static void main(String args[]) {
        String path = "/home/abhiram/codebase/590D/reservoir";
        List<Integer> samples = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        String line;
        int n, reservoir = -999, m, largest = -9999, smallest = 9999;

        //Read from File
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                samples.add(Integer.valueOf(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Enter m");
        Scanner scanner = new Scanner(System.in);
        m = Integer.valueOf(scanner.next());

        n = samples.size();
        for (int i = 0; i <= n; i++) //Intialise counts
            counts.add(0);

        System.out.print("\n Sampling now");
        for (int k = 0; k < m; k++) { //m is the number of times you want to perform Reservoir Sampling
            for (int i = 0; i < n; i++) { //n is the size of the sample
                int j = new Random().nextInt(i + 1);
                if (j == i) {  //Change reservoir with probability 1;
                    reservoir = samples.get(j);
                }
            }
            if (counts.get(reservoir) == 0)
                counts.set(reservoir, 1);
            else {
                int l = counts.get(reservoir) + 1;
                counts.set(reservoir, l);
            }
            reservoir = -999; //Reset Reservoir
        }

        System.out.print("\n Printing counts of Reservoir values \n");
        for (int i = 1; i < counts.size(); i++) {
            System.out.print("\t" + counts.get(i));
            if (i % 10 == 0) System.out.print("\n");

            if (counts.get(i) > largest)
                largest = counts.get(i);
            if (counts.get(i) < smallest)
                smallest = counts.get(i);
        }

        int range = largest - smallest;
        System.out.print("\n Total range of values " + range + " and ratio " + (double) range / m);
    }
}
