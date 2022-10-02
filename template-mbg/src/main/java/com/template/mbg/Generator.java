package com.template.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiuqfang
 * @date 2021/6/13
 */
public class Generator {
    public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        //MBG 执行过程中的警告信息
        List<String> warnings = new ArrayList<String>();

        //读取我们的 MBG 配置文件
        File fileFile = new File("template-mbg/src/main/resources/generatorConfig.xml");
        InputStream configStream = Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(fileFile);

        //当生成的代码重复时，覆盖原代码
        DefaultShellCallback callback = new DefaultShellCallback(true);

        //创建 MBG
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

        //执行生成代码
        myBatisGenerator.generate(null);

        //输出警告信息
        for (String warning : warnings) {
            System.out.println(warning);
        }
    }
}
