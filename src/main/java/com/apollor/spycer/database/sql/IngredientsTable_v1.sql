CREATE TABLE Ingredients (
    ingredient_id varchar(36) NOT NULL,
    recipe_id varchar(36) NOT NULL,
    ingredient varchar(64) NOT NULL,
    amount varchar(128) NOT NULL,
    PRIMARY KEY (ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES Recipes(recipe_id)
);