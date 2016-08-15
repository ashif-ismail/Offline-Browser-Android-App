package as.hif.offlinebrowser;


import android.content.Context;
import android.content.Intent;
import android.test.AndroidTestCase;
import android.util.Log;

import junit.framework.TestCase;

import org.hamcrest.Matcher;
import org.junit.Test;

import java.net.URISyntaxException;

/**
 * Created by almukthar on 27/4/16.
 */
public class OfflineUnitTest extends TestCase{
    String expectedWebAdress = "www.somesite.com";
    String expectedSearchTerm = "android";
    String expectedWikiKeyword = "google";

    @Test
    public void testWebPageSearchMethod() throws Exception {
        WikiActivity.forwardRequestTest(expectedWebAdress);
        assertEquals("www.somesite.com", expectedWebAdress);
    }
    @Test
    public void testGoogleSearchMethod() throws Exception{
        GoogleActivity.forwardRequestTest(expectedSearchTerm);
        assertEquals("android", expectedSearchTerm);
    }
    @Test
    public void testWikiPageSearchMethod() throws Exception {
        WikiActivity.forwardRequestTest(expectedWebAdress);
        assertEquals("google", expectedWikiKeyword);
    }
}
