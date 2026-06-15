package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;

@WebMvcTest(RecipeController.class)
@AutoConfigureMockMvc(addFilters = false)
class RecipeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private RecipeRepository recipeRepository;

	@Test
	void getAllRecipes_returnsRecipeList() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setId(1L);
		recipe.setName("Pancakes");
		recipe.setIngredients(List.of("flour", "eggs"));
		recipe.setInstructions("Mix and cook.");

		when(recipeRepository.findAll()).thenReturn(List.of(recipe));

		mockMvc.perform(get("/api/recipes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("Pancakes"))
				.andExpect(jsonPath("$[0].ingredients[0]").value("flour"));
	}

	@Test
	void createRecipe_withValidPayload_returnsCreatedRecipe() throws Exception {
		Recipe savedRecipe = new Recipe();
		savedRecipe.setId(2L);
		savedRecipe.setName("Test");
		savedRecipe.setIngredients(List.of("flour"));
		savedRecipe.setInstructions("Bake");

		when(recipeRepository.save(any(Recipe.class))).thenReturn(savedRecipe);

		mockMvc.perform(post("/api/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "Test",
						  "ingredients": ["flour"],
						  "instructions": "Bake"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(2))
				.andExpect(jsonPath("$.name").value("Test"));
	}

	@Test
	void createRecipe_withBlankName_returnsBadRequest() throws Exception {
		mockMvc.perform(post("/api/recipes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "",
						  "ingredients": ["flour"],
						  "instructions": "Bake"
						}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void updateRecipe_whenRecipeExists_returnsUpdatedRecipe() throws Exception {
		Recipe existing = new Recipe();
		existing.setId(3L);
		existing.setName("Old Name");
		existing.setIngredients(List.of("flour"));
		existing.setInstructions("Old instructions");

		Recipe updated = new Recipe();
		updated.setId(3L);
		updated.setName("Updated Name");
		updated.setIngredients(List.of("sugar"));
		updated.setInstructions("Updated instructions");

		when(recipeRepository.findById(3L)).thenReturn(java.util.Optional.of(existing));
		when(recipeRepository.save(any(Recipe.class))).thenReturn(updated);

		mockMvc.perform(put("/api/recipes/3")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "Updated Name",
						  "ingredients": ["sugar"],
						  "instructions": "Updated instructions"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Updated Name"));
	}

	@Test
	void deleteRecipe_whenRecipeExists_returnsNoContent() throws Exception {
		when(recipeRepository.existsById(4L)).thenReturn(true);

		mockMvc.perform(delete("/api/recipes/4"))
				.andExpect(status().isNoContent());

		verify(recipeRepository).deleteById(4L);
	}

	@Test
	void deleteRecipe_whenRecipeMissing_returnsNotFound() throws Exception {
		when(recipeRepository.existsById(99L)).thenReturn(false);

		mockMvc.perform(delete("/api/recipes/99"))
				.andExpect(status().isNotFound());
	}

}
