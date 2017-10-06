package vladimir.ru.critical_issue_test_project.model.entites;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vladimir on 17.10.2016.
 * List item
 */

public class ListItem implements Serializable {
    private String text;
    private boolean isSelected;

    public ListItem(String text, boolean isSelected) {
        this.text = text;
        this.isSelected = isSelected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void markAsSelected() {
        isSelected = true;
    }

    public void markAsUnselected() {
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
