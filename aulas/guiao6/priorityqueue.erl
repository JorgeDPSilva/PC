-module(priorityqueue).
-export([create/0, enqueue/3 , dequeue/1, test/0]).

create() -> [].

enqueue(PriQueue, Item, Priority) ->
     lists:sort(fun({_, P1}, {_, P2}) -> P1 < P2 end, PriQueue ++ [{Item, Priority}]).


dequeue([]) ->  empty;
dequeue([H|T]) -> {T, H}.


test () ->
    Q1 = create(),
    empty = dequeue(Q1),
    Q2 = enqueue(Q1, 1, 2),
    Q3 = enqueue(Q2, 2, 1),
    {_, 2} = dequeue(Q2),
    {Q4, 2} = dequeue(Q3),
    {Q5, 1} = dequeue(Q4),
    empty = dequeue(Q5),
    ok.