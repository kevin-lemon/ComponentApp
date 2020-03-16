package kevin.lemon.wrouter_api

import kevin.lemon.wrouter_api.bean.RouterBean

/**
 * Created by wxk on 2020/3/16.
 */
interface WRouterPath {
    /**
     * 例如：order分组下有这些信息，personal分组下有这些信息
     *
     * @return key:"/order/Order_MainActivity"   或  "/personal/Personal_MainActivity"
     * value: RouterBean==Order_MainActivity.class 或 RouterBean=Personal_MainActivity.class
     */
    fun getPathMap(): Map<String, RouterBean>
}