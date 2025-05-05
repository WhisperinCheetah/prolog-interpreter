inorder(nil) --> [].
inorder(node(L, N, R)) --> inorder(L), [N], inorder(R).

:- initialization(main).

main :-
    T = node(node(nil, b, node(nil, c, nil)), a, node(nil, d, nil)),
    phrase(inorder(T), Names),
    write(Names), nl.

