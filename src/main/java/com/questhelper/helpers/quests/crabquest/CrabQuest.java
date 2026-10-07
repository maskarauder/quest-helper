package com.questhelper.helpers.quests.crabquest;

import com.questhelper.panel.PanelDetails;
import com.questhelper.questhelpers.BasicQuestHelper;
import com.questhelper.requirements.Requirement;
import com.questhelper.requirements.conditional.Conditions;
import com.questhelper.requirements.conditional.NpcCondition;
import com.questhelper.requirements.item.ItemRequirement;
import com.questhelper.requirements.player.FreeInventorySlotRequirement;
import com.questhelper.requirements.player.SkillRequirement;
import com.questhelper.requirements.var.VarbitRequirement;
import com.questhelper.rewards.ExperienceReward;
import com.questhelper.rewards.QuestPointReward;
import com.questhelper.rewards.UnlockReward;
import com.questhelper.steps.ConditionalStep;
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
import static com.questhelper.requirements.util.LogicHelper.*;


public class CrabQuest extends BasicQuestHelper
{
	// Initial Reqs
	SkillRequirement sailingLevel;
	SkillRequirement fishingLevel;
	ItemRequirement bigFishingNet;
	FreeInventorySlotRequirement nineSlots;

	// Quest Reqs
	ItemRequirement shellOne, shellTwo, shellThree, shellFour, shellFive, shellSix, shellSeven;
	ItemRequirement seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank;
	ItemRequirement instrument;
	VarbitRequirement inQuestInstance;

	// Steps
	ObjectStep drinkFromBottle;
	NpcStep talkToCrab, talkToCrabButHesShy;
	Requirement shell1Obtained, shell2Obtained, shell3Obtained, shell4Obtained, shell5Obtained, shell6Obtained,
	           shell7Obtained, shellsObtained;
	ItemStep pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour, pickUpShellFive, pickUpShellSix,
	           pickUpShellSeven;
	NpcStep turnInShells;

	ItemStep pickUpBigNet;
	Conditions floatsamPresent;
	NpcStep resetFloatsam, fishSeasoakedBowstring, fishBatteredBarrel, fishWeatheredRosewoodPlank;
	ItemStep assembleInstrument;
	NpcStep turnInInstrument;

	VarbitRequirement penguinKicked, partialComplete;
	NpcCondition penguinRevealed;
	CrabStep crab1, crab2, crab3, crab4, crab5, crab6, crab7, crab8, crab9, crab10, crab11;

	NpcStep returnToTheBeach;

	@Override
	protected void setupRequirements()
	{
		sailingLevel = new SkillRequirement(Skill.SAILING, 40, false);
		fishingLevel = new SkillRequirement(Skill.FISHING, 30, false);

		bigFishingNet = new ItemRequirement("Big Fishing Net", ItemID.BIG_NET);
		bigFishingNet.canBeObtainedDuringQuest();

		inQuestInstance = new VarbitRequirement(VarbitID.CRAB_PUT_BACK_IN_INSTANCE, 1);
		nineSlots = new FreeInventorySlotRequirement(9);

		shellOne = new ItemRequirement("Shell", ItemID.CRAB_SHELL_1);
		shellTwo = new ItemRequirement("Shell", ItemID.CRAB_SHELL_2);
		shellThree = new ItemRequirement("Shell", ItemID.CRAB_SHELL_3);
		shellFour = new ItemRequirement("Shell", ItemID.CRAB_SHELL_4);
		shellFive = new ItemRequirement("Shell", ItemID.CRAB_SHELL_5);
		shellSix = new ItemRequirement("Shell", ItemID.CRAB_SHELL_6);
		shellSeven = new ItemRequirement("Shell", ItemID.CRAB_SHELL_7);

		seasoakedBowstring = new ItemRequirement("Sea-soaked Bowstring", ItemID.CRAB_BOWSTRING);
		batteredBarrel = new ItemRequirement("Battered Barrel", ItemID.CRAB_BARREL);
		weatheredRosewoodPlank = new ItemRequirement("Weathered Rosewood Plank", ItemID.CRAB_PLANK);

		instrument = new ItemRequirement("Instrument", ItemID.CRAB_BASS);

		shell1Obtained = or(shellOne, new VarbitRequirement(VarbitID.CRAB_SEASHELL_1, 1));
		shell2Obtained = or(shellTwo, new VarbitRequirement(VarbitID.CRAB_SEASHELL_2, 1));
		shell3Obtained = or(shellThree, new VarbitRequirement(VarbitID.CRAB_SEASHELL_3, 1));
		shell4Obtained = or(shellFour, new VarbitRequirement(VarbitID.CRAB_SEASHELL_4, 1));
		shell5Obtained = or(shellFive, new VarbitRequirement(VarbitID.CRAB_SEASHELL_5, 1));
		shell6Obtained = or(shellSix, new VarbitRequirement(VarbitID.CRAB_SEASHELL_6, 1));
		shell7Obtained = or(shellSeven, new VarbitRequirement(VarbitID.CRAB_SEASHELL_7, 1));
		shellsObtained = or(and(
								shell1Obtained, shell2Obtained, shell3Obtained, shell4Obtained,
								shell5Obtained, shell6Obtained, shell7Obtained
								),
							new VarbitRequirement(VarbitID.CRAB_SEASHELLS, 127));

		floatsamPresent = not(new VarbitRequirement(VarbitID.CRAB_FLOATSAM_HIDDEN, 3));


		partialComplete = new VarbitRequirement(VarbitID.CRAB_PARTIAL_COMPLETE, 1);
	}

