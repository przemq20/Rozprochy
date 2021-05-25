package edu.agh.ageOfEmpires.controller;

import edu.agh.ageOfEmpires.model.StartPage;
import edu.agh.ageOfEmpires.model.technology.Technology;
import edu.agh.ageOfEmpires.model.unit.AttackBonus;
import edu.agh.ageOfEmpires.model.unit.Unit;
import edu.agh.ageOfEmpires.service.technology.TechnologyService;
import edu.agh.ageOfEmpires.service.unit.UnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Controller
public class Controller {
    private final UnitService unitService;
    private final TechnologyService technologyService;
    Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(UnitService unitService, TechnologyService technologyService) {
        this.unitService = unitService;
        this.technologyService = technologyService;
    }

    @GetMapping("/")
    public String init(Model model, StartPage startPage) throws Exception {
        logger.info("Received GET '/'");
        List<Unit> units = unitService.getUnitList();
        model.addAttribute("units", units);
        return "unitChoose";
    }

    @PostMapping(path = "/")
    public String processForm(Model model, StartPage startPage) throws Exception {
        logger.info("Received POST '/'");
        int unit1Count = startPage.getUnit1Count();
        int unit2Count = startPage.getUnit2Count();
        int unit1Id = startPage.getUnit1Id();
        int unit2Id = startPage.getUnit2Id();
        Optional<Unit> unit1Op = unitService.getUnit(unit1Id);
        Optional<Unit> unit2Op = unitService.getUnit(unit2Id);

        if (unit1Op.isPresent() && unit2Op.isPresent() && unit1Count > 0 && unit2Count > 0) {
            Unit unit1 = unit1Op.get();
            Unit unit2 = unit2Op.get();

            Unit won = fight(unit1, unit2, unit1Count, unit2Count);
            Unit lost;
            if (won == unit1)
                lost = unit2;
            else lost = unit1;

            Map<String, Double> unit1Cost = calculateCost(unit1, unit1Count);
            Map<String, Double> unit2Cost = calculateCost(unit2, unit2Count);
            List<Technology> unit1AvailableTechnologies = getTechnologies(unit1);
            List<Technology> unit2AvailableTechnologies = getTechnologies(unit2);
            Map<String, Double> unit1TechCost = calculateTechnologiesCost(unit1AvailableTechnologies);
            Map<String, Double> unit2TechCost = calculateTechnologiesCost(unit2AvailableTechnologies);

            model.addAttribute("unit1Tech", unit1AvailableTechnologies);
            model.addAttribute("unit2Tech", unit2AvailableTechnologies);
            model.addAttribute("unit1TechCost", unit1TechCost);
            model.addAttribute("unit2TechCost", unit2TechCost);
            model.addAttribute("unit1Cost", unit1Cost);
            model.addAttribute("unit2Cost", unit2Cost);
            model.addAttribute("won", won);
            model.addAttribute("lost", lost);
            model.addAttribute("unit1", unit1);
            model.addAttribute("unit2", unit2);
            model.addAttribute("unit1Count", unit1Count);
            model.addAttribute("unit2Count", unit2Count);

            return "result";
        } else {
            try {
                throw new IllegalArgumentException();
            } catch (IllegalArgumentException e) {
                return "errorPage";
            }
        }
    }

    private Unit fight(Unit unit1, Unit unit2, int unit1Count, int unit2Count) {
        double calculatedAttack1 = calculateAttackPower(unit1, unit2);
        double calculatedAttack2 = calculateAttackPower(unit2, unit1);

        double calculatedDefence1 = calculateDefencePower(unit1);
        double calculatedDefence2 = calculateDefencePower(unit2);

        double calculatedPower1 = calculatedAttack1 / calculatedDefence2;
        double calculatedPower2 = calculatedAttack2 / calculatedDefence1;
        logger.info("Calculated power of " + unit1.getName() + " equals " + calculatedPower1);
        logger.info("Calculated power of " + unit2.getName() + " equals " + calculatedPower2);

        if (calculatedPower1 * unit1Count > calculatedPower2 * unit2Count)
            return unit1;
        else
            return unit2;
    }

    private double calculateDefencePower(Unit unit1) {
        int armorClose = Integer.parseInt(unit1.getArmor().split("/")[0]) + 1;
        int armorFar = Integer.parseInt(unit1.getArmor().split("/")[1]) + 1;
        return ((double) (armorClose) * unit1.getHit_points() + (armorFar) * unit1.getHit_points()) / 2;
    }


