package games.terraformingmars.rules;

import games.terraformingmars.TMGameState;
import games.terraformingmars.TMTypes;
import games.terraformingmars.components.TMCard;

public class TagRequirement implements Requirement {

    TMTypes.Tag[] tags;
    int[] nMin;

    public TagRequirement(TMTypes.Tag[] tag, int[] nMin) {
        this.tags = tag;
        this.nMin = nMin;
    }

    @Override
    public boolean testCondition(TMGameState gs) {
        for (int i = 0; i < nMin.length; i++) {
            TMTypes.Tag tag = tags[i];
            if (gs.getPlayerCardsPlayedTags()[gs.getCurrentPlayer()].get(tag).getValue() < nMin[i]) return false;
        }
        return true;
    }
}
