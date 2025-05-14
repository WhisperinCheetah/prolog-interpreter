check_equal(X, Y) :-
    X = Y,
    write('X = Y succeeded'), nl.

check_identical(X, Y) :-
    X == Y, !,
    write('X == Y succeeded'), nl.

check_identical(_, _) :-
    write('X == Y failed'), nl.

:- initialization(main).

main :-
    check_identical(A, 13),
    check_equal(A, 13),
    check_identical(A, 13),
    check_equal(42, 42),
    check_identical(42, 42).

