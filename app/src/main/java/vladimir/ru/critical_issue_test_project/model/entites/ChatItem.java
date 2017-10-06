package vladimir.ru.critical_issue_test_project.model.entites;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Vladimir on 15.10.2016.
 */

@Root(name = "quote")
public class ChatItem {
    @Element(name = "id")
    public long id;

    @Element(name = "date")
    public String date;

    @Element(name = "text")
    public String text;
}
