leq(0, _).
leq(s(X), s(Y)) :- leq(X, Y).

:- initialization(main).

main :-
    leq(MainX, s(s(s(0)))),
    write(MainX), nl,
    fail.
