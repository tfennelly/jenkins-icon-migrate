package org.jenkins;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class IconUrlTest {

    @Test
    public void test() {
        assertIcon(new IconUrl("a"),                   false, false, false);
        assertIcon(new IconUrl("a/16x16/x"),           false, true,  false);
        assertIcon(new IconUrl("a/16x16/aborted.png"), true,  true,  false);
        assertIcon(new IconUrl("a/16x16/aborted.gif"), true,  true,  false);
        assertIcon(new IconUrl("aborted-16x16"), false, false, true);

        // If we find a Jelly EL ${} pattern in the url, then it needs to be
        // checked manually to see if it's an icon
        assertIcon(new IconUrl("a/${x}/y"),            false, true, false);
    }

    private void assertIcon(IconUrl iconUrl, boolean expectIsTransformable, boolean expectRequiresHumanCheck, boolean expectIsAlreadyQualified) {
        Assert.assertEquals(expectIsTransformable, iconUrl.isTransformable());
        Assert.assertEquals(expectRequiresHumanCheck, iconUrl.requiresHumanCheck());
        Assert.assertEquals(expectIsAlreadyQualified, iconUrl.isAlreadyQualifiedIconName());
    }
}
