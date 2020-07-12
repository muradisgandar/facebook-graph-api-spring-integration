package sample.api.facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.OpenGraphRating;
import com.restfb.types.Page;
import sample.api.ApiBinding;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class FacebookGraph extends ApiBinding {

    private String accessToken;

    private static String FILEPATH = "/home/murad/photos/1";

    public FacebookGraph(String accessToken) {
        super(accessToken);
        this.accessToken = accessToken;
    }

    public List<Page> getPages() {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);
        Connection<Page> pages = facebookClient.fetchConnection("me/accounts", Page.class);

        return pages.getData();
    }

    public String pageAccessToken(String pageId) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken, Version.LATEST);

        Page page = facebookClient.fetchObject(pageId, Page.class, Parameter.with("fields", "access_token"));
        return page.getAccessToken();

    }

    public List<OpenGraphRating> getReviewWithRestFb(String pageId) {

        FacebookClient facebookClient = new DefaultFacebookClient(pageAccessToken(pageId), Version.LATEST);

        Connection<OpenGraphRating> ogRatingConn =
                facebookClient.fetchConnection("me/ratings",
                        OpenGraphRating.class,
                        Parameter.with("fields", "reviewer{id,name,picture},review_text,created_time,recommendation_type"));


        for (OpenGraphRating og : ogRatingConn.getData()) {
            if(og.getReviewer().getPicture() != null){
                downloadFile(og.getReviewer().getPicture().getUrl());
            }

        }

        return ogRatingConn.getData();
    }


    public void downloadFile(String photoUrl) {
        byte[] imageBytes = restTemplate.getForObject(photoUrl, byte[].class);
        try {
            String directoryCreateIfNotExist = decideUploadDirectoryCreateIfNotExist();
            Path path = Paths.get(directoryCreateIfNotExist + File.separator + Thread.currentThread().getId() + new Date().getTime() + ".jpeg");
            Files.write(path, imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private String decideUploadDirectoryCreateIfNotExist() throws IOException {
        // muveqqeti silmishem buralari projectde CurrentUserAuth var
        createDirectoryIfNotExist(FILEPATH);
        return FILEPATH;
    }

    private void createDirectoryIfNotExist(String directory) throws IOException {
        try {
            Path path = Path.of(directory);
            Files.createDirectory(path);
        } catch (FileAlreadyExistsException e) {
            // Normaldir. Demek ki hemin directory artiq daha once yaradilib.
        }
    }

}
