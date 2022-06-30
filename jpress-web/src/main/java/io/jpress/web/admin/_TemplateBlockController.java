/**
 * Copyright (c) 2016-2020, Michael Yang 杨福海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the GNU Lesser General Public License (LGPL) ,Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jpress.web.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.Ret;
import io.jboot.utils.JsonUtil;
import io.jboot.web.controller.annotation.RequestMapping;
import io.jboot.web.json.JsonBody;
import io.jpress.JPressConsts;
import io.jpress.core.menu.annotation.AdminMenu;
import io.jpress.core.template.BlockContainer;
import io.jpress.core.template.BlockHtml;
import io.jpress.core.template.Template;
import io.jpress.core.template.TemplateManager;
import io.jpress.core.template.editor.BsFormComponent;
import io.jpress.web.base.AdminControllerBase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Yang 杨福海 （fuhai999@gmail.com）
 * @version V1.0
 * @Package io.jpress.web.admin
 */
@RequestMapping(value = "/admin/template/block", viewPath = JPressConsts.DEFAULT_ADMIN_VIEW)
public class _TemplateBlockController extends AdminControllerBase {


    @AdminMenu(text = "板块", groupId = JPressConsts.SYSTEM_MENU_TEMPLATE, order = 7)
    public void index() {
        Template currentTemplate = TemplateManager.me().getCurrentTemplate();
        setAttr("template", currentTemplate);

        List<BlockContainer> blockContainers = currentTemplate.getBlockContainers();
        setAttr("blockContainers", blockContainers);

        List<BlockHtml> blockHtmls = currentTemplate.getBlockHtmls();
        setAttr("blockHtmls", blockHtmls);

        render("template/block.html");
    }


    /**
     * 获取编辑定义的组件
     */
    public void components() {
        Template currentTemplate = TemplateManager.me().getCurrentTemplate();
        List<BlockHtml> blockHtmls = currentTemplate.getBlockHtmls();

        if (blockHtmls == null || blockHtmls.isEmpty()) {
            renderJson(Ret.ok());
            return;
        }

        BsFormComponent block = new BsFormComponent();
        block.setName("板块");
        block.setTag("block");
        block.setDisableTools(true);

        List<BsFormComponent> components = new ArrayList<>();
        components.add(block);

        for (BlockHtml blockHtml : blockHtmls) {
            components.add(blockHtml.toBsFormComponent());
        }

        renderJson(Ret.ok("components", components));
    }


    /**
     * 获取当前模板配置的数据
     */
    public void datas() {
        Template currentTemplate = TemplateManager.me().getCurrentTemplate();

        List<BlockContainer> blockContainers = currentTemplate.getBlockContainers();
        setAttr("blockContainers", blockContainers);

        if (blockContainers == null || blockContainers.isEmpty()) {
            renderJson(Ret.ok());
            return;
        }

        JSONArray baseDatas = new JSONArray();
        for (BlockContainer container : blockContainers) {
            baseDatas.add(container.toBsFormData());
        }

        renderJson(Ret.ok("datas", baseDatas));
    }


    public void render(@JsonBody JSONObject jsonObject){
        String rawData = getRawData();
        String tag = JsonUtil.getString(jsonObject,"component.tag","");
        renderHtml(getTagHtml(tag));
    }


    private String getTagHtml(String tag){
        switch (tag){
            case "block":
                return "<div class=\"m-3 pt-2 bsFormItem bsFormFilter\">\n" +
                        "  <div class=\"card\">\n" +
                        "    <div class=\"card-header\">\n" +
                        "      <h3 class=\"card-title\">{{id}}</h3>\n" +
                        "      <div class=\"card-tools\">\n" +
                        "        <button type=\"button\" class=\"btn btn-tool\" data-card-widget=\"collapse\">\n" +
                        "          <i class=\"fas fa-plus\"></i>\n" +
                        "        </button>\n" +
                        "      </div>\n" +
                        "    </div>\n" +
                        "    <div class=\"card-body bsItemContainer\">{{$children[0]}}</div>\n" +
                        "  </div>\n" +
                        "</div>\n";

            default:
                return "<div>暂无内容</div>";
        }
    }


}