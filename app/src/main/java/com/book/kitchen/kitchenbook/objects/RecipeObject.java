package com.book.kitchen.kitchenbook.objects;

import android.graphics.Bitmap;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 25/11/2015.
 */
public class RecipeObject {
    String title;
    String shortDescription;
    String cookingTime;
    Boolean isPublic;
    int category;
    String longDescription;
    List<String> quantityList = new ArrayList<String>();
    List<String> ingredients = new ArrayList<String>();
    Bitmap mainImage;
    Bitmap image1;
    Bitmap image2;
    Bitmap image3;
    Bitmap image4;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getQuantity() {
        return quantityList;
    }

    public void setQuantity(List<String> quantity) {
        this.quantityList = quantity;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void appendIngredient(String ingredient){
        ingredients.add(ingredient);
    }

    public void appendQuantity(String quantity){
        quantityList.add(quantity);
    }
    public Bitmap getMainImage() {
        return mainImage;
    }

    public void setMainImage(Bitmap mainImage) {
        this.mainImage = mainImage;
    }

    public Bitmap getImage1() {
        return image1;
    }

    public void setImage1(Bitmap image1) {
        this.image1 = image1;
    }

    public Bitmap getImage2() {
        return image2;
    }

    public void setImage2(Bitmap image2) {
        this.image2 = image2;
    }

    public Bitmap getImage3() {
        return image3;
    }

    public void setImage3(Bitmap image3) {
        this.image3 = image3;
    }

    public Bitmap getImage4() {
        return image4;
    }

    public void setImage4(Bitmap image4) {
        this.image4 = image4;
    }
}
