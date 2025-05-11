grade(alice, a).
grade(bob, b).
grade(carol, a).
grade(dave, c).

got_an_a(Student) :-
    grade(Student, Grade),
    Grade = a.

did_not_get_an_a(Student) :-
    grade(Student, Grade),
    Grade \= a.

:- initialization(main).

main :-
    write("While "),
    got_an_a(X),
    write(X), write(" got an A, "), fail;
    write("but "),
    did_not_get_an_a(Y),
    write(Y), write(" did not get an A, "), fail; write("unfortunately."), nl.
