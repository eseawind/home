//@DECLARE@
package net.casnw.home.meta;

/**
 * 模块空间分辨率枚举类
 *
 * @author 敏玉芳 
 * @since 2013-12-01
 * @version 1.0
 */
public enum SpacescaleEnum {
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
    MIXING_GRID {
        public String getString() {
            return "MIXING_GRID";
        }
    },
    HRU {
        public String getString() {
            return "HRU";
        }
    };

    public abstract String getString();//这里是很重要的
}
