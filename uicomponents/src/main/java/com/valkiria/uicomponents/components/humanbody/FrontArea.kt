package com.valkiria.uicomponents.components.humanbody

import com.valkiria.uicomponents.R

enum class FrontArea(val image: Int) {
    HEAD(R.drawable.ic_front_head_human_body),
    NECK(R.drawable.ic_neck_human_body_front),
    CHEST(R.drawable.ic_chest_human_body),
    RIGHT_ARM(R.drawable.ic_right_arm_human_body),
    LEFT_ARM(R.drawable.ic_left_arm_human_body),
    RIGHT_FOREARM(R.drawable.ic_right_forearm_human_body),
    LEFT_FOREARM(R.drawable.ic_left_forearm_human_body),
    ABDOMEN(R.drawable.ic_abdomen_human_body),
    HIPS(R.drawable.ic_hips_human_body),
    RIGHT_HAND(R.drawable.ic_right_hand_human_doy),
    LEFT_HAND(R.drawable.ic_left_hand_human_body),
    RIGHT_THIGH(R.drawable.ic_right_thigh_human_body),
    LEFT_THIGH(R.drawable.ic_right_thigh_human_body), // TODO: change image
    RIGHT_KNEE(R.drawable.ic_right_knee_human_body),
    LEFT_KNEE(R.drawable.ic_left_knee_human_body),
    RIGHT_LEG(R.drawable.ic_right_leg_human_body),
    LEFT_LEG(R.drawable.ic_left_leg_human_body),
    RIGHT_FOOT(R.drawable.ic_right_foot_human_body),
    LEFT_FOOT(R.drawable.ic_left_foot_human_body),
    NONE(R.drawable.ic_front_human_body_background);