	public void setupSteps()
	{
		drinkFromBottle = new ObjectStep(this,
			ObjectID.CRAB_BOTTLE,
			new WorldPoint(3055, 2639, 0),
			"Drink from the Bottle on the shore of Dognose Island.",
			sailingLevel, fishingLevel, bigFishingNet, nineSlots, not(inQuestInstance));
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

		pickUpBigNet = new ItemStep(this,
			new WorldPoint(3061, 2646, 0),
			"Pick up the nearby Big Fishing Net.",
			bigFishingNet);

		fishSeasoakedBowstring = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			/* new WorldPoint(3048, 2632, 0), */
			"Fish a Sea-soaked bowstring from the nearby Floatsam.",
			bigFishingNet, floatsamPresent);
		fishSeasoakedBowstring.addAlternateNpcs(NpcID.CRAB_FLOATSAM_2, NpcID.CRAB_FLOATSAM_3);

		fishBatteredBarrel = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			"Fish a Battered Barrel from the nearby Floatsam.",
			seasoakedBowstring, bigFishingNet, floatsamPresent);
		fishBatteredBarrel.addAlternateNpcs(NpcID.CRAB_FLOATSAM_2, NpcID.CRAB_FLOATSAM_3);

		fishWeatheredRosewoodPlank = new NpcStep(this,
			NpcID.CRAB_FLOATSAM_1,
			"Fish a Weathered Rosewood Plank from the nearby Floatsam.",
			seasoakedBowstring, batteredBarrel, bigFishingNet, floatsamPresent);
		fishWeatheredRosewoodPlank.addAlternateNpcs(NpcID.CRAB_FLOATSAM_2, NpcID.CRAB_FLOATSAM_3);

		resetFloatsam = new NpcStep(this,
			NpcID.CRAB_MAINCRAB,
			"Speak to the crab to reset the floatsam.",
			not(floatsamPresent));
		resetFloatsam.setShowInSidebar(false);

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

		crab1 = new CrabStep(this, ObjectID.CRAB_ENAKH_LEAFLESSBUSH, new WorldPoint(3051, 2636, 0),
			);
		crab1Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3051, 2636, 0));
		crab2Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3056, 2640, 0));
		// crab3 one needs double-checking. I completed this step before taking the tile locations.
		crab3Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3041, 2649, 0));
		crab4Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3040, 2647, 0));
		crab5Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3037, 2646, 0));
		crab6Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3038, 2644, 0));
		crab7Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3038, 2638, 0));
		crab8Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3041, 2637, 0));
		crab9Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3042, 2639, 0));
		crab10Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3045, 2653, 0));
		crab11Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3047, 2655, 0));
		penguinRevealed = new NpcCondition(NpcID.CRAB_PENGUIN, new WorldPoint(3049, 2658, 0));
		crab12Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3049, 2657, 0));
	//	crab13Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3050, 2656, 0));
		crab14Revealed = new NpcCondition(NpcID.CRAB_BANDCRAB, new WorldPoint(3053, 2656, 0));

		returnToTheBeach = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_END,
			"Speak to the crab to finish the quest.",
			partialComplete);

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
		cShells.addStep(not(shell3Obtained), pickUpShellThree);
		cShells.addStep(not(shell6Obtained), pickUpShellSix);
		cShells.addStep(not(shell1Obtained), pickUpShellOne);
		cShells.addStep(not(shell7Obtained), pickUpShellSeven);
		cShells.addStep(not(shell4Obtained), pickUpShellFour);
		cShells.addStep(not(shell2Obtained), pickUpShellTwo);
		cShells.addStep(not(shell5Obtained), pickUpShellFive);
		steps.put(25, cShells);
		steps.put(30, turnInShells);
		steps.put(35, turnInShells);

		var cInstrument = new ConditionalStep(this, assembleInstrument, seasoakedBowstring, batteredBarrel,
			                                                                      weatheredRosewoodPlank);
		cInstrument.addStep(and(not(and(seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
							    not(bigFishingNet)), pickUpBigNet);
		cInstrument.addStep(and(not(and(seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
							    not(floatsamPresent)), resetFloatsam);
		cInstrument.addStep(not(seasoakedBowstring), fishSeasoakedBowstring);
		cInstrument.addStep(not(batteredBarrel), fishBatteredBarrel);
		cInstrument.addStep(not(weatheredRosewoodPlank), fishWeatheredRosewoodPlank);
		steps.put(37, cInstrument);

		var cInstrumentSafety = new ConditionalStep(this, turnInInstrument, instrument);
		cInstrumentSafety.addStep(and(not(instrument),
			                          not(and(seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
									  not(bigFishingNet)), pickUpBigNet);
		cInstrumentSafety.addStep(and(not(instrument),
			                          not(and(seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank)),
									  not(floatsamPresent)), resetFloatsam);
		cInstrumentSafety.addStep(and(not(instrument), not(seasoakedBowstring)), fishSeasoakedBowstring);
		cInstrumentSafety.addStep(and(not(instrument), not(batteredBarrel)), fishBatteredBarrel);
		cInstrumentSafety.addStep(and(not(instrument), not(weatheredRosewoodPlank)), fishWeatheredRosewoodPlank);
		cInstrumentSafety.addStep(and(not(instrument), seasoakedBowstring, batteredBarrel, weatheredRosewoodPlank),
			                      assembleInstrument);
		steps.put(40, cInstrumentSafety);

		steps.put(45, turnInInstrument);

		var cRustacean = new ConditionalStep(this, returnToTheBeach, partialComplete);
//		cRustacean.addStep(and(not(crab8Recruited), not(bandmemberRevealed)), revealCrab8);
		cRustacean.addStep(not(crab8Recruited), talkToCrab8);
//		cRustacean.addStep(and(not(crab2Recruited), not(bandmemberRevealed)), revealCrab2);
		cRustacean.addStep(not(crab2Recruited), talkToCrab2);
//		cRustacean.addStep(and(not(crab10Recruited), not(bandmemberRevealed)), revealCrab10);
		cRustacean.addStep(not(crab10Recruited), talkToCrab10);
//		cRustacean.addStep(and(not(crab7Recruited), not(bandmemberRevealed)), revealCrab7);
		cRustacean.addStep(not(crab7Recruited), talkToCrab7);
//		cRustacean.addStep(and(not(crab4Recruited), not(bandmemberRevealed)), revealCrab4);
		cRustacean.addStep(not(crab4Recruited), talkToCrab4);
//		cRustacean.addStep(and(not(crab3Recruited), not(bandmemberRevealed)), revealCrab3);
		cRustacean.addStep(not(crab3Recruited), talkToCrab3);
		steps.put(47, cRustacean);

		steps.put(50, returnToTheBeach);

		// Guide the player back if they're not in the instance.
		steps.replaceAll((stage, normalStep) ->
		{
			if (stage == 0) return normalStep;
			return new ConditionalStep(this, normalStep).addStep(not(inQuestInstance), drinkFromBottle);
		});

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
			talkToCrab,
			talkToCrabButHesShy
		)));

		sections.add(new PanelDetails("Collecting shells", List.of(
			pickUpShellThree, pickUpShellSix, pickUpShellOne, pickUpShellSeven,
			pickUpShellFour, pickUpShellTwo, pickUpShellFive, turnInShells
		)));

		sections.add(new PanelDetails("Assembling an instrument", List.of(
			pickUpBigNet,
			fishSeasoakedBowstring,
			fishBatteredBarrel,
			fishWeatheredRosewoodPlank,
			resetFloatsam,
			assembleInstrument,
			turnInInstrument
		)));

		sections.add(new PanelDetails("Recruit some bandmembers", List.of(
			revealCrab8, talkToCrab8,
			revealCrab2, talkToCrab2,
			revealCrab10, talkToCrab10,
			revealCrab7, talkToCrab7,
			revealCrab4, talkToCrab4,
			revealCrab3, talkToCrab3
		)));

		sections.add(new PanelDetails("Attend the concert", List.of(
			returnToTheBeach
		)));

		return sections;
	}
}
