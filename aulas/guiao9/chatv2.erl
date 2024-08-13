-module(chatv2). 
-export([start/1, stop/0]).

start(Port) -> 
    register(?MODULE,spawn(fun() -> server(Port) end)). 

stop() -> ?MODULE ! stop.

server(Port) ->
{ok, LSock} = gen_tcp:listen(Port,[{packet, line}, {reuseaddr, true}]),
RM = spawn(fun() -> rm(#[]) end),
Room = spawn(fun()-> room([]) end),
spawn(fun() -> acceptor(LSock, Room,RM) end),
%register(room_manager,spawn(fun()-> rm(#[]) end)),
receive stop -> ok end.

acceptor(LSock, Room,RM) ->
    {ok, Sock} = gen_tcp:accept(LSock),
    spawn(fun() -> acceptor(LSock, Room, RM) end),
    Room ! {enter, self()},
    user(Sock, Room, RM).

get_room(RM,Name)->
    RM ! {get_room,Name,self()},
    receive {room,R,RM} -> R end.


rm(Rooms) ->
    received
    {get_room, Name, From} ->
        case maps:find(name,Rooms) of
            {ok,Room} ->
                From ! {room,Room,self()},
                rm(Rooms);
            error ->
                Room = spawn(fun()-> room([]) end),
                From ! {room,Room,self()}
                rm(Rooms#{Name => Room})
            end
    end.

room(Pids) -> 
    receive
        {enter, Pid} ->
            io:format("user entered ~n", []), 
            room([Pid | Pids]);
        {line, Data} = Msg -> 
            io:format("received  ~p~n", [Data]), 
            [Pid ! Msg || Pid <- Pids], room(Pids);
        {leave, Pid} ->
            io:format("user left ~n", []), room(Pids -- [Pid])
            end.

handle_line(Line) ->

user(Sock, Room,RM) -> 
    receive
    {line, Data} -> 
        gen_tcp:send(Sock, Data), 
        user(Sock, Room,RM);
    {tcp, _, Data} ->
        case Data of
            "/room" ++ Rest ->
                RoomName = Rest -- "\n",
                NewRoom = get_room(RM,RoomName);   
          _ ->
            Room ! {line, Data}, 
            NewRoom = Room
    end,
    user(Sock, NewRoom,RM);
    {tcp_closed, _} ->
        Room ! {leave, self()};
    {tcp_error, _, _} -> 
        Room ! {leave, self()}
    end.