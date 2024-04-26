CREATE TABLE Recipes (
    recipe_id varchar(36) NOT NULL,
    recipe_title varchar(255) NOT NULL,
    created_date timestamp NOT NULL,
    updated_date timestamp NOT NULL,
    author_id varchar(36) NOT NULL,
    rating float(4),
    tag_1 varchar(64),
    tag_2 varchar(64),
    tag_3 varchar(64),
    PRIMARY KEY (recipe_id),
    FOREIGN KEY (author_id) REFERENCES Users(userid)
);