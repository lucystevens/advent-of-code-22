package day16

data class Actor(
    val current: ValveNode, // node this is moving to
    val timeLeft: Int
){
    fun getMoves(open: Set<String>, valves: Map<String, ValveNode>): List<Actor>{
        // move to and open a valve
        return current.tunnels.entries
            .filterNot { (key, _) -> open.contains(key) }
            .map { (key, dist) ->  Actor(valves[key]!!, dist+1) }
    }

    fun decrementTime() = Actor(current, timeLeft -1)
}