    private double calculateAttackPower(Unit unit1, Unit unit2) {
        int attack = unit1.getAttack() + calculateAttackBonus(unit1, unit2);
        int rangeClose = 1;
        int rangeFar = 1;
        if (unit1.getRange() != null) {
            rangeClose = Integer.parseInt(unit1.getRange().split("-")[0]);
            rangeFar = rangeClose;
            if (unit1.getRange().split("-").length > 1)
                rangeFar = Integer.parseInt(unit1.getRange().split("-")[1]);
        }

        double attackDelay = unit1.getAttack_delay() + 0.1;
        int accuracy = 100;

        if (unit1.getAccuracy() != null)
            accuracy = Integer.parseInt(unit1.getAccuracy().substring(0, unit1.getAccuracy().length() - 1));

        return ((attack + 0.2 * rangeFar) - (attackDelay)) * accuracy / 100;
    }

    private int calculateAttackBonus(Unit unit1, Unit unit2) {
        List<AttackBonus> attackBonuses = calculateAttackBonuses(unit1);
        int attackBonus = 0;

        if (attackBonuses.stream().map(bonus -> bonus.getUnitName().toLowerCase()).collect(Collectors.toList()).contains(unit2.getName().toLowerCase())) {
            int position = attackBonuses.stream().map(AttackBonus::getUnitName).collect(Collectors.toList()).indexOf(unit2.getName());
            attackBonus = attackBonuses.get(position).getBonus();
        }

        return attackBonus;
    }

    private List<AttackBonus> calculateAttackBonuses(Unit unit2) {
        List<AttackBonus> attackBonuses2 = new ArrayList<>();
        if (unit2.getAttack_bonus() != null) {
            unit2.getAttack_bonus().stream().map(a -> a.split(" ")).forEach(a ->
            {
                String[] aa = a[1].split("/");
                for (String d : aa) {
                    attackBonuses2.add(new AttackBonus(d, Integer.parseInt(a[0])));
                }
            });
        }
        return attackBonuses2;
    }

    private Map<String, Double> calculateCost(Unit unit1, int unit1Count) {
        Map<String, Double> map = new HashMap<>();
        double unit1Food = Double.parseDouble(unit1.getCost().getOrDefault("Food", 0).toString());
        double unit1Wood = Double.parseDouble(unit1.getCost().getOrDefault("Wood", 0).toString());
        double unit1Gold = Double.parseDouble(unit1.getCost().getOrDefault("Gold", 0).toString());
        double unit1Stone = Double.parseDouble(unit1.getCost().getOrDefault("Stone", 0).toString());

        if (unit1Food > 0)
            map.put("Food", unit1Food * unit1Count);
        if (unit1Wood > 0)
            map.put("Wood", unit1Wood * unit1Count);
        if (unit1Gold > 0)
            map.put("Gold", unit1Gold * unit1Count);
        if (unit1Stone > 0)
            map.put("Stone", (unit1Stone * unit1Count));
        return map;
    }

    private List<Technology> getTechnologies(Unit unit) throws Exception {
        List<Technology> allTechnologies = technologyService.getAvailableTechnologies();
        List<Technology> result = new ArrayList<>();
        String unitName = unit.getName().replace(" ", "_").toLowerCase();
        for (Technology technology : allTechnologies) {
            if (technology.getApplies_to() != null) {
                List<String> applicableFor = technology.getApplies_to().stream().map(a -> a.split("/")[a.split("/").length - 1]).collect(Collectors.toList());
                if (applicableFor.stream().anyMatch(a -> a.equals(unitName)))
                    result.add(technology);
            }
        }
        return result;
    }

    private Map<String, Double> calculateTechnologiesCost(List<Technology> technologies) {
        if (technologies.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, Double> map = new HashMap<>();
        double food = 0;
        double gold = 0;
        double wood = 0;
        double stone = 0;
        for (Technology technology : technologies) {
            food += Double.parseDouble(technology.getCost().getOrDefault("Food", 0).toString());
            gold += Double.parseDouble(technology.getCost().getOrDefault("Gold", 0).toString());
            wood += Double.parseDouble(technology.getCost().getOrDefault("Wood", 0).toString());
            stone += Double.parseDouble(technology.getCost().getOrDefault("Stone", 0).toString());
        }

        map.put("Food", food);
        map.put("Wood", wood);
        map.put("Gold", gold);
        map.put("Stone", stone);

        return map;
    }
}