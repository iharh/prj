package stuff;

import org.junit.jupiter.api.Test;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class StuffTests {
    @Test
    void testXpath() throws Exception {
        try (FileInputStream fileIS = new FileInputStream(new File("/home/iharh/Downloads/tutorials.xml"))) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(fileIS);

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/Tutorials/Tutorial";

            NodeList nodes = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            assertThat(nodes).isNotNull();
            assertThat(nodes.getLength()).isEqualTo(2);
        }
    }
}
