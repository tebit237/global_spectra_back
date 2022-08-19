///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package iwomi.config;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//import javax.sql.DataSource;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//
///**
// *
// * @author TAGNE
// */
//@Configuration
//public class DataSourceConfig {
//    
//    private static final Log log  =LogFactory.getLog(DataSourceConfig.class);
//    
//     @Value("${db.driver-class-name}")
//    private String dbDriverClassName;
//
//    @Value("${db.url}")
//    private String dbUrl;
//
//    @Value("${db.username}")
//    private String dbUsername;
//
//    @Value("${db.password}")
//    private String dbPassword;
//    
//    private final String key = "iwomiDatabaseKey";
//    private final String initVector = "InitVectoryann99";
//    
////    private final String Key_Size_in_Bits = "128";
////    private final String Select_Mode = "CBC";
////    private final String online_site_tool = "https://www.devglan.com/online-tools/aes-encryption-decryption";
//    
//       public String encrypt(String value) {
//        try {
//                IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//                SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//
//                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//
//                byte[] encrypted = cipher.doFinal(value.getBytes());
//                return new String(java.util.Base64.getEncoder().encode(encrypted));
//        } catch (Exception ex) {
//                ex.printStackTrace();
//        }
//        return null;
//    }
//    
//    public String decrypt(String encrypted) {
//	try {
//		IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
//		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//		byte[] original = cipher.doFinal(java.util.Base64.getDecoder().decode(encrypted));
//
//		return new String(original);
//	} catch (Exception ex) {
//		ex.printStackTrace();
//	}
//
//	return null;
//    }
//  
//    @Bean
//    public DataSource dataSource() {
//         log.info("Start verification nombre de tentative de connexion0909" + "\r\n");
//         System.out.println(" output of password :" + dbPassword);
//         System.out.println(" output of password 09:" + dbPassword);
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(dbDriverClassName);
//        dataSource.setUrl(dbUrl);
//        dataSource.setUsername(dbUsername);
//        dataSource.setPassword(this.decrypt(dbPassword));
//        //dataSource.setPassword(dbPassword);
//        return dataSource;
//            
//    }
//    
//}
