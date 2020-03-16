package kevin.lemon.wrouter_complier

import com.google.auto.service.AutoService
import kevin.lemon.wrouter_complier.utils.ProcessorConfig
import javax.annotation.processing.Processor
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedOptions
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion

/**
 * Created by wxk on 2020/3/16.
 */
@AutoService(Processor::class) // 允许/支持的注解类型，让注解处理器处理
// 允许/支持的注解类型，让注解处理器处理
// 允许/支持的注解类型，让注解处理器处理
@SupportedAnnotationTypes(ProcessorConfig.AROUTER_PACKAGE) // 指定JDK编译版本
// 指定JDK编译版本
// 指定JDK编译版本
@SupportedSourceVersion(SourceVersion.RELEASE_7) // 注解处理器接收的参数
// 注解处理器接收的参数
@SupportedOptions(ProcessorConfig.OPTIONS, ProcessorConfig.APT_PACKAGE)
class WrouterProcessor {

}