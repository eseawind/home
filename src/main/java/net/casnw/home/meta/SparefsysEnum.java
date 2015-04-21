//@DECLARE@
package net.casnw.home.meta;

/**
 * 模块空间参照系统枚举类
 *
 * @author 敏玉芳 
 * @since 2013-12-01
 * @version 1.0
 */
public enum SparefsysEnum {
    BASIN {
        public String getString() {
            return "BASIN";
}
    },
    SUB_BASIN {
        public String getString() {
            return "SUB_BASIN";
        }
    },
    GRID {
        public String getString() {
            return "GRID";
        }
    },
    HRU {
        public String getString() {
            return "HRU";
        }
    };

    public abstract String getString();//这里是很重要的
}
