package sample.facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.OpenGraphRating;
import com.restfb.types.Page;
import sample.service.FileService;

import java.util.List;

public class FacebookGraph extends ApiBinding {

    private final String accessToken;

    private final FileService fileService;

    public FacebookGraph(String accessToken, FileService fileService) {
        super(accessToken);
        this.accessToken = accessToken;
        this.fileService = fileService;
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
                fileService.downloadFile(og.getReviewer().getPicture().getUrl());
            }

        }

        return ogRatingConn.getData();
    }







}
