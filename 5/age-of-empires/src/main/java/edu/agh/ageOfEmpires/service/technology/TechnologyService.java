package edu.agh.ageOfEmpires.service.technology;

import com.google.gson.Gson;
import edu.agh.ageOfEmpires.model.technology.Technology;
import edu.agh.ageOfEmpires.model.technology.TechnologyList;
import edu.agh.ageOfEmpires.service.DataSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TechnologyService {
    Gson gson = new Gson();

    public List<Technology> getAvailableTechnologies() throws Exception {
        String technologiesData = DataSource.requestData(DataSource.URL + "/technologies");
        TechnologyList result = gson.fromJson(technologiesData, TechnologyList.class);
        return Optional.of(result)
                .map(TechnologyList::toList)
                .orElse(List.of());
    }

}
