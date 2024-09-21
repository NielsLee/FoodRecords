package lying.fengfeng.foodrecords

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform