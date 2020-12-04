package games.diamant;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.Deck;
import core.interfaces.IPrintable;
import core.turnorders.AlternatingTurnOrder;
import games.diamant.cards.DiamantCard;
import games.diamant.components.DiamantTreasureChest;
import games.diamant.components.DiamantHand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static core.CoreConstants.PARTIAL_OBSERVABLE;

public class DiamantGameState extends AbstractGameState implements IPrintable {
    Deck<DiamantCard>          mainDeck;
    Deck<DiamantCard>          discardDeck;
    Deck<DiamantCard>          path;

    List<DiamantTreasureChest> treasureChests;
    List<DiamantHand>          hands;
    List<Boolean>              playerOnCave;

    int nGemsOnPath             = 0;
    int nHazardPoissonGasOnPath = 0;
    int nHazardScorpionsOnPath  = 0;
    int nHazardSnakesOnPath     = 0;
    int nHazardRockfallsOnPath  = 0;
    int nHazardExplosionsOnPath = 0;

    int nCave = 0;

    /**
     * Constructor. Initialises some generic game state variables.
     *
     * @param gameParameters - game parameters.
     * @param nPlayers      - number of players for this game.
     */
    public DiamantGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, new AlternatingTurnOrder(nPlayers));
    }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<Component>() {{
            add(mainDeck);
            add(discardDeck);
            add(path);
            addAll(treasureChests);
            addAll(hands);
        }};
    };

    @Override
    protected AbstractGameState _copy(int playerId)
    {
        DiamantGameState dgs = new DiamantGameState(gameParameters.copy(), getNPlayers());

        dgs.mainDeck    = mainDeck.copy();
        dgs.discardDeck = discardDeck.copy();
        dgs.path        = path.copy();

        dgs.nGemsOnPath             = nGemsOnPath;
        dgs.nHazardPoissonGasOnPath = nHazardPoissonGasOnPath;
        dgs.nHazardScorpionsOnPath  = nHazardScorpionsOnPath;
        dgs.nHazardSnakesOnPath     = nHazardSnakesOnPath;
        dgs.nHazardRockfallsOnPath  = nHazardRockfallsOnPath;
        dgs.nHazardExplosionsOnPath = nHazardExplosionsOnPath;

        dgs.nCave          = nCave;
        dgs.hands          = new ArrayList<>();
        dgs.treasureChests = new ArrayList<>();

        for (DiamantHand dh : hands)
            dgs.hands.add((DiamantHand) dh.copy());

        for (DiamantTreasureChest dc : treasureChests)
            dgs.treasureChests.add((DiamantTreasureChest) dc.copy());

        dgs.playerOnCave.addAll(playerOnCave);

        // mainDeck is hidden. Shuffle it.
        if (PARTIAL_OBSERVABLE && playerId != -1)
            dgs.mainDeck.shuffle(new Random(getGameParameters().getRandomSeed()));

        return dgs;
    }

    @Override
    protected double _getScore(int playerId)
    {
        return new DiamantHeuristic().evaluateState(this, playerId);
    }

    // TODO: what it this?
    @Override
    protected ArrayList<Integer> _getUnknownComponentsIds(int playerId) {
        return null;
    }

    // TODO: what about hash?

    @Override
    protected void _reset() {
        mainDeck       = null;
        discardDeck    = null;
        path           = null;
        treasureChests = new ArrayList<>();
        hands          = new ArrayList<>();
        playerOnCave   = new ArrayList<>();

        nGemsOnPath             = 0;
        nHazardPoissonGasOnPath = 0;
        nHazardScorpionsOnPath  = 0;
        nHazardSnakesOnPath     = 0;
        nHazardRockfallsOnPath  = 0;
        nHazardExplosionsOnPath = 0;

        nCave = 0;
    }

    @Override
    protected boolean _equals(Object o)
    {
        if (this == o)                        return true;
        if (!(o instanceof DiamantGameState)) return false;
        if (!super.equals(o))                 return false;

        DiamantGameState that = (DiamantGameState) o;

        return nGemsOnPath             == that.nGemsOnPath             &&
               nHazardExplosionsOnPath == that.nHazardExplosionsOnPath &&
               nHazardPoissonGasOnPath == that.nHazardPoissonGasOnPath &&
               nHazardRockfallsOnPath  == that.nHazardRockfallsOnPath  &&
               nHazardScorpionsOnPath  == that.nHazardScorpionsOnPath  &&
               nHazardSnakesOnPath     == that.nHazardSnakesOnPath     &&
               nCave                   == that.nCave                   &&
               Objects.equals(mainDeck,       that.mainDeck)           &&
               Objects.equals(discardDeck,    that.discardDeck)        &&
               Objects.equals(hands,          that.hands)              &&
               Objects.equals(treasureChests, that.treasureChests)     &&
               Objects.equals(path,           that.path)               &&
               Objects.equals(playerOnCave,   that.playerOnCave);
    }

    public int GetNPlayersOnCave()
    {
        int n = 0;
        for (Boolean b: playerOnCave)
        {
            if (b) n++;
        }

        return n;
    }

    @Override
    public void printToConsole() {
        String[] strings = new String[13];

        StringBuilder str_gemsOnHand          = new StringBuilder();
        StringBuilder str_gemsOnTreasureChest = new StringBuilder();
        StringBuilder str_playersOnCave       = new StringBuilder();

        for (DiamantHand h:hands)                   { str_gemsOnHand.append(h.toString()).append(" ");          }
        for (DiamantTreasureChest c:treasureChests) { str_gemsOnTreasureChest.append(c.toString()).append(" "); }
        for (Boolean b : playerOnCave)
        {
            if (b) str_playersOnCave.append("T");
            else   str_playersOnCave.append("F");
        }

        strings[0]  = "----------------------------------------------------";
        strings[1]  = "Cave:                   " + nCave;
        strings[2]  = "Players on Cave:        " + str_playersOnCave.toString();
        strings[3]  = "Path:                   " + path.toString();
        strings[4]  = "Gems on Path:           " + nGemsOnPath;
        strings[5]  = "Gems on hand:           " + str_gemsOnHand.toString();
        strings[6]  = "Gems on treasure chest: " + str_gemsOnTreasureChest.toString();
        strings[7]  = "Hazard scorpions:       " + nHazardScorpionsOnPath;
        strings[8]  = "Hazard snakes:          " + nHazardSnakesOnPath;
        strings[9]  = "Hazard rockfalls:       " + nHazardRockfallsOnPath;
        strings[10] = "Hazard poisson gas:     " + nHazardPoissonGasOnPath;
        strings[11] = "Hazard explosions:      " + nHazardExplosionsOnPath;
        strings[12] = "----------------------------------------------------";

        for (String s : strings){
            System.out.println(s);
        }
    }

    public Deck<DiamantCard>          getMainDeck()       { return mainDeck;       }
    public Deck<DiamantCard>          getDiscardDeck()    { return discardDeck;    }
    public List<DiamantHand>          getHands()          { return hands;          }
    public List<DiamantTreasureChest> getTreasureChests() { return treasureChests; }
    public Deck<DiamantCard>          getPath()           { return path;           }
}
