# mybatis.generator.plugin
mybatis generator的插件集合,功能有:生成domain时把数据库中的注释也一并生成,提供类似于updateSelective那种方式的搜索功能,提供对mysql的sql分页功能

generatorConfig.xml例子:

        <plugin type="cn.jason.mybatis.plugin.PageableFindPlugin">
            <property name="canPageNullable" value="false"/>
            <property name="pageType" value="cn.jason.test.Page"/>
            <property name="pageStartName" value="start"/>
            <property name="pageSizeName" value="pageSize"/>
        </plugin>
        <plugin type="cn.jason.mybatis.plugin.SearchSelectivePlugin">
            <property name="canPageNullable" value="false"/>
            <property name="pageType" value="cn.jason.test.Page"/>
            <property name="pageStartName" value="start"/>
            <property name="pageSizeName" value="pageSize"/>
        </plugin>
        <commentGenerator type="cn.jason.mybatis.plugin.DatabaseCommentGenerator">
            <property name="suppressAllComments" value="false"/>
            <property name="skipDatabaseComment" value="true"/>
        </commentGenerator>
        <javaTypeResolver type="cn.jason.mybatis.plugin.RemappableJavaTypeResolver">
            <property name="forceBigDecimals" value="false" />
            <property name="TINYINT" value="java.lang.Integer"/>
        </javaTypeResolver>