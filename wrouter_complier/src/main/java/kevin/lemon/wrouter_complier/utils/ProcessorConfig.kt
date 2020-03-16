package kevin.lemon.wrouter_complier.utils

/**
 * Created by wxk on 2020/3/16.
 */
open interface ProcessorConfig {
    companion object {
        // Activity全类名
        const val ACTIVITY_PACKAGE = "android.app.Activity"

        // @ARouter注解 的 包名 + 类名
        const val AROUTER_PACKAGE = "com.xiangxue.arouter_annotation.ARouter"

        // 接收参数的TAG标记
        const val OPTIONS = "moduleName" // 同学们：目的是接收 每个module名称
        const val APT_PACKAGE = "packageNameForAPT" // 同学们：目的是接收 包名（APT 存放的包名）

        // ARouter api 包名
        const val AROUTER_API_PACKAGE = "com.xiangxue.arouter_api"

        // ARouter api 的 ARouterGroup 高层标准
        const val AROUTER_API_GROUP =
            "$AROUTER_API_PACKAGE.ARouterGroup"

        // ARouter api 的 ARouterPath 高层标准
        const val AROUTER_API_PATH =
            "$AROUTER_API_PACKAGE.ARouterPath"

        // 路由组，中的 Path 里面的 方法名
        const val PATH_METHOD_NAME = "getPathMap"

        // 路由组，中的 Group 里面的 方法名
        const val GROUP_METHOD_NAME = "getGroupMap"

        // 路由组，中的 Path 里面 的 变量名 1
        const val PATH_VAR1 = "pathMap"

        // 路由组，中的 Group 里面 的 变量名 1
        const val GROUP_VAR1 = "groupMap"

        // 路由组，PATH 最终要生成的 文件名
        const val PATH_FILE_NAME = "ARouter$\$Path$$"

        // 路由组，GROUP 最终要生成的 文件名
        const val GROUP_FILE_NAME = "ARouter$\$Group$$"
    }
}