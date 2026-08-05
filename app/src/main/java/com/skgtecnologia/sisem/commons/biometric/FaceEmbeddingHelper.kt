package com.skgtecnologia.sisem.commons.biometric

import android.graphics.PointF
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceLandmark
import timber.log.Timber
import kotlin.math.sqrt

/**
 * Extracts a normalized feature vector from a detected face.
 *
 * Priority:
 *  1. Face contour (CONTOUR_MODE_ALL) — 36 points → 72 features.
 *     Highly discriminative because the outline of each person's face
 *     differs significantly in shape.
 *  2. Extended landmarks fallback (up to 11 key points → 22 features).
 *
 * Both are L2-normalized and compared via cosine similarity.
 *
 * MATCH_THRESHOLD is raised to 0.88 because contour embeddings are more
 * discriminative and the extra features allow a stricter cut-off.
 */
@Suppress("MagicNumber")
object FaceEmbeddingHelper {

    private const val EPSILON = 1e-8f

    /** Minimum cosine similarity to accept a match. */
    const val MATCH_THRESHOLD = 0.88f

    /** Face bounding-box must be wider than this (in camera-image pixels) before capture. */
    const val MIN_FACE_PX = 100

    // ── Entry point ───────────────────────────────────────────────────────────

    @Suppress("ReturnCount")
    fun extractEmbedding(face: Face): FloatArray? {
        // Prefer face outline contour — far more discriminative than 5 key points
        val contourPoints = face.getContour(FaceContour.FACE)?.points
        if (!contourPoints.isNullOrEmpty() && contourPoints.size >= 10) {
            val v = normalize(contourPoints.toFeatureVector(face))
            Timber.d(
                "[FaceEmbed] method=CONTOUR points=${contourPoints.size} " +
                    "features=${v.size} " +
                    "sample=${v.take(6).joinToString { "%.4f".format(it) }}"
            )
            return v
        }
        Timber.w("[FaceEmbed] CONTOUR not available (${contourPoints?.size ?: 0} pts) — falling back to landmarks")
        return extractFromLandmarks(face)
    }

    // ── Contour-based (primary) ───────────────────────────────────────────────

    private fun List<PointF>.toFeatureVector(face: Face): FloatArray {
        val faceW = face.boundingBox.width().toFloat().coerceAtLeast(1f)
        val cx = face.boundingBox.centerX().toFloat()
        val cy = face.boundingBox.centerY().toFloat()
        return FloatArray(size * 2) { idx ->
            val pt = this[idx / 2]
            if (idx % 2 == 0) (pt.x - cx) / faceW else (pt.y - cy) / faceW
        }
    }

    // ── Landmark-based (fallback) ─────────────────────────────────────────────

    private val ALL_LANDMARKS = listOf(
        FaceLandmark.LEFT_EYE,
        FaceLandmark.RIGHT_EYE,
        FaceLandmark.NOSE_BASE,
        FaceLandmark.MOUTH_LEFT,
        FaceLandmark.MOUTH_RIGHT,
        FaceLandmark.LEFT_CHEEK,
        FaceLandmark.RIGHT_CHEEK,
        FaceLandmark.LEFT_EAR,
        FaceLandmark.RIGHT_EAR,
        FaceLandmark.MOUTH_BOTTOM
    )

    @Suppress("ReturnCount")
    private fun extractFromLandmarks(face: Face): FloatArray? {
        val pts = ALL_LANDMARKS.mapNotNull { face.getLandmark(it)?.position }
        if (pts.size < 5) return null
        val faceW = face.boundingBox.width().toFloat().coerceAtLeast(1f)
        val cx = face.boundingBox.centerX().toFloat()
        val cy = face.boundingBox.centerY().toFloat()
        val raw = FloatArray(pts.size * 2)
        pts.forEachIndexed { i, pt ->
            raw[i * 2] = (pt.x - cx) / faceW
            raw[i * 2 + 1] = (pt.y - cy) / faceW
        }
        val v = normalize(raw)
        Timber.d(
            "[FaceEmbed] method=LANDMARKS pts=${pts.size} " +
                "features=${v.size} " +
                "sample=${v.take(6).joinToString { "%.4f".format(it) }}"
        )
        return v
    }

    // ── Similarity & normalization ────────────────────────────────────────────

    /** Cosine similarity in [−1, 1]; 1 = identical direction. */
    fun similarity(a: FloatArray, b: FloatArray): Float {
        if (a.size != b.size) return 0f
        var dot = 0f
        var normA = 0f
        var normB = 0f
        for (j in a.indices) {
            dot += a[j] * b[j]
            normA += a[j] * a[j]
            normB += b[j] * b[j]
        }
        val denom = sqrt(normA) * sqrt(normB)
        return if (denom < EPSILON) 0f else dot / denom
    }

    private fun normalize(v: FloatArray): FloatArray {
        val norm = sqrt(v.map { it * it }.sum()).coerceAtLeast(EPSILON)
        return FloatArray(v.size) { v[it] / norm }
    }
}
