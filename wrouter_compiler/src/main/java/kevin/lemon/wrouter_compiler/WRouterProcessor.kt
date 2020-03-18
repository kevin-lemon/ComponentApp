package kevin.lemon.wrouter_compiler

import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import kevin.lemon.wrouter_annotation.WRouter
import kevin.lemon.wrouter_annotation.bean.RouterBean
import kevin.lemon.wrouter_compiler.utils.ProcessorConfig
import java.io.IOException
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic


/**
 * Created by wxk on 2020/3/16.
 */
// AutoService则是固定的写法，加个注解即可
// 通过auto-service中的@AutoService可以自动生成AutoService注解处理器，用来注册
// 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
// 指定JDK编译版本
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes(ProcessorConfig.AROUTER_PACKAGE)
// 注解处理器接收的参数
@SupportedOptions(ProcessorConfig.OPTIONS, ProcessorConfig.APT_PACKAGE)

class WRouterProcessor : AbstractProcessor() {
    // 操作Element的工具类（类，函数，属性，其实都是Element）
    private var elementTool: Elements? = null

    // type(类信息)的工具类，包含用于操作TypeMirror的工具方法
    private var typeTool: Types? = null

    // Message用来打印 日志相关信息
    private var messager: Messager? = null

    // 文件生成器， 类 资源 等，就是最终要生成的文件 是需要Filer来完成的
    private var filer: Filer? = null

    private var options // （模块传递过来的）模块名  app，personal
            : String? = null
    private var aptPackage // （模块传递过来的） 包名
            : String? = null

    // 仓库一  PATH
    private val mAllPathMap: MutableMap<String, List<RouterBean>> =
        HashMap<String, List<RouterBean>>()

    // 仓库二 GROUP
    private val mAllGroupMap: MutableMap<String, String> =
        HashMap()

    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        elementTool = processingEnvironment.elementUtils
        messager = processingEnvironment.messager
        filer = processingEnvironment.filer
        typeTool = processingEnvironment.typeUtils

