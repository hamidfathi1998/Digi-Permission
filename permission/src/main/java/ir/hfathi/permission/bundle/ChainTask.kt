package ir.hfathi.permission.bundle

interface ChainTask {

    fun request()

    fun requestAgain(permissions: List<String>)

    fun finish()
}