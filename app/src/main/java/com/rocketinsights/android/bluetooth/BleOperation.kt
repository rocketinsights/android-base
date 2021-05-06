package com.rocketinsights.android.bluetooth

import com.rocketinsights.android.bluetooth.BleOperationType.Companion.TYPE_READ
import com.rocketinsights.android.bluetooth.BleOperationType.Companion.TYPE_SUBSCRIBE
import com.rocketinsights.android.bluetooth.BleOperationType.Companion.TYPE_WRITE
import java.util.UUID

data class BleOperation private constructor(
    // Identify temporarily the BLE Operation until we get the final Characteristic Instance ID.
    val id: Int,
    val service: UUID,
    val charUuid: UUID,
    // Only used for characteristic write
    val data: ByteArray?,
    // Only used for characteristic notification subscription
    val enable: Boolean,
    @BleOperationType
    val type: Int,
    val highPriority: Boolean
) {

    companion object {
        private var sTemporaryId = 0

        private fun getTemporaryId() = sTemporaryId++

        fun newWriteOperation(
            service: UUID,
            charUuid: UUID,
            data: ByteArray?,
            highPriority: Boolean
        ): BleOperation {
            return BleOperation(
                getTemporaryId(),
                service,
                charUuid,
                data,
                false,
                TYPE_WRITE,
                highPriority
            )
        }

        fun newReadOperation(
            service: UUID,
            charUuid: UUID,
            highPriority: Boolean
        ): BleOperation {
            return BleOperation(
                getTemporaryId(),
                service,
                charUuid,
                null,
                false,
                TYPE_READ,
                highPriority
            )
        }

        fun newSubscribeOperation(
            service: UUID,
            charUuid: UUID,
            enable: Boolean,
            highPriority: Boolean
        ): BleOperation {
            return BleOperation(
                getTemporaryId(),
                service,
                charUuid,
                null,
                enable,
                TYPE_SUBSCRIBE,
                highPriority
            )
        }
    }
}
