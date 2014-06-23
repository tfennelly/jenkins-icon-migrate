package org.jenkins;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class IconMigrateTest {

    @Test
    public void test() throws Exception {
        IconMigrate iconMigrate = new IconMigrate(getClass().getResourceAsStream("/test1.jelly"));

        iconMigrate.processJellyFile();
        StringWriter writer = new StringWriter();
        iconMigrate.serializeDoc(writer);

        String result = writer.toString();
//        System.out.println(result);

        Assert.assertTrue(result.startsWith(EXPECTED_PREAMBLE));
        XMLUnit.setIgnoreWhitespace(true);
        XMLAssert.assertXMLEqual(EXPECTED_XML, result);
    }


    private static final String EXPECTED_PREAMBLE = "<!--\n" +
            "The MIT License\n" +
            "\n" +
            "Copyright (c) 2004-2009, Sun Microsystems, Inc., Kohsuke Kawaguchi\n" +
            "\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
            "of this software and associated documentation files (the \"Software\"), to deal\n" +
            "in the Software without restriction, including without limitation the rights\n" +
            "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
            "copies of the Software, and to permit persons to whom the Software is\n" +
            "furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in\n" +
            "all copies or substantial portions of the Software.\n" +
            "\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
            "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
            "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
            "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
            "THE SOFTWARE.\n" +
            "-->\n" +
            "\n" +
            "<?jelly escape-by-default='true'?>\n";

    private static final String EXPECTED_XML =
            "<j:jelly xmlns:j=\"jelly:core\" xmlns:l=\"/lib/layout\" xmlns:st=\"jelly:stapler\">\n" +
            "\n" +
            "    <!-- These should get transformed to icons -->\n" +
            "    <l:icon name=\"help-16x16\" style=\"vertical-align:top\" tooltip=\"nice icon\"/>\n" +
            "    <l:icon name=\"blue_anime-48x48\" onclick=\"alert('hi')\"/>\n" +
            "\n" +
            "    <l:task href=\"..\" icon=\"up-24x24\" title=\"${%Back to Loggers}\"/>\n" +
            "    <l:task href=\"..\" icon=\"up-24x24\" title=\"${%Back to Loggers}\"/>\n" +
            "\n" +
            "    <!-- These should be left as is because it doesn't match the pattern we're looking for -->\n" +
            "    <img src=\"images/a/blue.gif\"/>\n" +
            "    <l:task href=\"..\" icon=\"images/a/blue.gif\" title=\"${%Back to Loggers}\"/>\n" +
            "\n" +
            "</j:jelly>";
}