    companion object {

        private fun getHeadArea(width: Int, height: Int): List<Point> {
            val headArea = listOf(
                Point(600f, 275f),
                Point(615f, 162f),
                Point(724f, 83f),
                Point(841f, 162f),
                Point(864f, 275f),
                Point(815f, 366f),
                Point(728f, 433f),
                Point(645f, 366f),
                Point(600f, 275f)
            )

            return headArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getNeckArea(width: Int, height: Int): List<Point> {
            val neckArea = listOf(
                Point(656f, 411f),
                Point(728f, 460f),
                Point(803f, 422f),
                Point(826f, 501f),
                Point(928f, 561f),
                Point(735f, 580f),
                Point(521f, 561f),
                Point(630f, 505f),
                Point(656f, 411f)
            )

            return neckArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getChestArea(width: Int, height: Int): List<Point> {
            val chestArea = listOf(
                Point(566f, 607f),
                Point(728f, 622f),
                Point(890f, 603f),
                Point(939f, 739f),
                Point(909f, 886f),
                Point(732f, 863f),
                Point(558f, 886f),
                Point(528f, 742f),
                Point(566f, 607f)
            )

            return chestArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightArmArea(width: Int, height: Int): List<Point> {
            val rightArmArea = listOf(
                Point(325f, 1010f),
                Point(423f, 626f),
                Point(483f, 573f),
                Point(543f, 607f),
                Point(408f, 1055f),
                Point(325f, 1010f)
            )

            return rightArmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftArmArea(width: Int, height: Int): List<Point> {
            val leftArmArea = listOf(
                Point(1115f, 1010f),
                Point(1017f, 626f),
                Point(957f, 573f),
                Point(897f, 607f),
                Point(1032f, 1055f),
                Point(1115f, 1010f)
            )

            return leftArmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightForearmArea(width: Int, height: Int): List<Point> {
            val rightForearmArea = listOf(
                Point(309f, 1051f),
                Point(400f, 1085f),
                Point(291f, 1398f),
                Point(234f, 1383f),
                Point(260f, 1195f),
                Point(309f, 1051f)
            )

            return rightForearmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftForearmArea(width: Int, height: Int): List<Point> {
            val leftForearmArea = listOf(
                Point(1131f, 1051f),
                Point(1040f, 1085f),
                Point(1149f, 1398f),
                Point(1206f, 1383f),
                Point(1180f, 1195f),
                Point(1131f, 1051f)
            )

            return leftForearmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getAbdomenArea(width: Int, height: Int): List<Point> {
            val abdomenArea = listOf(
                Point(566f, 923f),
                Point(717f, 889f),
                Point(898f, 916f),
                Point(875f, 1048f),
                Point(901f, 1168f),
                Point(747f, 1232f),
                Point(566f, 1179f),
                Point(588f, 1097f),
                Point(566f, 923f)
            )

            return abdomenArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getHipsArea(width: Int, height: Int): List<Point> {
            val hipsArea = listOf(
                Point(562f, 1228f),
                Point(732f, 1259f),
                Point(898f, 1236f),
                Point(969f, 1357f),
                Point(875f, 1372f),
                Point(735f, 1413f),
                Point(588f, 1379f),
                Point(502f, 1368f),
                Point(562f, 1228f)
            )

            return hipsArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightHandArea(width: Int, height: Int): List<Point> {
            val rightHandArea = listOf(
                Point(227f, 1407f),
                Point(283f, 1413f),
                Point(313f, 1571f),
                Point(260f, 1511f),
                Point(230f, 1553f),
                Point(238f, 1684f),
                Point(189f, 1609f),
                Point(196f, 1519f),
                Point(227f, 1407f)
            )

            return rightHandArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftHandArea(width: Int, height: Int): List<Point> {
            val leftHandArea = listOf(
                Point(1229f, 1402f),
                Point(1271f, 1537f),
                Point(1267f, 1617f),
                Point(1233f, 1688f),
                Point(1222f, 1639f),
                Point(1226f, 1560f),
                Point(1192f, 1519f),
                Point(1154f, 1579f),
                Point(1173f, 1413f),
                Point(1229f, 1402f)
            )

            return leftHandArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightThighArea(width: Int, height: Int): List<Point> {
            val rightThighArea = listOf(
                Point(472f, 1537f),
                Point(513f, 1424f),
                Point(671f, 1436f),
                Point(698f, 1545f),
                Point(653f, 1959f),
                Point(536f, 1963f),
                Point(472f, 1537f)
            )

            return rightThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftThighArea(width: Int, height: Int): List<Point> {
            val leftThighArea = listOf(
                Point(758f, 1537f),
                Point(781f, 1439f),
                Point(939f, 1417f),
                Point(988f, 1545f),
                Point(920f, 1967f),
                Point(807f, 1959f),
                Point(758f, 1537f)
            )

            return leftThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightKneeArea(width: Int, height: Int): List<Point> {
            val rightKneeArea = listOf(
                Point(524f, 1997f),
                Point(675f, 1993f),
                Point(683f, 2118f),
                Point(523f, 2118f),
                Point(524f, 1997f)
            )

            return rightKneeArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftKneeArea(width: Int, height: Int): List<Point> {
            val leftKneeArea = listOf(
                Point(788f, 1997f),
                Point(935f, 1993f),
                Point(928f, 2118f),
                Point(781f, 2118f),
                Point(788f, 1997f),
            )

            return leftKneeArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightLegArea(width: Int, height: Int): List<Point> {
            val rightLegArea = listOf(
                Point(536f, 2144f),
                Point(679f, 2144f),
                Point(664f, 2446f),
                Point(679f, 2675f),
                Point(577f, 2675f),
                Point(573f, 2461f),
                Point(521f, 2246f),
                Point(536f, 2144f)
            )

            return rightLegArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftLegArea(width: Int, height: Int): List<Point> {
            val leftLegArea = listOf(
                Point(781f, 2144f),
                Point(931f, 2144f),
                Point(939f, 2227f),
                Point(898f, 2442f),
                Point(882f, 2679f),
                Point(784f, 2672f),
                Point(796f, 2430f),
                Point(781f, 2144f)
            )

            return leftLegArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightFootArea(width: Int, height: Int): List<Point> {
            val rightFootArea = listOf(
                Point(573f, 2694f),
                Point(671f, 2675f),
                Point(683f, 2800f),
                Point(551f, 2800f),
                Point(573f, 2694f)
            )

            return rightFootArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftFootArea(width: Int, height: Int): List<Point> {
            val leftFootArea = listOf(
                Point(784f, 2698f),
                Point(879f, 2698f),
                Point(909f, 2800f),
                Point(773f, 2800f),
                Point(784f, 2698f)
            )

            return leftFootArea.map { it.toProportionalPoint(width, height) }
        }

        fun fromPosition(x: Float, y: Float, width: Int, height: Int): FrontArea = when {
            getHeadArea(width, height).isInArea(x, y) -> HEAD
            getNeckArea(width, height).isInArea(x, y) -> NECK
            getChestArea(width, height).isInArea(x, y) -> CHEST
            getRightArmArea(width, height).isInArea(x, y) -> RIGHT_ARM
            getLeftArmArea(width, height).isInArea(x, y) -> LEFT_ARM
            getRightForearmArea(width, height).isInArea(x, y) -> RIGHT_FOREARM
            getLeftForearmArea(width, height).isInArea(x, y) -> LEFT_FOREARM
            getAbdomenArea(width, height).isInArea(x, y) -> ABDOMEN
            getHipsArea(width, height).isInArea(x, y) -> HIPS
            getRightHandArea(width, height).isInArea(x, y) -> RIGHT_HAND
            getLeftHandArea(width, height).isInArea(x, y) -> LEFT_HAND
            getRightThighArea(width, height).isInArea(x, y) -> RIGHT_THIGH
            getLeftThighArea(width, height).isInArea(x, y) -> LEFT_THIGH
            getRightKneeArea(width, height).isInArea(x, y) -> RIGHT_KNEE
            getLeftKneeArea(width, height).isInArea(x, y) -> LEFT_KNEE
            getRightLegArea(width, height).isInArea(x, y) -> RIGHT_LEG
            getLeftLegArea(width, height).isInArea(x, y) -> LEFT_LEG
            getRightFootArea(width, height).isInArea(x, y) -> RIGHT_FOOT
            getLeftFootArea(width, height).isInArea(x, y) -> LEFT_FOOT
            else -> NONE
        }
    }
}
