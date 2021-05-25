package edu.agh.ageOfEmpires.model.technology;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Technology {
    private int id;
    private String name;
    private String description;
    private String expansion;
    private String age;
    private String develops_in;
    private Map<String, Object> cost;
    private int build_time;
    private List<String> applies_to;
}
