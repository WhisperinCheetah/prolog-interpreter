% Old mag steeds door New vervangen worden
mib(Old, New, Old, New).

% Een term blijft enkel behouden als het een attoom is
% (dat is, niet compound), en als het niet Old is
mib(Old, _, Term, Term) :-
    atomic(Term),
    Term \= Old.

% Voor een samengestelde Term
mib(Old, New, Pre, Post) :-
    compound(Pre),
    functor(Pre,  F, N), % Pre heeft naam F en arriteit N
    functor(Post, F, N), % Post is term met zelfde naam (F) en arriteit (N)
    mib(N, Old, New, Pre, Post).

% Extra predicaat om de argumenten te vervangen van een samengestelde term
%
% N = het nr van het argument (strikt positief)
mib(0, _, _, _, _) :- !. % Argument 0 bestaat niet, als we hier komen zijn we klaar.

mib(N, Old, New, Pre, Post) :-
    arg(N, Pre, ArgPre),            % neem het N-de argument
    mib(Old, New, ArgPre, ArgPost), % vertaal het
    arg(N, Post, ArgPost),          % unificeer het met het N-de argument van output
    N1 is N-1,
    mib(N1, Old, New, Pre, Post).   % Herhaal voor argument N-1

