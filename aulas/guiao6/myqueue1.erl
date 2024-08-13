-module(myqueue).
-export([create/0, enqueue/2 , dequeue/1, test/0]).

create() -> {[], []}.

enqueue({In,Out},Item) ->  {[Item | In], Out}.

dequeue({[],[]}) ->  empty;
dequeue({In,[H|T]}) -> {{In, T}, H};
dequeue({In,[]}) -> dequeue({[],lists:reverse(In)}).



test () ->
    Q1 = create(),
    empty = dequeue(Q1),
    Q2 = enqueue(Q1, 1),
    Q3 = enqueue(Q2, 2),
    {_, 1} = dequeue(Q2),
    {Q4, 1} = dequeue(Q3),
    {Q5, 2} = dequeue(Q4),
    empty = dequeue(Q5),
    ok.
