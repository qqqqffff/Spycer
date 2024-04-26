CREATE TABLE Procedures (
    procedure_id varchar(36) NOT NULL,
    recipe_id varchar(36) NOT NULL,
    procedure varchar(255) NOT NULL,
    procedure_time int NOT NULL,
    PRIMARY KEY (procedure_id),
    FOREIGN KEY (recipe_id) REFERENCES Recipes(recipe_id)
);