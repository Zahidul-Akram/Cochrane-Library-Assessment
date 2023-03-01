import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Cochrane_Library_Assessment {
    
    private static final String base_URL = "https://www.cochranelibrary.com/cdsr/reviews/topics";
    private static final String bar_del = "|";
    private static final String NL_del = "\n";
    private static final String OUTPUT_FILE_NAME = "cochrane_reviews.txt";
    
    public static void main(String[] args) {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            List<String> reviewUrls = getReviewUrls(httpClient, base_URL);
            List<String> reviewDataList = new ArrayList<String>();
            for (String url : reviewUrls) {
                String reviewData = getReviewData(httpClient, url);
                reviewDataList.add(reviewData);
            }
            writeToFile(reviewDataList);
            System.out.println("Successfully written data to file: " + OUTPUT_FILE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static List<String> getReviewUrls(HttpClient httpClient, String baseUrl) throws IOException {
        List<String> reviewUrls = new ArrayList<String>();
        HttpGet request = new HttpGet(baseUrl);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String pageContent = EntityUtils.toString(entity);
        String[] reviewLinks = pageContent.split("<a href=\"/cdsr/doi/(.*?)\">");
        for (int i = 1; i < reviewLinks.length; i++) {
            String reviewUrl = "https://www.cochranelibrary.com/cdsr/doi/" + reviewLinks[i].substring(0, reviewLinks[i].indexOf("\""));
            reviewUrls.add(reviewUrl);
        }
        return reviewUrls;
    }
    
    private static String getReviewData(HttpClient httpClient, String reviewUrl) throws IOException {
        HttpGet request = new HttpGet(reviewUrl);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String pageContent = EntityUtils.toString(entity);
        String title = pageContent.substring(pageContent.indexOf("<h1 class=\"article-title\">") + "<h1 class=\"article-title\">".length(), pageContent.indexOf("</h1>"));
        String author = pageContent.substring(pageContent.indexOf("<div class=\"meta__authors\">") + "<div class=\"meta__authors\">".length(), pageContent.indexOf("</div>", pageContent.indexOf("<div class=\"meta__authors\">")));
        String date = pageContent.substring(pageContent.indexOf("<time class=\"meta__published-date\" datetime=") + "<time class=\"meta__published-date\" datetime=".length() + 1, pageContent.indexOf("T", pageContent.indexOf("<time class=\"meta__published-date\" datetime=")) + 6);
        return title + bar_del + author + bar_del + date + NL_del;
    }
    
    private static void writeToFile(List<String> reviewDataList) throws IOException {
        FileWriter writer = new FileWriter(OUTPUT_FILE_NAME);
        for (String reviewData : reviewDataList) {
            writer.write(reviewData);
        }
        writer.close();
    }
}
