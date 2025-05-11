:- dynamic declaration/1.

add_declaration_first(NewDecl) :-
    asserta(declaration(NewDecl)).

add_declaration_last(NewDecl) :-
    assertz(declaration(NewDecl)).

database :-
    add_declaration_first('Man is born free, and everywhere he is in chains.'),
    retract(declaration(_)),
    add_declaration_last('The revolution devours its own children.'),
    add_declaration_first('I disapprove of what you say, but I will defend to the death your right to say it.'),
    add_declaration_first('Give me Liberty, or give me Death!'),
    add_declaration_last('So this is how liberty dies, with thunderous applause.').

show_declarations :-
    declaration(Decl),
    write(Decl), nl,
    fail.

show_declarations.

:- initialization(main).

main :-
    database,
    show_declarations,
    retractall(declaration(_)),
    show_declarations.

