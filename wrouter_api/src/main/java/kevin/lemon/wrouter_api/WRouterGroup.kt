package kevin.lemon.wrouter_api

/**
 * Created by wxk on 2020/3/16.
 */
interface WRouterGroup {
    /**
     * 例如：order分组下有这些信息，personal分组下有这些信息
     * 例如："order" --- ARouterPath的实现类 -->（APT生成出来的 WRouter$$Path$$order）
     *
     * @return  key:"order"   value:系列的order组下面所有的（path---class）
     */
    fun getGroupMap(): Map<String, Class<out WRouterPath>>
}