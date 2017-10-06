package vladimir.ru.critical_issue_test_project.model.rest;

import retrofit.Callback;
import retrofit.http.GET;
import vladimir.ru.critical_issue_test_project.model.entites.ChatAnswer;

/**
 * Created by Vladimir on 15.10.2016.
 * API
 */

public interface ChatAPI {
    @GET("/testXmlFeed.xml")
    void getTestXml(
            Callback<ChatAnswer> callback
    );
}
