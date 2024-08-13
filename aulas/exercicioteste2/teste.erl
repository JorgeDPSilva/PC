-module(matcher).
-export([new/1,waitForConsumer/1, waitForProducer/1]).

new()->
    spawn(fun() -> matcher([],[]) end).

waitForConsumer(Matcher) ->
    Matcher ! {waitForConsumer, self()},
    receive {BoundedBuffer,Matcher} -> BoundedBuffer end.

waitForProducer(Matcher) ->
    Matcher ! {waitForProducer, self()},
    receive {BoundedBuffer,Matcher} -> BoundedBuffer end.

matcher([Prod | Prods] , [Cons | Cons]) ->
    BoundedBuffer = boundedBuffer:new(),
    Prod ! {BoundedBuffer, self()},
    Cons ! {BoundedBuffer, self()},
    matcher(Prods, Cons);
matcher(Producers,Consumers)->
    receive
        {waitForConsumer,Producer} -> matcher(Producers ++ [Producer], Consumers);
        {waitForProducer,Consumer} ->  matcher(Producers, Consumers ++ [Consumer]);
    end.


-module(controller).
-export([start/1,request_resource/1,release_resource/1]){
    
start () ->
    spawn(fun() -> controller([],[]) end).

request_resource(Controller) ->
    Controller ! {request_resource, self()},
    receive {resource,Controller} -> resource end.

release_resource(Controller) ->
    Controller ! {release_resource, self()},
    receive {resource,Controller} -> resource end.

controller([thread0 | threads0],[thread1, threads1]) ->
    Resource = resource:new(),
    thread0 ! {resource, self()},
    thread1 ! {resource, self()},
    controller(threads0, threads1);
controller(Threads0,Threads1)->
    receive
        {request_resource,Thread} -> controller(Threads0 ++ [Thread], Threads1);
        {release_resource,Thread} ->  controller(Threads0, Threads1 ++ [Thread]);
    end.
}