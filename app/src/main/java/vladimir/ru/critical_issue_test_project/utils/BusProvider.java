package vladimir.ru.critical_issue_test_project.utils;

import com.squareup.otto.Bus;

/**
 * Created by Vladimir on 17.10.2016.
 * Event bus instance
 */

public class BusProvider {
    private static final Bus UI_BUS = new Bus();

    private BusProvider() {}

    public static Bus getUIBusInstance () {
        return UI_BUS;
    }
}
