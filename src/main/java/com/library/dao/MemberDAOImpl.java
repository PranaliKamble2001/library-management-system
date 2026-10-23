package com.library.dao;

import java.sql.Connection;
import java.util.*;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import com.library.db.DBConnection;
import com.library.model.Member;

public class MemberDAOImpl implements MemberDAO {

	@Override
	public Member addMember(Member member) throws SQLException {
		String sql="INSERT INTO members(name,email,phone,address,membership_date,status)"
				+"VALUES(?,?,?,?,?,?)";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS))
		{
			LocalDate joinDate=member.getMembershipDate() !=null ? member.getMembershipDate() : LocalDate.now();
			
			ps.setString(1, member.getName());
			ps.setString(2, member.getEmail());
			ps.setString(3, member.getPhone());
			ps.setString(4, member.getAddress());
			ps.setDate(5, Date.valueOf(joinDate));
			ps.setString(6, member.getStatus() != null ? member.getStatus().name() : Member.Status.ACTIVE.name());
			
			ps.executeUpdate();
			
			try(ResultSet keys = ps.getGeneratedKeys())
			{
				if(keys.next())
				{
					member.setMemberId(keys.getInt(1));
				}
			}
			member.setMembershipDate(joinDate);
		}
		return member;
		
	}

	@Override
	public boolean updateMember(Member member) throws SQLException {
		String sql="UPDATE members SET name=?, email=?, phone=?, address=?, status=? WHERE member_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setString(1, member.getName());
			ps.setString(2, member.getEmail());
			ps.setString(3, member.getPhone());
			ps.setString(4, member.getAddress());
			ps.setString(5, member.getStatus().name());
			ps.setInt(6, member.getMemberId());
			
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public boolean deleteMember(int memberId) throws SQLException {
		String sql="DELETE FROM members WHERE member_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setInt(1, memberId);
			return ps.executeUpdate() > 0;
			
		}
		
	}

	@Override
	public Member getMemberById(int memberId) throws SQLException {
		String sql="SELECT * FROM members WHERE member_id=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql) )
		{
			ps.setInt(1, memberId);
			try(ResultSet rs=ps.executeQuery())
			{
				if(rs.next())
				{
					return mapRow(rs);
				}
			}
		}
		return null;
	}

	@Override
	public Member getMemberByEmail(String email) throws SQLException {
		String sql="SELECT * FROM members WHERE email=?";
		try(Connection conn=DBConnection.getConnection();
				PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setString(1, email);
			try(ResultSet rs=ps.executeQuery())
			{
				if(rs.next())
				{
					return mapRow(rs);
				}
			}
		}
		return null;
	}

	@Override
	public List<Member> getAllMembers() throws SQLException {
		
		String sql= "SELECT * FROM members ORDER BY name";
		List<Member>members=new ArrayList<>();
		try(Connection conn= DBConnection.getConnection();
			PreparedStatement ps=conn.prepareStatement(sql);
				ResultSet rs=ps.executeQuery())
		{
			while(rs.next())
			{
			   members.add(mapRow(rs));	
			}
		}
		return members;
	}

	@Override
	public List<Member> searchMembers(String keyword) throws SQLException {
		String sql="SELECT * FROM members WHERE name LIKE ? OR email LIKE ? ORDER BY name";
		List<Member>members = new ArrayList<>();
		String like= "%" + keyword + "%";
		try(Connection conn = DBConnection.getConnection();
			PreparedStatement ps=conn.prepareStatement(sql))
		{
			ps.setString(1, like);
			ps.setString(2, like);
			try(ResultSet rs=ps.executeQuery())
			{
				while(rs.next())
				{
					members.add(mapRow(rs));
				}
			}
		}
		return members;
	}
	
	private Member mapRow(ResultSet rs) throws SQLException
	{
		return new Member(
				rs.getInt("member_id"),
				rs.getString("name"),
				rs.getString("email"),
				rs.getString("phone"),
				rs.getString("address"),
				rs.getDate("membership_date").toLocalDate(),
				Member.Status.valueOf(rs.getString("status"))
				
				);
				
	}

}
