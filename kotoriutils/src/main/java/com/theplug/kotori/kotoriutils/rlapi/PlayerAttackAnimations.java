package com.theplug.kotori.kotoriutils.rlapi;

/*
 * Copyright (c) 2021, Matsyir <https://github.com/matsyir>
 * Copyright (c) 2020, Mazhar <https://twitter.com/maz_rs>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import net.runelite.api.HeadIcon;
import net.runelite.api.gameval.SpriteID;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum PlayerAttackAnimations
{
    // MELEE
    MELEE_VIGGORAS_CHAINMACE(245, AttackStyle.CRUSH),
    MELEE_DAGGER_STAB(376, AttackStyle.STAB),
    MELEE_DAGGER_SLASH(377, AttackStyle.SLASH), // tested w/ dds
    MELEE_SPEAR_STAB(381, AttackStyle.STAB), // tested w/ zammy hasta
    MELEE_SWORD_STAB(386, AttackStyle.STAB), // tested w/ dragon sword, obby sword, d long
    MELEE_SCIM_SLASH(390, AttackStyle.SLASH), // tested w/ rune & dragon scim, d sword, VLS, obby sword

    MELEE_LANCE_STAB(8288, AttackStyle.STAB),
    MELEE_LANCE_CRUSH(8290, AttackStyle.CRUSH),
    MELEE_LANCE_SLASH(8289, AttackStyle.SLASH),

    MELEE_FANG_STAB(9471, AttackStyle.STAB), // tested w/ fang
    MELEE_FANG_SPEC(6118, AttackStyle.STAB, true), // tested w/ fang spec

    MELEE_GENERIC_SLASH(393, AttackStyle.SLASH), // tested w/ zuriel's staff, d long slash, dclaws regular slash
    MELEE_STAFF_CRUSH(0, AttackStyle.SLASH), // 393 previously, save name to support old fights but no longer track

    MELEE_BATTLEAXE_SLASH(395, AttackStyle.SLASH), // tested w/ rune baxe
    MELEE_MACE_STAB(400, AttackStyle.STAB), // tested w/ d mace
    MELEE_BATTLEAXE_CRUSH(401, AttackStyle.CRUSH), // tested w/ rune baxe, dwh & statius warhammer animation, d mace
    MELEE_2H_CRUSH(406, AttackStyle.CRUSH), // tested w/ rune & dragon 2h
    MELEE_2H_SLASH(407, AttackStyle.SLASH), // tested w/ rune & dragon 2h
    MELEE_STAFF_CRUSH_2(414, AttackStyle.CRUSH), // tested w/ ancient staff, 3rd age wand
    MELEE_STAFF_CRUSH_3(419, AttackStyle.CRUSH), // Common staff crush. Air/fire/etc staves, smoke battlestaff, SOTD/SOL crush, zammy hasta crush
    MELEE_PUNCH(422, AttackStyle.CRUSH),
    MELEE_KICK(423, AttackStyle.CRUSH),
    MELEE_STAFF_STAB(428, AttackStyle.STAB), // tested w/ SOTD/SOL jab, vesta's spear stab, c hally
    MELEE_SPEAR_CRUSH(429, AttackStyle.CRUSH), // tested w/ vesta's spear
    MELEE_STAFF_SLASH(440, AttackStyle.SLASH), // tested w/ SOTD/SOL slash, zammy hasta slash, vesta's spear slash, c hally
    MELEE_DLONG_SPEC(1058, AttackStyle.SLASH, true), // tested w/ d long spec, also thammaron's sceptre crush (????)...
    MELEE_DRAGON_MACE_SPEC(1060, AttackStyle.CRUSH, true),
    MELEE_DRAGON_DAGGER_SPEC(1062, AttackStyle.STAB, true),
    MELEE_DRAGON_WARHAMMER_SPEC(1378, AttackStyle.CRUSH, true), // tested w/ dwh, statius warhammer spec
    MELEE_ABYSSAL_WHIP(1658, AttackStyle.SLASH), // tested w/ whip, tent whip
    MELEE_GRANITE_MAUL(1665, AttackStyle.CRUSH), // tested w/ normal gmaul, ornate maul
    MELEE_GRANITE_MAUL_SPEC(1667, AttackStyle.CRUSH, true), // tested w/ normal gmaul, ornate maul
    MELEE_DHAROKS_GREATAXE_CRUSH(2066, AttackStyle.CRUSH),
    MELEE_DHAROKS_GREATAXE_SLASH(2067, AttackStyle.SLASH),
    MELEE_AHRIMS_STAFF_CRUSH(2078, AttackStyle.CRUSH),
    MELEE_OBBY_MAUL_CRUSH(2661, AttackStyle.CRUSH),
    MELEE_ABYSSAL_DAGGER_STAB(3297, AttackStyle.STAB),
    MELEE_ABYSSAL_BLUDGEON_CRUSH(3298, AttackStyle.CRUSH),
    MELEE_LEAF_BLADED_BATTLEAXE_CRUSH(3852, AttackStyle.CRUSH),
    MELEE_INQUISITORS_MACE(4503, AttackStyle.CRUSH),
    MELEE_BARRELCHEST_ANCHOR_CRUSH(5865, AttackStyle.CRUSH),
    MELEE_LEAF_BLADED_BATTLEAXE_SLASH(7004, AttackStyle.SLASH),
    MELEE_GODSWORD_SLASH(7045, AttackStyle.SLASH), // tested w/ AGS, BGS, ZGS, SGS, AGS(or) sara sword
    MELEE_GODSWORD_CRUSH(7054, AttackStyle.CRUSH), // tested w/ AGS, BGS, ZGS, SGS, sara sword
    MELEE_GODSWORD_DEFENSIVE(7055, AttackStyle.SLASH), // tested w/ BGS
    MELEE_DRAGON_CLAWS_SPEC(7514, AttackStyle.SLASH, true),
    MELEE_VLS_SPEC(7515, AttackStyle.SLASH, true), // both VLS and dragon sword spec
    MELEE_ELDER_MAUL(7516, AttackStyle.CRUSH),
    MELEE_ZAMORAK_GODSWORD_SPEC(7638, AttackStyle.SLASH, true), // tested zgs spec
    MELEE_ZAMORAK_GODSWORD_OR_SPEC(7639, AttackStyle.SLASH, true), // UNTESTED, assumed due to ags(or)
    MELEE_SARADOMIN_GODSWORD_SPEC(7640, AttackStyle.SLASH, true), // tested sgs spec
    MELEE_SARADOMIN_GODSWORD_OR_SPEC(7641, AttackStyle.SLASH, true), // UNTESTED, assumed due to ags(or)
    MELEE_BANDOS_GODSWORD_SPEC(7642, AttackStyle.SLASH, true), // tested bgs spec
    MELEE_BANDOS_GODSWORD_OR_SPEC(7643, AttackStyle.SLASH, true), // UNTESTED, assumed due to ags(or)
    MELEE_ARMADYL_GODSWORD_SPEC(7644, AttackStyle.SLASH, true), // tested ags spec
    MELEE_ARMADYL_GODSWORD_OR_SPEC(7645, AttackStyle.SLASH, true), // tested ags(or) spec
    MELEE_SCYTHE(8056, AttackStyle.SLASH), // tested w/ all scythe styles (so could be crush, but unlikely)
    MELEE_GHAZI_RAPIER_STAB(8145, AttackStyle.STAB), // rapier slash is 390, basic slash animation. Also VLS stab.
    MELEE_ANCIENT_GODSWORD_SPEC(9171, AttackStyle.SLASH, true),
    MELEE_CRYSTAL_HALBERD_SPEC(1203, AttackStyle.SLASH, true),
    MELEE_SOULREAPER_AXE(10172, AttackStyle.SLASH, true),
    MELEE_SOULREAPER_AXE_SPEC(10173, AttackStyle.SLASH, true),
    MELEE_GUTHANS_LUNGE(2080, AttackStyle.STAB),
    MELEE_GUTHANS_SWIPE(2081, AttackStyle.STAB),
    MELEE_GUTHANS_POUNDMA(2082, AttackStyle.CRUSH),
    MELEE_TORAG_HAMMERS(2068, AttackStyle.CRUSH),
    MELEE_VERACS_FLAIL(2062, AttackStyle.STAB),


    // RANGED
    RANGED_CHINCHOMPA(7618, AttackStyle.RANGED),
    RANGED_SHORTBOW(426, AttackStyle.RANGED), // Confirmed same w/ 3 types of arrows, w/ maple, magic, & hunter's shortbow, craw's bow, dbow, dbow spec
    RANGED_RUNE_KNIFE_PVP(929, AttackStyle.RANGED), // 1 tick animation, has 1 tick delay between attacks. likely same for all knives. Same for morrigan's javelins, both spec & normal attack.
    RANGED_MAGIC_SHORTBOW_SPEC(1074, AttackStyle.RANGED, true),
    RANGED_CROSSBOW_PVP(4230, AttackStyle.RANGED), // Tested RCB & ACB w/ dragonstone bolts (e) & diamond bolts (e)
    RANGED_BLOWPIPE(5061, AttackStyle.RANGED), // tested in PvP with all styles. Has 1 tick delay between animations in pvp.
    RANGED_DARTS(6600, AttackStyle.RANGED), // tested w/ addy darts. Seems to be constant animation but sometimes stalls and doesn't animate
    RANGED_BALLISTA(7218, AttackStyle.RANGED), // Tested w/ dragon javelins.
    RANGED_DRAGON_THROWNAXE_SPEC(7521, AttackStyle.RANGED, true),
    RANGED_RUNE_CROSSBOW(7552, AttackStyle.RANGED),
    RANGED_RUNE_CROSSBOW_OR(9206, AttackStyle.RANGED),
    RANGED_BALLISTA_2(7555, AttackStyle.RANGED), // tested w/ light & heavy ballista, dragon & iron javelins.
    RANGED_RUNE_KNIFE(7617, AttackStyle.RANGED), // 1 tick animation, has 1 tick delay between attacks. Also d thrownaxe
    RANGED_DRAGON_KNIFE(8194, AttackStyle.RANGED),
    RANGED_DRAGON_KNIFE_SPEC(8291, AttackStyle.RANGED, true),
    RANGED_DRAGON_KNIFE_POISONED(8195, AttackStyle.RANGED), // tested w/ d knife p++
    RANGED_DRAGON_KNIFE_POISONED_SPEC(8292, AttackStyle.RANGED, true),
    RANGED_ZARYTE_CROSSBOW(9168, AttackStyle.RANGED),
    RANGED_ZARYTE_CROSSBOW_PVP(9166, AttackStyle.RANGED),
    RANGED_BLAZING_BLOWPIPE(10656, AttackStyle.RANGED),
    RANGED_VENATOR_BOW(9858, AttackStyle.RANGED),
    RANGED_KARIL_CROSSBOW(2075, AttackStyle.RANGED),

    // MAGIC
    MAGIC_STANDARD_BIND(710, AttackStyle.MAGIC), // tested w/ bind, snare, entangle
    MAGIC_STANDARD_STRIKE_BOLT_BLAST(711, AttackStyle.MAGIC), // tested w/ bolt
    MAGIC_STANDARD_BIND_STAFF(1161, AttackStyle.MAGIC), // tested w/ bind, snare, entangle, various staves
    MAGIC_STANDARD_STRIKE_BOLT_BLAST_STAFF(1162, AttackStyle.MAGIC), // strike, bolt and blast (tested all spells, different weapons)
    MAGIC_STANDARD_WAVE_STAFF(1167, AttackStyle.MAGIC), // tested many staves, also used for powered staves like Trident/Swamp/Sang
    MAGIC_STANDARD_SURGE_STAFF(7855, AttackStyle.MAGIC), // tested many staves
    MAGIC_ANCIENT_SINGLE_TARGET(1978, AttackStyle.MAGIC), // Rush & Blitz animations (tested all 8, different weapons)
    MAGIC_ANCIENT_MULTI_TARGET(1979, AttackStyle.MAGIC), // Burst & Barrage animations (tested all 8, different weapons)
    MAGIC_VOLATILE_NIGHTMARE_STAFF_SPEC(8532, AttackStyle.MAGIC),
    MAGIC_TUMEKENS_SHADOW(9493, AttackStyle.MAGIC),
    MAGIC_WARPED_SCEPTRE(10501, AttackStyle.MAGIC),
    MAGIC_ACCURSED_SCEPTRE_SPEC(9961, AttackStyle.MAGIC);

    private static final Map<Integer, PlayerAttackAnimations> DATA;

    private final int animationId;
    private final boolean isSpecial;
    private final AttackStyle attackStyle;

    PlayerAttackAnimations(int animationId, AttackStyle attackStyle)
    {
        if (attackStyle == null)
        {
            throw new InvalidParameterException("Attack Style must be valid for AnimationData");
        }
        this.animationId = animationId;
        this.attackStyle = attackStyle;
        this.isSpecial = false;
    }

    PlayerAttackAnimations(int animationId, AttackStyle attackStyle, boolean isSpecial)
    {
        if (attackStyle == null)
        {
            throw new InvalidParameterException("Attack Style must be valid for AnimationData");
        }
        this.animationId = animationId;
        this.attackStyle = attackStyle;
        this.isSpecial = isSpecial;
    }

    static
    {
        ImmutableMap.Builder<Integer, PlayerAttackAnimations> builder = new ImmutableMap.Builder<>();

        for (PlayerAttackAnimations data : values())
        {
            // allow to skip animation detection by using 0 or less as the animation id.
            if (data.animationId <= 0) { continue; }
            builder.put(data.animationId, data);
        }

        DATA = builder.build();
    }

    public static PlayerAttackAnimations fromId(int animationId)
    {
        return DATA.get(animationId);
    }

    @Override
    public String toString()
    {
        String[] words = super.toString().toLowerCase().split("_");
        Arrays.stream(words)
                .map(StringUtils::capitalize).collect(Collectors.toList()).toArray(words);

        return String.join(" ", words);
    }


    public enum AttackStyle
    {
        STAB(HeadIcon.MELEE, SpriteID.Combaticons.SWORD_STAB),
        SLASH(HeadIcon.MELEE, SpriteID.Combaticons.SWORD_SLASH),
        CRUSH(HeadIcon.MELEE, SpriteID.Combaticons2.HAMMER_POUND),
        RANGED(HeadIcon.RANGED, SpriteID.Staticons.RANGED),
        MAGIC(HeadIcon.MAGIC, SpriteID.Staticons.MAGIC);

        static final AttackStyle[] MELEE_STYLES = {STAB, SLASH, CRUSH};

        @Getter
        private final HeadIcon protection;

        @Getter
        private final int styleSpriteId;

        AttackStyle(HeadIcon protection, int styleSpriteId)
        {
            this.protection = protection;
            this.styleSpriteId = styleSpriteId;
        }

        public boolean isMelee()
        {
            return ArrayUtils.contains(AttackStyle.MELEE_STYLES, this);
        }
        
        @Override
        public String toString()
        {
            return StringUtils.capitalize(super.toString().toLowerCase());
        }
    }
}
