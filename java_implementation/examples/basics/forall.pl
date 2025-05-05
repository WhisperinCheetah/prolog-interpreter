likes(alice, pizza).
likes(alice, pasta).
likes(bob, pasta).

:- initialization(main).

main :-
    forall(likes(X, pizza), X = alice),
    write('Only alice likes pizza.'), nl,
    forall(likes(X, pizza), X = bob),
    write('Bob should not like pizza.'), nl.
