//package com.frog.travelwithme.utils.security;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.test.context.support.WithSecurityContextFactory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static callbuslap.jaritalk.common.EnumCollection.*;
//
//public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
//    @Override
//    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        String role = customUser.role().toUpperCase();
//        Boolean roleCheck = roleTypeCheck(role);
//        if (roleCheck) {
//            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role));
//
//            AuthAccount auth = AuthAccount.of(1L, "", "", role);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    auth, null, grantedAuthorities);
//            authentication.setDetails(customUser.username());
//            context.setAuthentication(authentication);
//            return context;
//        } else {
//            return context;
//        }
//    }
//
//    private Boolean roleTypeCheck(String role){
//        AccountType[] accountTypes = AccountType.values();
//        if(accountTypes.length > 0) {
//            for (AccountType type : accountTypes) {
//                if (type.getName().equals(role)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}
