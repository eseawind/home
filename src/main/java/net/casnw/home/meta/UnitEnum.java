//@DECLARE@
package net.casnw.home.meta;

/**
 * 变量单位枚举类
 *
 * @author 敏玉芳
 * @since 2013-12-01
 * @version 1.0
 */
public enum UnitEnum {

    mg {
                public String getString() {
                    return "mg";
                }
            },
    g {
                public String getString() {
                    return "g";
                }
            },
    kg {
                public String getString() {
                    return "kg";
                }
            },
    C {
                public String getString() {
                    return "C";
                }
            },
    T {
                public String getString() {
                    return "T";
                }
            },
    NULL {
                public String getString() {
                    return "NULL";
                }
            };

    public abstract String getString();//这里是很重要的
}
