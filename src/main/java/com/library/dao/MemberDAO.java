package com.library.dao;

import java.sql.SQLException;
import java.util.List;

import com.library.model.Member;

public interface MemberDAO 
{
    Member addMember(Member member) throws SQLException;
    
    boolean updateMember(Member member) throws SQLException;
    
    boolean deleteMember(int memberId) throws SQLException;
    
    Member getMemberById(int memberId) throws SQLException;
    
    Member getMemberByEmail(String email) throws SQLException;
    
    List<Member>getAllMembers() throws SQLException;
    
    List<Member>searchMembers(String keyword) throws SQLException;
    
    
    
}
