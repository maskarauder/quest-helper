package com.questhelper.helpers.quests.crabquest;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.conditional.Conditions;
import com.questhelper.requirements.conditional.NpcCondition;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.player.FreeInventorySlotRequirement;
import com.questhelper.requirements.player.SkillRequirement;
import static com.questhelper.requirements.util.LogicHelper.and;
import static com.questhelper.requirements.util.LogicHelper.not;
import static com.questhelper.requirements.util.LogicHelper.or;
import com.questhelper.requirements.util.Operation;
import com.questhelper.requirements.var.VarbitRequirement;
import com.questhelper.rewards.ExperienceReward;
import com.questhelper.rewards.QuestPointReward;
import com.questhelper.rewards.UnlockReward;
import com.questhelper.steps.ConditionalStep;
import com.questhelper.steps.ItemStep;
import com.questhelper.steps.NpcStep;
import com.questhelper.steps.ObjectStep;
import com.questhelper.steps.QuestStep;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;


public class CrabQuest extends BasicQuestHelper
{
	// Initial Reqs
	SkillRequirement sailingLevel;
	SkillRequirement fishingLevel;
	ItemRequirement bigFishingNet;
	FreeInventorySlotRequirement nineSlots, threeSlots, twoSlots, oneSlot;

	// Quest Reqs
	ItemRequirement shellOne, shellTwo, shellThree, shellFour, shellFive, shellSix, shellSeven;
	ItemRequirement seaSoakedBowstring, batteredBarrel, weatheredRosewoodPlank;
	ItemRequirement instrument;
	VarbitRequirement inQuestInstance, beforeFishing, pastFishing, penguinKicked, sufficientBandmatesRecruited;
	Requirement shellOneObtained, shellTwoObtained, shellThreeObtained, shellFourObtained, shellFiveObtained,
		shellSixObtained, shellSevenObtained, shellsObtained;
	Conditions floatsamPresent;
	NpcCondition penguinPresent;

	// Steps
	ObjectStep drinkFromBottle;
	NpcStep talkToCrab, talkToCrabButHesShy;
	ItemStep pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour, pickUpShellFive, pickUpShellSix,
		pickUpShellSeven;
	NpcStep turnInShells;
	ItemStep pickUpBigFishingNet;
	NpcStep resetFloatsam, fishSeaSoakedBowstring, fishBatteredBarrel, fishWeatheredRosewoodPlank;
	ItemStep assembleInstrument;
	NpcStep turnInInstrument;
	ObjectStep findPenguin, findCrabs;
	NpcStep kickPenguin, returnToTheBeach;

