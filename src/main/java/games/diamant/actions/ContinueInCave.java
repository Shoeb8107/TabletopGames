package games.diamant.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import core.interfaces.IPrintable;

public class ContinueInCave extends AbstractAction implements IPrintable {
    @Override
    public boolean execute(AbstractGameState gs) {
        // Nothing to be executed. The actions are executed at the end of the turn in the ForwardModel
        return true;
    }

    @Override
    public AbstractAction copy() {
        return new ContinueInCave();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        return obj instanceof ContinueInCave;
    }


    // TODO: ???
    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return "Continue in cave";
    }
}
