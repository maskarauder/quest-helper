package com.questhelper.helpers.quests.crabquest;

import com.questhelper.questhelpers.QuestHelper;
import com.questhelper.requirements.conditional.Conditions;
import com.questhelper.requirements.conditional.NpcCondition;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.NpcID;
import static com.questhelper.requirements.util.LogicHelper.*;

protected class CrabStep
{
	QuestHelper helper;
	int objectId;
	WorldPoint crabLocation;
	Conditions completed;
	QuestStep extraStep = null;


	protected ConditionalStep addSteps(ConditionalStep root) {
		NpcCondition crabPresent = new NpcCondition(NpcID.CRAB_BANDCRAB, crabLocation);

		ObjectStep reveal = new ObjectStep(helper, objectId,
			"Search the nearby plant for a crab.",
			not(completed), not(crabPresent));
		root.addStep(new Conditions(and(not(crabPresent), not(completed))), reveal);

		if (extraStep != null) root.addStep(not(crabPresent), extraStep);

		NpcStep recruit = new NpcStep(helper, NpcID.CRAB_BANDCRAB, crabLocation,
			"Recruit the crab to join your band.",
			crabPresent, not(completed));
		root.addStep(new Conditions(and(crabPresent, not(completed))), recruit);

		return root;
	}

	protected CrabStep(QuestHelper helper, int objectToInitiate, WorldPoint crabLocation, Conditions recruited,
					   QuestStep extra) {
		this.helper = helper;
		this.objectId = objectToInitiate;
		this.crabLocation = crabLocation;
		this.completed = recruited;
		this.extraStep = extra;
	}

	protected CrabStep(QuestHelper helper, int objectToInitiate, WorldPoint crabLocation, Conditions recruited) {
		this(helper, objectToInitiate, crabLocation, recruited, null);
	}
}
