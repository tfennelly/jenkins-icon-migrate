package org.jenkins;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class IconUrl {

    private String url;
    private int numTokens = 0;
    private String iconDim = null;
    private String iconBaseName = null;

    public IconUrl(String urlString) {
        if (urlString == null) {
            return;
        }

        this.url = urlString;

        String[] urlToks = urlString.split("/");
        numTokens = urlToks.length;

        if (numTokens >= 2) {
            boolean hasDimTok = iconDims.contains(urlToks[numTokens - 2]);
            boolean hasIconNameTok = iconNames.contains(urlToks[numTokens - 1]);

            if (hasDimTok) {
                iconDim = urlToks[numTokens - 2];
            }
            if (hasIconNameTok) {
                iconBaseName = urlToks[numTokens - 1];
                iconBaseName = iconBaseName.substring(0, iconBaseName.length() - 4);
            }
        }
    }

    public String getUrl() {
        return url;
    }

    public int getNumTokens() {
        return numTokens;
    }

    public String getIconQualifiedName() {
        return iconBaseName + "-" + iconDim;
    }

    public boolean isTransformable() {
        return (iconDim != null && iconBaseName != null);
    }

    public boolean requiresHumanCheck() {
        return (iconDim != null || iconBaseName != null || containsELToken());
    }

    public boolean isAlreadyQualifiedIconName() {
        if (url.length() > SUFFIX_LEN) {
            String baseName = url.substring(0, url.length() - SUFFIX_LEN);
            if (iconNames.contains(baseName + ".png") || iconNames.contains(baseName + ".gif")) {
                String dim = url.substring(url.length() - (SUFFIX_LEN - 1));
                if (iconDims.contains(dim)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean containsELToken() {
        Matcher m = JELLY_EL_PATTERN.matcher(url);
        return m.find();
    }

    private static final Pattern JELLY_EL_PATTERN = Pattern.compile("\\$\\{[^\\}]+\\}");

    private static final Set<String> iconDims = new HashSet<String>();
    private static final Set<String> iconNames = new HashSet<String>();
    private static final int SUFFIX_LEN = "-XXxXX".length();

    static {
        iconDims.add("16x16");
        iconDims.add("24x24");
        iconDims.add("32x32");
        iconDims.add("48x48");

        // 16x16
        iconNames.add("aborted.png");
        iconNames.add("accept.png");
        iconNames.add("attribute.png");
        iconNames.add("blue.png");
        iconNames.add("clock.png");
        iconNames.add("collapse.png");
        iconNames.add("computer-x.png");
        iconNames.add("computer.png");
        iconNames.add("disabled.png");
        iconNames.add("document_add.png");
        iconNames.add("document_delete.png");
        iconNames.add("document_edit.png");
        iconNames.add("edit-delete.png");
        iconNames.add("edit-select-all.png");
        iconNames.add("empty.png");
        iconNames.add("error.png");
        iconNames.add("expand.png");
        iconNames.add("fingerprint.png");
        iconNames.add("folder-error.png");
        iconNames.add("folder-open.png");
        iconNames.add("folder.png");
        iconNames.add("gear2.png");
        iconNames.add("go-next.png");
        iconNames.add("grey.png");
        iconNames.add("health-00to19.png");
        iconNames.add("health-20to39.png");
        iconNames.add("health-40to59.png");
        iconNames.add("health-60to79.png");
        iconNames.add("health-80plus.png");
        iconNames.add("help.png");
        iconNames.add("hourglass.png");
        iconNames.add("lock.png");
        iconNames.add("nobuilt.png");
        iconNames.add("notepad.png");
        iconNames.add("orange-square.png");
        iconNames.add("package.png");
        iconNames.add("person.png");
        iconNames.add("plugin.png");
        iconNames.add("red.png");
        iconNames.add("redo.png");
        iconNames.add("save.png");
        iconNames.add("search.png");
        iconNames.add("secure.png");
        iconNames.add("setting.png");
        iconNames.add("star-gold.png");
        iconNames.add("star.png");
        iconNames.add("stop.png");
        iconNames.add("terminal.png");
        iconNames.add("text-error.png");
        iconNames.add("text.png");
        iconNames.add("user.png");
        iconNames.add("warning.png");
        iconNames.add("yellow.png");
        iconNames.add("aborted.gif");
        iconNames.add("aborted_anime.gif");
        iconNames.add("blue.gif");
        iconNames.add("blue_anime.gif");
        iconNames.add("clock.gif");
        iconNames.add("clock_anime.gif");
        iconNames.add("computer-flash.gif");
        iconNames.add("computer-x.gif");
        iconNames.add("computer.gif");
        iconNames.add("disabled.gif");
        iconNames.add("disabled_anime.gif");
        iconNames.add("document_add.gif");
        iconNames.add("document_delete.gif");
        iconNames.add("document_edit.gif");
        iconNames.add("edit-delete.gif");
        iconNames.add("edit-select-all.gif");
        iconNames.add("empty.gif");
        iconNames.add("error.gif");
        iconNames.add("fingerprint.gif");
        iconNames.add("folder-error.gif");
        iconNames.add("folder-open.gif");
        iconNames.add("folder.gif");
        iconNames.add("gear2.gif");
        iconNames.add("go-next.gif");
        iconNames.add("green.gif");
        iconNames.add("green_anime.gif");
        iconNames.add("grey.gif");
        iconNames.add("grey_anime.gif");
        iconNames.add("health-00to19.gif");
        iconNames.add("health-20to39.gif");
        iconNames.add("health-40to59.gif");
        iconNames.add("health-60to79.gif");
        iconNames.add("health-80plus.gif");
        iconNames.add("help.gif");
        iconNames.add("hourglass.gif");
        iconNames.add("lock.gif");
        iconNames.add("nobuilt.gif");
        iconNames.add("nobuilt_anime.gif");
        iconNames.add("notepad.gif");
        iconNames.add("package.gif");
        iconNames.add("person.gif");
        iconNames.add("plugin.gif");
        iconNames.add("red.gif");
        iconNames.add("red_anime.gif");
        iconNames.add("redo.gif");
        iconNames.add("save.gif");
        iconNames.add("search.gif");
        iconNames.add("star-gold.gif");
        iconNames.add("star.gif");
        iconNames.add("stop.gif");
        iconNames.add("terminal.gif");
        iconNames.add("text-error.gif");
        iconNames.add("text.gif");
        iconNames.add("user.gif");
        iconNames.add("warning.gif");
        iconNames.add("yellow.gif");
        iconNames.add("yellow_anime.gif");

        // 24x24
        iconNames.add("aborted.png");
        iconNames.add("accept.png");
        iconNames.add("attribute.png");
        iconNames.add("blue.png");
        iconNames.add("clipboard.png");
        iconNames.add("clock.png");
        iconNames.add("computer-x.png");
        iconNames.add("computer.png");
        iconNames.add("delete-document.png");
        iconNames.add("disabled.png");
        iconNames.add("document-properties.png");
        iconNames.add("document.png");
        iconNames.add("edit-delete.png");
        iconNames.add("empty.png");
        iconNames.add("fingerprint.png");
        iconNames.add("folder-delete.png");
        iconNames.add("folder.png");
        iconNames.add("gear.png");
        iconNames.add("gear2.png");
        iconNames.add("graph.png");
        iconNames.add("grey.png");
        iconNames.add("health-00to19.png");
        iconNames.add("health-20to39.png");
        iconNames.add("health-40to59.png");
        iconNames.add("health-60to79.png");
        iconNames.add("health-80plus.png");
        iconNames.add("help.png");
        iconNames.add("installer.png");
        iconNames.add("lock.png");
        iconNames.add("monitor.png");
        iconNames.add("new-computer.png");
        iconNames.add("new-document.png");
        iconNames.add("new-package.png");
        iconNames.add("new-user.png");
        iconNames.add("next.png");
        iconNames.add("nobuilt.png");
        iconNames.add("notepad.png");
        iconNames.add("orange-square.png");
        iconNames.add("package.png");
        iconNames.add("plugin.png");
        iconNames.add("previous.png");
        iconNames.add("red.png");
        iconNames.add("redo.png");
        iconNames.add("refresh.png");
        iconNames.add("save.png");
        iconNames.add("search.png");
        iconNames.add("secure.png");
        iconNames.add("setting.png");
        iconNames.add("star-gold.png");
        iconNames.add("star.png");
        iconNames.add("terminal.png");
        iconNames.add("up.png");
        iconNames.add("user.png");
        iconNames.add("yellow.png");
        iconNames.add("aborted.gif");
        iconNames.add("aborted_anime.gif");
        iconNames.add("blue.gif");
        iconNames.add("blue_anime.gif");
        iconNames.add("clipboard.gif");
        iconNames.add("clock.gif");
        iconNames.add("clock_anime.gif");
        iconNames.add("computer-flash.gif");
        iconNames.add("computer-x.gif");
        iconNames.add("computer.gif");
        iconNames.add("delete-document.gif");
        iconNames.add("disabled.gif");
        iconNames.add("disabled_anime.gif");
        iconNames.add("document-properties.gif");
        iconNames.add("document.gif");
        iconNames.add("edit-delete.gif");
        iconNames.add("empty.gif");
        iconNames.add("fingerprint.gif");
        iconNames.add("folder-delete.gif");
        iconNames.add("folder.gif");
        iconNames.add("gear.gif");
        iconNames.add("gear2.gif");
        iconNames.add("graph.gif");
        iconNames.add("green.gif");
        iconNames.add("green_anime.gif");
        iconNames.add("grey.gif");
        iconNames.add("grey_anime.gif");
        iconNames.add("health-00to19.gif");
        iconNames.add("health-20to39.gif");
        iconNames.add("health-40to59.gif");
        iconNames.add("health-60to79.gif");
        iconNames.add("health-80plus.gif");
        iconNames.add("help.gif");
        iconNames.add("installer.gif");
        iconNames.add("monitor.gif");
        iconNames.add("new-computer.gif");
        iconNames.add("new-document.gif");
        iconNames.add("new-package.gif");
        iconNames.add("new-user.gif");
        iconNames.add("next.gif");
        iconNames.add("nobuilt.gif");
        iconNames.add("nobuilt_anime.gif");
        iconNames.add("notepad.gif");
        iconNames.add("orange-square.gif");
        iconNames.add("package.gif");
        iconNames.add("previous.gif");
        iconNames.add("red.gif");
        iconNames.add("red_anime.gif");
        iconNames.add("redo.gif");
        iconNames.add("refresh.gif");
        iconNames.add("save.gif");
        iconNames.add("search.gif");
        iconNames.add("setting.gif");
        iconNames.add("star-gold.gif");
        iconNames.add("star.gif");
        iconNames.add("terminal.gif");
        iconNames.add("up.gif");
        iconNames.add("user.gif");
        iconNames.add("yellow.gif");
        iconNames.add("yellow_anime.gif");

        // 32x32
        iconNames.add("aborted.png");
        iconNames.add("accept.png");
        iconNames.add("attribute.png");
        iconNames.add("blue.png");
        iconNames.add("clipboard.png");
        iconNames.add("clock.png");
        iconNames.add("computer-x.png");
        iconNames.add("computer.png");
        iconNames.add("disabled.png");
        iconNames.add("empty.png");
        iconNames.add("error.png");
        iconNames.add("folder.png");
        iconNames.add("gear2.png");
        iconNames.add("graph.png");
        iconNames.add("grey.png");
        iconNames.add("health-00to19.png");
        iconNames.add("health-20to39.png");
        iconNames.add("health-40to59.png");
        iconNames.add("health-60to79.png");
        iconNames.add("health-80plus.png");
        iconNames.add("lock.png");
        iconNames.add("nobuilt.png");
        iconNames.add("orange-square.png");
        iconNames.add("package.png");
        iconNames.add("plugin.png");
        iconNames.add("red.png");
        iconNames.add("secure.png");
        iconNames.add("setting.png");
        iconNames.add("star-gold.png");
        iconNames.add("star.png");
        iconNames.add("user.png");
        iconNames.add("yellow.png");
        iconNames.add("aborted.gif");
        iconNames.add("aborted_anime.gif");
        iconNames.add("blue.gif");
        iconNames.add("blue_anime.gif");
        iconNames.add("clipboard.gif");
        iconNames.add("clock.gif");
        iconNames.add("clock_anime.gif");
        iconNames.add("computer-flash.gif");
        iconNames.add("computer-x.gif");
        iconNames.add("computer.gif");
        iconNames.add("disabled.gif");
        iconNames.add("disabled_anime.gif");
        iconNames.add("empty.gif");
        iconNames.add("error.gif");
        iconNames.add("folder.gif");
        iconNames.add("graph.gif");
        iconNames.add("green.gif");
        iconNames.add("green_anime.gif");
        iconNames.add("grey.gif");
        iconNames.add("grey_anime.gif");
        iconNames.add("health-00to19.gif");
        iconNames.add("health-20to39.gif");
        iconNames.add("health-40to59.gif");
        iconNames.add("health-60to79.gif");
        iconNames.add("health-80plus.gif");
        iconNames.add("nobuilt.gif");
        iconNames.add("nobuilt_anime.gif");
        iconNames.add("plugin.gif");
        iconNames.add("red.gif");
        iconNames.add("red_anime.gif");
        iconNames.add("setting.gif");
        iconNames.add("star-gold.gif");
        iconNames.add("star.gif");
        iconNames.add("user.gif");
        iconNames.add("yellow.gif");
        iconNames.add("yellow_anime.gif");

        // 48x48
        iconNames.add("aborted.png");
        iconNames.add("accept.png");
        iconNames.add("attribute.png");
        iconNames.add("blue.png");
        iconNames.add("clipboard.png");
        iconNames.add("computer-x.png");
        iconNames.add("computer.png");
        iconNames.add("disabled.png");
        iconNames.add("document.png");
        iconNames.add("empty.png");
        iconNames.add("error.png");
        iconNames.add("fingerprint.png");
        iconNames.add("folder-delete.png");
        iconNames.add("folder.png");
        iconNames.add("gear2.png");
        iconNames.add("graph.png");
        iconNames.add("grey.png");
        iconNames.add("health-00to19.png");
        iconNames.add("health-20to39.png");
        iconNames.add("health-40to59.png");
        iconNames.add("health-60to79.png");
        iconNames.add("health-80plus.png");
        iconNames.add("help.png");
        iconNames.add("installer.png");
        iconNames.add("lock.png");
        iconNames.add("monitor.png");
        iconNames.add("network.png");
        iconNames.add("nobuilt.png");
        iconNames.add("notepad.png");
        iconNames.add("orange-square.png");
        iconNames.add("package.png");
        iconNames.add("plugin.png");
        iconNames.add("red.png");
        iconNames.add("redo.png");
        iconNames.add("refresh.png");
        iconNames.add("search.png");
        iconNames.add("secure.png");
        iconNames.add("setting.png");
        iconNames.add("star-gold.png");
        iconNames.add("star.png");
        iconNames.add("system-log-out.png");
        iconNames.add("terminal.png");
        iconNames.add("user.png");
        iconNames.add("warning.png");
        iconNames.add("yellow.png");
        iconNames.add("aborted.gif");
        iconNames.add("aborted_anime.gif");
        iconNames.add("blue.gif");
        iconNames.add("blue_anime.gif");
        iconNames.add("clipboard.gif");
        iconNames.add("computer-flash.gif");
        iconNames.add("computer-x.gif");
        iconNames.add("computer.gif");
        iconNames.add("disabled.gif");
        iconNames.add("disabled_anime.gif");
        iconNames.add("document.gif");
        iconNames.add("empty.gif");
        iconNames.add("error.gif");
        iconNames.add("fingerprint.gif");
        iconNames.add("folder-delete.gif");
        iconNames.add("folder.gif");
        iconNames.add("gear2.gif");
        iconNames.add("graph.gif");
        iconNames.add("green.gif");
        iconNames.add("green_anime.gif");
        iconNames.add("grey.gif");
        iconNames.add("grey_anime.gif");
        iconNames.add("health-00to19.gif");
        iconNames.add("health-20to39.gif");
        iconNames.add("health-40to59.gif");
        iconNames.add("health-60to79.gif");
        iconNames.add("health-80plus.gif");
        iconNames.add("help.gif");
        iconNames.add("installer.gif");
        iconNames.add("monitor.gif");
        iconNames.add("network.gif");
        iconNames.add("nobuilt.gif");
        iconNames.add("nobuilt_anime.gif");
        iconNames.add("notepad.gif");
        iconNames.add("orange-square.gif");
        iconNames.add("package.gif");
        iconNames.add("plugin.gif");
        iconNames.add("red.gif");
        iconNames.add("red_anime.gif");
        iconNames.add("redo.gif");
        iconNames.add("refresh.gif");
        iconNames.add("search.gif");
        iconNames.add("secure.gif");
        iconNames.add("setting.gif");
        iconNames.add("star-gold.gif");
        iconNames.add("star.gif");
        iconNames.add("system-log-out.gif");
        iconNames.add("terminal.gif");
        iconNames.add("user.gif");
        iconNames.add("warning.gif");
        iconNames.add("yellow.gif");
        iconNames.add("yellow_anime.gif");
    }
}
