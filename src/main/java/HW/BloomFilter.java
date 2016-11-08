package HW;

/**
 * Valar Dohaeris 10/21/16.
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 400Kbytes equals 400000 bytes. Which is 3200000 bits. We use a bitset which uses 1 bit per boolean value.
 * Therefore the size of our Bloom Filter hashTable is 3200000.
 *
 */
public class BloomFilter {

    private final static int n=3200000;

    private static List<String> m=new ArrayList<>();

    //Using hash codes of strings as an hashfunction here
    public static long hashFunction1(String s)
    {

        return Math.abs(s.hashCode())%n;
    }

    //Using primes because they provide a more uniform distribution of values.
    public static long hashFunction2(String s)
    {
        long a = 7;
        int b=2;
        long hash=0;
        long prime=3200003;

        hash=Math.abs(a*s.hashCode()+b)%prime;

        hash=hash%prime;
        return Math.abs(hash)%n;
    }

    public static long hashFunction3(String s)
    {
        long a = 11;
        int b=3;
        long hash=0;
        long prime=3200027;

        hash=Math.abs(a*s.hashCode()+b)%prime;

        hash=hash%prime;
        return Math.abs(hash)%n;
    }

    public static long hashFunction4(String s)
    {
        long a = 13;
        int b=5;
        long hash=0;
        long prime=3200033;

        hash=Math.abs(a*s.hashCode()+b)%prime;

        hash=hash%prime;
        return Math.abs(hash)%n;
    }

    public static long hashFunction5(String s)
    {
        long a = 17;
        int b=7;
        long hash=0;
        long prime=3200039;

        hash=Math.abs(a*s.hashCode()+b)%prime;

        hash=hash%prime;
        return Math.abs(hash)%n;
    }

    public static long hashFunction6(String s)
    {
        long a = 19;
        int b=11;
        long hash=0;
        long prime=3200051;

        hash=Math.abs(a*s.hashCode()+b)%prime;

        hash=hash%prime;
        return Math.abs(hash)%n;
    }

    public static long hashFunction7(String s)
    {
        long a = 21;
        int b=13;
        long hash=0;
        long prime=3200051;

        hash=Math.abs(a*s.hashCode()+b)%prime;

        hash=hash%prime;
        return Math.abs(hash)%n;
    }

    public static void readFile()
    {
        File myFile = new File("/home/abhiram/codebase/590D/words.xlsx");
        try {
        FileInputStream fis = new FileInputStream(myFile);


        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook = new XSSFWorkbook (fis);

        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // Traversing over each row of XLSX file
            for (Row row : mySheet) {
                // For each row and read the first column
                Iterator<Cell> cellIterator = row.cellIterator();
                Cell cell = cellIterator.next();
                if(cell!=null&&cell.getCellType()==Cell.CELL_TYPE_STRING&&cell.getStringCellValue()!=null)
                    m.add(cell.getStringCellValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String []args)
    {
        BitSet bloomFilter=new BitSet(n);

        //Store values from XL file to List m;
        readFile();

        System.out.println("Done Reading file");
        List<String> newWords=new ArrayList<>();

        /*BitSet already comes with all bits set to zero so we can skip that step in preprocessing.*/

        /* Compute hash values for each string in m*/
        for (String word:m)
        {
            bloomFilter.set((int)hashFunction1(word));
            bloomFilter.set((int)hashFunction2(word));
            bloomFilter.set((int)hashFunction3(word));
            bloomFilter.set((int)hashFunction4(word));
            bloomFilter.set((int)hashFunction5(word));
            bloomFilter.set((int)hashFunction6(word));
        }
        System.out.println("Done Preprocessing");

        //Generating random word of length 5
        Random random=new Random();
        for(int i=0;i<100;i++)
        {
            String newWord="";
            for(int j=0;j<5;j++) {
                char c = (char) ('a' + random.nextInt(26));
                newWord+=c;
            }
            newWords.add(newWord);
        }
        System.out.println("Done Generating words");

        //Valuate each word in the set and Set false positive rate
        int falsePositives=0;
        for (String newWord:newWords)
        {
            //Every hash function should return a 1 for the newWord to be an actual word or false positive
            if(bloomFilter.get((int)hashFunction1(newWord))
                    &&bloomFilter.get((int)hashFunction2(newWord))
                    &&bloomFilter.get((int)hashFunction3(newWord))
                    &&bloomFilter.get((int)hashFunction4(newWord))
                    &&bloomFilter.get((int)hashFunction5(newWord))
                    &&bloomFilter.get((int)hashFunction6(newWord)))
            {
                if(!m.contains(newWord))
                    falsePositives++;
            }
        }

        System.out.println("False Positves: "+falsePositives+" Rate: "+(double)falsePositives/100);
    }
}
