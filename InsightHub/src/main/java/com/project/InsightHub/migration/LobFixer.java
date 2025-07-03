// This solution didnt work -------


// package com.project.InsightHub.migration;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.Statement;

// import org.postgresql.largeobject.LargeObject;
// import org.postgresql.largeobject.LargeObjectManager;


// public class LobFixer {
//     public static void main(String[] args) throws Exception {
//         String url = "jdbc:postgresql://localhost:5432/insighthub?reWriteBatchedInserts=true&autoCommit=false";
//         String user = "postgres";
//         String pass = "password";

//         Connection conn = DriverManager.getConnection(url, user, pass);
//         conn.setAutoCommit(false);  // Required to use LargeObject API

//         Statement stmt = conn.createStatement();
//         ResultSet rs = stmt.executeQuery("SELECT id, content FROM knowledge_item");

//         LargeObjectManager lobj = conn.unwrap(org.postgresql.PGConnection.class).getLargeObjectAPI();

//         while (rs.next()) {
//             long id = rs.getLong("id");
//             Object lobVal = rs.getObject("content");

//             if (lobVal instanceof Long oid) {
//                 // Read the old content
//                 LargeObject obj = lobj.open(oid, LargeObjectManager.READ);
//                 byte[] data = obj.read((int) obj.size());
//                 obj.close();

//                 String textContent = new String(data);

//                 // Update content column with actual text
//                 PreparedStatement updateStmt = conn.prepareStatement("UPDATE knowledge_item SET content = ? WHERE id = ?");
//                 updateStmt.setString(1, textContent);
//                 updateStmt.setLong(2, id);
//                 updateStmt.executeUpdate();

//                 System.out.println("✅ Fixed ID: " + id);
//             }
//         }

//         conn.commit();
//         conn.close();
//     }
// }
