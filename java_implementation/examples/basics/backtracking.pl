leq(0, _).
leq(s(X), s(Y)) :- leq(X, Y).

:- initialization(main).

main :-
    leq(X, s(s(s(0)))),
    write(X), nl,
    fail.
