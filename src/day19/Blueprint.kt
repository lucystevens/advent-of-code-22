package day19

class Blueprint(
    val id: Int,
    val robots: Map<String, List<Pair<String, Int>>>
) {

    var best = 0

    fun reset(){
        best = 0
    }

    val maxNeeded = robots.keys.associateWith { type ->
        robots.values.flatten()
            .filter { it.first == type }
            .maxOfOrNull { it.second } ?:  0
    }

    // find all robots buildable with current resources
    fun getPossibleBuilds(resources: Map<String, Int>) =
        robots.entries.filter { (_, desc) ->
            desc.all { (resource, needed) ->
                val available = resources[resource] ?: 0
                available >= needed
            }
        }.map { it.key }

    fun build(type: String, resources: Map<String, Int>): Map<String, Int> {
        val robot = robots[type] ?: error("Invalid robot type $type")
        return resources.mapValues { (resource, quantity) ->
            val needed = robot.find { it.first == resource }?.second ?: 0
            quantity - needed
        }
    }
}