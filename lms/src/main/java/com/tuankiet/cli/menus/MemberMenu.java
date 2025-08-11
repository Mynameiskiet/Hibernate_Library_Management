package com.tuankiet.cli.menus;

import com.tuankiet.cli.helpers.InputHelper;
import com.tuankiet.dto.common.Page;
import com.tuankiet.dto.common.PageRequest;
import com.tuankiet.dto.common.Sort;
import com.tuankiet.dto.common.SortCriteria;
import com.tuankiet.dto.common.SortDirection;
import com.tuankiet.dto.request.CreateMemberRequest;
import com.tuankiet.dto.request.UpdateMemberRequest;
import com.tuankiet.dto.response.MemberResponse;
import com.tuankiet.dto.search.MemberSearchCriteria;
import com.tuankiet.services.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * CLI menu for member management
 * 
 * @author tuankiet
 * @since 1.0.0
 */
@Component
public class MemberMenu {
    
    private static final Logger logger = LoggerFactory.getLogger(MemberMenu.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    private final InputHelper inputHelper;
    private final MemberService memberService;
    
    @Autowired
    public MemberMenu(InputHelper inputHelper, MemberService memberService) {
        this.inputHelper = inputHelper;
        this.memberService = memberService;
    }
    
    public void displayMenu() {
        int choice;
        do {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("👥 MEMBER MANAGEMENT");
            System.out.println("=".repeat(50));
            System.out.println("1. ➕ Add New Member");
            System.out.println("2. 👁️  View Member Details");
            System.out.println("3. ✏️  Update Member");
            System.out.println("4. 🗑️  Delete Member");
            System.out.println("5. 🔍 Search Members");
            System.out.println("6. 📋 List All Members");
            System.out.println("0. ⬅️  Back to Main Menu");
            System.out.println("=".repeat(50));
            
            choice = inputHelper.readInt("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1 -> createMember();
                    case 2 -> viewMember();
                    case 3 -> updateMember();
                    case 4 -> deleteMember();
                    case 5 -> searchMembers();
                    case 6 -> listAllMembers();
                    case 0 -> logger.info("Returning to main menu from Member Management.");
                    default -> System.out.println("❌ Invalid choice! Please try again.");
                }
                
            } catch (Exception e) {
                logger.error("Error in member menu", e);
                System.out.println("❌ An error occurred: " + e.getMessage());
            }
        } while (choice != 0);
    }
    
