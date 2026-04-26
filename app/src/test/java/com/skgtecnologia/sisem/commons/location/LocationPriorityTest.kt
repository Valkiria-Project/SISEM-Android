package com.skgtecnologia.sisem.commons.location

import com.google.android.gms.location.Priority
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationPriorityTest {

    @Test
    fun `speed zero returns balanced power accuracy`() {
        assertEquals(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            decidePriority(0.0f)
        )
    }

    @Test
    fun `speed below threshold returns balanced power accuracy`() {
        assertEquals(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            decidePriority(0.49f)
        )
    }

    @Test
    fun `speed at threshold returns high accuracy`() {
        assertEquals(
            Priority.PRIORITY_HIGH_ACCURACY,
            decidePriority(0.5f)
        )
    }

    @Test
    fun `speed above threshold returns high accuracy`() {
        assertEquals(
            Priority.PRIORITY_HIGH_ACCURACY,
            decidePriority(10.0f)
        )
    }
}
