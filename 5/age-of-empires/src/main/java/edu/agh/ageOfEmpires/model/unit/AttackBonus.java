package edu.agh.ageOfEmpires.model.unit;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttackBonus {
    private final String unitName;
    private final int bonus;
}