    private void createMember() {
        System.out.println("\n➕ ADD NEW MEMBER");
        System.out.println("-".repeat(30));
        
        try {
            String firstName = inputHelper.readString("First Name: ", false);
            String lastName = inputHelper.readString("Last Name: ", false);
            String email = inputHelper.readString("Email: ", false);
            String phone = inputHelper.readString("Phone: ", false);
            String address = inputHelper.readString("Address: ", true);
            
            CreateMemberRequest request = new CreateMemberRequest(
                firstName, lastName, email, phone, address);
            
            MemberResponse response = memberService.create(request);
            
            System.out.println("✅ Member created successfully!");
            displayMemberDetails(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error creating member: " + e.getMessage());
        }
    }
    
    private void viewMember() {
        System.out.println("\n👁️ VIEW MEMBER DETAILS");
        System.out.println("-".repeat(30));
        
        try {
            UUID id = inputHelper.readUuid("Enter Member ID: ");
            if (id == null) {
                System.out.println("❌ Invalid Member ID.");
                return;
            }
            
            MemberResponse member = memberService.getById(id);
            displayMemberDetails(member);
            
        } catch (Exception e) {
            System.out.println("❌ Error viewing member: " + e.getMessage());
        }
    }
    
    private void updateMember() {
        System.out.println("\n✏️ UPDATE MEMBER");
        System.out.println("-".repeat(30));
        
        try {
            UUID id = inputHelper.readUuid("Enter Member ID to update: ");
            if (id == null) {
                System.out.println("❌ Invalid Member ID.");
                return;
            }
            
            MemberResponse existing = memberService.getById(id);
            
            System.out.println("Current member details:");
            displayMemberDetails(existing);
            
            System.out.println("\nEnter new values (press Enter to keep current value):");
            
            String firstName = inputHelper.readString("First Name [" + existing.getFirstName() + "]: ", true);
            if (firstName.trim().isEmpty()) firstName = existing.getFirstName();
            
            String lastName = inputHelper.readString("Last Name [" + existing.getLastName() + "]: ", true);
            if (lastName.trim().isEmpty()) lastName = existing.getLastName();
            
            String email = inputHelper.readString("Email [" + existing.getEmail() + "]: ", true);
            if (email.trim().isEmpty()) email = existing.getEmail();
            
            String phone = inputHelper.readString("Phone [" + existing.getPhoneNumber() + "]: ", true);
            if (phone.trim().isEmpty()) phone = existing.getPhoneNumber();
            
            String address = inputHelper.readString("Address [" + existing.getAddress() + "]: ", true);
            if (address.trim().isEmpty()) address = existing.getAddress();
            
            UpdateMemberRequest request = new UpdateMemberRequest(
                id, firstName, lastName, email, phone, address);
            
            MemberResponse response = memberService.update(request);
            
            System.out.println("✅ Member updated successfully!");
            displayMemberDetails(response);
            
        } catch (Exception e) {
            System.out.println("❌ Error updating member: " + e.getMessage());
        }
    }
    
    private void deleteMember() {
        System.out.println("\n🗑️ DELETE MEMBER");
        System.out.println("-".repeat(30));
        
        try {
            UUID id = inputHelper.readUuid("Enter Member ID to delete: ");
            if (id == null) {
                System.out.println("❌ Invalid Member ID.");
                return;
            }
            
            MemberResponse member = memberService.getById(id);
            
            System.out.println("Member to delete:");
            displayMemberDetails(member);
            
            String confirm = inputHelper.readString("Are you sure you want to delete this member? (yes/no): ", false);
            if ("yes".equalsIgnoreCase(confirm.trim())) {
                boolean deleted = memberService.delete(id);
                if (deleted) {
                    System.out.println("✅ Member deleted successfully!");
                } else {
                    System.out.println("❌ Member could not be deleted.");
                }
            } else {
                System.out.println("❌ Delete operation cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Error deleting member: " + e.getMessage());
        }
    }
    
    private void searchMembers() {
        System.out.println("\n🔍 SEARCH MEMBERS");
        System.out.println("-".repeat(30));
        
        try {
            MemberSearchCriteria criteria = new MemberSearchCriteria();
            
            String firstName = inputHelper.readString("First Name (optional): ", true);
            if (!firstName.trim().isEmpty()) {
                criteria.setFirstName(firstName);
            }
            
            String lastName = inputHelper.readString("Last Name (optional): ", true);
            if (!lastName.trim().isEmpty()) {
                criteria.setLastName(lastName);
            }
            
            String email = inputHelper.readString("Email (optional): ", true);
            if (!email.trim().isEmpty()) {
                criteria.setEmail(email);
            }
            
            String phone = inputHelper.readString("Phone (optional): ", true);
            if (!phone.trim().isEmpty()) {
                criteria.setPhoneNumber(phone);
            }
            
            int page = inputHelper.readInt("Page number (0-based, default 0): ");
            int size = inputHelper.readInt("Page size (default 10): ");
            if (size <= 0) size = 10;
            
            Sort sort = Sort.by(new SortCriteria("firstName", SortDirection.ASC));
            PageRequest pageRequest = new PageRequest(page, size, sort);
            Page<MemberResponse> result = memberService.search(criteria, pageRequest);
            
            displayMemberList(result);
            
        } catch (Exception e) {
            System.out.println("❌ Error searching members: " + e.getMessage());
        }
    }
    
    private void listAllMembers() {
        System.out.println("\n📋 ALL MEMBERS");
        System.out.println("-".repeat(30));
        
        try {
            int page = inputHelper.readInt("Page number (0-based, default 0): ");
            int size = inputHelper.readInt("Page size (default 10): ");
            if (size <= 0) size = 10;
            
            Sort sort = Sort.by(new SortCriteria("firstName", SortDirection.ASC));
            PageRequest pageRequest = new PageRequest(page, size, sort);
            Page<MemberResponse> result = memberService.search(new MemberSearchCriteria(), pageRequest);
            
            displayMemberList(result);
            
        } catch (Exception e) {
            System.out.println("❌ Error listing members: " + e.getMessage());
        }
    }
    
    private void displayMemberDetails(MemberResponse member) {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("👤 MEMBER DETAILS");
        System.out.println("=".repeat(40));
        System.out.println("ID: " + member.getId());
        System.out.println("Name: " + member.getFirstName() + " " + member.getLastName());
        System.out.println("Email: " + member.getEmail());
        System.out.println("Phone: " + member.getPhoneNumber());
        System.out.println("Address: " + (member.getAddress() != null ? member.getAddress() : "Not specified"));
        System.out.println("Registration Date: " + (member.getRegistrationDate() != null ? 
            member.getRegistrationDate().format(DATE_FORMATTER) : "Not specified"));
        System.out.println("Member Since: " + member.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        System.out.println("=".repeat(40));
    }
    
    private void displayMemberList(Page<MemberResponse> page) {
        if (page.getContent().isEmpty()) {
            System.out.println("No members found.");
            return;
        }
        
        System.out.println("\n" + "=".repeat(100));
        System.out.printf("%-36s %-20s %-30s %-15s%n", "ID", "Name", "Email", "Phone");
        System.out.println("=".repeat(100));
        
        for (MemberResponse member : page.getContent()) {
            String fullName = member.getFirstName() + " " + member.getLastName();
            if (fullName.length() > 20) {
                fullName = fullName.substring(0, 17) + "...";
            }
            
            String email = member.getEmail();
            if (email.length() > 30) {
                email = email.substring(0, 27) + "...";
            }
            
            System.out.printf("%-36s %-20s %-30s %-15s%n",
                member.getId().toString(),
                fullName,
                email,
                member.getPhoneNumber());
        }
        
        System.out.println("=".repeat(100));
        System.out.printf("Page %d of %d | Total: %d members%n",
            page.getCurrentPage() + 1,
            page.getTotalPages(),
            page.getTotalElements());
    }
}
