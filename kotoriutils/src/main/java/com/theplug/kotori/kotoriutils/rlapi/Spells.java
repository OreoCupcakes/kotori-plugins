package com.theplug.kotori.kotoriutils.rlapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Spells
{
    //Normal Spellbook Offensive Spells
    WIND_STRIKE("Wind Strike", WidgetInfoPlus.SPELL_WIND_STRIKE, 1, 0, 1, 0),
    WATER_STRIKE("Water Strike", WidgetInfoPlus.SPELL_WATER_STRIKE, 5, 0, 1, 0),
    EARTH_STRIKE("Earth Strike", WidgetInfoPlus.SPELL_EARTH_STRIKE, 9, 0, 1, 0),
    FIRE_STRIKE("Fire Strike", WidgetInfoPlus.SPELL_FIRE_STRIKE, 13, 0, 1, 0),
    WIND_BOLT("Wind Bolt", WidgetInfoPlus.SPELL_WIND_BOLT, 17, 0, 1, 0),
    WATER_BOLT("Water Bolt", WidgetInfoPlus.SPELL_WATER_BOLT, 23, 0, 1, 0),
    EARTH_BOLT("Earth Bolt", WidgetInfoPlus.SPELL_EARTH_BOLT, 29, 0, 1, 0),
    FIRE_BOLT("Fire Bolt", WidgetInfoPlus.SPELL_FIRE_BOLT, 35, 0, 1, 0),
    WIND_BLAST("Wind Blast", WidgetInfoPlus.SPELL_WIND_BLAST, 41, 0, 1, 0),
    WATER_BLAST("Water Blast", WidgetInfoPlus.SPELL_WATER_BLAST, 47, 0, 1, 0),
    EARTH_BLAST("Earth Blast", WidgetInfoPlus.SPELL_EARTH_BLAST, 53, 0, 1, 0),
    FIRE_BLAST("Fire Blast", WidgetInfoPlus.SPELL_FIRE_BLAST, 59, 0, 1, 0),
    WIND_WAVE("Wind Wave", WidgetInfoPlus.SPELL_WIND_WAVE, 62, 0, 1, 0),
    WATER_WAVE("Water Wave", WidgetInfoPlus.SPELL_WATER_WAVE, 65, 0, 1, 0),
    EARTH_WAVE("Earth Wave", WidgetInfoPlus.SPELL_EARTH_WAVE, 70, 0, 1, 0),
    FIRE_WAVE("Fire Wave", WidgetInfoPlus.SPELL_FIRE_WAVE, 75, 0, 1, 0),
    WIND_SURGE("Wind Surge", WidgetInfoPlus.SPELL_WIND_SURGE, 81, 0, 1, 0),
    WATER_SURGE("Water Surge", WidgetInfoPlus.SPELL_WATER_SURGE, 85, 0, 1, 0),
    EARTH_SURGE("Earth Surge", WidgetInfoPlus.SPELL_EARTH_SURGE, 90, 0, 1, 0),
    FIRE_SURGE("Fire Surge", WidgetInfoPlus.SPELL_FIRE_SURGE, 95, 0, 1, 0),
    CRUMBLE_UNDEAD("Crumble Undead", WidgetInfoPlus.SPELL_CRUMBLE_UNDEAD, 39, 0, 2, 0),
    IBAN_BLAST("Iban Blast", WidgetInfoPlus.SPELL_IBAN_BLAST, 50, 0, 1, 0),
    MAGIC_DART("Magic Dart", WidgetInfoPlus.SPELL_MAGIC_DART, 50, 0, 1, 0),
    SARADOMIN_STRIKE("Saradomin Strike", WidgetInfoPlus.SPELL_SARADOMIN_STRIKE, 60, 0, 1, 0),
    FLAMES_OF_ZAMORAK("Flames of Zamorak", WidgetInfoPlus.SPELL_FLAMES_OF_ZAMORAK, 60, 0, 1, 0),
    CLAWS_OF_GUTHIX("Claws of Guthix", WidgetInfoPlus.SPELL_CLAWS_OF_GUTHIX, 60, 0, 1, 0),

    //Normal Spellbook Curse Spells
    CONFUSE("Confuse", WidgetInfoPlus.SPELL_CONFUSE, 3, 0, 1, 0),
    WEAKEN("Weaken", WidgetInfoPlus.SPELL_WEAKEN, 11, 0, 1, 0),
    CURSE("Curse", WidgetInfoPlus.SPELL_CURSE, 19, 0, 1, 0),
    VULNERABILITY("Vulnerability", WidgetInfoPlus.SPELL_VULNERABILITY, 66, 0, 1, 0),
    ENFEEBLE("Enfeeble", WidgetInfoPlus.SPELL_ENFEEBLE, 73, 0, 1, 0),
    STUN("Stun", WidgetInfoPlus.SPELL_STUN, 80, 0, 1, 0),
    BIND("Bind", WidgetInfoPlus.SPELL_BIND, 20, 0, 1, 0),
    SNARE("Snare", WidgetInfoPlus.SPELL_SNARE, 50, 0, 1, 0),
    ENTANGLE("Entangle", WidgetInfoPlus.SPELL_ENTANGLE, 79, 0, 1, 0),
    TELE_BLOCK("Tele Block", WidgetInfoPlus.SPELL_TELE_BLOCK, 95, 0, 3, 0),

    //Normal Spellbook Support Spell
    CHARGE("Charge", WidgetInfoPlus.SPELL_CHARGE, 80, 0, 0, 1),

    //Normal Spellbook Teleport Spells
    LUMBRIDGE_HOME_TELEPORT("Lumbridge Home Teleport", WidgetInfoPlus.SPELL_LUMBRIDGE_HOME_TELEPORT, 1, 0, 0, 1),
    VARROCK_TELEPORT("Varrock Teleport", WidgetInfoPlus.SPELL_VARROCK_TELEPORT, 25, 0, 0, 1),
    VARROCK_TELEPORT_GRAND_EXCHANGE("Grand Exchange Varrock Teleport", WidgetInfoPlus.SPELL_VARROCK_TELEPORT, 25, 0, 0, 2),
    LUMBRIDGE_TELEPORT("Lumbridge Teleport", WidgetInfoPlus.SPELL_LUMBRIDGE_TELEPORT, 31, 0, 0, 1),
    FALADOR_TELEPORT("Falador Teleport", WidgetInfoPlus.SPELL_FALADOR_TELEPORT, 37, 0, 0, 1),
    TELEPORT_TO_HOUSE("Teleport to House", WidgetInfoPlus.SPELL_TELEPORT_TO_HOUSE, 40, 0, 0, 1),
    TELEPORT_TO_HOUSE_OUTSIDE("Outside Teleport to House", WidgetInfoPlus.SPELL_TELEPORT_TO_HOUSE, 40, 0, 0, 2),
    TELEPORT_TO_HOUSE_GROUP_PREVIOUS("Group: Previous Teleport to House", WidgetInfoPlus.SPELL_TELEPORT_TO_HOUSE, 40, 0, 0, 5),
    CAMELOT_TELEPORT("Camelot Teleport", WidgetInfoPlus.SPELL_CAMELOT_TELEPORT, 45, 0, 0, 1),
    CAMELOT_TELEPORT_SEERS("Seer's Camelot Teleport", WidgetInfoPlus.SPELL_CAMELOT_TELEPORT, 45, 0, 0, 2),
    ARDOUGNE_TELEPORT("Ardougne Teleport", WidgetInfoPlus.SPELL_ARDOUGNE_TELEPORT, 51, 0, 0, 1),
    WATCHTOWER_TELEPORT("Watchtower Teleport", WidgetInfoPlus.SPELL_WATCHTOWER_TELEPORT, 58, 0, 0, 1),
    WATCHTOWER_TELEPORT_YANILLE("Yanille Watchtower Teleport", WidgetInfoPlus.SPELL_WATCHTOWER_TELEPORT, 58, 0, 0, 2),
    TROLLHEIM_TELEPORT("Trollheim Teleport", WidgetInfoPlus.SPELL_TROLLHEIM_TELEPORT, 61, 0, 0, 1),
    APE_ATOLL_TELEPORT_NORMAL("Ape Atoll Teleport", WidgetInfoPlus.SPELL_TELEPORT_TO_APE_ATOLL, 64, 0, 0, 1),
    KOUREND_CASTLE_TELEPORT("Kourend Castle Teleport", WidgetInfoPlus.SPELL_TELEPORT_TO_KOUREND, 69, 0, 0, 1),
    TELEOTHER_LUMBRIDGE("Teleother Lumbridge", WidgetInfoPlus.SPELL_TELEOTHER_LUMBRIDGE, 74, 0, 3, 0),
    TELEOTHER_FALADOR("Teleother Falador", WidgetInfoPlus.SPELL_TELEOTHER_FALADOR, 82, 0, 3, 0),
    TELEPORT_TO_TARGET("Teleport to Target", WidgetInfoPlus.SPELL_BOUNTY_TARGET_TELEPORT, 85, 0, 0, 1),
    TELEOTHER_CAMELOT("Teleother Camelot", WidgetInfoPlus.SPELL_TELEOTHER_CAMELOT, 90, 0, 3, 0),

    //Normal Spellbook Alchemy Spells
    BONES_TO_BANANAS("Bones to Bananas", WidgetInfoPlus.SPELL_BONES_TO_BANANAS, 15, 0, 0, 1),
    BONES_TO_PEACHES("Bones to Peaches", WidgetInfoPlus.SPELL_BONES_TO_PEACHES, 60, 0, 0, 1),
    LOW_LEVEL_ALCHEMY("Low Level Alchemy", WidgetInfoPlus.SPELL_LOW_LEVEL_ALCHEMY, 21, 0, 6, 0),
    HIGH_LEVEL_ALCHEMY("High Level Alchemy", WidgetInfoPlus.SPELL_HIGH_LEVEL_ALCHEMY, 55, 0, 6, 0),
    SUPERHEAT_ITEM("Superheat Item", WidgetInfoPlus.SPELL_SUPERHEAT_ITEM, 43, 0, 6, 0),

    //Normal Spellbook Enchantment Spells
    LVL_1_ENCHANT("Lvl-1 Enchant", WidgetInfoPlus.SPELL_LVL_1_ENCHANT, 7, 0, 6, 0),
    LVL_2_ENCHANT("Lvl-2 Enchant", WidgetInfoPlus.SPELL_LVL_2_ENCHANT, 27, 0, 6, 0),
    LVL_3_ENCHANT("Lvl-3 Enchant", WidgetInfoPlus.SPELL_LVL_3_ENCHANT, 49, 0, 6, 0),
    LVL_4_ENCHANT("Lvl-4 Enchant", WidgetInfoPlus.SPELL_LVL_4_ENCHANT, 57, 0, 6, 0),
    LVL_5_ENCHANT("Lvl-5 Enchant", WidgetInfoPlus.SPELL_LVL_5_ENCHANT, 68, 0, 6, 0),
    LVL_6_ENCHANT("Lvl-6 Enchant", WidgetInfoPlus.SPELL_LVL_6_ENCHANT, 87, 0, 6, 0),
    LVL_7_ENCHANT("Lvl-7 Enchant", WidgetInfoPlus.SPELL_LVL_7_ENCHANT, 93, 0, 6, 0),
    CHARGE_WATER_ORB("Charge Water Orb", WidgetInfoPlus.SPELL_CHARGE_WATER_ORB, 56, 0, 5, 0),
    CHARGE_EARTH_ORB("Charge Earth Orb", WidgetInfoPlus.SPELL_CHARGE_EARTH_ORB, 60, 0, 5, 0),
    CHARGE_FIRE_ORB("Charge Fire Orb", WidgetInfoPlus.SPELL_CHARGE_FIRE_ORB, 63, 0, 5, 0),
    CHARGE_AIR_ORB("Charge Air Orb", WidgetInfoPlus.SPELL_CHARGE_AIR_ORB, 66, 0, 5, 0),

    //Normal Spellbook Other Spells
    TELEKINETIC_GRAB("Telekinetic Grab", WidgetInfoPlus.SPELL_TELEKINETIC_GRAB, 33, 0, 4, 0),
    ENCHANT_CROSSBOW_BOLT("Enchant Crossbow Bolt", WidgetInfoPlus.SPELL_ENCHANT_CROSSBOW_BOLT, 4, 0, 0, 1),

    //Ancient Spellbook Combat Spells
    SMOKE_RUSH("Smoke Rush", WidgetInfoPlus.SPELL_SMOKE_RUSH, 50, 1, 1, 0),
    SHADOW_RUSH("Shadow Rush", WidgetInfoPlus.SPELL_SHADOW_RUSH, 52, 1, 1, 0),
    BLOOD_RUSH("Blood Rush", WidgetInfoPlus.SPELL_BLOOD_RUSH, 56, 1, 1, 0),
    ICE_RUSH("Ice Rush", WidgetInfoPlus.SPELL_ICE_RUSH, 58, 1, 1, 0),
    SMOKE_BURST("Smoke Burst", WidgetInfoPlus.SPELL_SMOKE_BURST, 62, 1, 1, 0),
    SHADOW_BURST("Shadow Burst", WidgetInfoPlus.SPELL_SHADOW_BURST, 64, 1, 1, 0),
    BLOOD_BURST("Blood Burst", WidgetInfoPlus.SPELL_BLOOD_BURST, 68, 1, 1, 0),
    ICE_BURST("Ice Burst", WidgetInfoPlus.SPELL_ICE_BURST, 70, 1, 1, 0),
    SMOKE_BLITZ("Smoke Blitz", WidgetInfoPlus.SPELL_SMOKE_BLITZ, 74, 1, 1, 0),
    SHADOW_BLITZ("Shadow Blitz", WidgetInfoPlus.SPELL_SHADOW_BLITZ, 76, 1, 1, 0),
    BLOOD_BLITZ("Blood Blitz", WidgetInfoPlus.SPELL_BLOOD_BLITZ, 80, 1, 1, 0),
    ICE_BLITZ("Ice Blitz", WidgetInfoPlus.SPELL_ICE_BLITZ, 82, 1, 1, 0),
    SMOKE_BARRAGE("Smoke Barrage", WidgetInfoPlus.SPELL_SMOKE_BARRAGE, 86, 1, 1, 0),
    SHADOW_BARRAGE("Shadow Barrage", WidgetInfoPlus.SPELL_SHADOW_BARRAGE, 88, 1, 1, 0),
    BLOOD_BARRAGE("Blood Barrage", WidgetInfoPlus.SPELL_BLOOD_BARRAGE, 92, 1, 1, 0),
    ICE_BARRAGE("Ice Barrage", WidgetInfoPlus.SPELL_ICE_BARRAGE, 94, 1, 1, 0),

    //Ancient Spellbook Teleports
    EDGEVILLE_HOME_TELEPORT("Edgeville Home Teleport", WidgetInfoPlus.SPELL_EDGEVILLE_HOME_TELEPORT, 1, 1, 0, 1),
    PADDEWWA_TELEPORT("Paddewwa Teleport", WidgetInfoPlus.SPELL_PADDEWWA_TELEPORT, 54, 1, 0, 1),
    SENNTISTEN_TELEPORT("Senntisten Teleport", WidgetInfoPlus.SPELL_SENNTISTEN_TELEPORT, 60, 1, 0, 1),
    KHARYRLL_TELEPORT("Kharyrll Teleport", WidgetInfoPlus.SPELL_KHARYRLL_TELEPORT, 66, 1, 0, 1),
    LASSAR_TELEPORT("Lassar Teleport", WidgetInfoPlus.SPELL_LASSAR_TELEPORT, 72, 1, 0, 1),
    DAREEYAK_TELEPORT("Dareeyak Teleport", WidgetInfoPlus.SPELL_DAREEYAK_TELEPORT, 78, 1, 0, 1),
    CARRALLANGER_TELEPORT("Carrallanger Teleport", WidgetInfoPlus.SPELL_CARRALLANGER_TELEPORT, 84, 1, 0, 1),
    ANNAKARL_TELEPORT("Annakarl Teleport", WidgetInfoPlus.SPELL_ANNAKARL_TELEPORT, 90, 1, 0, 1),
    GHORROCK_TELEPORT("Ghorrock Teleport", WidgetInfoPlus.SPELL_GHORROCK_TELEPORT, 96, 1, 0, 1),

    //Lunar Spellbook Combat Spells
    MONSTER_EXAMINE("Monster Examine", WidgetInfoPlus.SPELL_MONSTER_EXAMINE, 66, 2, 2, 0),
    CURE_OTHER("Cure Other", WidgetInfoPlus.SPELL_CURE_OTHER, 68, 2, 3, 0),
    CURE_ME("Cure Me", WidgetInfoPlus.SPELL_CURE_ME, 71, 2, 0, 1),
    CURE_GROUP("Cure Group", WidgetInfoPlus.SPELL_CURE_GROUP, 74, 2, 0, 1),
    STAT_SPY("Stat Spy", WidgetInfoPlus.SPELL_STAT_SPY, 75, 2, 3, 0),
    DREAM("Dream", WidgetInfoPlus.SPELL_DREAM, 79, 2, 0, 1),
    STAT_RESTORE_POT_SHARE("Stat Restore Pot Share", WidgetInfoPlus.SPELL_STAT_RESTORE_POT_SHARE, 81, 2, 6, 0),
    BOOST_POTION_SHARE("Boost Potion Share", WidgetInfoPlus.SPELL_BOOST_POTION_SHARE, 84, 2, 6, 0),
    ENERGY_TRANSFER("Energy Transfer", WidgetInfoPlus.SPELL_ENERGY_TRANSFER, 91, 2, 3, 0),
    HEAL_OTHER("Heal Other", WidgetInfoPlus.SPELL_HEAL_OTHER, 92, 2, 3, 0),
    VENGEANCE_OTHER("Vengeance Other", WidgetInfoPlus.SPELL_VENGEANCE_OTHER, 93, 2, 3, 0),
    VENGEANCE("Vengeance", WidgetInfoPlus.SPELL_VENGEANCE, 94, 2, 0, 1),
    HEAL_GROUP("Heal Group", WidgetInfoPlus.SPELL_HEAL_GROUP, 95, 2, 0, 1),

    //Lunar Spellbook Teleports
    LUNAR_HOME_TELEPORT("Lunar Home Teleport", WidgetInfoPlus.SPELL_LUNAR_HOME_TELEPORT, 1, 2, 0, 1),
    MOONCLAN_TELEPORT("Moonclan Teleport", WidgetInfoPlus.SPELL_MOONCLAN_TELEPORT, 69, 2, 0, 1),
    TELE_GROUP_MOONCLAN("Tele Group Moonclan", WidgetInfoPlus.SPELL_TELE_GROUP_MOONCLAN, 70, 2, 0, 1),
    OURANIA_TELEPORT("Ourania Teleport", WidgetInfoPlus.SPELL_OURANIA_TELEPORT, 71, 2, 0, 1),
    WATERBIRTH_TELEPORT("Waterbirth Teleport", WidgetInfoPlus.SPELL_WATERBIRTH_TELEPORT, 72, 2, 0, 1),
    TELE_GROUP_WATERBIRTH("Tele Group Waterbirth", WidgetInfoPlus.SPELL_TELE_GROUP_WATERBIRTH, 73, 2, 0, 1),
    BARBARIAN_TELEPORT("Barbarian Teleport", WidgetInfoPlus.SPELL_BARBARIAN_TELEPORT, 75, 2, 0, 1),
    TELE_GROUP_BARBARIAN("Tele Group Barbarian", WidgetInfoPlus.SPELL_TELE_GROUP_BARBARIAN, 76, 2, 0, 1),
    KHAZARD_TELEPORT("Khazard Teleport", WidgetInfoPlus.SPELL_KHAZARD_TELEPORT, 78, 2, 0, 1),
    TELE_GROUP_KHAZARD("Tele Group Khazard", WidgetInfoPlus.SPELL_TELE_GROUP_KHAZARD, 79, 2, 0, 1),
    FISHING_GUILD_TELEPORT("Fishing Guild Teleport", WidgetInfoPlus.SPELL_FISHING_GUILD_TELEPORT, 85, 2, 0, 1),
    TELE_GROUP_FISHING_GUILD("Tele Group Fishing Guild", WidgetInfoPlus.SPELL_TELE_GROUP_FISHING_GUILD, 86, 2, 0, 1),
    CATHERBY_TELEPORT("Catherby Teleport", WidgetInfoPlus.SPELL_CATHERBY_TELEPORT, 87, 2, 0, 1),
    TELE_GROUP_CATHERBY("Tele Group Catherby", WidgetInfoPlus.SPELL_TELE_GROUP_CATHERBY, 88, 2, 0, 1),
    ICE_PLATEAU_TELEPORT("Ice Plateau Teleport", WidgetInfoPlus.SPELL_ICE_PLATEAU_TELEPORT, 89, 2, 0, 1),
    TELE_GROUP_ICE_PLATEAU("Tele Group Ice Plateau", WidgetInfoPlus.SPELL_TELE_GROUP_ICE_PLATEAU, 90, 2, 0, 1),

    //Lunar Spellbook Utility Spells
    BAKE_PIE("Bake Pie", WidgetInfoPlus.SPELL_BAKE_PIE, 65, 2, 0, 1),
    GEOMANCY("Geomancy", WidgetInfoPlus.SPELL_GEOMANCY, 65, 2, 0, 1),
    CURE_PLANT("Cure Plant", WidgetInfoPlus.SPELL_CURE_PLANT, 66, 2, 5, 0),
    NPC_CONTACT("NPC Contact", WidgetInfoPlus.SPELL_NPC_CONTACT, 67, 2, 0, 1),
    NPC_CONTACT_PREVIOUS("Previous NPC Contact", WidgetInfoPlus.SPELL_NPC_CONTACT, 67, 2, 0, 2),
    HUMIDIFY("Humidify", WidgetInfoPlus.SPELL_HUMIDIFY, 68, 2, 0, 1),
    HUMIDIFY_TARGET("Humidify", WidgetInfoPlus.SPELL_HUMIDIFY, 68, 2, 2, 0),
    HUNTER_KIT("Hunter Kit", WidgetInfoPlus.SPELL_HUNTER_KIT, 71, 2, 0, 1),
    SPIN_FLAX("Spin Flax", WidgetInfoPlus.SPELL_SPIN_FLAX, 76, 2, 0, 1),
    SUPERGLASS_MAKE("Superglass Make", WidgetInfoPlus.SPELL_SUPERGLASS_MAKE, 77, 2, 0, 1),
    TAN_LEATHER("Tan Leather", WidgetInfoPlus.SPELL_TAN_LEATHER, 78, 2, 0, 1),
    STRING_JEWELLERY("String Jewellery", WidgetInfoPlus.SPELL_STRING_JEWELLERY, 80, 2, 0, 1),
    MAGIC_IMBUE("Magic Imbue", WidgetInfoPlus.SPELL_MAGIC_IMBUE, 82, 2, 0, 1),
    FERTILE_SOIL("Fertile Soil", WidgetInfoPlus.SPELL_FERTILE_SOIL, 83, 2, 5, 0),
    PLANK_MAKE("Plank Make", WidgetInfoPlus.SPELL_PLANK_MAKE, 86, 2, 6, 0),
    RECHARGE_DRAGONSTONE("Recharge Dragonstone", WidgetInfoPlus.SPELL_RECHARGE_DRAGONSTONE, 89, 2, 0, 1),
    SPELLBOOK_SWAP("Spellbook Swap", WidgetInfoPlus.SPELL_SPELLBOOK_SWAP, 96, 2, 0, 1),
    SPELLBOOK_SWAP_NORMAL("Standard Spellbook Swap", WidgetInfoPlus.SPELL_SPELLBOOK_SWAP, 96, 2, 0, 2),
    SPELLBOOK_SWAP_ANCIENT("Ancient Spellbook Swap", WidgetInfoPlus.SPELL_SPELLBOOK_SWAP, 96, 2, 0, 3),
    SPELLBOOK_SWAP_ARCEUUS("Arceuus Spellbook Swap", WidgetInfoPlus.SPELL_SPELLBOOK_SWAP, 96, 2, 0, 4),

    //Arceuus Spellbook Teleports
    ARCEUUS_HOME_TELEPORT("Arceuus Home Teleport", WidgetInfoPlus.SPELL_ARCEUUS_HOME_TELEPORT, 1, 3, 0, 1),
    ARCEUUS_LIBRARY_TELEPORT("Arceuus Library Teleport", WidgetInfoPlus.SPELL_ARCEUUS_LIBRARY_TELEPORT, 6, 3, 0, 1),
    DRAYNOR_MANOR_TELEPORT("Draynor Manor Teleport", WidgetInfoPlus.SPELL_DRAYNOR_MANOR_TELEPORT, 17, 3, 0, 1),
    BATTLEFRONT_TELEPORT("Battlefront Teleport", WidgetInfoPlus.SPELL_BATTLEFRONT_TELEPORT, 23, 3, 0, 1),
    MIND_ALTAR_TELEPORT("Mind Altar Teleport", WidgetInfoPlus.SPELL_MIND_ALTAR_TELEPORT, 28, 3, 0, 1),
    RESPAWN_TELEPORT("Respawn Teleport", WidgetInfoPlus.SPELL_RESPAWN_TELEPORT, 34, 3, 0, 1),
    SALVE_GRAVEYARD_TELEPORT("Salve Graveyard Teleport", WidgetInfoPlus.SPELL_SALVE_GRAVEYARD_TELEPORT, 40, 3, 0, 1),
    FENKENSTRAINS_CASTLE_TELEPORT("Fenkenstrain's Castle Teleport", WidgetInfoPlus.SPELL_FENKENSTRAINS_CASTLE_TELEPORT, 48, 3, 0, 1),
    WEST_ARDOUGNE_TELEPORT("West Ardougne Teleport", WidgetInfoPlus.SPELL_WEST_ARDOUGNE_TELEPORT, 61, 3, 0, 1),
    HARMONY_ISLAND_TELEPORT("Harmony Island Teleport", WidgetInfoPlus.SPELL_HARMONY_ISLAND_TELEPORT, 65, 3, 0, 1),
    CEMETERY_TELEPORT("Cemetery Teleport", WidgetInfoPlus.SPELL_CEMETERY_TELEPORT, 71, 3, 0, 1),
    BARROWS_TELEPORT("Barrows Teleport", WidgetInfoPlus.SPELL_BARROWS_TELEPORT, 83, 3, 0, 1),
    APE_ATOLL_TELEPORT_ARCEUUS("Ape Atoll Teleport", WidgetInfoPlus.SPELL_APE_ATOLL_TELEPORT, 90, 3, 0, 1),

    //Arceuus Spellbook Combat Spells
    GHOSTLY_GRASP("Ghostly Grasp", WidgetInfoPlus.SPELL_GHOSTLY_GRASP, 35, 3, 1, 0),
    SKELETAL_GRASP("Skeletal Grasp", WidgetInfoPlus.SPELL_SKELETAL_GRASP, 56, 3, 1, 0),
    UNDEAD_GRASP("Undead Grasp", WidgetInfoPlus.SPELL_UNDEAD_GRASP, 79, 3, 1, 0),
    INFERIOR_DEMONBANE("Inferior Demonbane", WidgetInfoPlus.SPELL_INFERIOR_DEMONBANE, 44, 3, 1, 0),
    SUPERIOR_DEMONBANE("Superior Demonbane", WidgetInfoPlus.SPELL_SUPERIOR_DEMONBANE, 62, 3, 1, 0),
    DARK_DEMONBANE("Dark Demonbane", WidgetInfoPlus.SPELL_DARK_DEMONBANE, 82, 3, 1, 0),
    LESSER_CORRUPTION("Lesser Corruption", WidgetInfoPlus.SPELL_LESSER_CORRUPTION, 64, 3, 0, 1),
    GREATER_CORRUPTION("Greater Corruption", WidgetInfoPlus.SPELL_GREATER_CORRUPTION, 85, 3, 0, 1),
    RESURRECT_LESSER_GHOST("Resurrect Lesser Ghost", WidgetInfoPlus.SPELL_RESURRECT_LESSER_GHOST, 38, 3, 0, 1),
    RESURRECT_LESSER_SKELETON("Resurrect Lesser Skeleton", WidgetInfoPlus.SPELL_RESURRECT_LESSER_SKELETON, 38, 3, 0, 1),
    RESURRECT_LESSER_ZOMBIE("Resurrect Lesser Zombie", WidgetInfoPlus.SPELL_RESURRECT_LESSER_ZOMBIE, 38, 3, 0, 1),
    RESURRECT_SUPERIOR_GHOST("Resurrect Superior Ghost", WidgetInfoPlus.SPELL_RESURRECT_SUPERIOR_GHOST, 57, 3, 0, 1),
    RESURRECT_SUPERIOR_SKELETON("Resurrect Superior Skeleton", WidgetInfoPlus.SPELL_RESURRECT_SUPERIOR_SKELETON, 57, 3, 0, 1),
    RESURRECT_SUPERIOR_ZOMBIE("Resurrect Superior Zombie", WidgetInfoPlus.SPELL_RESURRECT_SUPERIOR_ZOMBIE, 57, 3, 0, 1),
    RESURRECT_GREATER_GHOST("Resurrect Greater Ghost", WidgetInfoPlus.SPELL_RESURRECT_GREATER_GHOST, 76, 3, 0, 1),
    RESURRECT_GREATER_SKELETON("Resurrect Greater Skeleton", WidgetInfoPlus.SPELL_RESURRECT_GREATER_SKELETON, 76, 3, 0, 1),
    RESURRECT_GREATER_ZOMBIE("Resurrect Greater Zombie", WidgetInfoPlus.SPELL_RESURRECT_GREATER_ZOMBIE, 76, 3, 0, 1),
    DARK_LURE("Dark Lure", WidgetInfoPlus.SPELL_DARK_LURE, 50, 3, 1, 0),
    MARK_OF_DARKNESS("Mark of Darkness", WidgetInfoPlus.SPELL_MARK_OF_DARKNESS, 59, 3, 0, 1),
    WARD_OF_ARCEUUS("Ward of Arceuus", WidgetInfoPlus.SPELL_WARD_OF_ARCEUUS, 73, 3, 0, 1),

    //Arceuus Spellbook Utility Spells
    BASIC_REANIMATION("Basic Reanimation", WidgetInfoPlus.SPELL_BASIC_REANIMATION, 16, 3, 7, 0),
    ADEPT_REANIMATION("Adept Reanimation", WidgetInfoPlus.SPELL_ADEPT_REANIMATION, 41, 3, 7, 0),
    EXPERT_REANIMATION("Expert Reanimation", WidgetInfoPlus.SPELL_EXPERT_REANIMATION, 72, 3, 7, 0),
    MASTER_REANIMATION("Master Reanimation", WidgetInfoPlus.SPELL_MASTER_REANIMATION, 90, 3, 7, 0),
    DEMONIC_OFFERING("Demonic Offering", WidgetInfoPlus.SPELL_DEMONIC_OFFERING, 84, 3, 0, 1),
    SINISTER_OFFERING("Sinister Offering", WidgetInfoPlus.SPELL_SINISTER_OFFERING, 92, 3, 0, 1),
    SHADOW_VEIL("Shadow Veil", WidgetInfoPlus.SPELL_SHADOW_VEIL, 47, 3, 0, 1),
    VILE_VIGOUR("Vile Vigour", WidgetInfoPlus.SPELL_VILE_VIGOUR, 66, 3, 0, 1),
    DEGRIME("Degrime", WidgetInfoPlus.SPELL_DEGRIME, 70, 3, 0, 1),
    RESURRECT_CROPS("Resurrect Crops", WidgetInfoPlus.SPELL_RESURRECT_CROPS, 78, 3, 5, 0),
    DEATH_CHARGE("Death Charge", WidgetInfoPlus.SPELL_DEATH_CHARGE, 80, 3, 0, 1);

    private final String name;
    private final WidgetInfoPlus spell;
    private final int level;
    /*
        According to the SPELLBOOK_VARBIT
        0 = Normal
        1 = Ancients
        2 = Lunar
        3 = Arceuus
     */
    private final int spellbook;
    /*
        Who you can cast the spell on
        0 = No target
        1 = NPC and player targeting
        2 = NPC target only
        3 = Player target only
        4 = Ground item target only
        5 = Game object target only
        6 = Inventory widget target only
        7 = Ground item and inventory widget targeting
     */
    private final int targetType;
    /*
        Menu Entry ID for the spell, some spells have additional options, i.e. Varrock/GE teleport or NPC Contact. This number refers to the menu index it's at.
        0 = Targeting spell, so no id value
        1 = Generally means the left click option of the menu entry
        2 = 2nd menu entry in the right click menu
        3 = and so on.
     */
    private final int menuEntryId;

    @Override
    public String toString()
    {
        return getName();
    }
}