	@Override
	protected void setupRequirements()
	{
		sailingLevel = new SkillRequirement(Skill.SAILING, 40, false);
		fishingLevel = new SkillRequirement(Skill.FISHING, 30, false);

		bigFishingNet = new ItemRequirement("Big Fishing Net", ItemID.BIG_NET);
		bigFishingNet.canBeObtainedDuringQuest();

		inQuestInstance = new VarbitRequirement(VarbitID.CRAB_PUT_BACK_IN_INSTANCE, 1);
		nineSlots = new FreeInventorySlotRequirement(9);
		threeSlots = new FreeInventorySlotRequirement(3);
		twoSlots = new FreeInventorySlotRequirement(2);
		oneSlot = new FreeInventorySlotRequirement(1);

		shellOne = new ItemRequirement("Shell", ItemID.CRAB_SHELL_1);
		shellTwo = new ItemRequirement("Shell", ItemID.CRAB_SHELL_2);
		shellThree = new ItemRequirement("Shell", ItemID.CRAB_SHELL_3);
		shellFour = new ItemRequirement("Shell", ItemID.CRAB_SHELL_4);
		shellFive = new ItemRequirement("Shell", ItemID.CRAB_SHELL_5);
		shellSix = new ItemRequirement("Shell", ItemID.CRAB_SHELL_6);
		shellSeven = new ItemRequirement("Shell", ItemID.CRAB_SHELL_7);

		seaSoakedBowstring = new ItemRequirement("Sea-soaked Bowstring", ItemID.CRAB_BOWSTRING);
		batteredBarrel = new ItemRequirement("Battered Barrel", ItemID.CRAB_BARREL);
		weatheredRosewoodPlank = new ItemRequirement("Weathered Rosewood Plank", ItemID.CRAB_PLANK);
		beforeFishing = new VarbitRequirement(VarbitID.CRAB_MAIN, 37, Operation.LESS);
		pastFishing = new VarbitRequirement(VarbitID.CRAB_MAIN, 40, Operation.GREATER);

		instrument = new ItemRequirement("Instrument", ItemID.CRAB_BASS);

		shellOneObtained = or(shellOne, new VarbitRequirement(VarbitID.CRAB_SEASHELL_1, 1));
		shellTwoObtained = or(shellTwo, new VarbitRequirement(VarbitID.CRAB_SEASHELL_2, 1));
		shellThreeObtained = or(shellThree, new VarbitRequirement(VarbitID.CRAB_SEASHELL_3, 1));
		shellFourObtained = or(shellFour, new VarbitRequirement(VarbitID.CRAB_SEASHELL_4, 1));
		shellFiveObtained = or(shellFive, new VarbitRequirement(VarbitID.CRAB_SEASHELL_5, 1));
		shellSixObtained = or(shellSix, new VarbitRequirement(VarbitID.CRAB_SEASHELL_6, 1));
		shellSevenObtained = or(shellSeven, new VarbitRequirement(VarbitID.CRAB_SEASHELL_7, 1));
		shellsObtained = or(and(
				shellOneObtained, shellTwoObtained, shellThreeObtained, shellFourObtained,
				shellFiveObtained, shellSixObtained, shellSevenObtained
			),
			new VarbitRequirement(VarbitID.CRAB_SEASHELLS, 127));

		floatsamPresent = not(new VarbitRequirement(VarbitID.CRAB_FLOATSAM_HIDDEN, 3));

		penguinPresent = new NpcCondition(NpcID.CRAB_PENGUIN, new WorldPoint(3049, 2658, 0));
		penguinKicked = new VarbitRequirement(VarbitID.CRAB_KICKED_PENGUIN, 1);
		sufficientBandmatesRecruited = new VarbitRequirement(VarbitID.CRAB_PARTIAL_COMPLETE, 1);
	}

	public void setupSteps()
	{
		drinkFromBottle = new ObjectStep(this,
			ObjectID.CRAB_BOTTLE,
			new WorldPoint(3055, 2639, 0),
			"Drink from the Bottle on the shore of Dognose Island.",
			sailingLevel, fishingLevel, not(inQuestInstance));
		drinkFromBottle.addDialogStep("Yes.");

		talkToCrab = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_START,
			"Talk to the crab.");

