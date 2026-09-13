package com.questhelper.helpers.quests.crabquest;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.player.FreeInventorySlotRequirement;
import com.questhelper.requirements.player.SkillRequirement;
import com.questhelper.rewards.ExperienceReward;
import com.questhelper.rewards.QuestPointReward;
import com.questhelper.rewards.UnlockReward;
import com.questhelper.steps.EmoteStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrabQuest extends BasicQuestHelper
{
	// Initial Reqs
	SkillRequirement sailingLevel;
	SkillRequirement fishingLevel;
	ItemRequirement bigFishingNet;
	FreeInventorySlotRequirement nineSlots;

	// Quest Reqs
	ItemRequirement shellOne, shellTwo, shellThree, shellFour, shellFive, shellSix, shellSeven, shellEight, shellNine;
	ItemRequirement seaSoakedBowString, batteredBarrel, weatheredRosewoodPlank;
	ItemRequirement instrument;

	// Steps
	ObjectStep drinkFromBottle;
	NpcStep talkToCrab;
	ObjectStep pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour, pickUpShellFive, pickUpShellSix,
	           pickUpShellSeven, pickUpShellEight, pickUpShellNine;
	NpcStep turnInShells;
	QuestStep fishNearbyDebris;
	ObjectStep assembleInstrument;
	NpcStep turnInInstrument;

	EmoteStep waitForCrab;
	NpcStep talkToCrab1, talkToCrab2, talkToCrab3, talkToCrab4, talkToCrab5, talkToCrab6, talkToCrab7,
	        talkToCrab8, talkToCrab9, talkToCrab10, talkToCrab11, talkToCrab12;

	NpcStep returnToTheBeach;

	@Override
	protected void setupZones()
	{

	}

	@Override
	protected void setupRequirements()
	{
		sailingLevel = new SkillRequirement(Skill.SAILING, 40, false);
		fishingLevel = new SkillRequirement(Skill.FISHING, 30, false);

		bigFishingNet = new ItemRequirement("Big Fishing Net", ItemID.BIG_NET);
		bigFishingNet.canBeObtainedDuringQuest();

		nineSlots = new FreeInventorySlotRequirement(9);
	}

	public void setupSteps()
	{
		drinkFromBottle = new ObjectStep(this,
										 62439, //ObjectID.CRAB_BOTTLE,
										 new WorldPoint(3055, 2639, 0),
										 "Drink from the Bottle on the shore of Dognose Island.",
			                             sailingLevel, fishingLevel, bigFishingNet, nineSlots);

		talkToCrab = new NpcStep(this, 16471, "Talk to the crab.");

		pickUpShellOne = new ObjectStep(this,
										34577,
										new WorldPoint(3051, 2634, 0),
										"Pick up a nearby Shell.");

		pickUpShellTwo = new ObjectStep(this,
			34575,
			new WorldPoint(3059, 2648, 0),
			"Pick up a nearby Shell.");

		pickUpShellThree = new ObjectStep(this,
			34582,
			new WorldPoint(3048, 2645, 0),
			"Pick up a nearby Shell.");

		pickUpShellFour = new ObjectStep(this,
			34581,
			new WorldPoint(3040, 2636, 0),
			"Pick up a nearby Shell.");

		pickUpShellFive = new ObjectStep(this,
			34578,
			new WorldPoint(3036, 2643, 0),
			"Pick up a nearby Shell.");

		pickUpShellSix = new ObjectStep(this,
			34576,
			new WorldPoint(3044, 2652, 0),
			"Pick up a nearby Shell.");

		pickUpShellSeven = new ObjectStep(this,
			34579,
			new WorldPoint(3050, 2655, 0),
			"Pick up a nearby Shell.");

		pickUpShellEight = new ObjectStep(this,
			34583,
			new WorldPoint(3055, 2654, 0),
			"Pick up a nearby Shell.");

		pickUpShellNine = new ObjectStep(this,
			34580,
			new WorldPoint(3063, 2642, 0),
			"Pick up a nearby Shell.");

		/*
		ObjectStep drinkFromBottle;
		NpcStep talkToCrab;
		ObjectStep pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour, pickUpShellFive, pickUpShellSix,
			pickUpShellSeven, pickUpShellEight;
		NpcStep turnInShells;
		QuestStep fishNearbyDebris;
		ObjectStep assembleInstrument;
		NpcStep turnInInstrument;

		EmoteStep waitForCrab;
		NpcStep talkToCrab1, talkToCrab2, talkToCrab3, talkToCrab4, talkToCrab5, talkToCrab6, talkToCrab7,
			talkToCrab8, talkToCrab9, talkToCrab10, talkToCrab11, talkToCrab12;

		NpcStep returnToTheBeach;
		 */


	}

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupSteps();

		var steps = new HashMap<Integer, QuestStep>();

		steps.put(0, drinkFromBottle);

		return steps;
	}

	@Override
	public List<Requirement> getGeneralRecommended()
	{
		return List.of(

		);
	}

	@Override
	public List<ItemRequirement> getItemRecommended()
	{
		return List.of(
			bigFishingNet
		);
	}

	@Override
	public QuestPointReward getQuestPointReward()
	{
		return new QuestPointReward(1);
	}

	@Override
	public List<ExperienceReward> getExperienceRewards()
	{
		return List.of(
			new ExperienceReward(Skill.SAILING, 10000),
			new ExperienceReward(Skill.FISHING, 3000)
		);
	}

	@Override
	public List<UnlockReward> getUnlockRewards()
	{
		return List.of(
			new UnlockReward("Some colorful seashells"),
			new UnlockReward("A nagging doubt that you might have just dreamt all of this")
		);
	}

	@Override
	public List<PanelDetails> getPanels()
	{
		var sections = new ArrayList<PanelDetails>();

		sections.add(new PanelDetails("Starting off", List.of(
			drinkFromBottle
		)));

		return sections;
	}
}
