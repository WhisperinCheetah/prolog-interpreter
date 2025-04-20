likes(alice, pizza).
likes(alice, pasta).
likes(bob, pasta).

likes_italian_food(Person) :-
    likes(Person, pizza) ;
    likes(Person, pasta).

:- initialization(main).

main :-
    likes_italian_food(alice),
    write('Alice likes Italian food.'), nl,
    likes_italian_food(bob),
    write('Bob likes Italian food.'), nl.
