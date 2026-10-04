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

	// Steps
	ObjectStep drinkFromBottle;
	NpcStep talkToCrab;
	VarbitRequirement shell1Obtained, shell2Obtained, shell3Obtained, shell4Obtained, shell5Obtained, shell6Obtained,
	           shell7Obtained, shellsObtained;
	ItemStep pickUpShellOne, pickUpShellTwo, pickUpShellThree, pickUpShellFour, pickUpShellFive, pickUpShellSix,
	           pickUpShellSeven, pickUpShellEight, pickUpShellNine;
	NpcStep turnInShells;

	ItemStep pickUpBigNet;
	NpcStep fishSeasoakedBowstring, fishBatteredBarrel, fishWeatheredRosewoodPlank;
	ItemStep assembleInstrument;
	NpcStep turnInInstrument;

	ObjectStep revealCrab1, revealCrab2, revealCrab3, revealCrab4, revealCrab5, revealCrab6, revealCrab7, revealCrab8;
	VarbitRequirement crab1Revealed, crab1Recruited, crab2Revealed, crab2Recruited, crab3Revealed, crab3Recruited,
		crab4Revealed, crab4Recruited, crab5Revealed, crab5Recruited, crab6Revealed, crab6Recruited, crab7Revealed,
	        crab7Recruited, crab8Revealed, crab8Recruited, bandCompleted;
	NpcStep talkToCrab1, talkToCrab2, talkToCrab3, talkToCrab4, talkToPenguin, talkToCrab5, talkToCrab6, talkToCrab7,
	        talkToCrab8;

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

		seasoakedBowstring = new ItemRequirement("Sea-soaked Bowstring", ItemID.CRAB_BOWSTRING);
		batteredBarrel = new ItemRequirement("Battered Barrel", ItemID.CRAB_BARREL);
		weatheredRosewoodPlank = new ItemRequirement("Weathered Rosewood Plank", ItemID.CRAB_PLANK);

		instrument = new ItemRequirement("Instrument", ItemID.CRAB_BASS);

		shell1Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_1, 1));
		shell2Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_2, 1));
		shell3Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_3, 1));
		shell4Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_4, 1));
		shell5Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_5, 1));
		shell6Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_6, 1));
		shell7Obtained = new VarbitRequirement((VarbitID.CRAB_SEASHELL_7, 1));
		shellsObtained = new VarbitRequirement((VarbitID.CRAB_SEASHELLS, 127));

		crab1Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_1, 1));
		crab1Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_1, 1));
		crab2Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_2, 1));
		crab2Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_2, 1));
		crab3Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_3, 1));
		crab3Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_3, 1));
		crab4Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_4, 1));
		crab4Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_4, 1));
		crab5Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_5, 1));
		crab5Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_5, 1));
		crab6Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_6, 1));
		crab6Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_6, 1));
		crab7Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_7, 1));
		crab7Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_7, 1));
		crab8Revealed = new VarbitRequirement((VarbitID.CRAB_BANDMATE_8, 1));
		crab8Recruited = new VarbitRequirement((VarbitID.CRAB_RECRUITED_BANDMATE_8, 1));
		bandCompleted = new VarbitRequirement(VarbitID.CRAB_BAND, 128);
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

		pickUpShellThree = new ItemStep(this,
			new WorldPoint(3051, 2634, 0),
			"Pick up a nearby Shell.",
			shellThree);

		pickUpShellOne = new ItemStep(this,
			new WorldPoint(3059, 2648, 0),
			"Pick up a nearby Shell.",
			shellOne, shell3Obtained);

		pickUpShellSeven = new ItemStep(this,
			new WorldPoint(3040, 2636, 0),
			"Pick up a nearby Shell.",
			shellSeven, shell1Obtained);

		pickUpShellFour = new ItemStep(this,
			new WorldPoint(3036, 2643, 0),
			"Pick up a nearby Shell.",
			shellFour, shell7Obtained);

		pickUpShellTwo = new ItemStep(this,
			new WorldPoint(3044, 2652, 0),
			"Pick up a nearby Shell.",
			shellTwo, shell4Obtained);

		pickUpShellFive = new ItemStep(this,
			new WorldPoint(3050, 2655, 0),
			"Pick up a nearby Shell.",
			shellFive, shell2Obtained);

		pickUpShellSix = new ItemStep(this,
			new WorldPoint(3063, 2642, 0),
			"Pick up a nearby Shell.",
			shellSix, shell5Obtained);

		turnInShells = new NpcStep(this, NpcID.CRAB_MAINCRAB_START,
			"Show the shells to the crab.",
			shellOne, shellTwo, shellThree,
			shellFour, shellFive, shellSix,
			shellSeven, shellsObtained);

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

		revealCrab8 = new ObjectStep(this,
			ObjectID.CRAB_ENAKH_LEAFLESSBUSH,
			new WorldPoint(3050, 2635, 0),
			"Shake the bush to reveal a crab.");

		talkToCrab8 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab2 = new ObjectStep(this,
			ObjectID.CRAB_ENAKH_LEAFLESSBUSH,
			new WorldPoint(3057, 2640, 0),
			"Shake the bush to reveal a crab.");

		talkToCrab2 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab10 = new ObjectStep(this,
			ObjectID.CRAB_PALM_UPDATE03,
			new WorldPoint(3044, 2653, 0),
			"Investigate the palm to reveal a crab.");

		talkToCrab10 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab7 = new ObjectStep(this,
			ObjectID.CRAB_AVIUM_TREE_1,
			new WorldPoint(3046, 2656, 0),
			"Investigate the avium tree to reveal a crab.");

		// Had to sit emote here.

		talkToCrab7 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab4 = new ObjectStep(this,
			ObjectID.CRAB_PALM_UPDATE01,
			new WorldPoint(3048, 2658, 0),
			"Investigate the palm to reveal a crab.");

		talkToPenguin = new NpcStep(this,
			NpcID.CRAB_PENGUIN,
			"Attempt to recruit the 'crab' to join your band.");

		talkToCrab4 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab3 = new ObjectStep(this,
			ObjectID.CRAB_AVIUM_TREE_2,
			new WorldPoint(3050, 2657, 0),
			"Investigate the avium tree to reveal a crab.");

		// Had to sit here

		talkToCrab3 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab7 = new ObjectStep(this,
			ObjectID.CRAB_GROUNDCOVER_PLANT4_WITHERED,
			new WorldPoint(3054, 2656, 0),
			"Investigate the plant to reveal a crab.");

		talkToCrab7 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		revealCrab8 = new ObjectStep(this,
			ObjectID.CRAB_PALM_UPDATE01,
			new WorldPoint(3040, 2649, 0),
			"Investigate the palm to reveal a crab.");

		talkToCrab8 = new NpcStep(this,
			NpcID.CRAB_BANDCRAB,
			"Recruit the crab to join your band.");

		returnToTheBeach = new NpcStep(this,
			NpcID.CRAB_MAINCRAB_END,
			"Speak to the crab to start the final cutscene.");
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

		var cShells = new ConditionalStep(this, pickUpShellThree);
		cShells.addStep(shell3Obtained, pickUpShellOne);
		cShells.addStep(shell1Obtained, pickUpShellSeven);
		cShells.addStep(shell7Obtained, pickUpShellFour);
		cShells.addStep(shell4Obtained, pickUpShellTwo);
		cShells.addStep(shell2Obtained, pickUpShellFive);
		cShells.addStep(shell5Obtained, pickUpShellSix);
		cShells.addStep(shellsObtained, turnInShells);
		steps.put(25, cShells);

		var cBowstring = new ConditionalStep(this, pickUpBigNet);
		cBowstring.addStep(bigFishingNet, fishSeasoakedBowstring);
		steps.put(30, cBowstring);

		var cBarrel = new ConditionalStep(this, pickUpBigNet);
		cBarrel.addStep(bigFishingNet, fishBatteredBarrel);
		steps.put(35, cBarrel);

		var cPlank = new ConditionalStep(this, pickUpBigNet);
		cPlank.addStep(bigFishingNet, fishWeatheredRosewoodPlank);
		steps.put(37, cPlank);

		steps.put(40, assembleInstrument);
		steps.put(45, turnInInstrument);

		var cRustacean = new ConditionalStep(this, revealCrab8);
		cRustacean.addStep(crab8Revealed, talkToCrab8);
		cRustacean.addStep(crab8Recruited, revealCrab2);
		cRustacean.addStep(crab2Revealed, talkToCrab2);
		cRustacean.addStep(crab2Recruited, revealCrab10);
		cRustacean.addStep(crab10Revealed, talkToCrab10);
		cRustacean.addStep(crab10Recruited, kickPenguin);
		cRustacean.addStep(penguinKicked, revealCrab7);
		cRustacean.addStep(crab7Revealed, talkToCrab7);
		cRustacean.addStep(crab7Recruited, revealCrab4);
		cRustacean.addStep(crab4Revealed, talkToCrab4);
		cRustacean.addStep(crab4Recruited, revealCrab3);
		cRustacean.addStep(crab3Revealed, talkToCrab3);
		cRustacean.addStep(crab)
		steps.put(47, revealCrab1);
		steps.put(48, talkToCrab1);
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

		sections.add(new PanelDetails("Recruit some bandmembers", List.of(
			revealCrab1,
			talkToCrab1,

		)));
		return sections;
	}
}
