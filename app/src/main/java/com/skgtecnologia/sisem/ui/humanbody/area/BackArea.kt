package com.skgtecnologia.sisem.ui.humanbody.area

import com.skgtecnologia.sisem.R

@Suppress("ComplexMethod", "MagicNumber", "TooManyFunctions")
enum class BackArea(val image: Int) {
    HEAD(R.drawable.img_back_head_human_body),
    NECK(R.drawable.img_neck_human_body_front),
    BACK(R.drawable.img_chest_human_body),
    LOWER_BACK(R.drawable.img_lower_back_human_body),
    RIGHT_ARM(R.drawable.img_right_arm_human_body),
    LEFT_ARM(R.drawable.img_left_arm_human_body),
    RIGHT_FOREARM(R.drawable.img_right_forearm_human_body),
    LEFT_FOREARM(R.drawable.img_left_forearm_human_body),
    RIGHT_HAND(R.drawable.img_right_hand_human_doy),
    LEFT_HAND(R.drawable.img_left_hand_human_body),
    LEFT_BUTTOCK(R.drawable.img_left_buttock_human_body),
    RIGHT_BUTTOCK(R.drawable.img_right_buttock_human_body),
    RIGHT_THIGH(R.drawable.img_right_back_thigh_human_body),
    LEFT_THIGH(R.drawable.img_left_back_thigh_human_body),
    RIGHT_KNEE(R.drawable.img_right_knee_human_body),
    LEFT_KNEE(R.drawable.img_left_knee_human_body),
    RIGHT_LEG(R.drawable.img_right_leg_human_body),
    LEFT_LEG(R.drawable.img_left_leg_human_body),
    RIGHT_FOOT(R.drawable.img_right_foot_human_body),
    LEFT_FOOT(R.drawable.img_left_foot_human_body),
    NONE(R.drawable.img_front_human_body);

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

        private fun getBackArea(width: Int, height: Int): List<Point> {
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

        private fun getLowerBackArea(width: Int, height: Int): List<Point> {
            val lowerBackArea = listOf(
                Point(566f, 935f),
                Point(720f, 893f),
                Point(913f, 923f),
                Point(901f, 1074f),
                Point(920f, 1228f),
                Point(739f, 1296f),
                Point(558f, 1244f),
                Point(588f, 1115f),
                Point(566f, 935f)
            )

            return lowerBackArea.map { it.toProportionalPoint(width, height) }
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

        private fun getRightButtockArea(width: Int, height: Int): List<Point> {
            val leftButtockArea = listOf(
                Point(547f, 1277f),
                Point(694f, 1315f),
                Point(724f, 1424f),
                Point(702f, 1530f),
                Point(882f, 1492f),
                Point(585f, 1579f),
                Point(502f, 1541f),
                Point(487f, 1439f),
                Point(547f, 1277f)
            )

            return leftButtockArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftButtockArea(width: Int, height: Int): List<Point> {
            val rightButtockArea = listOf(
                Point(758f, 1311f),
                Point(913f, 1277f),
                Point(980f, 1515f),
                Point(886f, 1571f),
                Point(777f, 1541f),
                Point(739f, 1458f),
                Point(758f, 1311f)
            )

            return rightButtockArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightThighArea(width: Int, height: Int): List<Point> {
            val rightThighArea = listOf(
                Point(487f, 1568f),
                Point(581f, 1598f),
                Point(698f, 1571f),
                Point(656f, 1959f),
                Point(555f, 1959f),
                Point(487f, 1568f)
            )

            return rightThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftThighArea(width: Int, height: Int): List<Point> {
            val leftThighArea = listOf(
                Point(758f, 1564f),
                Point(882f, 1601f),
                Point(984f, 1564f),
                Point(920f, 1959f),
                Point(811f, 1959f),
                Point(758f, 1564f)
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

        fun fromPosition(x: Float, y: Float, width: Int, height: Int): BackArea = when {
            getHeadArea(width, height).isInArea(x, y) -> HEAD
            getNeckArea(width, height).isInArea(x, y) -> NECK
            getBackArea(width, height).isInArea(x, y) -> BACK
            getLowerBackArea(width, height).isInArea(x, y) -> LOWER_BACK
            getRightArmArea(width, height).isInArea(x, y) -> RIGHT_ARM
            getLeftArmArea(width, height).isInArea(x, y) -> LEFT_ARM
            getRightForearmArea(width, height).isInArea(x, y) -> RIGHT_FOREARM
            getLeftForearmArea(width, height).isInArea(x, y) -> LEFT_FOREARM
            getRightHandArea(width, height).isInArea(x, y) -> RIGHT_HAND
            getLeftHandArea(width, height).isInArea(x, y) -> LEFT_HAND
            getRightButtockArea(width, height).isInArea(x, y) -> RIGHT_BUTTOCK
            getLeftButtockArea(width, height).isInArea(x, y) -> LEFT_BUTTOCK
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
