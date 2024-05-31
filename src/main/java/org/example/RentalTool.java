package org.example;
import java.math.BigDecimal;

public class RentalTool {	
	public String toolCode;
	public String toolType;
	public String brand;
	public BigDecimal dailyCharge;
	public boolean weekdayCharge;
	public boolean weekendCharge;
	public boolean holidayCharge;
    
    public RentalTool(
    		String toolCode, 
    		String toolType, 
    		String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;       
    }
    
    public void setProperty(
    		ToolType property) {
    	this.dailyCharge = property.dailyCharge;
        this.weekdayCharge = property.weekdayCharge;
        this.weekendCharge = property.weekendCharge;
        this.holidayCharge = property.holidayCharge;
    }
}