		talkToCrabButHesShy = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_BLUSHING,
			"Talk to the crab while he's blushing.");

		pickUpShellThree = new ItemStep(this,
			new WorldPoint(3051, 2634, 0),
			"Pick up a nearby Shell.",
			shellThree);

		pickUpShellSix = new ItemStep(this,
			new WorldPoint(3063, 2642, 0),
			"Pick up a nearby Shell.",
			shellSix);

		pickUpShellOne = new ItemStep(this,
			new WorldPoint(3059, 2648, 0),
			"Pick up a nearby Shell.",
			shellOne);

		pickUpShellSeven = new ItemStep(this,
			new WorldPoint(3040, 2636, 0),
			"Pick up a nearby Shell.",
			shellSeven);

		pickUpShellFour = new ItemStep(this,
			new WorldPoint(3036, 2643, 0),
			"Pick up a nearby Shell.",
			shellFour);

		pickUpShellTwo = new ItemStep(this,
			new WorldPoint(3044, 2652, 0),
			"Pick up a nearby Shell.",
			shellTwo);

		pickUpShellFive = new ItemStep(this,
			new WorldPoint(3050, 2655, 0),
			"Pick up a nearby Shell.",
			shellFive);

		turnInShells = new NpcStep(this, NpcID.CRAB_MAINCRAB_START,
			"Show the shells to the crab.",
			shellsObtained);

		pickUpBigFishingNet = new ItemStep(this,
			new WorldPoint(3061, 2646, 0),
			"Pick up the nearby Big Fishing Net.",
			bigFishingNet);

		fishSeaSoakedBowstring = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			/* new WorldPoint(3048, 2632, 0), */
			"Fish a Sea-soaked bowstring from the nearby Floatsam.",
			bigFishingNet, threeSlots);
		fishSeaSoakedBowstring.addAlternateNpcs(NpcID.CRAB_FLOATSAM_2, NpcID.CRAB_FLOATSAM_3);

		fishBatteredBarrel = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			"Fish a Battered Barrel from the nearby Floatsam.",
			seaSoakedBowstring, bigFishingNet, twoSlots);
		fishBatteredBarrel.addAlternateNpcs(NpcID.CRAB_FLOATSAM_2, NpcID.CRAB_FLOATSAM_3);

		fishWeatheredRosewoodPlank = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			"Fish a Weathered Rosewood Plank from the nearby Floatsam.",
			seaSoakedBowstring, batteredBarrel, bigFishingNet, oneSlot);
		fishWeatheredRosewoodPlank.addAlternateNpcs(NpcID.CRAB_FLOATSAM_2, NpcID.CRAB_FLOATSAM_3);

		resetFloatsam = new NpcStep(this,
			NpcID.CRAB_MAINCRAB,
			"Speak to the crab to reset the floatsam.");

		assembleInstrument = new ItemStep(this,
			"Assemble the instrument using the items in your inventory.",
			seaSoakedBowstring.highlighted(),
			batteredBarrel.highlighted(),
			weatheredRosewoodPlank.highlighted());

		turnInInstrument = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_START,
			"Show the instrument to the crab.",
			instrument);
		turnInInstrument.addDialogStep("A disaster.");
		turnInInstrument.addDialogStep("Let me show you!");

		findPenguin = new ObjectStep(this, ObjectID.CRAB_PALM_UPDATE01,
			new WorldPoint(3048, 2658, 0),
			"Check the palm to find a penguin.");

		kickPenguin = new NpcStep(this, NpcID.CRAB_PENGUIN, new WorldPoint(3049, 2658, 0),
			"Speak to the penguin.");

		findCrabs = new ObjectStep(this, ObjectID.CRAB_ENAKH_LEAFLESSBUSH,
			new WorldPoint(3050, 2636, 0),
			"Interact with the nearby plants to find crabs.\n" +
				"Speak with the crabs to recruit them to join the band.\n" +
				"If you are prompted that the crabs might be shy, perform the Sit emote for a short time.\n",
			true);
		findCrabs.addAlternateObjects(ObjectID.CRAB_AVIUM_TREE_1, ObjectID.CRAB_AVIUM_TREE_2,
			ObjectID.CRAB_GROUNDCOVER_PLANT4_WITHERED, ObjectID.CRAB_MAGICTREE, ObjectID.CRAB_PALM_UPDATE01,
			ObjectID.CRAB_PALM_UPDATE02, ObjectID.CRAB_PALM_UPDATE03);
		findCrabs.setHideWorldArrow(true);
		findCrabs.addDialogStep("1, 2, 3, 10, 11, 12, 13, 20, 21, 22, 23...");
		findCrabs.addDialogStep("Every 100 years.");
		findCrabs.addDialogStep("On the beach.");
		findCrabs.addDialogStep("You can't possibly expect me to know that!");

		returnToTheBeach = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_END,
			"Speak to the crab to finish the quest.");

		returnToTheBeach.addDialogStep("Wait for leaders to turn up.");
	}

	@Override
	public Map<Integer, QuestStep> loadSteps()
	{
		initializeRequirements();
		setupSteps();

		var steps = new HashMap<Integer, QuestStep>();

		steps.put(0, drinkFromBottle);

		steps.put(5, talkToCrab);
		steps.put(10, talkToCrab);
		steps.put(15, talkToCrab);
		steps.put(20, talkToCrabButHesShy);

		var cShells = new ConditionalStep(this, turnInShells);
		cShells.addStep(and(not(oneSlot),
			or(shellOne,
				shellTwo,
				shellThree,
				shellFour,
				shellFive,
				shellSix,
				shellSeven)), turnInShells);
		cShells.addStep(not(shellThreeObtained), pickUpShellThree);
		cShells.addStep(not(shellSixObtained), pickUpShellSix);
		cShells.addStep(not(shellOneObtained), pickUpShellOne);
		cShells.addStep(not(shellSevenObtained), pickUpShellSeven);
		cShells.addStep(not(shellFourObtained), pickUpShellFour);
		cShells.addStep(not(shellTwoObtained), pickUpShellTwo);
		cShells.addStep(not(shellFiveObtained), pickUpShellFive);
		steps.put(25, cShells);
		steps.put(30, turnInShells);
		steps.put(35, turnInShells);

		var cInstrument = new ConditionalStep(this, assembleInstrument, seaSoakedBowstring, batteredBarrel,
			weatheredRosewoodPlank);
		cInstrument.addStep(and(not(and(seaSoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
			not(bigFishingNet)), pickUpBigFishingNet);
		cInstrument.addStep(and(not(and(seaSoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
			not(floatsamPresent)), resetFloatsam);
		cInstrument.addStep(not(seaSoakedBowstring), fishSeaSoakedBowstring);
		cInstrument.addStep(not(batteredBarrel), fishBatteredBarrel);
		cInstrument.addStep(not(weatheredRosewoodPlank), fishWeatheredRosewoodPlank);
		steps.put(37, cInstrument);

		var cInstrumentSafety = new ConditionalStep(this, turnInInstrument, instrument);
		cInstrumentSafety.addStep(and(not(instrument),
			not(and(seaSoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
			not(bigFishingNet)), pickUpBigFishingNet);
		cInstrumentSafety.addStep(and(not(instrument),
			not(and(seaSoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
			not(floatsamPresent)), resetFloatsam);
		cInstrumentSafety.addStep(and(not(instrument), not(seaSoakedBowstring)), fishSeaSoakedBowstring);
		cInstrumentSafety.addStep(and(not(instrument), not(batteredBarrel)), fishBatteredBarrel);
		cInstrumentSafety.addStep(and(not(instrument), not(weatheredRosewoodPlank)), fishWeatheredRosewoodPlank);
		cInstrumentSafety.addStep(and(not(instrument), seaSoakedBowstring, batteredBarrel, weatheredRosewoodPlank),
			assembleInstrument);
		steps.put(40, cInstrumentSafety);

		steps.put(45, turnInInstrument);

		var cRustacean = new ConditionalStep(this, returnToTheBeach);
		cRustacean.addStep(and(not(penguinPresent), not(penguinKicked)), findPenguin);
		cRustacean.addStep(not(penguinKicked), kickPenguin);
		cRustacean.addStep(not(sufficientBandmatesRecruited), findCrabs);
		steps.put(47, cRustacean);

		steps.put(50, returnToTheBeach);
		steps.put(55, returnToTheBeach);
		steps.put(60, returnToTheBeach);

		// Guide the player back if they're not in the instance.
		steps.replaceAll((stage, normalStep) ->
		{
			if (stage == 0)
			{
				return normalStep;
			}
			return new ConditionalStep(this, normalStep).addStep(not(inQuestInstance), drinkFromBottle);
		});

		return steps;
	}

	@Override
	public List<Requirement> getGeneralRecommended()
	{
		return List.of(
			nineSlots
		);
	}

	@Override
	public List<Requirement> getGeneralRequirements()
	{
		return List.of(
			sailingLevel,
			fishingLevel,
			threeSlots
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
			talkToCrab,
			talkToCrabButHesShy
		)));

		sections.add(new PanelDetails("Collecting shells", List.of(
			pickUpShellThree, pickUpShellSix, pickUpShellOne, pickUpShellSeven,
			pickUpShellFour, pickUpShellTwo, pickUpShellFive, turnInShells
		), oneSlot));

		PanelDetails netPanel = new PanelDetails("Prepare to fish up an instrument", List.of(
			pickUpBigFishingNet
		), oneSlot);
		netPanel.setHideCondition(or(bigFishingNet, pastFishing));
		sections.add(netPanel);

		PanelDetails resetPanel = new PanelDetails("Reset the floatsam", List.of(
			resetFloatsam
		));
		resetPanel.setHideCondition(or(beforeFishing, floatsamPresent, pastFishing));
		sections.add(resetPanel);

		sections.add(new PanelDetails("Assembling an instrument", List.of(
			fishSeaSoakedBowstring,
			fishBatteredBarrel,
			fishWeatheredRosewoodPlank,
			assembleInstrument,
			turnInInstrument
		), threeSlots));

		sections.add(new PanelDetails("Recruit some band members", List.of(
			findPenguin, kickPenguin, findCrabs
		)));

		sections.add(new PanelDetails("Attend the concert", List.of(
			returnToTheBeach
		)));

		return sections;
	}
}
