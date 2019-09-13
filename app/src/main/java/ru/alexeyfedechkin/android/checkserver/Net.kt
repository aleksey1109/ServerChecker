package ru.alexeyfedechkin.android.checkserver

object Net {

    /**
     *
     * @param responseCode
     * @return
     */
    fun checkServerStatus(responseCode:Int):ServerStatus{
        return ServerStatus.OFFLINE
    }

}