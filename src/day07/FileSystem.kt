package day07

fun List<String>.toFileSystem(): AocDirectory {
    val root = AocDirectory("/")
    var ptr = root
    forEach {
        if(it.startsWith("$ cd")){
            val dirname = it.substring(5)
            ptr = when(dirname){
                ".." -> ptr.parent!!
                "/" -> root
                else -> ptr.cd(dirname)
            }
        }
        else if(it == "$ ls"){
            // handle next lines
        }
        else {
            val data = it.split(" ")
            when(data[0]){
                "dir" -> AocDirectory(data[1], ptr)
                else -> AocFile(data[1], data[0].toLong(), ptr)
            }
        }
    }
    return root
}

interface FsEntry{
    val name: String
    val type: String
    val parent: AocDirectory?

    fun size(): Long
    fun getAllFiles(): List<FsEntry>
    fun print(indent: String = "")
}

data class AocDirectory (
    override val name: String,
    override val parent: AocDirectory? = null
) : FsEntry {
    init {
        parent?.children?.add(this)
    }
    override val type = "dir"
    val children: MutableList<FsEntry> = mutableListOf()

    override fun size(): Long =
        children.sumOf { it.size() }

    override fun getAllFiles() =
        children.flatMap { it.getAllFiles() } + this

    override fun print(indent: String) {
        println("$indent- $name (dir)")
        children.forEach {
            it.print("$indent  ")
        }
    }

    fun cd(dirname: String) =
        children.first { c -> c.name == dirname } as AocDirectory
}

data class AocFile (
    override val name: String,
    val size: Long,
    override val parent: AocDirectory
) : FsEntry {
    init { parent.children += this }
    override val type = "file"
    override fun size(): Long = size

    override fun getAllFiles() = listOf(this)

    override fun print(indent: String) {
        println("$indent- $name (file, size=$size)")
    }
}