package org.jenkins;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class IconMigrate {

    private static final String LAYOUT_NS = "/lib/layout";

    private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    private static TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private static Set<String> mightNeedFixing = new LinkedHashSet<String>();
    private static File mightNeedFixingDetailFile;

    static {
        documentBuilderFactory.setNamespaceAware(true);
    }

    private File file;
    private Document doc;
    private String docPreamble;
    private int transformCount = 0;
    private String docAsString;

    public IconMigrate(File file) throws Exception {
        if (!file.isFile()) {
            System.err.println(String.format("File '%s' doesn't exist, or is not a readable file.", file.getAbsolutePath()));
            System.exit(1);
            return;
        }

        this.file = file;
        docAsString = readStream(new FileInputStream(file));
    }

    public IconMigrate(InputStream docStream) throws Exception {
        docAsString = readStream(docStream);
    }

    public void processJellyFile() throws IOException, SAXException, ParserConfigurationException {
        buildDoc();
        processElement(doc.getDocumentElement());
    }

    public void processTextFile() {
        if (docAsString.contains("<img ") || docAsString.contains(":task ")) {
            requiresHumanCheck(file, "<img>");
        }
    }

    private void buildDoc() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

        doc = docBuilder.parse(new InputSource(new StringReader(docAsString)));

        Element rootElement = doc.getDocumentElement();

        int indexOfRootElement = docAsString.indexOf("<" + rootElement.getNodeName());
        if (indexOfRootElement != -1) {
            docPreamble = docAsString.substring(0, indexOfRootElement);
        } else {
            docPreamble = "";
        }
    }

    public void serializeDoc(File file) throws Exception {
        serializeDoc(new FileWriter(file));
    }

    public void serializeDoc(Writer writer) throws Exception {
        try {
            writer.write(docPreamble);

            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(new DOMSource(doc.getDocumentElement()), new StreamResult(writer));
        } finally {
            writer.close();
        }
    }

    private void processElement(Element element) {
        if (element.hasChildNodes()) {
            NodeList children = element.getChildNodes();
            int numChildren = children.getLength();

            for (int i = 0 ; i < numChildren; i++) {
                Node child = children.item(i);
                if (child.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) child;
                    if (childElement.getTagName().equalsIgnoreCase("img")) {
                        transformImg(childElement);
                    } else if (childElement.getLocalName().equals("task") && LAYOUT_NS.equals(childElement.getNamespaceURI())) {
                        transformTask(childElement);
                    } else {
                        processElement(childElement);
                    }
                }
            }
        }
    }

    private void transformImg(Element img) {
        String src = img.getAttribute("src");
        IconUrl iconUrl = new IconUrl(src);

        if (iconUrl.isTransformable()) {
            // This <img> looks safe to transform
            Element icon = doc.createElementNS(LAYOUT_NS, "l:icon");
            icon.setAttribute("name", iconUrl.getIconQualifiedName());

            mapAttribute("title", img, "tooltip", icon);
            mapAttribute("tooltip", img, "tooltip", icon); // will override previous title->tooltip mapping, if there was one.
            mapAttribute("style", img, "style", icon);
            mapAttribute("onclick", img, "onclick", icon);

            // Replace the <img> with the <l:icon>
            img.getParentNode().replaceChild(icon, img);

            // Add the namespace...
            addLayoutNSDecl();

            transformCount++;
        } else if (iconUrl.requiresHumanCheck()) {
            // This <img> needs to be checked by a human
            requiresHumanCheck(file, "<img> - " + iconUrl.getUrl());
        }
    }

    private void transformTask(Element task) {
        String icon = task.getAttribute("icon");
        IconUrl iconUrl = new IconUrl(icon);

        if (iconUrl.isTransformable()) {
            // This <task> looks safe to transform
            task.setAttribute("icon", iconUrl.getIconQualifiedName());
            transformCount++;
        } else if (!iconUrl.isAlreadyQualifiedIconName()) {
            // This <task> needs to be checked by a human
            requiresHumanCheck(file, "<l:task> - " + iconUrl.getUrl());
        }
    }

    private static void requiresHumanCheck(File fileToCheck, String reason) {
        if (fileToCheck != null) {
            String fileKey = "\t" + fileToCheck.getAbsolutePath() + "\n";

            if (!mightNeedFixing.contains(fileKey)) {
                mightNeedFixing.add(fileKey);
                addLineToMightNeedFixingDetailFile(fileToCheck.getAbsolutePath());
            }
            addLineToMightNeedFixingDetailFile("\t" + reason);
        }
    }

    private static void addLineToMightNeedFixingDetailFile(String lineText) {
        try {
            FileWriter fileWriter = new FileWriter(mightNeedFixingDetailFile, true);

            try {
                fileWriter.write(lineText);
                fileWriter.write("\n");
                fileWriter.flush();
            } finally {
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void mapAttribute(String fromAttr, Element from, String toAttr, Element to) {
        String attributeVal = from.getAttribute(fromAttr);
        if (attributeVal != null && attributeVal.length() > 0) {
            to.setAttribute(toAttr, attributeVal);
        }
    }

    private void addLayoutNSDecl() {
        doc.getDocumentElement().setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI , "xmlns:l", LAYOUT_NS);
    }

    private String readStream(InputStream docStream) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(docStream);
            char[] buffer = new char[256];
            int readCount = 0;

            while ((readCount = reader.read(buffer)) != -1) {
                stringBuilder.append(buffer, 0, readCount);
            }

            return stringBuilder.toString();
        } finally {
            // Going to close here for convenience sake
            docStream.close();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            File file = new File(args[0]);

            if (file.isFile()) {
                processFile(file);
            } else if (file.isDirectory()) {
                mightNeedFixing.clear();

                mightNeedFixingDetailFile = File.createTempFile("icon-migrate-", ".txt");

                int numFilesProcessed = processDir(file);
                System.out.println();
                System.out.println(String.format("+++ Processed %d files.", numFilesProcessed));

                if (!mightNeedFixing.isEmpty()) {
                    System.out.println(String.format("+++ The following %d files have <img> and/or <l:task> elements that need manual translation (or we could not parse them to check):", mightNeedFixing.size()));
                    System.out.print(mightNeedFixing.toString());

                    System.out.println("\n\n");
                    System.out.println("Detail file: " + mightNeedFixingDetailFile.getAbsolutePath());
                }
            }
        } else {
            System.out.println("No file/directory name provided.");
        }
    }

    public static boolean processFile(File file) throws Exception {
        IconMigrate iconMigrate = new IconMigrate(file);

        if (file.getName().endsWith(".jelly")) {
            iconMigrate.processJellyFile();
        } else {
            iconMigrate.processTextFile();
        }

        if (iconMigrate.transformCount > 0) {
            System.out.println("Processed " + file.getAbsolutePath());
            iconMigrate.serializeDoc(file);
            return true;
        }
        return false;
    }

    public static int processDir(File dir) {
        if (dir.getName().equals("target") && new File(dir.getParentFile(), "pom.xml").exists()) {
            // Maven target dir... ignore...
            return 0;
        }

        File[] files = dir.listFiles();
        int numFilesProcessed = 0;

        for (File file : files) {
            if (file.isFile()) {
                if (file.getName().endsWith(".jelly") || file.getName().endsWith(".groovy")) {
                    try {
                        if (processFile(file)) {
                            numFilesProcessed++;
                        }
                    } catch (Exception e) {
                        System.err.println("Error processing " + file.getAbsolutePath());
                        requiresHumanCheck(file, "Error processing - " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } else if (file.isDirectory()) {
                numFilesProcessed += processDir(file);
            }
        }

        return numFilesProcessed;
    }
}