        options = processingEnvironment.options[ProcessorConfig.OPTIONS]
        aptPackage = processingEnvironment.options[ProcessorConfig.APT_PACKAGE]
        messager?.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>>>>>>>>>>>>>>> options:$options")
        messager?.printMessage(
            Diagnostic.Kind.NOTE,
            ">>>>>>>>>>>>>>>>>>>>>> aptPackage:$aptPackage"
        )
        if (options != null && aptPackage != null) {
            messager?.printMessage(Diagnostic.Kind.NOTE, "APT 环境搭建完成....")
        } else {
            messager?.printMessage(
                Diagnostic.Kind.NOTE,
                "APT 环境有问题，请检查 options 与 aptPackage 为null..."
            )
        }
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        messager!!.printMessage(
            Diagnostic.Kind.NOTE,
            "start process"
        )
        if (set == null || set.isEmpty()) {
            messager?.printMessage(Diagnostic.Kind.NOTE, "并没有发现 被@ARouter注解的地方呀")
            return false
        }
        // Activity type
        val activityType = elementTool!!.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE)
        val activityMirror = activityType!!.asType()
        // 获取所有被 @ARouter 注解的 元素集合

        // 获取所有被 @ARouter 注解的 元素集合
        val elements: Set<Element?>? =
            roundEnvironment?.getElementsAnnotatedWith(
                WRouter::class.java
            )
        elements?.forEach {
            // 获取简单类名，例如：MainActivity
            // String packageName = elementTool.getPackageOf(element).getQualifiedName().toString();
            // 获取简单类名，例如：MainActivity
            val className: String = it?.simpleName.toString()
            messager?.printMessage(
                Diagnostic.Kind.NOTE,
                "被@ARetuer注解的类有：$className"
            ) // 打印出 就证明APT没有问题
            // 拿到注解

            // 拿到注解
            val wRouter: WRouter = it!!.getAnnotation(WRouter::class.java)
            // TODO PATH

            // TODO PATH
            val routerBean: RouterBean = RouterBean.Builder()
                .addGroup(wRouter.group)
                .addPath(wRouter.path)
                .addElement(it)
                .build()
            // 必须是Activity

            // 必须是Activity
            val elementMirror: TypeMirror = it.asType() // 当前 == Activity

            if (typeTool!!.isSubtype(elementMirror, activityMirror)) {
                routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY)
            } else {
                // 不匹配抛出异常，这里谨慎使用！考虑维护问题
                throw RuntimeException("@ARouter注解目前仅限用于Activity类之上")
            }
            // 校验 path  group  用户传递过来的

            // 校验 path  group  用户传递过来的
            if (checkRouterPath(routerBean)) {
                messager?.printMessage(
                    Diagnostic.Kind.NOTE,
                    "RouterBean Check Success:$routerBean"
                )

                var group = routerBean.getGroup()
                group?.let {
                    // PATH 仓库一
                    var routerBeans = mAllPathMap[group]
                    // 如果从Map中找不到key为：bean.getGroup()的数据，就新建List集合再添加进Map
                    if (routerBeans.isNullOrEmpty()) {
                        routerBeans = ArrayList()
                        routerBeans.add(routerBean)
                        mAllPathMap.put(group, routerBeans)
                    } else { // 从Map中找到key中返回的Value值routerBeans，就直接添加进去
                        routerBeans = routerBeans as MutableList<RouterBean>
                        routerBeans.add(routerBean)
                    }
                }
            } else {
                messager?.printMessage(
                    Diagnostic.Kind.ERROR,
                    "@ARouter注解未按规范配置，如：/app/MainActivity"
                )
            }

            // 定义（拿到标准 TYPE） PATH  GROUP
            elementTool?.let { elementTool ->
                val pathType = elementTool.getTypeElement(ProcessorConfig.AROUTER_API_PATH)
                val groupType = elementTool.getTypeElement(ProcessorConfig.AROUTER_API_GROUP)
                createPathFile(pathType)
                createGroupFile(groupType, pathType)
            }
        }
        return true
    }

    private fun checkRouterPath(routerBean: RouterBean): Boolean {
        val group: String? = routerBean.getGroup()

        val path: String? =
            routerBean.getPath() //  同学们，一定要记住： "/app/MainActivity"   "/order/Order_MainActivity"   "/personal/Personal_MainActivity"


        // @ARouter注解中的path值，必须要以 / 开头（模仿阿里Arouter规范）

        // @ARouter注解中的path值，必须要以 / 开头（模仿阿里Arouter规范）
        if (path.isNullOrEmpty() || !path.startsWith("/")) {
            // ERROR 故意去奔溃的
            messager?.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的path值，必须要以 / 开头")
            return false
        }

        // 比如开发者代码为：path = "/MainActivity"，最后一个 / 符号必然在字符串第1位

        // 比如开发者代码为：path = "/MainActivity"，最后一个 / 符号必然在字符串第1位
        if (path.lastIndexOf("/") == 0) {
            // 架构师定义规范，让开发者遵循
            messager?.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解未按规范配置，如：/app/MainActivity")
            return false
        }

        // 从第一个 / 到第二个 / 中间截取，如：/app/MainActivity 截取出 app 作为group

        // 从第一个 / 到第二个 / 中间截取，如：/app/MainActivity 截取出 app 作为group
        val finalGroup = path.substring(1, path.indexOf("/", 1))
        // finalGroup == app, personal, order

        // @ARouter注解中的group有赋值情况   用户传递进来时 order，  我截取出来的也必须是 order
        // finalGroup == app, personal, order

        // @ARouter注解中的group有赋值情况   用户传递进来时 order，  我截取出来的也必须是 order
        if (!group.isNullOrEmpty() && group != options) {
            // 架构师定义规范，让开发者遵循
            messager?.printMessage(Diagnostic.Kind.ERROR, "@ARouter注解中的group值必须和子模块名一致！")
            return false
        } else {
            routerBean.setGroup(finalGroup) // 赋值  order 添加进去了
        }

        return true
    }

    fun createPathFile(pathType: TypeElement) {
        if (mAllPathMap.isNullOrEmpty()) {
            return
        }

        // Map<String, RouterBean>  返回值
        try {
        } catch (e: IOException) {
            messager?.printMessage(Diagnostic.Kind.ERROR, e.message)
        }
    }

    fun createGroupFile(grouptype: TypeElement, pathType: TypeElement) {
        try {

        } catch (e: IOException) {
            messager?.printMessage(Diagnostic.Kind.ERROR, e.message)
        }
    }

}