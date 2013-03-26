package inesskillsparser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

/**
 * @author fet
 */
public class INESSkillsParser {

    static final String url = "http://www.ines.org.es/content/competencias-por-miembro";
    TagNode rootNode;

    public INESSkillsParser() throws IOException {
        URL htmlPage = new URL(url);
        HtmlCleaner cleaner = new HtmlCleaner();
        rootNode = cleaner.clean(htmlPage);
    }

    public ArrayList parse() {
        TagNode tables[] = rootNode.getElementsByName("table", true);
        ArrayList<Skill> skills = new ArrayList<>();

        for (int i = 0; tables != null && i < tables.length; i++) {
            TagNode tableTR[] = tables[i].getElementsByName("tr", true);
            //SKILL NAME
            Skill skill = new Skill(tableTR[0].getText().toString().trim());
            for (int j = 3; tableTR != null && j < tableTR.length; j++) {
                TagNode tableTD[] = tableTR[j].getElementsByName("td", true);
                //COMPANY NAME + SKILL LEVEL [HIGH | MED | LOW]
                Company company = new Company(tableTD[0].getText().toString().trim());
                if (tableTD[1].getText().toString().trim().equals("1")) {
                    company.isHigh = true;
                    company.isMed = false;
                    company.isLow = false;
                }
                if (tableTD[2].getText().toString().trim().equals("1")) {
                    company.isMed = true;
                    company.isHigh = false;
                    company.isLow = false;
                }
                if (tableTD[3].getText().toString().trim().equals("1")) {
                    company.isLow = true;
                    company.isMed = false;
                    company.isHigh = false;
                }
                skill.addCompany(company);
            }
            skills.add(skill);
        }
        return skills;
    }

    public ArrayList getCompanies(ArrayList<Skill> skills) {
        ArrayList<Company> companies = new ArrayList<>();

        Iterator<Skill> itr = skills.iterator();
        while (itr.hasNext()) {
            Skill s = itr.next();
            //System.out.println(s.name);
            ArrayList<Company> skillCompanies = s.companies;
            Iterator<Company> itr2 = skillCompanies.iterator();
            while (itr2.hasNext()) {
                Company c = itr2.next();
                //System.out.println("\t" + c.name + " lvl:" + c.getSkillLevel());
                String name = c.name;
                String skill = s.name;
                String level = c.getSkillLevel();
                Iterator<Company> comp = companies.iterator();
                boolean found = false;
                while (comp.hasNext()) {
                    Company c2 = comp.next();
                    if (name.equals(c2.name)) {
                        c2.addSkill(skill, level);
                        found = true;
                    }
                }
                if (!found) {
                    Company c3 = new Company(name);
                    c3.addSkill(skill, level);
                    companies.add(c3);
                }
            }
        }
        return companies;
    }
}