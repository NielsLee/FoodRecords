package lying.fengfeng.foodrecords.utils

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

actual object UUID {
    @OptIn(ExperimentalUuidApi::class)
    actual fun getRandom(): String {
        return Uuid.random().toHexString()
    }
}