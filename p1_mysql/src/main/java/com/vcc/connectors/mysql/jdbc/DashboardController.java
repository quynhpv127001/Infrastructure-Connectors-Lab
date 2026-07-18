package com.vcc.connectors.mysql.jdbc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class DashboardController {

    private final UserRepositoryDb1 repoDb1;
    private final UserRepositoryDb2 repoDb2;
    
    // In-memory fallback if DB is dead
    private final List<User> mockUsersA = new ArrayList<>(List.of(new User(1L, "admin_mock_A", "adminA@sandbox.local")));
    private final List<User> mockUsersB = new ArrayList<>(List.of(new User(1L, "admin_mock_B", "adminB@sandbox.local")));
    private long mockIdCounter = 2L;
    private final boolean useMock = true; // Flag bypass DB for instant UI testing

    public DashboardController(UserRepositoryDb1 repoDb1, UserRepositoryDb2 repoDb2) {
        this.repoDb1 = repoDb1;
        this.repoDb2 = repoDb2;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) String searchA,
            @RequestParam(required = false) String searchB,
            Model model) {
        
        List<User> usersA;
        List<User> usersB;

        if (useMock) {
            usersA = (searchA != null && !searchA.isEmpty()) ? 
                mockUsersA.stream().filter(u -> u.getUsername().contains(searchA) || u.getEmail().contains(searchA)).collect(Collectors.toList()) 
                : new ArrayList<>(mockUsersA);
            usersB = (searchB != null && !searchB.isEmpty()) ? 
                mockUsersB.stream().filter(u -> u.getUsername().contains(searchB) || u.getEmail().contains(searchB)).collect(Collectors.toList()) 
                : new ArrayList<>(mockUsersB);
        } else {
            try {
                usersA = (searchA != null && !searchA.isEmpty()) ? repoDb1.search(searchA) : repoDb1.findAll();
                usersB = (searchB != null && !searchB.isEmpty()) ? repoDb2.search(searchB) : repoDb2.findAll();
            } catch (Exception e) {
                usersA = new ArrayList<>();
                usersB = new ArrayList<>();
            }
        }
        
        model.addAttribute("usersA", usersA);
        model.addAttribute("usersB", usersB);
        model.addAttribute("searchA", searchA);
        model.addAttribute("searchB", searchB);
        
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String tenant, @RequestParam String username, @RequestParam String email) {
        if (useMock) {
            User user = new User(mockIdCounter++, username, email);
            if ("tenantA".equals(tenant)) mockUsersA.add(user);
            if ("tenantB".equals(tenant)) mockUsersB.add(user);
        } else {
            try {
                User user = new User(null, username, email);
                if ("tenantA".equals(tenant)) {
                    repoDb1.save(user);
                } else if ("tenantB".equals(tenant)) {
                    repoDb2.save(user);
                }
            } catch (Exception e) {
            }
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String tenant, @RequestParam Long id) {
        if (useMock) {
            if ("tenantA".equals(tenant)) mockUsersA.removeIf(u -> u.getId().equals(id));
            if ("tenantB".equals(tenant)) mockUsersB.removeIf(u -> u.getId().equals(id));
        } else {
            try {
                if ("tenantA".equals(tenant)) {
                    repoDb1.delete(id);
                } else if ("tenantB".equals(tenant)) {
                    repoDb2.delete(id);
                }
            } catch (Exception e) {
            }
        }
        return "redirect:/";
    }

    @PostMapping("/update")
    public String update(@RequestParam String tenant, @RequestParam Long id, @RequestParam String username, @RequestParam String email) {
        if (useMock) {
            List<User> list = "tenantA".equals(tenant) ? mockUsersA : mockUsersB;
            list.stream().filter(u -> u.getId().equals(id)).findFirst().ifPresent(u -> {
                u.setUsername(username);
                u.setEmail(email);
            });
        } else {
            try {
                User user = new User(id, username, email);
                if ("tenantA".equals(tenant)) {
                    repoDb1.update(user);
                } else if ("tenantB".equals(tenant)) {
                    repoDb2.update(user);
                }
            } catch (Exception e) {
            }
        }
        return "redirect:/";
    }
}
