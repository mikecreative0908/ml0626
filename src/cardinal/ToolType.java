package cardinal;

import java.math.BigDecimal;

public enum ToolType {
    LADDER(new BigDecimal("1.99"), true, true, false),
    CHAINSAW(new BigDecimal("1.49"), true, false, true),
    JACKHAMMER(new BigDecimal("2.99"), true, false, false);

    public final BigDecimal dailyCharge;
    public final boolean weekdayCharge;
    public final boolean weekendCharge;
    public final boolean holidayCharge;

    ToolType(BigDecimal dailyCharge, boolean weekdayCharge, boolean weekendCharge, boolean holidayCharge) {
        this.dailyCharge = dailyCharge;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
    }
}