SELECT students.name, students.age, faculty.name AS faculty_name
FROM students
INNER JOIN faculty ON students.faculty_id = faculty.id;

SELECT s.name, s.age
FROM students s
INNER JOIN avatar a ON s.id = a.student_id;