package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.entity.Category;
import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.service.impl.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@GetMapping
    //public String listCategories(Model model) {
    //    model.addAttribute("categories", categoryService.getAllCategories());
    //    return "admin/Categories/categories";
    //}
    @GetMapping
    public String getCategoryList(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Category> categories = (search != null && !search.isEmpty())
                ? categoryService.searchCategory(search)
                : categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("search", search);
        return "admin/Categories/categories";
    }
    @GetMapping("/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/Categories/addCategory";
    }


    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "admin/Categories/editCategory";
    }


    @PostMapping("/update")
    public String updateCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }


}
