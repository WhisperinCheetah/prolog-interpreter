age(robespierre, 25).
age(danton, 29).
age(marat, 35).
age(camus, 22).
age(desmoulins, 19).

eligible_for_event(Person) :-
    age(Person, Age),
    between(20, 30, Age).

:- initialization(list_eligible_members).

list_eligible_members :-
    eligible_for_event(Person),
    write('Citizen '), write(Person), write(' is eligible for the event.'), nl,
    fail.

