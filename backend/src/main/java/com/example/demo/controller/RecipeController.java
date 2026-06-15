package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

	private final RecipeRepository recipeRepository;

	public RecipeController(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@GetMapping
	public List<Recipe> getAllRecipes() {
		return recipeRepository.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Recipe createRecipe(@Valid @RequestBody Recipe recipe) {
		recipe.setId(null);
		return recipeRepository.save(recipe);
	}

	@PutMapping("/{id}")
	public Recipe updateRecipe(@PathVariable Long id, @Valid @RequestBody Recipe updatedRecipe) {
		return recipeRepository.findById(id)
				.map(existing -> {
					existing.setName(updatedRecipe.getName());
					existing.setInstructions(updatedRecipe.getInstructions());
					existing.setIngredients(updatedRecipe.getIngredients());
					return recipeRepository.save(existing);
				})
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRecipe(@PathVariable Long id) {
		if (!recipeRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found");
		}
		recipeRepository.deleteById(id);
	}

}
