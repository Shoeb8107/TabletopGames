package games.pandemic;

import core.*;
import evaluation.Run;
import games.pandemic.gui.PandemicGUI;
import players.ActionController;
import players.OSLA;
import players.RandomPlayer;
import utilities.Utils;

import java.util.*;

public class PandemicGame extends Game {

    public PandemicGame(List<AbstractPlayer> agents, PandemicParameters params) {
        super(Run.GameType.Pandemic, agents, new PandemicForwardModel(params, agents.size()),
                new PandemicGameState(params, agents.size()));
    }
    public PandemicGame(AbstractForwardModel model, AbstractGameState gameState) {
        super(Run.GameType.Pandemic, model, gameState);
    }

    public static void main(String[] args){

        ActionController ac = new ActionController();

        List<AbstractPlayer> players = new ArrayList<>();
        players.add(new RandomPlayer(new Random()));
        players.add(new RandomPlayer(new Random()));
        players.add(new RandomPlayer(new Random()));
//        players.add(new HumanGUIPlayer(ac));
        players.add(new OSLA());


        PandemicParameters params = new PandemicParameters("data/pandemic/", System.currentTimeMillis());
        PandemicGame game = new PandemicGame(players, params);

        AbstractGUI gui = new PandemicGUI((PandemicGameState)game.getGameState(), ac);
        game.run(gui);

//        game.run(null);
        System.out.println(game.gameState.getGameStatus());

//        runMany(players, forwardModel);
    }

    public static void runMany(List<AbstractPlayer> players, AbstractForwardModel model) {
        HashMap<Utils.GameResult, Integer> results = new HashMap<>();
        for (Utils.GameResult r: Utils.GameResult.values()) {
            results.put(r, 0);
        }

        PandemicParameters params = new PandemicParameters("data/pandemic/", System.currentTimeMillis());
        for (int i = 0; i < 10000; i++) {
            PandemicGame game = new PandemicGame(players, params);
            game.run(null);
            Utils.GameResult result = game.gameState.getGameStatus();
            int prevCount = results.get(result);
            results.put(result, prevCount + 1);
        }

        System.out.println(results.toString());
    }
}
