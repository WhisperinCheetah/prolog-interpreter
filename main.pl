likes(john,mary).
likes(john,john).

loves(X,Y) :- likes(X,Y) , likes(Y,X).	
