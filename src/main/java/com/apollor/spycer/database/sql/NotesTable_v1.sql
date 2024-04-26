CREATE TABLE Notes (
    note_id varchar(36) NOT NULL,
    recipe_id varchar(36) NOT NULL,
    note varchar(255) NOT NULL,
    PRIMARY KEY (note_id),
    FOREIGN KEY (recipe_id) REFERENCES Recipes(recipe_id)
);