package lying.fengfeng.foodrecords

interface Platform {
    val name: String
    val version: Int
}

expect fun getPlatform(): Platform