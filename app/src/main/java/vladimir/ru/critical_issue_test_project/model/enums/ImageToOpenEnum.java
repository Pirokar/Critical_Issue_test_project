package vladimir.ru.critical_issue_test_project.model.enums;

/**
 * Created by Vladimir on 16.10.2016.
 * Needs to understand how to get a picture
 */

public enum ImageToOpenEnum {
    NONE(0),
    GALLERY(1),
    CAMERA(2);

    public int type;

    ImageToOpenEnum(int type) {
        this.type = type;
    }

    public static ImageToOpenEnum resolve(int type) {
        try {
            for(ImageToOpenEnum t : values()) {
                if (t.type == type) {
                    return t;
                }
            }
        } catch (Exception e) {
            return NONE;
        }

        return NONE;
    }

    public static int resolve(ImageToOpenEnum type) {
        try {
            return type.type;
        } catch (Exception e) {
            return NONE.type;
        }
    }
}
