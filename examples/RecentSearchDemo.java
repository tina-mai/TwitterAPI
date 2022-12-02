import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

// INSTRUCTIONS:
// - Run the RecentSearchDemo.java file
// - Type in a search word (e.g. "cats")
// - Give the program a few minutes to conjure up your magical graph
// - Program will show a graph of the likes that the tweets with that word got across 24 hours in a day (sorted into morning, afternoon, and night); each hour is the average likes that tweets with that word got over the last 7 days

public class RecentSearchDemo {

    // To set your environment variables in your terminal run the following line:
    // export 'BEARER_TOKEN'='<your_bearer_token>'

    public static int maxResults = 100; // must be >= 10 and <= 100
    public static TwitterUser user = new TwitterUser("wsj");
    public static TwitterWord word = new TwitterWord("cats");

    public static void main(String args[]) throws IOException, URISyntaxException, ParseException, java.text.ParseException {
        String bearerToken = "AAAAAAAAAAAAAAAAAAAAAF2vhwEAAAAAmUihYKuFWe%2BmdJsnQCy4UQQa8sk%3DGcYDJdMZ8QXyuiA6KqUnLhxzo1RdlMoZGbMn4sjN3G6g0Whyui";
        Scanner s = new Scanner(System.in);
        if (null != bearerToken) {
            // Replace the search term with a term of your choice
//            String response = search("from:TwitterDev OR from:SnowBotDev OR from:DailyNASA", bearerToken);

//            String response = search("cat -is:retweet", bearerToken);
//            System.out.println("\n——— Unformatted return ———\n");
//            System.out.println(response1);


            System.out.println("Type a word you'd like to see the like statistics for.");
            String userResponse = s.nextLine();
            String response = search(userResponse + " -is:reply -is:retweet", bearerToken);

            Plot p = new Plot(word.getTimes(), word.getLikes());

        } else {
            System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

    // this method calls the recent search endpoint with a search term (the user input word) passed to it as a query parameter
    private static String search(String searchString, String bearerToken) throws IOException, URISyntaxException, ParseException, java.text.ParseException {
        String searchResponse = null;
        int tweetCount = 0;
        int totalLikes = 0;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        // create a new Calendar object and set it to the current time, then change it by iterating backwards through each hour in the last 7 days
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date()); // current date
        cal.add(Calendar.HOUR, 8); // convert our time zone to GMT
        cal.add(Calendar.MINUTE, -5); // subtract 5 minutes so the time zone conversion doesn't break Twitter rules
        // WARNING: DON'T RUN CODE <5 MINUTES INTO A NEW HOUR (e.g. 2:04)

        System.out.println("Loading... (this may take a few minutes)");

        for (int j=0; j<24; j++) { // iterating through hours

            tweetCount = 0;
            totalLikes = 0;

            for (int k=0; k<7; k++) { // iterating through days

                String endTime = sdf.format(cal.getTime());

                URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent?tweet.fields=attachments,author_id,created_at,public_metrics,source&max_results=" + maxResults + "&end_time=" + endTime);

                // averaging likes

                ArrayList<NameValuePair> queryParameters;
                queryParameters = new ArrayList<>();
                queryParameters.add(new BasicNameValuePair("query", searchString));

                uriBuilder.addParameters(queryParameters);

                HttpGet httpGet = new HttpGet(uriBuilder.build());

                httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
                httpGet.setHeader("Content-Type", "application/json");


                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();

                if (entity != null) {

                    // isolating tweet text from HTTP –> JSON format
                    searchResponse = EntityUtils.toString(entity);
                    // parsing JSON
                    JSONObject result = new JSONObject(searchResponse); // convert String to JSON Object

                    JSONArray tweetList = result.getJSONArray("data");

                    tweetCount += tweetList.length();

                    //System.out.println(tweetList.length());

                    for (int i=0; i<tweetList.length(); i++) {
                        JSONObject oj = tweetList.getJSONObject(i); // gets one tweet from the list
                        String tweet = oj.getString("text");
                        String id = oj.getString("id");

        //                String date = oj.getString("created_at");
        //                SimpleDateFormat dt = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss");
        //                Date formattedDate = dt.parse(date);
        //                SimpleDateFormat dt1 = new SimpleDateFormat("yyyyy-mm-dd");
        //                System.out.println("Date: " + dt1.format(formattedDate));

                        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(oj.getString("created_at"));
        //                String formattedDate = new SimpleDateFormat("MM/dd/yyyy, h:ma").format(date);
                        time = date.getHours();
                        likes = oj.getJSONObject("public_metrics").getInt("like_count");
                        totalLikes += likes;

                        // user.addLike(likes);
                        // user.addTime(time);

//                        tweetInfo(id,bearerToken);
//                        System.out.println("Tweet: " + tweet);
//        //                System.out.println("Date: " + date);
//        //                System.out.println("Date: " + formattedDate);
//                        System.out.println("Time: " + time);
//                        System.out.println("Likes: " + likes);
//                        System.out.println("——————————");

                    }
                }
                cal.add(Calendar.DATE, -1); // iterate to the next day
            }
            cal.add(Calendar.DATE, 7);
            cal.add(Calendar.HOUR, -1);

//            if (j == 0) {
//                cal.add(Calendar.MINUTE, 5);
//            }
            if (j == 22) {
                cal.add(Calendar.MINUTE, 5); // add 5 minutes in the last iteration to prevent stupid API from breaking, because it takes around 5 minutes to run
            }

            if (tweetCount == 0)
                word.addLike(0);
            else
                word.addLike(totalLikes/tweetCount);
            word.addTime(time);

            // some of the tweets don't have contexts so I made a try catch to print out the entity name if they do
//            try {
//                JSONArray name = oj.getJSONArray("context_annotations");
//                JSONObject name2 =name.getJSONObject(0);
//                JSONObject entity1 = name2.getJSONObject("entity");
//                entityName = entity1.getString("name");
//            } catch(Exception e) {
//                entityName = null;
//            }

        }

        return searchResponse;
    }

}
