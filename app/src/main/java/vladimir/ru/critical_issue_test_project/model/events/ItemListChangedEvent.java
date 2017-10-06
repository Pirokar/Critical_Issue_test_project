package vladimir.ru.critical_issue_test_project.model.events;

/**
 * Created by Vladimir on 18.10.2016.
 * Needs to notify when list element was changed
 */

public class ItemListChangedEvent {
    private int elementPosition;
    private String text;

    public ItemListChangedEvent(int elementPosition, String text) {
        this.elementPosition = elementPosition;
        this.text = text;
    }

    public int getElementPosition() {
        return elementPosition;
    }

    public String getText() {
        return text;
    }
}
