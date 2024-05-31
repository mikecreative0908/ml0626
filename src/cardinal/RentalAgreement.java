package cardinal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
//import cardinal.RentalTool;

public class RentalAgreement {
	private RentalTool rentedTool;
    private int rentalDays;
    private LocalDate checkoutDate;
    private int discountPercent;

    private LocalDate dueDate;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public RentalAgreement(RentalTool rentedTool, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        if (rentalDays < 1) throw new IllegalArgumentException("Rental day count must be 1 or greater.");
        if (discountPercent < 0 || discountPercent > 100) throw new IllegalArgumentException("Discount percent must be in range 0-100.");

        this.rentedTool = rentedTool;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.discountPercent = discountPercent;

        calculateDueDate();
        calculateCharges();
    }

    private void calculateDueDate() {
        dueDate = checkoutDate.plusDays(rentalDays);
    }

    private int calculateChargeDays() {
        Set<LocalDate> holidays = getHolidays(checkoutDate.getYear());
        int chargeDays = 0;

        for (int i = 0; i < rentalDays; i++) {
            LocalDate date = checkoutDate.plusDays(i + 1);
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            boolean isWeekday = EnumSet.range(DayOfWeek.MONDAY, DayOfWeek.FRIDAY).contains(dayOfWeek);
            boolean isWeekend = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY).contains(dayOfWeek);
            boolean isHoliday = holidays.contains(date);

            if ((isWeekday && rentedTool.weekdayCharge) 
                || (isWeekend && rentedTool.weekendCharge) 
                || (isHoliday && rentedTool.holidayCharge)) {
                chargeDays++;
            }
        }
        return chargeDays;
    }

    private void calculateCharges() {
        int chargeDays = calculateChargeDays();
        preDiscountCharge = rentedTool.dailyCharge.multiply(BigDecimal.valueOf(chargeDays));
        discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        finalCharge = preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private Set<LocalDate> getHolidays(int year) {
        Set<LocalDate> holidays = new HashSet<>();
        holidays.add(getIndependenceDay(year));
        holidays.add(LocalDate.of(year, 9, 1).with(java.time.temporal.TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY))); // Labor Day
        return holidays;
    }

    private LocalDate getIndependenceDay(int year) {
        LocalDate julyFourth = LocalDate.of(year, 7, 4);
        switch (julyFourth.getDayOfWeek()) {
            case SATURDAY:
                return julyFourth.minusDays(1);
            case SUNDAY:
                return julyFourth.plusDays(1);
            default:
                return julyFourth;
        }
    }

    public void printRentalAgreement() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        System.out.println("Tool code: " + rentedTool.toolCode);
        System.out.println("Tool type: " + rentedTool.toolType);
        System.out.println("Tool brand: " + rentedTool.brand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Check out date: " + checkoutDate.format(dateFormatter));
        System.out.println("Due date: " + dueDate.format(dateFormatter));
        System.out.println("Daily rental charge: $" + rentedTool.dailyCharge);
        System.out.println("Charge days: " + calculateChargeDays());
        System.out.println("Pre-discount charge: $" + preDiscountCharge);
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: $" + discountAmount);
        System.out.println("Final charge: $" + finalCharge);
    }
}
