package io.jpress.module.page.model;

import io.jboot.db.annotation.Table;
import io.jboot.utils.StrUtil;
import io.jboot.web.json.JsonIgnore;
import io.jpress.commons.utils.UrlUtils;
import io.jpress.module.page.model.base.BaseSinglePageCategory;

/**
 * Generated by JPress.
 */
@Table(tableName = "single_page_category", primaryKey = "id")
public class SinglePageCategory extends BaseSinglePageCategory<SinglePageCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 普通的分类
     * 分类可以有多个层级
     */
    public static final String TYPE_CATEGORY = "category";

    /**
     * 标签
     * 标签只有一个层级
     */
    public static final String TYPE_TAG = "tag";


    /**
     * 用户自建分类，目前暂不考虑这部分的实现
     */
    public static final String TYPE_USER_CATEGORY = "user_category";


    @JsonIgnore
    public boolean isTag() {
        return TYPE_TAG.equals(getType());
    }

    @JsonIgnore
    public String getUrl() {
        String prefix = TYPE_CATEGORY.equals(getType()) ? "/page/category/" : "/page/tag/";
        return UrlUtils.getUrl(prefix, getSlug());
    }


    public String getUrlWithPageNumber(int pageNumber){
        if (pageNumber <= 1) {
            return getUrl();
        }

        String prefix = TYPE_CATEGORY.equals(getType()) ? "/page/category/" : "/page/tag/";
        return UrlUtils.getUrl(prefix, getSlug(),"-",pageNumber);
    }

    @JsonIgnore
    public String getHtmlView() {
        return StrUtil.isBlank(getStyle()) ? "pagelist.html" : "pagelist_" + getStyle().trim() + ".html";
    }
	
}
