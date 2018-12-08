package Utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class Utils {

    public static void saveSeq(String fileName, int[] seq) {
        try {
            BufferedWriter outputFile = new BufferedWriter(new FileWriter(fileName));

            String seqList = StringUtils.join(seq, ',');
            outputFile.write("" + seq.length);
            outputFile.newLine();
            outputFile.write(seqList);
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] loadSequence(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            int seqLength = Integer.valueOf(line);
            int[] seq = new int[seqLength];
            line = reader.readLine();
            String[] numbers = StringUtils.split(line, ',');
            for (int i = 0; i < seqLength; i++) {
                seq[i] = Integer.valueOf(numbers[i]);
            }
            reader.close();
            return seq;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void analizeSeq(String dataSetName, int[] seq, boolean printHeader) {
        int[] repeatCount = new int[10];
        int start = 0;
        int end = 0;
        while (end < seq.length) {
            if (seq[start] == seq[end]) {
                end++;
            } else {
                int len = Math.min(end - start - 1, repeatCount.length - 1);
                start = end;
                repeatCount[len]++;
            }
        }
        int len = Math.min(end - start, repeatCount.length - 1);
        repeatCount[len]++;

        if (printHeader) {
            System.out.println("DataSet Name, 0 times, 1 times, 2 times, 3 times, 4 times, 5 times, 6 times, 7 times, 8 times, 9+ times");
        }
        System.out.print(dataSetName + ",");
        System.out.println(StringUtils.join(repeatCount, ','));
    }
}
