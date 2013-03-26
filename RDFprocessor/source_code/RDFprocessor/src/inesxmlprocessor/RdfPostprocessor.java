package inesxmlprocessor;

import inesskillsparser.Company;
import inesskillsparser.INESSkillsParser;
import inesskillsparser.Skill;
import java.io.IOException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import webchecker.BingRanking;

/**
 * Para procesar los RDF de ines, añadir las skills, eliminar campos incorrectos
 * y añadir provenance
 *
 * @author Felipe
 */
public class RdfPostprocessor {

    boolean valid = false;
    int numCompanies = 0;
    BingRanking g = new BingRanking();
    Long ranking = 0L;
    private String provenanceName;

    public void processXML(String fileName, ArrayList<Company> companies) {
        Document doc = loadXML(fileName);
        doc = processXMLSkillsAndProvenance(doc, companies);
        //if (valid) {
        if (true) {
            saveXML(doc, fileName);
        }
    }

    public Document loadXML(String fileName) {
        Document doc = null;
        try {
            File file = new File("data/input/" + fileName);
            //Create instance of DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //Get the DocumentBuilder
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            //Parsing XML Document
            doc = docBuilder.parse(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc;
    }

    public Document processXMLSkillsAndProvenance(Document doc, ArrayList<Company> companies) {
        doc.getDocumentElement().normalize();

        valid = false;

        //GET COMPANY NAME
        NodeList nameRoot = doc.getElementsByTagName("ecos:CompanyName");
        NodeList nameChild1 = nameRoot.item(0).getChildNodes();
        NodeList nameChild2 = nameChild1.item(1).getChildNodes();
        String cName = nameChild2.item(1).getTextContent();
        //System.out.println(cName );

        //GET COMPANY SKILLS CONTAINER
        NodeList skillsRoot = doc.getElementsByTagName("ecos:Skill");

        for (int j = 0; j < skillsRoot.getLength(); j++) {

            NodeList desc1 = skillsRoot.item(j).getChildNodes();
            NodeList desc2 = desc1.item(1).getChildNodes();
            NodeList desc3 = desc2.item(1).getChildNodes();
            NodeList skills = desc3.item(1).getChildNodes();

            //remove old skills
            for (int s = 0; s < skills.getLength(); s++) {
                if (skills.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    desc3.item(1).removeChild(skills.item(s));
                }
            }
        }

        Iterator<Company> comp = companies.iterator();

        while (comp.hasNext()) {
            Company c = comp.next();

            if (c.name.contains(cName) || cName.contains(c.name)) {

                valid = true;
                ArrayList<Skill> compSkills = c.skills;
                Iterator<Skill> itr3 = compSkills.iterator();
                while (itr3.hasNext()) {
                    Skill s2 = itr3.next();

                    Element li = doc.createElement("rdf:li");
                    Element desc = doc.createElement("rdf:Description");
                    Element name = doc.createElement("ecos:name");
                    Element level = doc.createElement("ecos:level");
                    name.setTextContent(s2.name);
                    level.setTextContent(s2.level);
                    desc.appendChild(name);
                    desc.appendChild(level);
                    li.appendChild(desc);

                    NodeList desc1 = skillsRoot.item(0).getChildNodes();
                    NodeList desc2 = desc1.item(1).getChildNodes();
                    NodeList desc3 = desc2.item(1).getChildNodes();
                    desc3.item(1).appendChild(li);
                }
            }
        }

        NodeList details = null;
        NodeList plan1 = null;
        try {
            //get RDF plan CONTAINER
            NodeList planRoot = doc.getElementsByTagName("ecos:Plan");
            plan1 = planRoot.item(0).getChildNodes();
            details = plan1.item(1).getChildNodes();
        } catch (Exception e) {
            //System.out.println(e);
        }

        //remove duplicate plan detail
        if (details != null && plan1 != null) {
            for (int s = 2; s < details.getLength(); s++) {
                if (details.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    //System.out.println(details.item(s).getNodeName());
                    plan1.item(1).removeChild(details.item(s));
                }
            }
        }

        //get Enterprise CONTAINER and add INES PROVENANCE
        NodeList enterpriseRoot = doc.getElementsByTagName("ecos:Enterprise");
        try {
            Element provenance = doc.createElement("ecos:provenance");
            Element desc = doc.createElement("rdf:Description");
            Element name = doc.createElement("ecos:name");
            name.setTextContent(provenanceName);
            desc.appendChild(name);
            provenance.appendChild(desc);


            enterpriseRoot.item(0).appendChild(provenance);
        } catch (Exception e) {
        }

        //set Google based Ranking
        Element provenance = doc.createElement("ecos:ranking");
        Element desc = doc.createElement("rdf:Description");
        Element name = doc.createElement("ecos:value");
        try {
            ranking = g.getResultsNumber(cName);
            name.setTextContent(ranking.toString());
            desc.appendChild(name);
            provenance.appendChild(desc);


            enterpriseRoot.item(0).appendChild(provenance);
        } catch (Exception e) {
            System.err.println(e);
            ranking = 0L;
            name.setTextContent(ranking.toString());
            desc.appendChild(name);
            provenance.appendChild(desc);
        }

        System.out.println(++numCompanies + " : " + cName + " : " + ranking);

        return doc;
    }

    public void saveXML(Document doc, String fileName) {
        try {
            //setting up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //generating string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            //Saving the XML content to File
            OutputStream f0;
            byte[] buf = xmlString.getBytes();
            f0 = new FileOutputStream("data/output/" + fileName);
            for (int i = 0; i < buf.length; i++) {
                f0.write(buf[i]);
            }
            f0.close();
            buf = null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        String provenance = args[0];
        INESSkillsParser parser = new INESSkillsParser();
        ArrayList<Skill> skills = parser.parse();
        ArrayList<Company> companies = parser.getCompanies(skills);

        RdfPostprocessor p = new RdfPostprocessor();
        p.provenanceName = provenance;
        File folder = new File("data/input");
        File[] listOfFiles = folder.listFiles();
        String filename;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                filename = listOfFiles[i].getName();
                if (filename.toLowerCase().endsWith(".rdf")) {
                    p.processXML(filename, companies);
                }
            }
        }
    }
}
