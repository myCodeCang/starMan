package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 参数设置表
 * Created by kevin on 2017/10/25.
 */
@Entity
@Table(name="USER_OPTIONS")
public class UserOptions extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="option_name")
    private String optionName;		// 配置名

    @Column(name="option_value")
    private String optionValue;		// 配置值

    @Column(name="group_name")
    private String groupName;       // 分组名

    @Column(name="option_label")
    private String optionLabel;     // 标签名

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    @Override
    protected ErrorMessages validate() {
        ErrorMessages errors = ErrorMessages.createErrorMessages();
        if(StringUtils.isBlank(getOptionName())){
            errors.add("配置名不能为空!");
        }
        if(StringUtils.isBlank(getOptionValue())){
            errors.add("配置值不能为空!");
        }
        if(StringUtils.isBlank(getGroupName())){
            errors.add("组名不能为空!");
        }

        return errors;
    }
}
