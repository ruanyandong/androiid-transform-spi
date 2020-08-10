package com.wings.spi.processor

import com.android.build.gradle.AppExtension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

/**
 * @author -> Wings
 * @date -> 2020/8/9
 * @email -> ruanyandongai@gmail.com
 * 729368173@qq.com
 * @phone -> 18983790146
 * @blog -> https://ruanyandong.github.io
 * -> https://blog.csdn.net/qq_34681580
 */
class SpiPlugin implements Plugin<Project> {

    @Override
    void apply(@NotNull Project project) {
        if (project == null){
            return
        }
        if (!project.plugins.hasPlugin("com.android.application")){
            throw NullPointerException("this plugin must apply com.android.application plugin")
        }

        // 获取Android扩展
        def android = project.extensions.getByType(AppExtension)
        // 注册Transform，其实就是添加了Task
        android.registerTransform(new SpiTransform(project))

    }

}
