package edu.agh.ageOfEmpires.model.unit;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UnitList {
    List<Unit> units;

    public List<Unit> toList() {
        return new ArrayList<>(units);
    }
}
