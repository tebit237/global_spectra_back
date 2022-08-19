///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package iwomi.base.services;
//
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.json.JSONException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//
///**
// *
// * @author tebit roger
// */
//public class Treatement implements Runnable {
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    List<String> fichier;
//    int i;
//    Map<String, Object> fich;
//
//    public Treatement(List<String> fichier, int i, Map<String, Object> fich) {
//        this.fichier = fichier;
//        this.i = i;
//        this.fich = fich;
//    }
//
//    @Override
//    public void run() {
//        try {
////                tsdsfsdfs(fichier, i, fich);
//
//            Long Total;
//            int Friqunce;
//            String efi = fichier.get(i);
//            String sse;
//            List<Map<String, Object>> FIC = findByCalc(efi);
//            Map<String, String> type = new HashMap<>();
//            type = getType1(efi);
//            Total = getTotal(efi, type);
//            Friqunce = (int) Math.ceil(Total / 100.0);
////        sse = liveReportingService.beginDetailsReportingToTheVue(idOpe, efi, Total);
//            System.out.println("the result element is :" + type.get("result"));
//            if (type.get("result").toString().equalsIgnoreCase("duplicate")) {
//                System.out.println("this is the date: " + fich.get("date").toString());
//                List<Map<String, Object>> dup = findByDup1(fich.get("date").toString(), efi);
//                int e = dup.size();
//                Friqunce = (int) Math.ceil(e / 100.0);
//                Total = new Long(e);
//                if (!minim.containsKey(efi)) {
//                    minim.put(efi, new Long(Friqunce));
//                }
//                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//                for (int t = 0; t < dup.size(); t++) {
//                    System.out.println("Fichier type Duplicate");
//                    saveCalcTmp1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), minimum);
//                    liveReportingService.detailsReportingToTheVue2(uniquecode, efi, minim.get(efi));
//                }
////            liveReportingService.endDetailsReportingToTheVue1(sse, efi, 1L, Total, Total);
//            } else if (type.get("result").toString().equalsIgnoreCase("duplicatepost")) {
//                System.out.println("this is the date: " + fich.get("date").toString());
//                List<Map<String, Object>> dup = findByDup1(fich.get("date").toString(), efi);
//                int e = dup.size();
//                Friqunce = (int) Math.ceil(e / 100.0);
//                Total = new Long(e);
//                if (!minim.containsKey(efi)) {
//                    minim.put(efi, new Long(Friqunce));
//                }
//                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//                for (int t = 0; t < dup.size(); t++) {
//                    System.out.println("Fichier type Duplicate");
//                    saveCalcTmp1(fich.get("etab").toString(), dup.get(t), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), minimum);
//                    liveReportingService.detailsReportingToTheVue2(uniquecode, efi, minim.get(efi));
//                }
////            liveReportingService.endDetailsReportingToTheVue1(sse, efi, 1L, Total, Total);
//            } else if (type.get("result").toString().equalsIgnoreCase("sql")) {
//                liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, 2L);
//                System.out.println("the value of result:" + type.get("result"));
//                System.out.println("the value of sql:" + type.get("sql"));
//                String formule = type.get("sql").toString();
//                System.out.println("Fichier type SQL");
//                System.out.println("to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')");
//                formule = formule.replaceAll("//dar//", "to_date('" + fich.get("date").toString() + "','yyyy-mm-dd')");
//                if (type.get("result").toString().equalsIgnoreCase("1")) {// link
//                    int ret = findBySql3(formule, fich.get("date").toString(), efi,
//                            type.get("result").toString());
//                    System.out.println("entered the first");
//                } else {// internal database
//                    System.out.println("entered the second");
//                    int ret = findBySql2v1(formule, fich.get("date").toString(), efi);
//                }
//            } else {
//                System.out.println("Fichier type calculation");
//                if (efi.equalsIgnoreCase("F1000")) {
//                    savePostF1000_1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    System.out.println("entering for *** values ");
//                    savePoint250202019(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    savecol250202019(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    liveReportingService.endDetailsReportingToTheVue1(uniquecode, efi, 1L, Total, Total);
//                } else if (efi.equalsIgnoreCase("F1139")) {
//                    sse = uniquecode;//liveOperationRepository.findOne(idOpe).getCodeUnique();//
//                    List<Map<String, Object>> FICPOST = findCalcByPost_F1139(efi, parameters.get("F1139"));
//                    List<Map<String, Object>> FICSQL = findCalcBySql(efi);
//                    List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(efi);
//                    List<Map<String, Object>> FICTRAITEMENT1 = findCalcByTraitement(efi);;
//                    List<Map<String, Object>> FICSQLr = findCalcByPoint1_F1139(efi, parameters.get("F1139"));// all *** cells
//                    List<Map<String, Object>> FICSQLrr = findCalcByPoint2_F1139(efi, parameters.get("F1139"));// all *** cells
//                    int e = FICPOST.size() + FICSQL.size() + FICTRAITEMENT.size() + FICTRAITEMENT1.size() + FICSQLr.size() + FICSQLrr.size();
//                    Friqunce = (int) Math.ceil(e / 100.0);
//                    Total = new Long(e);
//                    if (!minim.containsKey(efi)) {
//                        minim.put(efi, new Long(Friqunce));
//                    }
//                    liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//                    switch (parameters.get("F1139")) {
//                        case "CMR":
//                            savePost250202019_F1139v1(efi, fich.get("etab").toString(),
//                                    fich.get("date").toString(), fich.get("cuser").toString(),
//                                    fich.get("codeUnique").toString(), parameters.get("F1139"), FICPOST);
//                            saveSql250202019V1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL);
//                            saveCondition250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT);
//                            saveTraitement250202019v1(efi, fich.get("etab").toString(),
//                                    fich.get("date").toString(), fich.get("cuser").toString(),
//                                    fich.get("codeUnique").toString(), FICTRAITEMENT1);
//                            savePoint250202019_F1139v1(efi, fich.get("etab").toString(),
//                                    fich.get("date").toString(), fich.get("cuser").toString(),
//                                    fich.get("codeUnique").toString(), parameters.get("F1139"), FICSQLr);
//                            savecol250202019_F1139v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                                    fich.get("cuser").toString(), fich.get("codeUnique").toString(),
//                                    parameters.get("F1139"), FICSQLrr);
//                            break;
//                    }
//
//                } else {
//
//                    sse = uniquecode;//liveReportingService.beginDetailsReportingToTheVue(idOpe, efi, Total);
//                    System.out.println("the value of sse:" + sse);
//                    List<Map<String, Object>> FICPOST = findCalcByPost(efi);
//                    List<Map<String, Object>> FICSQL = findCalcBySql(efi);
//                    List<Map<String, Object>> FICSQL1 = findCalcByPoint1(efi);// all *** cells
//                    List<Map<String, Object>> FICTRAITEMENT = findCalcByCondition(efi);
//                    List<Map<String, Object>> FICTRAITEMENT1 = findCalcByTraitement(efi);;
//                    List<Map<String, Object>> FICSQL2 = findCalcByPoint2(efi);// all *** cells
//                    List<Map<String, Object>> FICSQL3 = findCalcByPoint1(efi);// all *** cells
//                    int e = FICPOST.size() + FICSQL.size() + FICSQL1.size() + FICTRAITEMENT.size() + FICTRAITEMENT1.size() + FICSQL2.size() + FICSQL3.size();
//                    Friqunce = (int) Math.ceil(e / 100.0);
//                    Total = new Long(e);
//                    if (!minim.containsKey(efi)) {
//                        minim.put(efi, new Long(Friqunce));
//                    }
//                    liveReportingService.beginDetailsReportingToTheVue2(idOpe, efi, Total);
//                    savePost250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICPOST);
//                    saveSql250202019V1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL);
//                    savePoint30032020v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL1);
//                    saveCondition250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT);
////                    saveIntermidiate250202019(efi, fich.get("etab").toString(), fich.get("date").toString(),
////                            fich.get("cuser").toString(), fich.get("codeUnique").toString());
//                    saveTraitement250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICTRAITEMENT1);
//                    savecol250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL2);
//                    savePoint250202019v1(efi, fich.get("etab").toString(), fich.get("date").toString(),
//                            fich.get("cuser").toString(), fich.get("codeUnique").toString(), FICSQL3);
//                }
//            }
//
//            saveCalcv1(fich.get("date").toString(), efi);
//            liveReportingService.endDetailsReportingToTheVue2(uniquecode, efi, 1L, Total, Total);
//
//        } catch (ParseException ex) {
//            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SQLException ex) {
//            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JSONException ex) {
//            Logger.getLogger(ReportCalculateServiceImplS.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public List<Map<String, Object>> findByCalc(String fic) {
//        String sql = "Select distinct u.calc,u.fichi,u.col,u.post,u.typeval,u.divd, a.rang ,(case when EXISTS(SELECT * from srppfich WHERE col =u.col) then 1 else 0 end)status from srpcalc u LEFT JOIN srppfich a \n"
//                + "on u.fichi = a.fich and u.col = a.col and u.post =a.poste where u.fichi =?";
//        return jdbcTemplate.queryForList(sql, new Object[]{fic});
//    }
//}
