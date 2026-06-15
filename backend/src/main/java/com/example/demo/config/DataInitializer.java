package com.example.demo.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	private final RecipeRepository recipeRepository;

	public DataInitializer(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public void run(String... args) {
		if (recipeRepository.count() > 0) {
			return;
		}

		Recipe pancakes = new Recipe();
		pancakes.setName("Fluffy Pancakes");
		pancakes.setIngredients(List.of("flour", "eggs", "milk", "butter", "sugar", "baking powder"));
		pancakes.setInstructions("Mix dry ingredients, whisk in wet ingredients, and cook on a griddle until golden.");
		recipeRepository.save(pancakes);

		Recipe carbonara = new Recipe();
		carbonara.setName("Spaghetti Carbonara");
		carbonara.setIngredients(List.of("spaghetti", "eggs", "parmesan", "pancetta", "black pepper"));
		carbonara.setInstructions("Cook pasta, crisp pancetta, toss with eggs and cheese off heat until creamy.");
		recipeRepository.save(carbonara);

		Recipe cookies = new Recipe();
		cookies.setName("Chocolate Chip Cookies");
		cookies.setIngredients(List.of("flour", "butter", "sugar", "brown sugar", "eggs", "vanilla", "chocolate chips"));
		cookies.setInstructions("Cream butter and sugars, mix in eggs and dry ingredients, fold in chips, and bake at 350F for 10-12 minutes.");
		recipeRepository.save(cookies);
	}

}
