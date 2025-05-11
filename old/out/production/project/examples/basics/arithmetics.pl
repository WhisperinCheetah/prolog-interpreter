name(     character(Name,     _,     _,  _), Name).
class(    character(   _, Class,     _,  _), Class).
level(    character(   _,     _, Level,  _), Level).
hitpoints(character(   _,     _,     _, HP), HP).

levelup(character(Name, Class, Level, HP), character(Name, Class, Next, HP)) :-
    succ(Level, Next).

aid(character(Name, Class, Level, HP), character(Name, Class, Level, T)) :-
    T is HP+5.

hit(character(Name, Class, Level, HP), character(Name, Class, Level, T)) :-
    T is HP-5.

status(character(Name, Class, Level, HP)) :-
    write(Name), write(' is a level '), write(Level), write(' '), write(Class), write(' with '), write(HP), write(' hitpoints.'), nl.

:- initialization(main).

main :-
    Gimli = character(gimli, fighter, 4, 35), status(Gimli),
    Legolas = character(legolas, ranger, 5, 30), status(Legolas),
    Gandalf = character(gandalf, wizard, 10, 25), status(Gandalf),
    Frodo = character(frodo, rogue, 2, 20), status(Frodo),
    write('legolas threw gimli, and gimli took 5 damage.'), nl,
    hit(Gimli, Thrown), status(Thrown),
    write('gandalf casts aid.'), nl,
    aid(Thrown, Aided), status(Aided),
    write('legolas leveled up.'), nl,
    levelup(Legolas, Leveled), status(Leveled).
