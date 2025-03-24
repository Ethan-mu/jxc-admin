package cn.toesbieya.jxc.enumeration;

/**
 * 数据权限范围枚举
 */
public enum DataScopeEnum {
    /**
     * 全部数据权限
     */
    ALL(1, "全部"),
    
    /**
     * 部门及以下数据权限
     */
    DEPT_AND_CHILD(2, "部门及以下"),
    
    /**
     * 本部门数据权限
     */
    DEPT(3, "本部门"),
    
    /**
     * 仅本人数据权限
     */
    SELF(4, "仅本人"),
    
    /**
     * 自定义数据权限
     */
    CUSTOM(5, "自定义");
    
    private final int value;
    private final String desc;
    
    DataScopeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    public int getValue() {
        return value;
    }
    
    /**
     * 兼容旧代码的方法
     * @return 数据权限代码
     */
    public int getCode() {
        return value;
    }
    
    public String getDesc() {
        return desc;
    }
    
    /**
     * 根据值获取对应的枚举
     */
    public static DataScopeEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        
        for (DataScopeEnum scope : values()) {
            if (scope.getValue() == value) {
                return scope;
            }
        }
        
        return null;
    }
}
