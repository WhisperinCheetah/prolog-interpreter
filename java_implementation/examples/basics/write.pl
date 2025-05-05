:- initialization(main).

main :- write('gpl zegt: '), groet(wereld), nl.

groet(X) :- write(dag(X)).
