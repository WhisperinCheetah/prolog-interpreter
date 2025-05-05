palindrome --> [].
palindrome --> [_].
palindrome --> [X], palindrome, [X].

:- initialization(main).

main :-
    string_chars("hellolleh", Chars),
    phrase(palindrome, Chars).

