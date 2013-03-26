/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inesskillsparser;

import java.util.ArrayList;

/**
 *
 * @author fet
 */
public class Company {

    public String name;
    public boolean isLow = true;
    public boolean isMed = false;
    public boolean isHigh = false;
    public ArrayList<Skill> skills = new ArrayList<>();

    Company(String name) {
        this.name = name;
    }

    public String getSkillLevel() {
        String response = "Intermediate";
        if (isMed) {
            response = "Expert";
        }
        if (isHigh) {
            response = "Advanced";
        }
        return response;
    }

    public void addSkill(String name, String level) {
        Skill skill = new Skill(name);
        skill.level = level;
        skills.add(skill);
    }
}
