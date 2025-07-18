package jwhs.cheftoo.cookingorder.service;

import jwhs.cheftoo.cookingorder.entity.CookingOrder;
import jwhs.cheftoo.cookingorder.repository.CookingOrderRepository;
import jwhs.cheftoo.image.service.S3Service;
import jwhs.cheftoo.recipe.entity.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CookingOrderService {
    private final CookingOrderRepository cookingOrderRepository;
    private final S3Service s3Service;

    public List<CookingOrder> findAllByRecipe(Recipe recipe) {
        return cookingOrderRepository.findAllByRecipe(recipe);
    }
    public void deleteCookingOrderImageByRecipe(Recipe recipe) {
        List<CookingOrder> cookingOrderList = findAllByRecipe(recipe);
        for (int i = 0; i < cookingOrderList.size(); i++) {
            String key = cookingOrderList.get(i).getImgPath();
            s3Service.deleteCookingOrderImage(key);
        }
    }
}
