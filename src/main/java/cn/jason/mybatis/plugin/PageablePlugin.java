package cn.jason.mybatis.plugin;

import com.google.common.base.Preconditions;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.Iterator;

public abstract class PageablePlugin extends PluginAdapter {
	protected FullyQualifiedJavaType pageType;
	protected boolean canPageNullable;
	protected String pageStartName, pageSizeName;

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
		super.initialized(introspectedTable);
		Preconditions.checkState(properties.containsKey("canPageNullable"), "canPageNullable(生成的方法的分页参数是否可以为null)为必填");
		Preconditions.checkState(properties.containsKey("pageType"), "SearchSelectivePlugins:pageType(分页的实体)为必填");
		Preconditions.checkState(properties.containsKey("pageStartName"), "SearchSelectivePlugins:pageStartName(分页实体内代表分页初始位置的属性名)为必填");
		Preconditions.checkState(properties.containsKey("pageSizeName"), "SearchSelectivePlugins:pageStartName(分页实体内代表每页条数的属性名)为必填");
		String canPageNullable = properties.getProperty("canPageNullable").toLowerCase();
		Preconditions.checkState(canPageNullable.equals("true") || canPageNullable.equals("false"), "canPageNullable为boolean类型");
		this.canPageNullable = Boolean.valueOf(canPageNullable);
		String pageType = properties.getProperty("pageType");
		try {
			Class.forName(pageType);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("没有为" + pageType + "的类");
		}
		this.pageType = new FullyQualifiedJavaType(pageType);
		this.pageStartName = properties.getProperty("pageStartName");
		this.pageSizeName = properties.getProperty("pageSizeName");
	}

	protected Parameter createPageParameter() {
		return new Parameter(pageType, "page", "@Param(\"page\")");
	}

	protected void appendLimit(XmlElement newElement) {
		if (canPageNullable) {
			XmlElement pageElement = new XmlElement("if");
			pageElement.addAttribute(new Attribute("test", "page != null"));
			pageElement.addElement(new TextElement("limit #{page." + this.pageStartName + "}, #{page." + this.pageSizeName + "}"));
			newElement.addElement(pageElement);
		}else {
			newElement.addElement(new TextElement("limit #{page." + this.pageStartName + "}, #{page." + this.pageSizeName + "}"));
		}
	}

	protected void removeType(XmlElement newElement) {
		for (Iterator<Attribute> iterator = newElement.getAttributes().iterator(); iterator.hasNext(); ) {
			Attribute attribute = iterator.next();
			if ("parameterType".equals(attribute.getName())) {
				iterator.remove();
				break;
			}
		}
	}

	protected void removeXmlElement(XmlElement element, String elementName, String innerElementContent) {
		for (Iterator<Element> iterator = element.getElements().iterator(); iterator.hasNext(); ) {
			Element innerElement = iterator.next();
			if (innerElement instanceof XmlElement) {
				XmlElement xmlElement = (XmlElement) innerElement;
				if (xmlElement.getName().equals(elementName) && xmlElement.getElements().size() == 1) {
					Element childElement = xmlElement.getElements().get(0);
					if (childElement instanceof TextElement) {
						TextElement textElement = (TextElement) childElement;
						if (textElement.getContent().equals(innerElementContent)) {
							iterator.remove();
						}
					}
				}
			}
		}
	}
}
