ALTER TABLE students
ADD CONSTRAINT age_constraint CHECK (age >= 16);

ALTER TABLE students
ADD CONSTRAINT name_constraint UNIQUE (name),
ALTER COLUMN name SET NOT NULL

ALTER TABLE faculty
ADD CONSTRAINT name_color_unique UNIQUE (name, color);

ALTER TABLE students
ALTER COLUMN age SET default 20;