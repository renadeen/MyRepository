package ru.kontur.elba;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class ru.kontur.elba.LogonActivityTest \
 * ru.kontur.elba.tests/android.test.InstrumentationTestRunner
 */
public class LogonActivityTest extends ActivityInstrumentationTestCase2<LogonActivity> {

    public LogonActivityTest() {
        super("ru.kontur.elba", LogonActivity.class);
    }

}
