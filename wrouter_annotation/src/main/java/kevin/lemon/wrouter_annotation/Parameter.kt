package kevin.lemon.wrouter_annotation

/**
 * Created by wxk on 2020/3/20.
 */
// 详细路由路径（必填），如："/app/MainActivity"
// 路由组名（选填，如果开发者不填写，可以从path中截取出来）
@Target(AnnotationTarget.FIELD)
@Retention(value = AnnotationRetention.BINARY)
annotation class Parameter(val name:String = "")