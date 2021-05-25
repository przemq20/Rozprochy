package edu.agh.ageOfEmpires.model.unit;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Unit {
    private int id;
    private String name;
    private String description;
    private String expansion;
    private String age;
    private String created_in;
    private Map<String, Object> cost;
    private int build_time;
    private double reload_time;
    private double attack_delay;
    private double movement_rate;
    private int line_of_sight;
    private int hit_points;
    private String range;
    private int attack;
    private String armor;
    private List<String> attack_bonus;
    private List<String> armor_bonus;
    private int search_bonus;
    private String accuracy;
    private double blast_radius;
}
