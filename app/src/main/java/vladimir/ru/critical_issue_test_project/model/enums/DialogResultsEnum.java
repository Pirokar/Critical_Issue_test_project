package vladimir.ru.critical_issue_test_project.model.enums;

/**
 * Created by Vladimir on 18.10.2016.
 * Presents dialog results
 */

public enum DialogResultsEnum {
    OK(0),
    CANCEL(1);

    public int type;

    DialogResultsEnum(int type) {
        this.type = type;
    }

    public static DialogResultsEnum resolve(int type) {
        try {
            for(DialogResultsEnum t : values()) {
                if (t.type == type) {
                    return t;
                }
            }
        } catch (Exception e) {
            return OK;
        }

        return OK;
    }

    public static int resolve(DialogResultsEnum type) {
        try {
            return type.type;
        } catch (Exception e) {
            return OK.type;
        }
    }
}
