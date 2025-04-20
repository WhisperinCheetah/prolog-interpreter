likes(john,mary).
likes(mary,john).
	
loves(X,Y) :- likes(X,Y), likes(Y,X).
