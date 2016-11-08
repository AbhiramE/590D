package HW;

import HW.DTO.Hashtags;
import HW.DTO.Tweet;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.util.Pair;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Valar Dohaeris 10/27/16.
 */
public class MajorityAlgorithm {


    public static void main(String[] args) {
        HashMap<String, Double> reservoir = new HashMap<>();
        String pathCorpus = "C:/Users/Shruti Jadon/Downloads/tweetstream/tweetstream.txt";

        double count = 0;


        try (BufferedReader br = new BufferedReader(new FileReader(pathCorpus))) {
            String line;

            //Read the line
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("u\"", "\"");
                line = line.replaceAll("u\'", "\'");
                line = line.replace("\n", "");
                line = line.replace("\r", "");

                //Parse json into twitter object to capture the hashtags
                Gson gson = new Gson();
                try {
                    Tweet tweet = gson.fromJson(line, Tweet.class);
                    List<String> hastags = new ArrayList<>();

                    if ((tweet != null) && (tweet.entities != null) && (tweet.entities.hashtags != null)) {

                        hastags.addAll(tweet.entities.hashtags.stream().map(Hashtags::getText).collect(Collectors.toList()));

                        //For each hashtag operate on the majority algorithm
                        for (String hashtag : hastags) {
                            hashtag=hashtag.toLowerCase();

                            //Put the first 500 elements into the hash table
                            if (count < 500) {
                                if (!reservoir.keySet().contains(hashtag)) {
                                    reservoir.put(hashtag, (double) 1);
                                    count++;
                                }
                            } else {
                                //If the element exists in the hash table increment its count
                                if (reservoir.keySet().contains(hashtag))
                                    reservoir.put(hashtag, reservoir.get(hashtag) + 1);
                                else {
                                    //If the hash table size is less than 500 add element to the hash table
                                    if (reservoir.keySet().size() < 500)
                                        reservoir.put(hashtag, (double) 1);
                                    else {
                                        /* If the element doesn't exist in the hash table and size is 500
                                         decrement the count of all elements in the hash table. */
                                        for (String k : reservoir.keySet()) {
                                            reservoir.put(k, reservoir.get(k) - 1);
                                        }
                                    }
                                }
                                count++; //Counting to find m
                            }

                            //Delete all zeroes in the hash table
                            HashMap<String, Double> intermediate = new HashMap<>();
                            for (String key:reservoir.keySet())
                                if(reservoir.get(key)!=0)
                                    intermediate.put(key,reservoir.get(key));

                            reservoir=intermediate;
                        }
                    }
                } catch (JsonSyntaxException ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Result in reservoir
        System.out.println("Frequencies should be greater than "+count*.002);
        for (String hashtag : reservoir.keySet())
            System.out.println(hashtag + " " + reservoir.get(hashtag));
    }
}