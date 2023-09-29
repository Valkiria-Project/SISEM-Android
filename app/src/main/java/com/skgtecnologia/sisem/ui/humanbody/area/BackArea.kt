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
                Point(523f, 271f),
                Point(541f, 178f),
                Point(650f, 92f),
                Point(767f, 161f),
                Point(801f, 281f),
                Point(733f, 384f),
                Point(582f, 384f),
                Point(523f, 271f)
            )

            return headArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getNeckArea(width: Int, height: Int): List<Point> {
            val neckArea = listOf(
                Point(602f, 428f),
                Point(719f, 428f),
                Point(736f, 494f),
                Point(825f, 569f),
                Point(664f, 593f),
                Point(493f, 576f),
                Point(592f, 490f),
                Point(602f, 428f)
            )

            return neckArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getBackArea(width: Int, height: Int): List<Point> {
            val chestArea = listOf(
                Point(503f, 610f),
                Point(661f, 620f),
                Point(825f, 610f),
                Point(873f, 740f),
                Point(842f, 891f),
                Point(664f, 867f),
                Point(493f, 888f),
                Point(462f, 734f),
                Point(503f, 610f)
            )

            return chestArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLowerBackArea(width: Int, height: Int): List<Point> {
            val lowerBackArea = listOf(
                Point(493f, 939f),
                Point(657f, 891f),
                Point(842f, 915f),
                Point(832f, 1148f),
                Point(853f, 1227f),
                Point(664f, 1292f),
                Point(486f, 1238f),
                Point(517f, 1138f),
                Point(493f, 939f)
            )

            return lowerBackArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightArmArea(width: Int, height: Int): List<Point> {
            val rightArmArea = listOf(
                Point(362f, 593f),
                Point(479f, 620f),
                Point(338f, 1059f),
                Point(253f, 1022f),
                Point(362f, 593f)
            )

            return rightArmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftArmArea(width: Int, height: Int): List<Point> {
            val leftArmArea = listOf(
                Point(945f, 596f),
                Point(863f, 600f),
                Point(990f, 1059f),
                Point(1069f, 1022f),
                Point(945f, 596f)
            )

            return leftArmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightForearmArea(width: Int, height: Int): List<Point> {
            val rightForearmArea = listOf(
                Point(239f, 1049f),
                Point(328f, 1087f),
                Point(222f, 1399f),
                Point(163f, 1385f),
                Point(239f, 1049f)
            )

            return rightForearmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftForearmArea(width: Int, height: Int): List<Point> {
            val leftForearmArea = listOf(
                Point(993f, 1090f),
                Point(1079f, 1052f),
                Point(1154f, 1388f),
                Point(1103f, 1399f),
                Point(1131f, 1051f)
            )

            return leftForearmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightHandArea(width: Int, height: Int): List<Point> {
            val rightHandArea = listOf(
                Point(163f, 1406f),
                Point(122f, 1608f),
                Point(163f, 1680f),
                Point(253f, 1567f),
                Point(218f, 1409f),
                Point(163f, 1406f)
            )

            return rightHandArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftHandArea(width: Int, height: Int): List<Point> {
            val leftHandArea = listOf(
                Point(1158f, 1406f),
                Point(1199f, 1608f),
                Point(1154f, 1687f),
                Point(1072f, 1563f),
                Point(1099f, 1416f),
                Point(1158f, 1406f)
            )

            return leftHandArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightButtockArea(width: Int, height: Int): List<Point> {
            val leftButtockArea = listOf(
                Point(486f, 1268f),
                Point(640f, 1316f),
                Point(650f, 1433f),
                Point(637f, 1512f),
                Point(506f, 1567f),
                Point(414f, 1502f),
                Point(486f, 1268f)
            )

            return leftButtockArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftButtockArea(width: Int, height: Int): List<Point> {
            val rightButtockArea = listOf(
                Point(691f, 1310f),
                Point(846f, 1268f),
                Point(914f, 1502f),
                Point(808f, 1567f),
                Point(705f, 1532f),
                Point(674f, 1443f),
                Point(691f, 1310f)
            )

            return rightButtockArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightThighArea(width: Int, height: Int): List<Point> {
            val rightThighArea = listOf(
                Point(417f, 1563f),
                Point(506f, 1587f),
                Point(630f, 1563f),
                Point(585f, 1954f),
                Point(486f, 1954f),
                Point(417f, 1563f)
            )

            return rightThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftThighArea(width: Int, height: Int): List<Point> {
            val leftThighArea = listOf(
                Point(685f, 1556f),
                Point(808f, 1587f),
                Point(911f, 1556f),
                Point(842f, 1954f),
                Point(743f, 1954f),
                Point(685f, 1556f)
            )

            return leftThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightKneeArea(width: Int, height: Int): List<Point> {
            val rightKneeArea = listOf(
                Point(475f, 1988f),
                Point(589f, 1988f),
                Point(589f, 2098f),
                Point(475f, 2098f),
                Point(475f, 1988f)
            )

            return rightKneeArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftKneeArea(width: Int, height: Int): List<Point> {
            val leftKneeArea = listOf(
                Point(733f, 1988f),
                Point(842f, 1988f),
                Point(842f, 2105f),
                Point(733f, 2105f),
                Point(733f, 1988f),
            )

            return leftKneeArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightLegArea(width: Int, height: Int): List<Point> {
            val rightLegArea = listOf(
                Point(462f, 2136f),
                Point(609f, 2136f),
                Point(595f, 2674f),
                Point(513f, 2674f),
                Point(462f, 2136f)
            )

            return rightLegArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftLegArea(width: Int, height: Int): List<Point> {
            val leftLegArea = listOf(
                Point(709f, 2143f),
                Point(856f, 2143f),
                Point(805f, 2679f),
                Point(722f, 2679f),
                Point(709f, 2143f)
            )

            return leftLegArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightFootArea(width: Int, height: Int): List<Point> {
            val rightFootArea = listOf(
                Point(510f, 2700f),
                Point(602f, 2700f),
                Point(616f, 2797f),
                Point(479f, 2797f),
                Point(510f, 2700f)
            )

            return rightFootArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftFootArea(width: Int, height: Int): List<Point> {
            val leftFootArea = listOf(
                Point(715f, 2700f),
                Point(808f, 2700f),
                Point(853f, 2797f),
                Point(705f, 2797f),
                Point(715f, 2700f)
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
