-module(jogo).
-export([start/0,participa/1, adivinha/2]).


start -> spawn(fun() -> jogo([]) end).

participa(Jogo)->
    Jogo ! {participa , self()},
    receive
        {Partida, Jogo} -> Partida 
    end.

adivinha(Partida, Numero) ->
    Partida ! {adivinha, Numero, self()},
    receive
        {Resultado, Partida} -> Resultado
    end.

jogo(jogadores) when length(jogadores) < 4 -> 
    receive
        {participa, from} ->
            jogo([from | jogadores])
        end;
        

jogo(jogadores)  -> 
    Numero = random:uniform(100),
    Partida = spawn(fun() -> partida(Numero,1,false,false) end),
    [Jogador ! {Partida, self()} || Jogador <- jogadores],
    spawn(fun() -> receive after 60000 -> Partida ! timeout end end),
    jogo([]).
    

partida(Numero,Tentativas,Timeout,Ganhou) ->
    receive 
        {adivinha, N, From} ->
            Res = 
                if 
                    Ganhou -> "Perdeu";
                    Timeout -> "Timeout";
                    Tentativas > 100 -> "Perdeu por tentativas";
                    Numero < n -> "Menor";
                    Numero > n -> "Maior";
                    true -> "GANHOU";
                end,
            From ! {Res, self()},
            partida(Numero, Tentativas+1, Timeout, Ganhou orelse Res =:= "GANHOU");
        timeout -> 
            partida(Numero, Tentativas, true, Ganhou)
        end.