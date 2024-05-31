package org.example;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.Assert.*;

public class RentalToolTest {
    @Test
    public void testValidCheckout() {
        RentalTool[] tools = {
                new RentalTool("CHNS", "Chainsaw", "Stihl"),
                new RentalTool("LADW", "Ladder", "Werner"),
                new RentalTool("JAKD", "Jackhammer", "DeWalt"),
                new RentalTool("JAKR", "Jackhammer", "Ridgids"),
        };

        for (RentalTool rentalTool : tools) {
            switch (rentalTool.toolType) {
                case "Chainsaw":
                    rentalTool.setProperty(ToolType.CHAINSAW);
                    break;
                case "Ladder":
                    rentalTool.setProperty(ToolType.LADDER);
                    break;
                case "Jackhammer":
                    rentalTool.setProperty(ToolType.JACKHAMMER);
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
                RentalTool tool = switch ((String) prototypes[i].get("toolCode")) {
                    case "JAKR" -> tools[3];
                    case "LADW" -> tools[1];
                    case "CHNS" -> tools[0];
                    case "JAKD" -> tools[2];
                    default -> null;
                };
                RentalAgreement agreement = new RentalAgreement(tool, (int)prototypes[i].get("rentalDays"), (int)prototypes[i].get("discount"), checkoutDate);
                agreement.printRentalAgreement();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void testInvalidRentalDays() {
        RentalTool tool = new RentalTool("CHNS", "Chainsaw", "Stihl");
        tool.setProperty(ToolType.CHAINSAW);
        LocalDate checkoutDate = LocalDate.of(2023, 8, 24);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new RentalAgreement(tool, 0, 10, checkoutDate);
        });

        assertTrue(exception.getMessage().contains("Rental day count must be 1 or greater."));
    }

    @Test
    public void testInvalidDiscountPercent() {
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
