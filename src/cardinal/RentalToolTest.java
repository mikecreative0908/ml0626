package cardinal;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RentalToolTest {
	@Test
    void testValidCheckout() {
        RentalTool[] tools = {
			new RentalTool("CHNS", "Chainsaw", "Stihl"), 
			new RentalTool("LADW", "Ladder", "Werner"), 
			new RentalTool("JAKD", "Jackhammer", "DeWalt"),
			new RentalTool("JAKR", "Jackhammer", "Ridgids"),
        };
        
        for (int i = 0; i < tools.length; i++) {
        	switch (tools[i].toolType) {
        	case "Chainsaw":
        		tools[i].setProperty(ToolType.CHAINSAW);
        		break;
        	case "Ladder":
        		tools[i].setProperty(ToolType.LADDER);
        		break;
        	case "Jackhammer":
        		tools[i].setProperty(ToolType.JACKHAMMER);
        		break;
        	}
        }
        
        Map<String, Object>[] prototypes = new Map[6]; 
        prototypes[0] = Map.of("toolCode", "JAKR", "checkoutDate", "9/3/15", "rentalDays", 5, "discount", 101);
        prototypes[1] = Map.of("toolCode", "LADW", "checkoutDate", "7/2/20", "rentalDays", 1, "discount", 10);
        prototypes[2] = Map.of("toolCode", "CHNS", "checkoutDate", "7/2/15", "rentalDays", 5, "discount", 25);
        prototypes[3] = Map.of("toolCode", "JAKD", "checkoutDate", "9/3/15", "rentalDays", 6, "discount", 0);
        prototypes[4] = Map.of("toolCode", "JAKR", "checkoutDate", "7/2/15", "rentalDays", 9, "discount", 0);
        prototypes[5] = Map.of("toolCode", "JAKR", "checkoutDate", "7/2/20", "rentalDays", 4, "discount", 50);
        
        for (int i = 0; i < prototypes.length; i++) {
        	try {
        		System.out.println("==================Test_" + (i + 1) + "==================");
            	String date = (String)prototypes[i].get("checkoutDate");
            	String[] parts = date.split("/");
            	LocalDate checkoutDate = LocalDate.of(Integer.parseInt(parts[2]), Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            	RentalTool tool = null;
            	switch((String)prototypes[i].get("toolCode")) {
            	case "JAKR":
            		tool = tools[3];
            		break;
            	case "LADW":
            		tool = tools[1];
            		break;
            	case "CHNS":
            		tool = tools[0];
            		break;
            	case "JAKD":
            		tool = tools[2];
            		break;
            	}
            	RentalAgreement agreement = new RentalAgreement(tool, (int)prototypes[i].get("rentalDays"), (int)prototypes[i].get("discount"), checkoutDate);
            	agreement.printRentalAgreement();
        	} catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());             
            }        	
        }
    }

    @Test
    void testInvalidRentalDays() {
        RentalTool tool = new RentalTool("CHNS", "Chainsaw", "Stihl");
        tool.setProperty(ToolType.CHAINSAW);
        LocalDate checkoutDate = LocalDate.of(2023, 8, 24);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RentalAgreement(tool, 0, 10, checkoutDate);
        });

        assertTrue(exception.getMessage().contains("Rental day count must be 1 or greater."));
    }

    @Test
    void testInvalidDiscountPercent() {
        RentalTool tool = new RentalTool("JAKD", "Jackhammer", "DeWalt");
        tool.setProperty(ToolType.JACKHAMMER);
        LocalDate checkoutDate = LocalDate.of(2023, 9, 3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RentalAgreement(tool, 4, 101, checkoutDate);
        });
        
        if (exception.getMessage().contains("Discount percent must be in range 0-100.")) {
        	System.out.println("Discount percent must be in range 0-100.");
        }
//        assertTrue();
    }

}
