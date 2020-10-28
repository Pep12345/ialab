fly(X):-bird(X),\+penguin(X).
bird(X):-penguin(X).
bird(tweety).
penguin(tux).
penguin(tweety).