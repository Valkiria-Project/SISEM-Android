package com.skgtecnologia.sisem.ui.humanbody.area

import android.os.Parcelable
import com.skgtecnologia.sisem.R
import kotlinx.parcelize.Parcelize

@Suppress("ComplexMethod", "MagicNumber", "TooManyFunctions")
@Parcelize
enum class FrontArea(val image: Int) : Parcelable {
    HEAD(R.drawable.img_front_head_human_body),
    NECK(R.drawable.img_neck_human_body_front),
    CHEST(R.drawable.img_chest_human_body),
    RIGHT_ARM(R.drawable.img_right_arm_human_body),
    LEFT_ARM(R.drawable.img_left_arm_human_body),
    RIGHT_FOREARM(R.drawable.img_right_forearm_human_body),
    LEFT_FOREARM(R.drawable.img_left_forearm_human_body),
    ABDOMEN(R.drawable.img_abdomen_human_body),
    HIPS(R.drawable.img_hips_human_body),
    RIGHT_HAND(R.drawable.img_right_hand_human_doy),
    LEFT_HAND(R.drawable.img_left_hand_human_body),
    RIGHT_THIGH(R.drawable.img_right_thigh_human_body),
    LEFT_THIGH(R.drawable.img_left_thigh_human_body),
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
                Point(530f, 272f),
                Point(544f, 162f),
                Point(654f, 83f),
                Point(767f, 159f),
                Point(794f, 285f),
                Point(743f, 371f),
                Point(661f, 426f),
                Point(578f, 375f),
                Point(530f, 272f)
            )

            return headArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getNeckArea(width: Int, height: Int): List<Point> {
            val neckArea = listOf(
                Point(602f, 431f),
                Point(657f, 462f),
                Point(715f, 438f),
                Point(733f, 486f),
                Point(825f, 554f),
                Point(661f, 582f),
                Point(475f, 561f),
                Point(589f, 486f),
                Point(602f, 431f)
            )

            return neckArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getChestArea(width: Int, height: Int): List<Point> {
            val chestArea = listOf(
                Point(496f, 596f),
                Point(654f, 616f),
                Point(818f, 599f),
                Point(870f, 736f),
                Point(839f, 884f),
                Point(657f, 860f),
                Point(489f, 880f),
                Point(458f, 723f),
                Point(496f, 596f)
            )

            return chestArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightArmArea(width: Int, height: Int): List<Point> {
            val rightArmArea = listOf(
                Point(472f, 598f),
                Point(338f, 1047f),
                Point(249f, 1012f),
                Point(359f, 618f),
                Point(472f, 598f)
            )

            return rightArmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftArmArea(width: Int, height: Int): List<Point> {
            val leftArmArea = listOf(
                Point(856f, 601f),
                Point(979f, 1054f),
                Point(1072f, 1023f),
                Point(945f, 601f),
                Point(856f, 601f)
            )

            return leftArmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightForearmArea(width: Int, height: Int): List<Point> {
            val rightForearmArea = listOf(
                Point(239f, 1047f),
                Point(325f, 1084f),
                Point(222f, 1396f),
                Point(163f, 1376f),
                Point(239f, 1047f)
            )

            return rightForearmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftForearmArea(width: Int, height: Int): List<Point> {
            val leftForearmArea = listOf(
                Point(997f, 1081f),
                Point(1099f, 1393f),
                Point(1151f, 1383f),
                Point(1079f, 1050f),
                Point(997f, 1081f)
            )

            return leftForearmArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getAbdomenArea(width: Int, height: Int): List<Point> {
            val abdomenArea = listOf(
                Point(493f, 916f),
                Point(647f, 889f),
                Point(835f, 916f),
                Point(805f, 1040f),
                Point(835f, 1163f),
                Point(671f, 1225f),
                Point(499f, 1177f),
                Point(517f, 1060f),
                Point(493f, 916f)
            )

            return abdomenArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getHipsArea(width: Int, height: Int): List<Point> {
            val hipsArea = listOf(
                Point(493f, 1228f),
                Point(664f, 1252f),
                Point(829f, 1232f),
                Point(894f, 1359f),
                Point(801f, 1369f),
                Point(661f, 1407f),
                Point(523f, 1376f),
                Point(431f, 1366f),
                Point(493f, 1228f)
            )

            return hipsArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightHandArea(width: Int, height: Int): List<Point> {
            val rightHandArea = listOf(
                Point(160f, 1400f),
                Point(126f, 1588f),
                Point(163f, 1674f),
                Point(249f, 1564f),
                Point(218f, 1410f),
                Point(160f, 1400f)
            )

            return rightHandArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftHandArea(width: Int, height: Int): List<Point> {
            val leftHandArea = listOf(
                Point(1158f, 1400f),
                Point(1195f, 1606f),
                Point(1151f, 1678f),
                Point(1069f, 1561f),
                Point(1099f, 1407f),
                Point(1158f, 1400f)
            )

            return leftHandArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightThighArea(width: Int, height: Int): List<Point> {
            val rightThighArea = listOf(
                Point(431f, 1427f),
                Point(606f, 1431f),
                Point(582f, 1945f),
                Point(469f, 1955f),
                Point(407f, 1520f),
                Point(431f, 1427f)
            )

            return rightThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftThighArea(width: Int, height: Int): List<Point> {
            val leftThighArea = listOf(
                Point(705f, 1434f),
                Point(866f, 1410f),
                Point(914f, 1530f),
                Point(849f, 1955f),
                Point(739f, 1955f),
                Point(688f, 1537f),
                Point(705f, 1434f)
            )

            return leftThighArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightKneeArea(width: Int, height: Int): List<Point> {
            val rightKneeArea = listOf(
                Point(479f, 1990f),
                Point(589f, 1990f),
                Point(589f, 2110f),
                Point(479f, 2110f),
                Point(479f, 1990f)
            )

            return rightKneeArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftKneeArea(width: Int, height: Int): List<Point> {
            val leftKneeArea = listOf(
                Point(715f, 1986f),
                Point(849f, 1986f),
                Point(849f, 2106f),
                Point(715f, 2106f),
                Point(715f, 1986f)
            )

            return leftKneeArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightLegArea(width: Int, height: Int): List<Point> {
            val rightLegArea = listOf(
                Point(462f, 2135f),
                Point(613f, 2135f),
                Point(595f, 2671f),
                Point(503f, 2671f),
                Point(462f, 2135f)
            )

            return rightLegArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftLegArea(width: Int, height: Int): List<Point> {
            val leftLegArea = listOf(
                Point(712f, 2132f),
                Point(856f, 2132f),
                Point(811f, 2674f),
                Point(719f, 2674f),
                Point(712f, 2132f)
            )

            return leftLegArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getRightFootArea(width: Int, height: Int): List<Point> {
            val rightFootArea = listOf(
                Point(503f, 2687f),
                Point(599f, 2689f),
                Point(613f, 2795f),
                Point(475f, 2795f),
                Point(503f, 2687f)
            )

            return rightFootArea.map { it.toProportionalPoint(width, height) }
        }

        private fun getLeftFootArea(width: Int, height: Int): List<Point> {
            val leftFootArea = listOf(
                Point(715f, 2688f),
                Point(815f, 2688f),
                Point(846f, 2795f),
                Point(702f, 2795f),
                Point(715f, 2688f)
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
