package jwhs.cheftoo.sauce.service;


import jwhs.cheftoo.recipe.entity.Recipe;
import jwhs.cheftoo.sauce.entity.Sauce;
import jwhs.cheftoo.sauce.repository.SauceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SauceService {

    private final SauceRepository sauceRepository;

    public List<Sauce> findAllSauceByRecipe(Recipe recipe) {
        return sauceRepository.findAllByRecipe(recipe);
    }

    public void saveAll(List<Sauce> sauceList) {
        sauceRepository.saveAll(sauceList);
    }


}
