package com.wings.spi.processor

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import javassist.ClassPool
import javassist.CtClass
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.ClassFile
import javassist.bytecode.annotation.Annotation
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

public class SpiTransform extends Transform{

    private static final String CLASS_REGISTRY = 'com.wings.spi.repository.ServiceRegistry'
    private static final String CLASS_REGISTRY_PATH = 'com/wings/spi/repository/ServiceRegistry.class'
    private static final String ANNOTATION_IMPL = 'com.wings.spi.annotation.ServiceProvider'

    private Project mProject

    public SpiTransform(Project project) {
        this.mProject = project
    }

    @Override
    String getName() {
        return "SpiTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Collections.singleton(QualifiedContent.Scope.SUB_PROJECTS)

    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        // 1
        transformInvocation.outputProvider.deleteAll()

        def pool = ClassPool.getDefault()

        JarInput registryJarInput
        def impls = []

        // 2
        transformInvocation.inputs.each { input ->

            input.jarInputs.each { JarInput jarInput ->
                pool.appendClassPath(jarInput.file.absolutePath)

                if (new JarFile(jarInput.file).getEntry(CLASS_REGISTRY_PATH) != null) {
                    registryJarInput = jarInput
                } else {
                    def jarFile = new JarFile(jarInput.file)
                    jarFile.entries().grep { entry -> entry.name.endsWith(".class") }.each { entry ->
                        InputStream stream = jarFile.getInputStream(entry)
                        if (stream != null) {
                            CtClass ctClass = pool.makeClass(stream)
                            if (ctClass.hasAnnotation(ANNOTATION_IMPL)) {
                                impls.add(ctClass)
                            }
                            ctClass.detach()
                        }
                    }

                    FileUtils.copyFile(jarInput.file, transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR))
                }
            }
        }

        if (registryJarInput == null) {
            return
        }

        // 3
        def stringBuilder = new StringBuilder()
        stringBuilder.append('{\n')
        stringBuilder.append('services = new java.util.HashMap();')
        impls.each { CtClass ctClass ->
            ClassFile classFile = ctClass.getClassFile()
            // 注意：如果是AnnotationsAttribute.visibleTag，则ServiceProvider注解需要加元注解信息
            //      如果是AnnotationsAttribute.invisibleTag,则ServiceProvider注解不能加元注解信息
            AnnotationsAttribute attr = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag)
            Annotation annotation = attr.getAnnotation(ANNOTATION_IMPL)
            def value = annotation.getMemberValue('service')
            stringBuilder.append('services.put(')
                    .append(value)
                    .append(', new ')
                    .append(ctClass.name)
                    .append('());\n')
        }
        stringBuilder.append('}\n')

        def registryClz = pool.get(CLASS_REGISTRY)
        registryClz.makeClassInitializer().setBody(stringBuilder.toString())

        // 4
        def outDir = transformInvocation.outputProvider.getContentLocation(registryJarInput.name, registryJarInput.contentTypes, registryJarInput.scopes, Format.JAR)

        copyJar(registryJarInput.file, outDir, CLASS_REGISTRY_PATH, registryClz.toBytecode())
    }

    private static void copyJar(File srcFile, File outDir, String fileName, byte[] bytes) {
        outDir.getParentFile().mkdirs()

        def jarOutputStream = new JarOutputStream(new FileOutputStream(outDir))
        def buffer = new byte[1024]
        int read = 0

        def jarFile = new JarFile(srcFile)
        jarFile.entries().each { JarEntry jarEntry ->
            if (jarEntry.name == fileName) {
                jarOutputStream.putNextEntry(new JarEntry(fileName))
                jarOutputStream.write(bytes)
            } else {
                jarOutputStream.putNextEntry(jarEntry)
                def inputStream = jarFile.getInputStream(jarEntry)
                while ((read = inputStream.read(buffer)) != -1) {
                    jarOutputStream.write(buffer, 0, read)
                }
            }
        }
        jarOutputStream.close()
    }

}