package edu.agh.ageOfEmpires.model.technology;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TechnologyList {
    List<Technology> technologies;

    public List<Technology> toList() {
        return new ArrayList<>(technologies);
    }
}
