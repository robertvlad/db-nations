package org.java.query.nations.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		String url = "jdbc:mysql://localhost:3306/db-nations";
		String user = "root";
		String password = "2512";
		
		try (Scanner sc = new Scanner(System.in);
				Connection con = DriverManager.getConnection(url, user, password)) {
			
			System.out.println("Connected");
			
//			String sql = "select c.name, c.country_id, r.name, c2.name \r\n"
//					+ "from countries c \r\n"
//					+ "join regions r on r.region_id = c.region_id \r\n"
//					+ "join continents c2 on r.continent_id = c2.continent_id \r\n"
//					+ "order by c.name";
//			
//			try (PreparedStatement ps = con.prepareStatement(sql);
//					ResultSet rs = ps.executeQuery()){
//				
//				while(rs.next()) {
//					
//					final String nameCountry = rs.getString(1);
//					final int idCountry = rs.getInt(2);
//					final String nameRegion = rs.getString(3);
//					final String nameContinent = rs.getString(4);
//					
//					System.out.println(nameCountry + " - " + idCountry + " - " + nameRegion + " - " + nameContinent);
//				}
//			} catch (SQLException ex) {
//				
//				System.err.println("SQL Query Error");
//			}
			
			String sql = "select c.name, c.country_id, r.name, c2.name \r\n"
					+ "from countries c \r\n"
					+ "join regions r on r.region_id = c.region_id \r\n"
					+ "join continents c2 on r.continent_id = c2.continent_id \r\n"
					+ "where c.name like ? \r\n"
					+ "order by c.name";
			
			System.out.println("Insert the characters that the Country name should have");
			String inputCharacter = sc.nextLine();
			
			try (PreparedStatement ps = con.prepareStatement(sql)){
				
				ps.setString(1, "%" + inputCharacter + "%");
				
				try (ResultSet rs = ps.executeQuery()) {
					
					while(rs.next()) {
						
						final String nameCountry = rs.getString(1);
						final int idCountry = rs.getInt(2);
						final String nameRegion = rs.getString(3);
						final String nameContinent = rs.getString(4);
						
						System.out.println(nameCountry + " - " + idCountry + " - " + nameRegion + " - " + nameContinent);
					}
					
				} catch (SQLException ex) {
					
					System.err.println("Error input name ");
				}
			} catch (SQLException ex) {
				
				System.err.println("SQL Query Error");
			}
			
			String sql1 = "select c.name , cs.`year` , cs.population , cs.gdp \r\n"
					+ "from countries c \r\n"
					+ "join country_stats cs on c.country_id = cs.country_id \r\n"
					+ "where cs.country_id = ?\r\n"
					+ "order by cs.`year` desc \r\n"
					+ "limit 1";
			
			String sql2 = "select l.`language` \r\n"
					+ "from countries c \r\n"
					+ "join country_languages cl on c.country_id = cl.country_id \r\n"
					+ "join languages l on l.language_id = cl.language_id \r\n"
					+ "where c.country_id = ?";
			
			
			System.out.println("\n--------------------------------------------\n");
			
			System.out.println("Input the id of the country to know it stats");
			
			final String strIdNation = sc.nextLine();
			final int inputId = Integer.valueOf(strIdNation);
			
			try (PreparedStatement ps1 = con.prepareStatement(sql1);
					PreparedStatement ps2 = con.prepareStatement(sql2)) {				
				
				ps1.setInt(1, inputId);
				ps2.setInt(1, inputId);
				
				try (ResultSet rs1 = ps1.executeQuery();
						ResultSet rs2 = ps2.executeQuery()) {
					
					if (!rs1.next()) return;
					
					String nameCountry = rs1.getString(1);
					int yearReport = rs1.getInt(2);
					int populationReport = rs1.getInt(3);
					long gdpReport = rs1.getLong(4);
					
					System.out.println("Details for country: " + nameCountry + "\nLangauges: ");
					
					while(rs2.next()) {
						System.out.print(rs2.getString(1) + (rs2.isLast() ? "" : ", "));
					}
							
					System.out.println("\nYear: " + yearReport
							+ "\nPopulation: " + populationReport
							+ "\nGDP: " + gdpReport);
					
				} catch (SQLException ex) {
					
					System.err.println("Error input");
				}				
			} catch (SQLException ex) {
				
				System.err.println("Error input");
			}
			
			
		} catch (SQLException ex) {
			
			System.err.println("Error during connection to DB");
		}
	}
}
