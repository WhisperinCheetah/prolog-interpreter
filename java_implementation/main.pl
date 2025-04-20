likes(john,mary).
likes(mary,john).
lovers(X,Y) :- likes(X,Y), likes(Y,X).