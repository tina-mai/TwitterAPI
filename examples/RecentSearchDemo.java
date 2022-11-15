import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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

/*
 * Sample code to demonstrate the use of the Recent search endpoint
 * */
public class RecentSearchDemo {

    // To set your enviornment variables in your terminal run the following line:
    // export 'BEARER_TOKEN'='<your_bearer_token>'

    public static int maxResults = 100; // must be >= 10 and <= 100
    public static TwitterUser user = new TwitterUser("elonmusk");

    public static void main(String args[]) throws IOException, URISyntaxException, ParseException {
        String bearerToken = "AAAAAAAAAAAAAAAAAAAAAF2vhwEAAAAAmUihYKuFWe%2BmdJsnQCy4UQQa8sk%3DGcYDJdMZ8QXyuiA6KqUnLhxzo1RdlMoZGbMn4sjN3G6g0Whyui";

        if (null != bearerToken) {
            // Replace the search term with a term of your choice
//            String response = search("from:TwitterDev OR from:SnowBotDev OR from:DailyNASA", bearerToken);

//            String response1 = search("cat -is:retweet", bearerToken);
//            System.out.println("\n——— Unformatted return ———\n");
//            System.out.println(response1);

            String response2 = search("from:elonmusk -is:reply -is:retweet", bearerToken);
            Plot p = new Plot(user.getTimes(), user.getLikes());

        } else {
            System.out.println("There was a problem getting you bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

    // this method takes in the id generated from the search method, uses it to get expanded info (the username etc)
    // about tweet
    private static void tweetInfo(String id, String bearerToken) throws IOException, URISyntaxException{
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uri2 =  new URIBuilder("https://api.twitter.com/2/tweets/" + id + "?expansions=author_id");
        HttpGet httpGet2 = new HttpGet(uri2.build());

        httpGet2.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet2.setHeader("Content-Type", "application/json");
        HttpResponse response2 = httpClient.execute(httpGet2);
        HttpEntity entity2 = response2.getEntity();

        if (null != entity2) {
            System.out.println("------User info--------");
            String info = EntityUtils.toString(entity2);
            JSONObject result = new JSONObject(info);
            JSONObject userInfo1 = result.getJSONObject("includes");
            JSONArray userInfo2 = userInfo1.getJSONArray("users");
            JSONObject userInfo3 = userInfo2.getJSONObject(0);
            String name = userInfo3.getString("name");
            String username = userInfo3.getString("username");
            System.out.println("Name: " + name +";  Username: " +  username);
        }

    }

    /*
     * This method calls the recent search endpoint with a search term passed to it as a query parameter
     * */
    private static String search(String searchString, String bearerToken) throws IOException, URISyntaxException, ParseException {
        String searchResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        // I added the "tweet fields" that we want information about in this line
        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent?tweet.fields=attachments,author_id,created_at,public_metrics,source&max_results=" + maxResults); // "&max_result=n" gives n number tweets; 10 <= n <= 100
//        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/recent?tweet.fields=attachments,author_id,created_at,public_metrics,source&min_faves=5");


        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));

        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());

        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");


        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
//        if (null != entity) {
//            searchResponse = EntityUtils.toString(entity, "UTF-8");
//        }

        if (entity != null) {

            // isolating tweet text from HTTP –> JSON format
            searchResponse = EntityUtils.toString(entity);
            // parsing JSON
            JSONObject result = new JSONObject(searchResponse); // convert String to JSON Object

            JSONArray tweetList = result.getJSONArray("data");

            System.out.println(tweetList.length());

            for (int i=0; i<maxResults; i++) {
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
                int time = date.getHours();
                int likes = oj.getJSONObject("public_metrics").getInt("like_count");

                user.addLike(likes);
                user.addTime(time);

                tweetInfo(id,bearerToken);
                System.out.println("Tweet: " + tweet);
//                System.out.println("Date: " + date);
//                System.out.println("Date: " + formattedDate);
                System.out.println("Time: " + time);
                System.out.println("Likes: " + likes);
                System.out.println("——————————");

            }

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
