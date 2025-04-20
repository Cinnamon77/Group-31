package com.bookkeeping;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Properties;

/**
 * @author bookkeeping
 */
@Data
class DbConfig {
    private String dbName;
    private String username;
    private String password;
}

/**
 * @author bookkeeping
 */
public class MyBatisPlusGenerator {
    /**
     * 项目根路径（注意：本项目中追加的是此组件模块的文件名。实际的单模块项目中，请使用下面的）
     */
    private static final String PROJECT_PATH = Paths.get(System.getProperty("user.dir")) + "/src/main/java";

    public static void main(String[] args) {
        DbConfig dbConfig = readConfig();
        String mapperXmlPath = "src/main/resources";
        FastAutoGenerator.create("jdbc:mysql://localhost:33060/" + dbConfig.getDbName() + "?useSSL=false&autoReconnect=true&characterEncoding=utf8",
                        dbConfig.getUsername(),
                        dbConfig.getPassword())
                .globalConfig(builder -> builder
                        .author("bookkeeping")
                        .outputDir(PROJECT_PATH)
                        .commentDate("yyyy-MM-dd")
                        .disableOpenDir()
                        .enableSpringdoc()
                        .enableSwagger()
                )
                .packageConfig(builder -> builder
                        .parent(MyBatisPlusGenerator.class.getPackage().getName())
                        .entity("entity.model")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlPath + "/mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder -> builder
                        .controllerBuilder()
                        .disable()
                        .entityBuilder()
                        .enableLombok()
                        .enableFileOverride()
                        .logicDeleteColumnName("is_deleted")
                        .addTableFills(new Column("created_at", FieldFill.INSERT))
                        .addTableFills(new Column("updated_at", FieldFill.INSERT_UPDATE))
                        .serviceBuilder()
                        .disableService()
                        .disableServiceImpl()
                        .mapperBuilder()
                        .enableFileOverride()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

    private static DbConfig readConfig() {
        DbConfig dbConfig = new DbConfig();
        try {
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream("src/main/resources/application-dev.properties");
            properties.load(inputStream);
            dbConfig.setDbName("bookkeeping");
            dbConfig.setUsername(properties.getProperty("spring.datasource.username"));
            dbConfig.setPassword(properties.getProperty("spring.datasource.password"));
        } catch (IOException e) {
            System.out.println("读取配置文件失败：" + e.getMessage());
        }
        return dbConfig;
    }
}
