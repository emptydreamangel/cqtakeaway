package com.blue.jitian.Config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;


/**
 * MyBatis Plus配置类
 * 用于配置自动填充、分页等功能
 */
@Configuration
public class MyBatisPlusConfig implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间（created_at）
        this.strictInsertFill(metaObject, "created_at", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createdAt", LocalDateTime.class, LocalDateTime.now());
        
        // 自动填充更新时间（updated_at）
        this.strictInsertFill(metaObject, "updated_at", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
        
        // 自动填充注册时间（register_time）
        this.strictInsertFill(metaObject, "register_time", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "registerTime", LocalDateTime.class, LocalDateTime.now());
        
        // 自动填充领取时间（receive_time）
        this.strictInsertFill(metaObject, "receive_time", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "receiveTime", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 更新时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        // 自动填充更新时间
        this.strictUpdateFill(metaObject, "updated_at", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 配置MyBatis Plus分页插件
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
