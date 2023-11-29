package com.skgtecnologia.sisem.domain.inventory.model

enum class InventoryType(val type: String) {
    BIOMEDICAL("CARD_INVENTORY_BIOMEDICAL"),
    MEDICINE("CARD_INVENTORY_MEDICINES"),
    TRANSFER_AND_RETURN("CARD_TRANSFER_AND_RETURN"),
    VEHICLE("CARD_INVENTORY_VEHICLE");

    companion object {
        fun from(type: String): InventoryType? = entries.firstOrNull { it.type == type }
    }
}
