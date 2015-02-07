package cn.jason.mybatis.plugin;


import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * 可以在生成domain的时候将数据库相应字段的注释也自动的生成了
 */
public class DatabaseCommentGenerator extends DefaultCommentGenerator {
	private boolean skipDatabaseComment = false;

	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
		if (!skipDatabaseComment && StringUtils.isNotEmpty(introspectedColumn.getRemarks())) {
			field.addJavaDocLine("/**");
			field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
			field.addJavaDocLine(" */");
		}
	}

	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
	}

	@Override
	public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
		super.addConfigurationProperties(properties);
		skipDatabaseComment = isTrue(properties
				.getProperty("skipDatabaseComment"));
	}
}
