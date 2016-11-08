package HW;

import HW.Hashtags;
import HW.Tweet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CountMin {


    private static double width = 2000;


    private static int height=25;


    //Node objects are the entries that are contained in the minHeap priority queue
    public static class Node
    {
        Double frequency;
        String hashtag;


        Node(String hashtag, Double frequency)
        {
            this.hashtag=hashtag;
            this.frequency=frequency;
        }
    }


    public static double hashFunctionSet00s(String s,int a, int b)
    {
        double hash=0;
        int prime=2003; //2011, 2017, 2027, 2029, 2039, 2053


        for (int i = 0; i < s.length(); i++)
            hash = (a*s.charAt(i)+b)%prime;


        return Math.abs(hash)%width;
    }


    public static double hashFunctionSet10s(String s,int a, int b)
    {
        double hash=0;
        int prime=2017;


        for (int i = 0; i < s.length(); i++)
            hash = (a*s.charAt(i)+b)%prime;


        return Math.abs(hash)%width;
    }


    public static double hashFunctionSet20s(String s,int a, int b)
    {
        double hash=0;
        int prime=2011; //2011, 2017, 2027, 2029, 2039, 2053


        for (int i = 0; i < s.length(); i++)
            hash = (a*s.charAt(i)+b)%prime;


        return Math.abs(hash)%width;
    }


    //This is the hash function picker which picks a hash function and returns a hash value
    public static double callHashFunction(List<Pair<Integer,Integer>> coefficients, int i, String hashTag)
    {
        if(i<10)
            return hashFunctionSet00s(hashTag,coefficients.get(i%10).getKey(),coefficients.get(i%10).getValue());
        if(i<20)
            return  hashFunctionSet10s(hashTag,coefficients.get(i%10).getKey(),coefficients.get(i%10).getValue());
        if(i<30)
            return hashFunctionSet20s(hashTag,coefficients.get(i%10).getKey(),coefficients.get(i%10).getValue());

        return Double.parseDouble(null);
    }


    public static void main(String args[]) {

// pass the path of intended tweet text file
        String pathCorpus = "C:/Users/Shruti Jadon/Downloads/tweetstream/tweetstream.txt";
        List<Pair<Integer,Integer>> abPairs=new ArrayList<>();
        abPairs.add(new Pair<>(1,2));
        abPairs.add(new Pair<>(2,3));
        abPairs.add(new Pair<>(3,4));
        abPairs.add(new Pair<>(4,5));
        abPairs.add(new Pair<>(5,6));
        abPairs.add(new Pair<>(6,7));
        abPairs.add(new Pair<>(7,8));
        abPairs.add(new Pair<>(8,9));
        abPairs.add(new Pair<>(9,10));
        abPairs.add(new Pair<>(10,11));


        double streamSize=0;int k=500;
        List<List<Integer>> countMin=new ArrayList<>();


        //This defines how the priority queue will be sorted. We sort it by frequency,
        // if the frequencies are same we sort it by alphabetical order
        PriorityQueue<Node> minHeap = new PriorityQueue<>( ( r1, r2 ) -> {
            int cp =  r1.frequency.compareTo( r2.frequency);
            if ( cp == 0 ) {
                cp = r1.hashtag.compareTo(r2.hashtag);
            }
            return cp;
        });

        double ratio=0;

        for (int i=0;i<height;i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < width; j++)
                row.add(j,0);
            countMin.add(row);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(pathCorpus))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("u\"", "\"");
                line = line.replaceAll("u\'", "\'");
                line = line.replace("\n", "");
                line = line.replace("\r", "");
                try {
                    Gson gson = new Gson();
                    Tweet tweet = gson.fromJson(line, Tweet.class);
                    List<String> hastags = new ArrayList<>();

                    //Parse json and operate CountMinSketch on the hashtag
                    if ((tweet != null) && (tweet.entities != null) && (tweet.entities.hashtags != null)) {
                        hastags.addAll(tweet.entities.hashtags.stream().map(Hashtags::getText).collect(Collectors.toList()));

                        if (hastags.size() > 0) {
                            for (String hashtag : hastags) {
                                hashtag = hashtag.toLowerCase();
                                streamSize++;
                                double frequency = 99999999;

                        /*
                        Operate on all hash Functions while keeping track of the minimum because that's
                        the frequency!
                        */
                                for (int i = 0; i < height; i++) {
                                    double bucket = callHashFunction(abPairs, i, hashtag);
                                    double minFreq = countMin.get(i).get((int) bucket) + 1;
                                    countMin.get(i).set((int) bucket, (int) minFreq);

                                    if (frequency > minFreq)
                                        frequency = minFreq;
                                }


                                //Calculate the m/k ratio and compare
                                double ratioMk = streamSize / k;
                                ratio = ratioMk;

                                if (frequency > ratioMk) {
                                    boolean isNodePresent = false;
                                    Node deleteThisNode = new Node("", (double) 0);

                                    //Run a search through the array to find if the hashtag is in the minHeap
                                    for (Node node : minHeap) {
                                        if (node.hashtag.equals(hashtag)) {
                                            deleteThisNode = node;
                                            isNodePresent = true;
                                            break;
                                        }
                                    }

                                    if (isNodePresent) {
                                        //If the node is present remove node and add the node with the latest frequency
                                        minHeap.remove(deleteThisNode);
                                        minHeap.add(new Node(hashtag, frequency));
                                    } else {
                                        //Look for the minimum element in the heap
                                        Node min = minHeap.peek();
                                        if (min != null) {

                                            //If its frequency is less than the m/k ratio delete it
                                            if (min.frequency != null && min.frequency < ratioMk)
                                                minHeap.poll();
                                        }
                                        minHeap.add(new Node(hashtag, frequency));
                                    }
                                }
                            }
                            System.out.println(hastags);
                        }
                    }
                }
                catch (JsonSyntaxException ignored) {}
            }

            System.out.println(ratio);
            for (Node node:minHeap)
                System.out.println(node.hashtag+" "+node.frequency);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
