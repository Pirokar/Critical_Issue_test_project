package vladimir.ru.critical_issue_test_project.model.entites;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Vladimir on 15.10.2016.
 */

@Root(name = "result")
public class ChatAnswer {
    @Element(name = "totalPages")
    public int totalPages;

    @ElementList(name = "quotes")
    public List<ChatItem> items;
}
