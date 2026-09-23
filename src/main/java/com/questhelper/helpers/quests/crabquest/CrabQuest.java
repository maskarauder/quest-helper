package com.questhelper.helpers.quests.crabquest;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.player.FreeInventorySlotRequirement;
import com.questhelper.requirements.player.SkillRequirement;
import com.questhelper.requirements.var.VarbitRequirement;
import com.questhelper.rewards.ExperienceReward;
import com.questhelper.rewards.QuestPointReward;
import com.questhelper.rewards.UnlockReward;
import com.questhelper.steps.EmoteStep;
import com.questhelper.steps.ItemStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import net.runelite.api.World;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.VarbitID;
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

	VarbitRequirement test;

	// Quest Reqs
	ItemRequirement shellOne, shellTwo, shellThree, shellFour, shellFive, shellSix, shellSeven, shellEight, shellNine;
	ItemRequirement seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank;
	ItemRequirement instrument;

	// Steps
	ObjectStep drinkFromBottle;
	NpcStep talkToCrab;
	ItemStep pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour, pickUpShellFive, pickUpShellSix,
	           pickUpShellSeven, pickUpShellEight, pickUpShellNine;
	NpcStep turnInShells;

	ItemStep pickUpBigNet;
	NpcStep fishSeasoakedBowstring, fishBatteredBarrel, fishWeatheredRosewoodPlank;
	ItemStep assembleInstrument;
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

		shellOne = new ItemRequirement("Shell", ItemID.CRAB_SHELL_1);
		shellTwo = new ItemRequirement("Shell", ItemID.CRAB_SHELL_2);
		shellThree = new ItemRequirement("Shell", ItemID.CRAB_SHELL_3);
		shellFour = new ItemRequirement("Shell", ItemID.CRAB_SHELL_4);
		shellFive = new ItemRequirement("Shell", ItemID.CRAB_SHELL_5);
		shellSix = new ItemRequirement("Shell", ItemID.CRAB_SHELL_6);
		shellSeven = new ItemRequirement("Shell", ItemID.CRAB_SHELL_7);
		shellEight = new ItemRequirement("Shell", ItemID.CRAB_SHELL_BAD1);
		shellNine = new ItemRequirement("Shell", ItemID.CRAB_SHELL_BAD2);

		seasoakedBowstring = new ItemRequirement("Sea-soaked Bowstring", ItemID.CRAB_BOWSTRING);
		batteredBarrel = new ItemRequirement("Battered Barrel", ItemID.CRAB_BARREL);
		weatheredRosewoodPlank = new ItemRequirement("Weathered Rosewood Plank", ItemID.CRAB_PLANK);

		instrument = new ItemRequirement("Instrument", ItemID.CRAB_BASS);
		//test = new VarbitRequirement((VarbitID.CRAB))
	}

	public void setupSteps()
	{
		drinkFromBottle = new ObjectStep(this,
			ObjectID.CRAB_BOTTLE,
			new WorldPoint(3055, 2639, 0),
			"Drink from the Bottle on the shore of Dognose Island.",
			sailingLevel, fishingLevel, bigFishingNet, nineSlots);

		talkToCrab = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_START,
			"Talk to the crab.");

		pickUpShellOne = new ItemStep(this,
			new WorldPoint(3051, 2634, 0),
			"Pick up a nearby Shell.",
			shellThree);

		pickUpShellTwo = new ItemStep(this,
			new WorldPoint(3059, 2648, 0),
			"Pick up a nearby Shell.",
			shellOne);

		pickUpShellThree = new ItemStep(this,
			new WorldPoint(3048, 2645, 0),
			"Pick up a nearby Shell.",
			shellEight);

		pickUpShellFour = new ItemStep(this,
			new WorldPoint(3040, 2636, 0),
			"Pick up a nearby Shell.",
			shellSeven);

		pickUpShellFive = new ItemStep(this,
			new WorldPoint(3036, 2643, 0),
			"Pick up a nearby Shell.",
			shellFour);

		pickUpShellSix = new ItemStep(this,
			new WorldPoint(3044, 2652, 0),
			"Pick up a nearby Shell.",
			shellTwo);

		pickUpShellSeven = new ItemStep(this,
			new WorldPoint(3050, 2655, 0),
			"Pick up a nearby Shell.",
			shellFive);

		pickUpShellEight = new ItemStep(this,
			new WorldPoint(3055, 2654, 0),
			"Pick up a nearby Shell.",
			shellNine);

		pickUpShellNine = new ItemStep(this,
			new WorldPoint(3063, 2642, 0),
			"Pick up a nearby Shell.",
			shellSix);

		turnInShells = new NpcStep(this, NpcID.CRAB_MAINCRAB_START,
			"Show the shells to the crab.",
			shellOne, shellTwo, shellThree,
			shellFour, shellFive, shellSix,
			shellSeven, shellEight, shellNine);

		pickUpBigNet = new ItemStep(this,
			new WorldPoint(3061, 2646, 0),
			"Pick up the nearby Big Fishing Net.",
			bigFishingNet);

		fishSeasoakedBowstring = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_3,
			/* new WorldPoint(3048, 2632, 0), */
			"Fish a Sea-soaked bowstring from the nearby Floatsam.",
			bigFishingNet);

		fishBatteredBarrel = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_2,
			"Fish a Battered Barrel from the nearby Floatsam.",
			seasoakedBowstring, bigFishingNet);

		fishWeatheredRosewoodPlank = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			"Fish a Weathered Rosewood Plank from the nearby Floatsam.",
			seasoakedBowstring, batteredBarrel, bigFishingNet);

		assembleInstrument = new ItemStep(this,
			"Assemble the instrument using the items in your inventory.",
			seasoakedBowstring.highlighted(),
			batteredBarrel.highlighted(),
			weatheredRosewoodPlank.highlighted());

		turnInInstrument = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_START,
			"Show the instrument to the crab.",
			instrument);
		turnInInstrument.addDialogStep("A disaster.");
		turnInInstrument.addDialogStep("Let me show you!");

		/*
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

		steps.put(5, talkToCrab);

		steps.put(10, pickUpShellOne);
		steps.put(15, pickUpShellTwo);
		steps.put(20, pickUpShellThree);
		steps.put(20, pickUpShellFour);
		steps.put(20, pickUpShellFive);
		steps.put(20, pickUpShellSix);
		steps.put(20, pickUpShellSeven);
		steps.put(20, pickUpShellEight);
		steps.put(20, pickUpShellNine);
		steps.put(20, turnInShells);

		steps.put(25, pickUpBigNet);
		steps.put(30, fishSeasoakedBowstring);
		steps.put(35, fishBatteredBarrel);
		steps.put(37, fishWeatheredRosewoodPlank);
		steps.put(40, assembleInstrument);
		steps.put(45, turnInInstrument);

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
			drinkFromBottle,
			talkToCrab
		)));

		sections.add(new PanelDetails("Collecting shells", List.of(
			pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour,
			pickUpShellFive, pickUpShellSix, pickUpShellSeven, pickUpShellEight,
			pickUpShellNine, turnInShells
		)));

		sections.add(new PanelDetails("Assembling an instrument", List.of(
			pickUpBigNet,
			fishSeasoakedBowstring,
			fishBatteredBarrel,
			fishWeatheredRosewoodPlank,
			assembleInstrument,
			turnInInstrument
		)));

		return sections;
	}
}
