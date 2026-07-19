package com.mht.mhtblogservice.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class CardConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 卡片池唯一主键ID (对应数据库自增主键 id)
     */
    private Long id;

    /**
     * 首页展示位置 (对应数据库 card_index)
     * 0: 暂不展示，仅存在于资产池中
     * 1: 左侧珊瑚橙大长卡
     * 2: 右上角卡片
     * 3: 右中钱袋子卡片
     * 4: 左下角考试试卷卡片
     * 5: 右下角更多卡片
     */
    private Integer cardIndex;

    /**
     * 卡片标题 (对应数据库 title)
     */
    private String title;

    /**
     * 卡片描述 (对应数据库 description)
     */
    private String description;

    /**
     * 图标类型 (对应数据库 icon_type)
     * 可选值: macbook, money_bag, exam_paper, none
     */
    private String iconType;

    /**
     * 点击跳转的目标外链或 GitHub 链接 (对应数据库 target_url)
     */
    private String targetUrl;

    /**
     * 滑块开关状态 (对应数据库 status)
     * 0: 隐藏/关闭
     * 1: 展示/开启
     */
    private Integer status;
}
