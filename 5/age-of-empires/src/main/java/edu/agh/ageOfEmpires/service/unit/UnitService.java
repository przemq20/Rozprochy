package edu.agh.ageOfEmpires.service.unit;

import com.google.gson.Gson;
import edu.agh.ageOfEmpires.model.unit.Unit;
import edu.agh.ageOfEmpires.model.unit.UnitList;
import edu.agh.ageOfEmpires.service.DataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Configuration
@Service
public class UnitService {
    Gson gson = new Gson();

    public List<Unit> getUnitList() throws Exception {
        String json = DataSource.requestData(DataSource.URL + "/units");
        UnitList result = gson.fromJson(json, UnitList.class);
        return Optional.of(result)
                .map(UnitList::toList)
                .orElse(List.of());
    }

    public Optional<Unit> getUnit(int index) throws Exception {
        String json = DataSource.requestData(DataSource.URL + "/unit/" + index);
        Unit result = gson.fromJson(json, Unit.class);
        return Optional.of(result);
    }
}
