//@DECLARE@
package net.casnw.home.meta;

/**
 * 模块时间分辨率枚举类
 *
 * @author 敏玉芳 
 * @since 2013-12-01
 * @version 1.0
 */
public enum TimescaleEnum {

    YEAR {
        public String getString() {
            return "YEAR";
        }
    },
    MONTH {
        public String getString() {
            return "MONTH";
        }
    },
    Day {
        public String getString() {
            return "Day";
        }
    },
    Hour {
        public String getString() {
            return "Hour";
        }
    },
    Minite {
        public String getString() {
            return "Minite";
        }
    },
    Second {
        public String getString() {
            return "Second";
        }
    };

    public abstract String getString();//这里是很重要的

    
}
