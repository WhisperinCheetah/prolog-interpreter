% Merlin's Potion lab

:- dynamic ingredient/1.

% Rules of the game

% Add a new ingredient if it's not already present
add_ingredient(Item) :-
    \+ ingredient(Item),
    assertz(ingredient(Item)),
    write('Added ingredient: '), write(Item), nl.

% Use an ingredient (remove it from memory)
use_ingredient(Item) :-
    ingredient(Item),
    retract(ingredient(Item)),
    write('Used ingredient: '), write(Item), nl.

% Make a potion from two ingredients
make_potion(Item1, Item2) :-
    ingredient(Item1),
    ingredient(Item2),
    Item1 \= Item2,
    retract(ingredient(Item1)),
    retract(ingredient(Item2)),
    write('You mixed '), write(Item1), write(' and '), write(Item2),
    write(' to create a potion!'), nl.

% Show what's left
show_ingredients :-
    findall(X, ingredient(X), List),
    write('Remaining ingredients: '), write(List), nl.

% Reset lab
reset_lab :-
    retractall(ingredient(_)),
    write('Potion lab reset.'), nl.

% Merlin's recipes

stock :-
    add_ingredient(wolfsbane),
    add_ingredient(mandrake),
    add_ingredient(unicorn_blood),
    add_ingredient(phoenix_feather),
    add_ingredient(dittany).

brew :-
    stock,
    make_potion(wolfsbane, mandrake),
    show_ingredients.

:- initialization(brew).
