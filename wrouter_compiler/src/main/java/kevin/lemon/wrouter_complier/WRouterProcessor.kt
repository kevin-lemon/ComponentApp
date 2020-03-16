package kevin.lemon.wrouter_complier

import kevin.lemon.wrouter_annotation.WRouter
import kevin.lemon.wrouter_annotation.bean.RouterBean
import kevin.lemon.wrouter_complier.utils.ProcessorConfig
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
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
    private val mAllPathMap: Map<String, List<RouterBean>> =
        HashMap<String, List<RouterBean>>()

    // 仓库二 GROUP
    private val mAllGroupMap: Map<String, String> =
        HashMap()

    override fun init(processingEnvironment: ProcessingEnvironment?) {
        super.init(processingEnvironment)
        processingEnvironment?.let {
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
    }

    override fun process(
        set: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        if (set == null || set.isEmpty()) {
            messager?.printMessage(Diagnostic.Kind.NOTE, "并没有发现 被@ARouter注解的地方呀")
            return false
        }
        // Activity type

        // Activity type
        val activityType = elementTool?.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE)
        val activityMirror = activityType?.asType()
        // 获取所有被 @ARouter 注解的 元素集合

        // 获取所有被 @ARouter 注解的 元素集合
        val elements: Set<Element?>? =
            roundEnvironment?.getElementsAnnotatedWith(
                WRouter::class.java
            )
        elements?.forEach {
            // 获取简单类名，例如：MainActivity
            // 有三处地方使用到注解  for 3
            // 获取类节点，获取包节点 （com.xiangxue.xxxxxx）
            // String packageName = elementTool.getPackageOf(element).getQualifiedName().toString();

            // 获取简单类名，例如：MainActivity
            val className: String = it?.simpleName.toString()
            messager?.printMessage(
                Diagnostic.Kind.NOTE,
                "被@ARetuer注解的类有：$className"
            ) // 打印出 就证明APT没有问题

        }
        return true
    }

}