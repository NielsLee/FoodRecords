package lying.fengfeng.foodrecords.utils

import java.util.UUID

actual object UUID {
    actual fun getRandom(): String {
        return UUID.randomUUID().toString()
    }
}