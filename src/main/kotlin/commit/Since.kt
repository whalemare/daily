package commit

sealed class Since {

    /**
     * TODO: rewrite to windows
     * Works only on Unix
     */
    object LatestTag : Raw("git describe --tags --abbrev=0")

    open class Tag(tag: String) : Raw("$tag..HEAD")

    /**
     * Expected some raw result that will be putted
     * ```bash
     * git log --since='PARAM'
     * ```
     * where PARAM it is [text] param
     * @param text 6am, 9pm, c3eeab6..0158d99, tag-name..HEAD, today
     */
    open class Raw(val text: String) : Since()
}
